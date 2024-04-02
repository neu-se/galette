package edu.neu.ccs.prl.galette.internal.transform;

import edu.neu.ccs.prl.galette.internal.patch.RegistryPatcher;
import edu.neu.ccs.prl.galette.internal.runtime.Handle;
import edu.neu.ccs.prl.galette.internal.runtime.InvokedViaHandle;
import edu.neu.ccs.prl.galette.internal.runtime.Patched;
import edu.neu.ccs.prl.galette.internal.runtime.collection.SimpleList;
import edu.neu.ccs.prl.galette.internal.runtime.collection.SimpleMap;
import edu.neu.ccs.prl.galette.internal.runtime.mask.*;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.LinkedList;
import org.objectweb.asm.Type;

public final class MaskRegistry {
    private static final Class<?>[] SOURCES = new Class[] {
        UnsafeMasks.class,
        ClassMasks.class,
        ClassLoaderMasks.class,
        SystemMasks.class,
        EnumMasks.class,
        ArrayMasks.class,
        ReflectionMasks.class,
        SerializationMasks.class,
        StringConcatHelperMasks.class,
        SunFloatingDecimalMasks.class,
        JdkFloatingDecimalMasks.class,
        AbstractStringBuilderMasks.class,
        ToDecimalMasks.class,
        BooleanMasks.class,
        ByteMasks.class,
        CharacterMasks.class,
        DoubleMasks.class,
        FloatMasks.class,
        IntegerMasks.class,
        LongMasks.class,
        ShortMasks.class
    };
    private static final SimpleMap<String, MaskInfo> masks = new SimpleMap<>();

    private MaskRegistry() {
        throw new AssertionError();
    }

    static {
        initialize();
    }

    public static MaskInfo getMask(String className, String methodName, String descriptor) {
        return getMask(getKey(className, methodName, descriptor));
    }

    public static MaskInfo getMask(String key) {
        return masks.get(key);
    }

    public static String getKey(String className, String methodName, String descriptor) {
        return className + "." + methodName + descriptor;
    }

    public static SimpleList<String> getKeys() {
        return masks.getKeys();
    }

    /**
     * The body of this method is replaced by {@link RegistryPatcher} to avoid uses of reflection.
     */
    @Patched
    private static void initialize() {
        for (Class<?> clazz : SOURCES) {
            for (Method method : clazz.getDeclaredMethods()) {
                if (method.isAnnotationPresent(Mask.class) || method.isAnnotationPresent(Masks.class)) {
                    if (!Modifier.isStatic(method.getModifiers()) || !Modifier.isPublic(method.getModifiers())) {
                        throw new IllegalStateException("Mask methods must be public and static");
                    }
                }
                for (Mask mask : method.getAnnotationsByType(Mask.class)) {
                    String key = createKey(method, mask);
                    MethodRecord record = MethodRecord.createRecord(clazz, method);
                    masks.put(key, new MaskInfo(mask.type(), record));
                }
            }
        }
    }

    @InvokedViaHandle(handle = Handle.MASK_REGISTRY_PUT)
    private static void put(
            String key,
            int typeOrdinal,
            int opcode,
            String owner,
            String name,
            String descriptor,
            boolean isInterface) {
        MethodRecord record = new MethodRecord(opcode, owner, name, descriptor, isInterface);
        masks.put(key, new MaskInfo(MaskType.values()[typeOrdinal], record));
    }

    private static String createKey(Method method, Mask mask) {
        Type returnType = computeReturnType(method, mask);
        Type[] parameterTypes = computeParameterTypes(method, mask);
        String descriptor = Type.getMethodDescriptor(returnType, parameterTypes);
        return MaskRegistry.getKey(mask.owner(), mask.name(), descriptor);
    }

    private static Type computeReturnType(Method method, Mask mask) {
        if (!mask.returnDescriptor().isEmpty()) {
            return Type.getReturnType("()" + mask.returnDescriptor());
        } else {
            return Type.getReturnType(method);
        }
    }

    private static Type[] computeParameterTypes(Method method, Mask mask) {
        if (!mask.parametersDescriptor().isEmpty()) {
            return Type.getArgumentTypes(mask.parametersDescriptor() + "V");
        }
        String descriptor = Type.getMethodDescriptor(method);
        Type returnType = Type.getReturnType(descriptor);
        LinkedList<Type> parameters = new LinkedList<>(Arrays.asList(Type.getArgumentTypes(descriptor)));
        if (mask.type() == MaskType.POST_PROCESS && !returnType.equals(Type.VOID_TYPE)) {
            // Remove the parameter for the original return
            parameters.removeFirst();
        }
        if (!mask.isStatic()) {
            // Remove the parameter for the receiver
            parameters.removeFirst();
        }
        return parameters.toArray(new Type[0]);
    }

    public static final class MaskInfo {
        private final MaskType type;
        private final MethodRecord record;

        public MaskInfo(MaskType type, MethodRecord record) {
            this.type = type;
            this.record = record;
        }

        public MaskType getType() {
            return type;
        }

        public MethodRecord getRecord() {
            return record;
        }
    }
}
