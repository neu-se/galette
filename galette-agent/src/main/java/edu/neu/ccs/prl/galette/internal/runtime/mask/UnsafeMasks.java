package edu.neu.ccs.prl.galette.internal.runtime.mask;

import edu.neu.ccs.prl.galette.internal.runtime.PrimitiveBoxer;
import edu.neu.ccs.prl.galette.internal.runtime.Tag;
import edu.neu.ccs.prl.galette.internal.runtime.TagFrame;
import edu.neu.ccs.prl.galette.internal.transform.GaletteTransformer;
import java.security.ProtectionDomain;

public final class UnsafeMasks {
    private static final UnsafeWrapper UNSAFE = UnsafeWrapper.createInstance();

    @Mask(
            owner = "jdk/internal/misc/Unsafe",
            name = "defineAnonymousClass0",
            returnDescriptor = "Ljava/lang/Class;",
            type = MaskType.FIX_ARGUMENTS)
    @Mask(
            owner = "sun/misc/Unsafe",
            name = "defineAnonymousClass",
            returnDescriptor = "Ljava/lang/Class;",
            type = MaskType.FIX_ARGUMENTS)
    public static Object[] defineAnonymousClass(Object receiver, Class<?> hostClass, byte[] data, Object[] cpPatches) {
        byte[] instrumented = GaletteTransformer.getInstanceAndTransform(data, true);
        return new Object[] {receiver, hostClass, instrumented, cpPatches};
    }

    @Mask(
            owner = "jdk/internal/misc/Unsafe",
            name = "defineClass0",
            returnDescriptor = "Ljava/lang/Class;",
            type = MaskType.FIX_ARGUMENTS)
    @Mask(
            owner = "sun/misc/Unsafe",
            name = "defineClass",
            returnDescriptor = "Ljava/lang/Class;",
            type = MaskType.FIX_ARGUMENTS)
    public static Object[] defineClass(
            Object receiver, String name, byte[] b, int off, int len, ClassLoader loader, ProtectionDomain domain) {
        byte[] buffer = copy(b, off, len);
        if (buffer != null) {
            byte[] instrumented = GaletteTransformer.getInstanceAndTransform(buffer, false);
            b = instrumented;
            off = 0;
            len = instrumented.length;
        }
        return new Object[] {receiver, name, b, PrimitiveBoxer.box(off), PrimitiveBoxer.box(len), loader, domain};
    }

    @Mask(owner = "jdk/internal/misc/Unsafe", name = "putBoolean", type = MaskType.POST_PROCESS)
    @Mask(owner = "sun/misc/Unsafe", name = "putBoolean", type = MaskType.POST_PROCESS)
    public static void putBoolean(Object receiver, Object o, long offset, boolean x, TagFrame frame) {
        put(o, offset, frame, boolean[].class);
    }

    @Mask(owner = "jdk/internal/misc/Unsafe", name = "putByte", type = MaskType.POST_PROCESS)
    @Mask(owner = "sun/misc/Unsafe", name = "putByte", type = MaskType.POST_PROCESS)
    public static void putByte(Object receiver, Object o, long offset, byte x, TagFrame frame) {
        put(o, offset, frame, byte[].class);
    }

    @Mask(owner = "jdk/internal/misc/Unsafe", name = "putChar", type = MaskType.POST_PROCESS)
    @Mask(owner = "sun/misc/Unsafe", name = "putChar", type = MaskType.POST_PROCESS)
    public static void putChar(Object receiver, Object o, long offset, char x, TagFrame frame) {
        put(o, offset, frame, char[].class);
    }

    @Mask(owner = "jdk/internal/misc/Unsafe", name = "putDouble", type = MaskType.POST_PROCESS)
    @Mask(owner = "sun/misc/Unsafe", name = "putDouble", type = MaskType.POST_PROCESS)
    public static void putDouble(Object receiver, Object o, long offset, double x, TagFrame frame) {
        put(o, offset, frame, double[].class);
    }

    @Mask(owner = "jdk/internal/misc/Unsafe", name = "putFloat", type = MaskType.POST_PROCESS)
    @Mask(owner = "sun/misc/Unsafe", name = "putFloat", type = MaskType.POST_PROCESS)
    public static void putFloat(Object receiver, Object o, long offset, float x, TagFrame frame) {
        put(o, offset, frame, float[].class);
    }

    @Mask(owner = "jdk/internal/misc/Unsafe", name = "putInt", type = MaskType.POST_PROCESS)
    @Mask(owner = "sun/misc/Unsafe", name = "putInt", type = MaskType.POST_PROCESS)
    public static void putInt(Object receiver, Object o, long offset, int x, TagFrame frame) {
        put(o, offset, frame, int[].class);
    }

    @Mask(owner = "jdk/internal/misc/Unsafe", name = "putLong", type = MaskType.POST_PROCESS)
    @Mask(owner = "sun/misc/Unsafe", name = "putLong", type = MaskType.POST_PROCESS)
    public static void putLong(Object receiver, Object o, long offset, long x, TagFrame frame) {
        put(o, offset, frame, long[].class);
    }

    @Mask(owner = "jdk/internal/misc/Unsafe", name = "putShort", type = MaskType.POST_PROCESS)
    @Mask(owner = "sun/misc/Unsafe", name = "putShort", type = MaskType.POST_PROCESS)
    public static void putShort(Object receiver, Object o, long offset, short x, TagFrame frame) {
        put(o, offset, frame, short[].class);
    }

    @Mask(owner = "jdk/internal/misc/Unsafe", name = "putObject", type = MaskType.POST_PROCESS)
    @Mask(owner = "jdk/internal/misc/Unsafe", name = "putReference", type = MaskType.POST_PROCESS)
    @Mask(owner = "sun/misc/Unsafe", name = "putObject", type = MaskType.POST_PROCESS)
    public static void putObject(Object receiver, Object o, long offset, Object x, TagFrame frame) {
        put(o, offset, frame, Object[].class);
    }

    @Mask(owner = "jdk/internal/misc/Unsafe", name = "putBooleanVolatile", type = MaskType.POST_PROCESS)
    @Mask(owner = "sun/misc/Unsafe", name = "putBooleanVolatile", type = MaskType.POST_PROCESS)
    public static void putBooleanVolatile(Object receiver, Object o, long offset, boolean x, TagFrame frame) {
        putVolatile(o, offset, frame, boolean[].class);
    }

    @Mask(owner = "jdk/internal/misc/Unsafe", name = "putByteVolatile", type = MaskType.POST_PROCESS)
    @Mask(owner = "sun/misc/Unsafe", name = "putByteVolatile", type = MaskType.POST_PROCESS)
    public static void putByteVolatile(Object receiver, Object o, long offset, byte x, TagFrame frame) {
        putVolatile(o, offset, frame, byte[].class);
    }

    @Mask(owner = "jdk/internal/misc/Unsafe", name = "putCharVolatile", type = MaskType.POST_PROCESS)
    @Mask(owner = "sun/misc/Unsafe", name = "putCharVolatile", type = MaskType.POST_PROCESS)
    public static void putCharVolatile(Object receiver, Object o, long offset, char x, TagFrame frame) {
        putVolatile(o, offset, frame, char[].class);
    }

    @Mask(owner = "jdk/internal/misc/Unsafe", name = "putDoubleVolatile", type = MaskType.POST_PROCESS)
    @Mask(owner = "sun/misc/Unsafe", name = "putDoubleVolatile", type = MaskType.POST_PROCESS)
    public static void putDoubleVolatile(Object receiver, Object o, long offset, double x, TagFrame frame) {
        putVolatile(o, offset, frame, double[].class);
    }

    @Mask(owner = "jdk/internal/misc/Unsafe", name = "putFloatVolatile", type = MaskType.POST_PROCESS)
    @Mask(owner = "sun/misc/Unsafe", name = "putFloatVolatile", type = MaskType.POST_PROCESS)
    public static void putFloatVolatile(Object receiver, Object o, long offset, float x, TagFrame frame) {
        putVolatile(o, offset, frame, float[].class);
    }

    @Mask(owner = "jdk/internal/misc/Unsafe", name = "putIntVolatile", type = MaskType.POST_PROCESS)
    @Mask(owner = "sun/misc/Unsafe", name = "putIntVolatile", type = MaskType.POST_PROCESS)
    public static void putIntVolatile(Object receiver, Object o, long offset, int x, TagFrame frame) {
        putVolatile(o, offset, frame, int[].class);
    }

    @Mask(owner = "jdk/internal/misc/Unsafe", name = "putLongVolatile", type = MaskType.POST_PROCESS)
    @Mask(owner = "sun/misc/Unsafe", name = "putLongVolatile", type = MaskType.POST_PROCESS)
    public static void putLongVolatile(Object receiver, Object o, long offset, long x, TagFrame frame) {
        putVolatile(o, offset, frame, long[].class);
    }

    @Mask(owner = "jdk/internal/misc/Unsafe", name = "putShortVolatile", type = MaskType.POST_PROCESS)
    @Mask(owner = "sun/misc/Unsafe", name = "putShortVolatile", type = MaskType.POST_PROCESS)
    public static void putShortVolatile(Object receiver, Object o, long offset, short x, TagFrame frame) {
        putVolatile(o, offset, frame, short[].class);
    }

    @Mask(owner = "jdk/internal/misc/Unsafe", name = "putObjectVolatile", type = MaskType.POST_PROCESS)
    @Mask(owner = "jdk/internal/misc/Unsafe", name = "putReferenceVolatile", type = MaskType.POST_PROCESS)
    @Mask(owner = "sun/misc/Unsafe", name = "putObjectVolatile", type = MaskType.POST_PROCESS)
    public static void putObjectVolatile(Object receiver, Object o, long offset, Object x, TagFrame frame) {
        putVolatile(o, offset, frame, Object[].class);
    }

    @Mask(owner = "jdk/internal/misc/Unsafe", name = "getBoolean", type = MaskType.POST_PROCESS)
    @Mask(owner = "sun/misc/Unsafe", name = "getBoolean", type = MaskType.POST_PROCESS)
    public static boolean getBoolean(boolean returnValue, Object receiver, Object o, long offset, TagFrame frame) {
        get(o, offset, frame, boolean[].class);
        return returnValue;
    }

    @Mask(owner = "jdk/internal/misc/Unsafe", name = "getByte", type = MaskType.POST_PROCESS)
    @Mask(owner = "sun/misc/Unsafe", name = "getByte", type = MaskType.POST_PROCESS)
    public static byte getByte(byte returnValue, Object receiver, Object o, long offset, TagFrame frame) {
        get(o, offset, frame, byte[].class);
        return returnValue;
    }

    @Mask(owner = "jdk/internal/misc/Unsafe", name = "getChar", type = MaskType.POST_PROCESS)
    @Mask(owner = "sun/misc/Unsafe", name = "getChar", type = MaskType.POST_PROCESS)
    public static char getChar(char returnValue, Object receiver, Object o, long offset, TagFrame frame) {
        get(o, offset, frame, char[].class);
        return returnValue;
    }

    @Mask(owner = "jdk/internal/misc/Unsafe", name = "getDouble", type = MaskType.POST_PROCESS)
    @Mask(owner = "sun/misc/Unsafe", name = "getDouble", type = MaskType.POST_PROCESS)
    public static double getDouble(double returnValue, Object receiver, Object o, long offset, TagFrame frame) {
        get(o, offset, frame, double[].class);
        return returnValue;
    }

    @Mask(owner = "jdk/internal/misc/Unsafe", name = "getFloat", type = MaskType.POST_PROCESS)
    @Mask(owner = "sun/misc/Unsafe", name = "getFloat", type = MaskType.POST_PROCESS)
    public static float getFloat(float returnValue, Object receiver, Object o, long offset, TagFrame frame) {
        get(o, offset, frame, float[].class);
        return returnValue;
    }

    @Mask(owner = "jdk/internal/misc/Unsafe", name = "getInt", type = MaskType.POST_PROCESS)
    @Mask(owner = "sun/misc/Unsafe", name = "getInt", type = MaskType.POST_PROCESS)
    public static int getInt(int returnValue, Object receiver, Object o, long offset, TagFrame frame) {
        get(o, offset, frame, int[].class);
        return returnValue;
    }

    @Mask(owner = "jdk/internal/misc/Unsafe", name = "getLong", type = MaskType.POST_PROCESS)
    @Mask(owner = "sun/misc/Unsafe", name = "getLong", type = MaskType.POST_PROCESS)
    public static long getLong(long returnValue, Object receiver, Object o, long offset, TagFrame frame) {
        get(o, offset, frame, long[].class);
        return returnValue;
    }

    @Mask(owner = "jdk/internal/misc/Unsafe", name = "getShort", type = MaskType.POST_PROCESS)
    @Mask(owner = "sun/misc/Unsafe", name = "getShort", type = MaskType.POST_PROCESS)
    public static short getShort(short returnValue, Object receiver, Object o, long offset, TagFrame frame) {
        get(o, offset, frame, short[].class);
        return returnValue;
    }

    @Mask(owner = "jdk/internal/misc/Unsafe", name = "getObject", type = MaskType.POST_PROCESS)
    @Mask(owner = "jdk/internal/misc/Unsafe", name = "getReference", type = MaskType.POST_PROCESS)
    @Mask(owner = "sun/misc/Unsafe", name = "getObject", type = MaskType.POST_PROCESS)
    public static Object getObject(Object returnValue, Object receiver, Object o, long offset, TagFrame frame) {
        get(o, offset, frame, Object[].class);
        return returnValue;
    }

    @Mask(owner = "jdk/internal/misc/Unsafe", name = "getBooleanVolatile", type = MaskType.POST_PROCESS)
    @Mask(owner = "sun/misc/Unsafe", name = "getBooleanVolatile", type = MaskType.POST_PROCESS)
    public static boolean getBooleanVolatile(
            boolean returnValue, Object receiver, Object o, long offset, TagFrame frame) {
        getVolatile(o, offset, frame, boolean[].class);
        return returnValue;
    }

    @Mask(owner = "jdk/internal/misc/Unsafe", name = "getByteVolatile", type = MaskType.POST_PROCESS)
    @Mask(owner = "sun/misc/Unsafe", name = "getByteVolatile", type = MaskType.POST_PROCESS)
    public static byte getByteVolatile(byte returnValue, Object receiver, Object o, long offset, TagFrame frame) {
        getVolatile(o, offset, frame, byte[].class);
        return returnValue;
    }

    @Mask(owner = "jdk/internal/misc/Unsafe", name = "getCharVolatile", type = MaskType.POST_PROCESS)
    @Mask(owner = "sun/misc/Unsafe", name = "getCharVolatile", type = MaskType.POST_PROCESS)
    public static char getCharVolatile(char returnValue, Object receiver, Object o, long offset, TagFrame frame) {
        getVolatile(o, offset, frame, char[].class);
        return returnValue;
    }

    @Mask(owner = "jdk/internal/misc/Unsafe", name = "getDoubleVolatile", type = MaskType.POST_PROCESS)
    @Mask(owner = "sun/misc/Unsafe", name = "getDoubleVolatile", type = MaskType.POST_PROCESS)
    public static double getDoubleVolatile(double returnValue, Object receiver, Object o, long offset, TagFrame frame) {
        getVolatile(o, offset, frame, double[].class);
        return returnValue;
    }

    @Mask(owner = "jdk/internal/misc/Unsafe", name = "getFloatVolatile", type = MaskType.POST_PROCESS)
    @Mask(owner = "sun/misc/Unsafe", name = "getFloatVolatile", type = MaskType.POST_PROCESS)
    public static float getFloatVolatile(float returnValue, Object receiver, Object o, long offset, TagFrame frame) {
        getVolatile(o, offset, frame, float[].class);
        return returnValue;
    }

    @Mask(owner = "jdk/internal/misc/Unsafe", name = "getIntVolatile", type = MaskType.POST_PROCESS)
    @Mask(owner = "sun/misc/Unsafe", name = "getIntVolatile", type = MaskType.POST_PROCESS)
    public static int getIntVolatile(int returnValue, Object receiver, Object o, long offset, TagFrame frame) {
        getVolatile(o, offset, frame, int[].class);
        return returnValue;
    }

    @Mask(owner = "jdk/internal/misc/Unsafe", name = "getLongVolatile", type = MaskType.POST_PROCESS)
    @Mask(owner = "sun/misc/Unsafe", name = "getLongVolatile", type = MaskType.POST_PROCESS)
    public static long getLongVolatile(long returnValue, Object receiver, Object o, long offset, TagFrame frame) {
        getVolatile(o, offset, frame, long[].class);
        return returnValue;
    }

    @Mask(owner = "jdk/internal/misc/Unsafe", name = "getShortVolatile", type = MaskType.POST_PROCESS)
    @Mask(owner = "sun/misc/Unsafe", name = "getShortVolatile", type = MaskType.POST_PROCESS)
    public static short getShortVolatile(short returnValue, Object receiver, Object o, long offset, TagFrame frame) {
        getVolatile(o, offset, frame, short[].class);
        return returnValue;
    }

    @Mask(owner = "jdk/internal/misc/Unsafe", name = "getObjectVolatile", type = MaskType.POST_PROCESS)
    @Mask(owner = "jdk/internal/misc/Unsafe", name = "getReferenceVolatile", type = MaskType.POST_PROCESS)
    @Mask(owner = "sun/misc/Unsafe", name = "getObjectVolatile", type = MaskType.POST_PROCESS)
    public static Object getObjectVolatile(Object returnValue, Object receiver, Object o, long offset, TagFrame frame) {
        getVolatile(o, offset, frame, Object[].class);
        return returnValue;
    }

    @Mask(owner = "sun/misc/Unsafe", name = "putOrderedInt", type = MaskType.POST_PROCESS)
    public static void putOrderedInt(Object receiver, Object o, long offset, int x, TagFrame frame) {
        putVolatile(o, offset, frame, int[].class);
    }

    @Mask(owner = "sun/misc/Unsafe", name = "putOrderedLong", type = MaskType.POST_PROCESS)
    public static void putOrderedLong(Object receiver, Object o, long offset, long x, TagFrame frame) {
        putVolatile(o, offset, frame, long[].class);
    }

    @Mask(owner = "sun/misc/Unsafe", name = "putOrderedObject", type = MaskType.POST_PROCESS)
    public static void putOrderedObject(Object receiver, Object o, long offset, Object x, TagFrame frame) {
        putVolatile(o, offset, frame, Object[].class);
    }

    @Mask(owner = "jdk/internal/misc/Unsafe", name = "compareAndSetInt", type = MaskType.POST_PROCESS)
    @Mask(owner = "sun/misc/Unsafe", name = "compareAndSwapInt", type = MaskType.POST_PROCESS)
    public static boolean compareAndSwapInt(
            boolean result, Object receiver, Object o, long offset, int expected, int x, TagFrame frame) {
        return compareAndSwap(result, o, offset, frame, int[].class);
    }

    @Mask(owner = "jdk/internal/misc/Unsafe", name = "compareAndSetLong", type = MaskType.POST_PROCESS)
    @Mask(owner = "sun/misc/Unsafe", name = "compareAndSwapLong", type = MaskType.POST_PROCESS)
    public static boolean compareAndSwapLong(
            boolean result, Object receiver, Object o, long offset, long expected, long x, TagFrame frame) {
        return compareAndSwap(result, o, offset, frame, long[].class);
    }

    @Mask(owner = "jdk/internal/misc/Unsafe", name = "compareAndSetObject", type = MaskType.POST_PROCESS)
    @Mask(owner = "jdk/internal/misc/Unsafe", name = "compareAndSetReference", type = MaskType.POST_PROCESS)
    @Mask(owner = "sun/misc/Unsafe", name = "compareAndSwapObject", type = MaskType.POST_PROCESS)
    public static boolean compareAndSwapObject(
            boolean result, Object receiver, Object o, long offset, Object expected, Object x, TagFrame frame) {
        return compareAndSwap(result, o, offset, frame, Object[].class);
    }

    @Mask(owner = "jdk/internal/misc/Unsafe", name = "compareAndExchangeInt", type = MaskType.POST_PROCESS)
    public static int compareAndExchangeInt(
            int result, Object receiver, Object o, long offset, int expected, int x, TagFrame frame) {
        compareAndExchange(frame, o, offset, result == expected, int[].class);
        return result;
    }

    @Mask(owner = "jdk/internal/misc/Unsafe", name = "compareAndExchangeShort", type = MaskType.REPLACE)
    public static short compareAndExchangeShort(
            Object receiver, Object o, long offset, short expected, short x, TagFrame frame) {
        boolean free = UnsafeFlagAccessor.reserve();
        short result;
        try {
            result = UNSAFE.compareAndExchangeShort(o, offset, expected, x);
        } finally {
            if (free) {
                UnsafeFlagAccessor.free();
            }
        }
        compareAndExchange(frame, o, offset, result == expected, short[].class);
        return result;
    }

    @Mask(owner = "jdk/internal/misc/Unsafe", name = "compareAndExchangeByte", type = MaskType.REPLACE)
    public static byte compareAndExchangeByte(
            Object receiver, Object o, long offset, byte expected, byte x, TagFrame frame) {
        boolean free = UnsafeFlagAccessor.reserve();
        byte result;
        try {
            result = UNSAFE.compareAndExchangeByte(o, offset, expected, x);
        } finally {
            if (free) {
                UnsafeFlagAccessor.free();
            }
        }
        compareAndExchange(frame, o, offset, result == expected, byte[].class);
        return result;
    }

    @Mask(owner = "jdk/internal/misc/Unsafe", name = "compareAndExchangeLong", type = MaskType.POST_PROCESS)
    public static long compareAndExchangeLong(
            long result, Object receiver, Object o, long offset, long expected, long x, TagFrame frame) {
        compareAndExchange(frame, o, offset, result == expected, long[].class);
        return result;
    }

    @Mask(owner = "jdk/internal/misc/Unsafe", name = "compareAndExchangeObject", type = MaskType.POST_PROCESS)
    @Mask(owner = "jdk/internal/misc/Unsafe", name = "compareAndExchangeReference", type = MaskType.POST_PROCESS)
    public static Object compareAndExchangeObject(
            Object result, Object receiver, Object o, long offset, Object expected, Object x, TagFrame frame) {
        compareAndExchange(frame, o, offset, result == expected, Object[].class);
        return result;
    }

    @Mask(owner = "jdk/internal/misc/Unsafe", name = "byte2bool", type = MaskType.POST_PROCESS)
    public static boolean byte2bool(boolean returnValue, Object receiver, byte b, TagFrame frame) {
        Tag receiverTag = frame.dequeue();
        Tag valueTag = frame.dequeue();
        frame.setReturnTag(valueTag);
        return returnValue;
    }

    @Mask(owner = "jdk/internal/misc/Unsafe", name = "bool2byte", type = MaskType.POST_PROCESS)
    public static byte bool2byte(byte returnValue, Object receiver, boolean b, TagFrame frame) {
        Tag receiverTag = frame.dequeue();
        Tag valueTag = frame.dequeue();
        frame.setReturnTag(valueTag);
        return returnValue;
    }

    private static void put(Object o, long offset, TagFrame frame, Class<?> arrayType) {
        if (UnsafeFlagAccessor.reserve()) {
            try {
                Tag receiverTag = frame.dequeue();
                Tag oTag = frame.dequeue();
                Tag offsetTag = frame.dequeue();
                Tag xTag = frame.dequeue();
                UnsafeTagLocator.putTag(o, offset, offsetTag, xTag, arrayType);
            } finally {
                UnsafeFlagAccessor.free();
            }
        }
    }

    private static void putVolatile(Object o, long offset, TagFrame frame, Class<?> arrayType) {
        if (UnsafeFlagAccessor.reserve()) {
            try {
                Tag receiverTag = frame.dequeue();
                Tag oTag = frame.dequeue();
                Tag offsetTag = frame.dequeue();
                Tag xTag = frame.dequeue();
                UnsafeTagLocator.putTagVolatile(o, offset, offsetTag, xTag, arrayType);
            } finally {
                UnsafeFlagAccessor.free();
            }
        }
    }

    private static void get(Object o, long offset, TagFrame frame, Class<?> arrayType) {
        if (UnsafeFlagAccessor.reserve()) {
            try {
                Tag receiverTag = frame.dequeue();
                Tag oTag = frame.dequeue();
                Tag offsetTag = frame.dequeue();
                frame.setReturnTag(UnsafeTagLocator.getTag(o, offset, offsetTag, arrayType));
            } finally {
                UnsafeFlagAccessor.free();
            }
        }
    }

    private static void getVolatile(Object o, long offset, TagFrame frame, Class<?> arrayType) {
        if (UnsafeFlagAccessor.reserve()) {
            try {
                Tag receiverTag = frame.dequeue();
                Tag oTag = frame.dequeue();
                Tag offsetTag = frame.dequeue();
                frame.setReturnTag(UnsafeTagLocator.getTagVolatile(o, offset, offsetTag, arrayType));
            } finally {
                UnsafeFlagAccessor.free();
            }
        }
    }

    private static boolean compareAndSwap(boolean result, Object o, long offset, TagFrame frame, Class<?> arrayType) {
        if (UnsafeFlagAccessor.reserve()) {
            try {
                Tag receiverTag = frame.dequeue();
                Tag oTag = frame.dequeue();
                Tag offsetTag = frame.dequeue();
                Tag expectedTag = frame.dequeue();
                Tag xTag = frame.dequeue();
                frame.setReturnTag(Tag.getEmptyTag());
                if (result) {
                    // Swap succeeded, update the tag
                    // There is no way to swap both the tag and the value atomically
                    UnsafeTagLocator.putTagVolatile(o, offset, offsetTag, xTag, arrayType);
                }
            } finally {
                UnsafeFlagAccessor.free();
            }
        }
        return result;
    }

    private static void compareAndExchange(TagFrame frame, Object o, long offset, boolean result, Class<?> arrayType) {
        if (UnsafeFlagAccessor.reserve()) {
            try {
                Tag receiverTag = frame.dequeue();
                Tag oTag = frame.dequeue();
                Tag offsetTag = frame.dequeue();
                Tag expectedTag = frame.dequeue();
                Tag xTag = frame.dequeue();
                // Set the return tag to be the current value
                frame.setReturnTag(UnsafeTagLocator.getTagVolatile(o, offset, offsetTag, arrayType));
                if (result) {
                    // Exchange succeeded, update the tag
                    // There is no way to exchange both the tag and the value atomically
                    UnsafeTagLocator.putTagVolatile(o, offset, offsetTag, xTag, arrayType);
                }
            } finally {
                UnsafeFlagAccessor.free();
            }
        }
    }

    public static byte[] copy(byte[] b, int off, int len) {
        if (b != null && off >= 0 && len >= 0 && off + len <= b.length) {
            byte[] buffer = new byte[len];
            System.arraycopy(b, off, buffer, 0, len);
            return buffer;
        }
        return null;
    }
}
