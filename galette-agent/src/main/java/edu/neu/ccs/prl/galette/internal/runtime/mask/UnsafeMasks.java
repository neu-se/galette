package edu.neu.ccs.prl.galette.internal.runtime.mask;

import edu.neu.ccs.prl.galette.internal.runtime.Tag;
import edu.neu.ccs.prl.galette.internal.runtime.TagFrame;
import edu.neu.ccs.prl.galette.internal.transform.GaletteTransformer;
import java.security.ProtectionDomain;

@SuppressWarnings("unused")
public final class UnsafeMasks {
    private static final UnsafeWrapper UNSAFE = UnsafeWrapper.createInstance();

    @Mask(owner = "jdk/internal/misc/Unsafe", name = "defineAnonymousClass0")
    @Mask(owner = "sun/misc/Unsafe", name = "defineAnonymousClass")
    public static Class<?> defineAnonymousClass(Object receiver, Class<?> hostClass, byte[] data, Object[] cpPatches) {
        byte[] instrumented = GaletteTransformer.getInstanceAndTransform(data, true);
        return UNSAFE.defineAnonymousClass(hostClass, instrumented, cpPatches);
    }

    @Mask(owner = "jdk/internal/misc/Unsafe", name = "defineClass0")
    @Mask(owner = "sun/misc/Unsafe", name = "defineClass")
    public static Class<?> defineClass(
            Object receiver, String name, byte[] b, int off, int len, ClassLoader loader, ProtectionDomain domain) {
        byte[] buffer = copy(b, off, len);
        if (buffer != null) {
            byte[] instrumented = GaletteTransformer.getInstanceAndTransform(buffer, false);
            return UNSAFE.defineClass(name, instrumented, 0, instrumented.length, loader, domain);
        }
        return UNSAFE.defineClass(name, b, off, len, loader, domain);
    }

    @Mask(owner = "jdk/internal/misc/Unsafe", name = "putBoolean")
    @Mask(owner = "sun/misc/Unsafe", name = "putBoolean")
    public static void putBoolean(Object receiver, Object o, long offset, boolean x, TagFrame frame) {
        Tag oTag = frame.pop();
        Tag offsetTag = frame.pop();
        Tag xTag = frame.pop();
        UNSAFE.putBoolean(o, offset, x);
        putTag(o, offset, xTag);
    }

    @Mask(owner = "jdk/internal/misc/Unsafe", name = "putByte")
    @Mask(owner = "sun/misc/Unsafe", name = "putByte")
    public static void putByte(Object receiver, Object o, long offset, byte x, TagFrame frame) {
        Tag oTag = frame.pop();
        Tag offsetTag = frame.pop();
        Tag xTag = frame.pop();
        UNSAFE.putByte(o, offset, x);
        putTag(o, offset, xTag);
    }

    @Mask(owner = "jdk/internal/misc/Unsafe", name = "putChar")
    @Mask(owner = "sun/misc/Unsafe", name = "putChar")
    public static void putChar(Object receiver, Object o, long offset, char x, TagFrame frame) {
        Tag oTag = frame.pop();
        Tag offsetTag = frame.pop();
        Tag xTag = frame.pop();
        UNSAFE.putChar(o, offset, x);
        putTag(o, offset, xTag);
    }

    @Mask(owner = "jdk/internal/misc/Unsafe", name = "putDouble")
    @Mask(owner = "sun/misc/Unsafe", name = "putDouble")
    public static void putDouble(Object receiver, Object o, long offset, double x, TagFrame frame) {
        Tag oTag = frame.pop();
        Tag offsetTag = frame.pop();
        Tag xTag = frame.pop();
        UNSAFE.putDouble(o, offset, x);
        putTag(o, offset, xTag);
    }

    @Mask(owner = "jdk/internal/misc/Unsafe", name = "putFloat")
    @Mask(owner = "sun/misc/Unsafe", name = "putFloat")
    public static void putFloat(Object receiver, Object o, long offset, float x, TagFrame frame) {
        Tag oTag = frame.pop();
        Tag offsetTag = frame.pop();
        Tag xTag = frame.pop();
        UNSAFE.putFloat(o, offset, x);
        putTag(o, offset, xTag);
    }

    @Mask(owner = "jdk/internal/misc/Unsafe", name = "putInt")
    @Mask(owner = "sun/misc/Unsafe", name = "putInt")
    public static void putInt(Object receiver, Object o, long offset, int x, TagFrame frame) {
        Tag oTag = frame.pop();
        Tag offsetTag = frame.pop();
        Tag xTag = frame.pop();
        UNSAFE.putInt(o, offset, x);
        putTag(o, offset, xTag);
    }

    @Mask(owner = "jdk/internal/misc/Unsafe", name = "putLong")
    @Mask(owner = "sun/misc/Unsafe", name = "putLong")
    public static void putLong(Object receiver, Object o, long offset, long x, TagFrame frame) {
        Tag oTag = frame.pop();
        Tag offsetTag = frame.pop();
        Tag xTag = frame.pop();
        UNSAFE.putLong(o, offset, x);
        putTag(o, offset, xTag);
    }

    @Mask(owner = "jdk/internal/misc/Unsafe", name = "putShort")
    @Mask(owner = "sun/misc/Unsafe", name = "putShort")
    public static void putShort(Object receiver, Object o, long offset, short x, TagFrame frame) {
        Tag oTag = frame.pop();
        Tag offsetTag = frame.pop();
        Tag xTag = frame.pop();
        UNSAFE.putShort(o, offset, x);
        putTag(o, offset, xTag);
    }

    @Mask(owner = "jdk/internal/misc/Unsafe", name = "putObject")
    @Mask(owner = "jdk/internal/misc/Unsafe", name = "putReference")
    @Mask(owner = "sun/misc/Unsafe", name = "putObject")
    public static void putObject(Object receiver, Object o, long offset, Object x, TagFrame frame) {
        Tag oTag = frame.pop();
        Tag offsetTag = frame.pop();
        Tag xTag = frame.pop();
        UNSAFE.putObject(o, offset, x);
        putTag(o, offset, xTag);
    }

    @Mask(owner = "jdk/internal/misc/Unsafe", name = "putBooleanVolatile")
    @Mask(owner = "sun/misc/Unsafe", name = "putBooleanVolatile")
    public static void putBooleanVolatile(Object receiver, Object o, long offset, boolean x, TagFrame frame) {
        Tag oTag = frame.pop();
        Tag offsetTag = frame.pop();
        Tag xTag = frame.pop();
        UNSAFE.putBooleanVolatile(o, offset, x);
        putTagVolatile(o, offset, xTag);
    }

    @Mask(owner = "jdk/internal/misc/Unsafe", name = "putByteVolatile")
    @Mask(owner = "sun/misc/Unsafe", name = "putByteVolatile")
    public static void putByteVolatile(Object receiver, Object o, long offset, byte x, TagFrame frame) {
        Tag oTag = frame.pop();
        Tag offsetTag = frame.pop();
        Tag xTag = frame.pop();
        UNSAFE.putByteVolatile(o, offset, x);
        putTagVolatile(o, offset, xTag);
    }

    @Mask(owner = "jdk/internal/misc/Unsafe", name = "putCharVolatile")
    @Mask(owner = "sun/misc/Unsafe", name = "putCharVolatile")
    public static void putCharVolatile(Object receiver, Object o, long offset, char x, TagFrame frame) {
        Tag oTag = frame.pop();
        Tag offsetTag = frame.pop();
        Tag xTag = frame.pop();
        UNSAFE.putCharVolatile(o, offset, x);
        putTagVolatile(o, offset, xTag);
    }

    @Mask(owner = "jdk/internal/misc/Unsafe", name = "putDoubleVolatile")
    @Mask(owner = "sun/misc/Unsafe", name = "putDoubleVolatile")
    public static void putDoubleVolatile(Object receiver, Object o, long offset, double x, TagFrame frame) {
        Tag oTag = frame.pop();
        Tag offsetTag = frame.pop();
        Tag xTag = frame.pop();
        UNSAFE.putDoubleVolatile(o, offset, x);
        putTagVolatile(o, offset, xTag);
    }

    @Mask(owner = "jdk/internal/misc/Unsafe", name = "putFloatVolatile")
    @Mask(owner = "sun/misc/Unsafe", name = "putFloatVolatile")
    public static void putFloatVolatile(Object receiver, Object o, long offset, float x, TagFrame frame) {
        Tag oTag = frame.pop();
        Tag offsetTag = frame.pop();
        Tag xTag = frame.pop();
        UNSAFE.putFloatVolatile(o, offset, x);
        putTagVolatile(o, offset, xTag);
    }

    @Mask(owner = "jdk/internal/misc/Unsafe", name = "putIntVolatile")
    @Mask(owner = "sun/misc/Unsafe", name = "putIntVolatile")
    public static void putIntVolatile(Object receiver, Object o, long offset, int x, TagFrame frame) {
        Tag oTag = frame.pop();
        Tag offsetTag = frame.pop();
        Tag xTag = frame.pop();
        UNSAFE.putIntVolatile(o, offset, x);
        putTagVolatile(o, offset, xTag);
    }

    @Mask(owner = "jdk/internal/misc/Unsafe", name = "putLongVolatile")
    @Mask(owner = "sun/misc/Unsafe", name = "putLongVolatile")
    public static void putLongVolatile(Object receiver, Object o, long offset, long x, TagFrame frame) {
        Tag oTag = frame.pop();
        Tag offsetTag = frame.pop();
        Tag xTag = frame.pop();
        UNSAFE.putLongVolatile(o, offset, x);
        putTagVolatile(o, offset, xTag);
    }

    @Mask(owner = "jdk/internal/misc/Unsafe", name = "putShortVolatile")
    @Mask(owner = "sun/misc/Unsafe", name = "putShortVolatile")
    public static void putShortVolatile(Object receiver, Object o, long offset, short x, TagFrame frame) {
        Tag oTag = frame.pop();
        Tag offsetTag = frame.pop();
        Tag xTag = frame.pop();
        UNSAFE.putShortVolatile(o, offset, x);
        putTagVolatile(o, offset, xTag);
    }

    @Mask(owner = "jdk/internal/misc/Unsafe", name = "putObjectVolatile")
    @Mask(owner = "jdk/internal/misc/Unsafe", name = "putReferenceVolatile")
    @Mask(owner = "sun/misc/Unsafe", name = "putObjectVolatile")
    public static void putObjectVolatile(Object receiver, Object o, long offset, Object x, TagFrame frame) {
        Tag oTag = frame.pop();
        Tag offsetTag = frame.pop();
        Tag xTag = frame.pop();
        UNSAFE.putObjectVolatile(o, offset, x);
        putTagVolatile(o, offset, xTag);
    }

    @Mask(owner = "jdk/internal/misc/Unsafe", name = "getBoolean")
    @Mask(owner = "sun/misc/Unsafe", name = "getBoolean")
    public static boolean getBoolean(Object receiver, Object o, long offset, TagFrame frame) {
        Tag oTag = frame.pop();
        Tag offsetTag = frame.pop();
        boolean result = UNSAFE.getBoolean(o, offset);
        frame.setReturnTag(getTag(o, offset));
        return result;
    }

    @Mask(owner = "jdk/internal/misc/Unsafe", name = "getByte")
    @Mask(owner = "sun/misc/Unsafe", name = "getByte")
    public static byte getByte(Object receiver, Object o, long offset, TagFrame frame) {
        Tag oTag = frame.pop();
        Tag offsetTag = frame.pop();
        byte result = UNSAFE.getByte(o, offset);
        frame.setReturnTag(getTag(o, offset));
        return result;
    }

    @Mask(owner = "jdk/internal/misc/Unsafe", name = "getChar")
    @Mask(owner = "sun/misc/Unsafe", name = "getChar")
    public static char getChar(Object receiver, Object o, long offset, TagFrame frame) {
        Tag oTag = frame.pop();
        Tag offsetTag = frame.pop();
        char result = UNSAFE.getChar(o, offset);
        frame.setReturnTag(getTag(o, offset));
        return result;
    }

    @Mask(owner = "jdk/internal/misc/Unsafe", name = "getDouble")
    @Mask(owner = "sun/misc/Unsafe", name = "getDouble")
    public static double getDouble(Object receiver, Object o, long offset, TagFrame frame) {
        Tag oTag = frame.pop();
        Tag offsetTag = frame.pop();
        double result = UNSAFE.getDouble(o, offset);
        frame.setReturnTag(getTag(o, offset));
        return result;
    }

    @Mask(owner = "jdk/internal/misc/Unsafe", name = "getFloat")
    @Mask(owner = "sun/misc/Unsafe", name = "getFloat")
    public static float getFloat(Object receiver, Object o, long offset, TagFrame frame) {
        Tag oTag = frame.pop();
        Tag offsetTag = frame.pop();
        float result = UNSAFE.getFloat(o, offset);
        frame.setReturnTag(getTag(o, offset));
        return result;
    }

    @Mask(owner = "jdk/internal/misc/Unsafe", name = "getInt")
    @Mask(owner = "sun/misc/Unsafe", name = "getInt")
    public static int getInt(Object receiver, Object o, long offset, TagFrame frame) {
        Tag oTag = frame.pop();
        Tag offsetTag = frame.pop();
        int result = UNSAFE.getInt(o, offset);
        frame.setReturnTag(getTag(o, offset));
        return result;
    }

    @Mask(owner = "jdk/internal/misc/Unsafe", name = "getLong")
    @Mask(owner = "sun/misc/Unsafe", name = "getLong")
    public static long getLong(Object receiver, Object o, long offset, TagFrame frame) {
        Tag oTag = frame.pop();
        Tag offsetTag = frame.pop();
        long result = UNSAFE.getLong(o, offset);
        frame.setReturnTag(getTag(o, offset));
        return result;
    }

    @Mask(owner = "jdk/internal/misc/Unsafe", name = "getShort")
    @Mask(owner = "sun/misc/Unsafe", name = "getShort")
    public static short getShort(Object receiver, Object o, long offset, TagFrame frame) {
        Tag oTag = frame.pop();
        Tag offsetTag = frame.pop();
        short result = UNSAFE.getShort(o, offset);
        frame.setReturnTag(getTag(o, offset));
        return result;
    }

    @Mask(owner = "jdk/internal/misc/Unsafe", name = "getObject")
    @Mask(owner = "jdk/internal/misc/Unsafe", name = "getReference")
    @Mask(owner = "sun/misc/Unsafe", name = "getObject")
    public static Object getObject(Object receiver, Object o, long offset, TagFrame frame) {
        Tag oTag = frame.pop();
        Tag offsetTag = frame.pop();
        Object result = UNSAFE.getObject(o, offset);
        frame.setReturnTag(getTag(o, offset));
        return result;
    }

    @Mask(owner = "jdk/internal/misc/Unsafe", name = "getBooleanVolatile")
    @Mask(owner = "sun/misc/Unsafe", name = "getBooleanVolatile")
    public static boolean getBooleanVolatile(Object receiver, Object o, long offset, TagFrame frame) {
        Tag oTag = frame.pop();
        Tag offsetTag = frame.pop();
        boolean result = UNSAFE.getBooleanVolatile(o, offset);
        frame.setReturnTag(getTag(o, offset));
        return result;
    }

    @Mask(owner = "jdk/internal/misc/Unsafe", name = "getByteVolatile")
    @Mask(owner = "sun/misc/Unsafe", name = "getByteVolatile")
    public static byte getByteVolatile(Object receiver, Object o, long offset, TagFrame frame) {
        Tag oTag = frame.pop();
        Tag offsetTag = frame.pop();
        byte result = UNSAFE.getByteVolatile(o, offset);
        frame.setReturnTag(getTag(o, offset));
        return result;
    }

    @Mask(owner = "jdk/internal/misc/Unsafe", name = "getCharVolatile")
    @Mask(owner = "sun/misc/Unsafe", name = "getCharVolatile")
    public static char getCharVolatile(Object receiver, Object o, long offset, TagFrame frame) {
        Tag oTag = frame.pop();
        Tag offsetTag = frame.pop();
        char result = UNSAFE.getCharVolatile(o, offset);
        frame.setReturnTag(getTag(o, offset));
        return result;
    }

    @Mask(owner = "jdk/internal/misc/Unsafe", name = "getDoubleVolatile")
    @Mask(owner = "sun/misc/Unsafe", name = "getDoubleVolatile")
    public static double getDoubleVolatile(Object receiver, Object o, long offset, TagFrame frame) {
        Tag oTag = frame.pop();
        Tag offsetTag = frame.pop();
        double result = UNSAFE.getDoubleVolatile(o, offset);
        frame.setReturnTag(getTag(o, offset));
        return result;
    }

    @Mask(owner = "jdk/internal/misc/Unsafe", name = "getFloatVolatile")
    @Mask(owner = "sun/misc/Unsafe", name = "getFloatVolatile")
    public static float getFloatVolatile(Object receiver, Object o, long offset, TagFrame frame) {
        Tag oTag = frame.pop();
        Tag offsetTag = frame.pop();
        float result = UNSAFE.getFloatVolatile(o, offset);
        frame.setReturnTag(getTag(o, offset));
        return result;
    }

    @Mask(owner = "jdk/internal/misc/Unsafe", name = "getIntVolatile")
    @Mask(owner = "sun/misc/Unsafe", name = "getIntVolatile")
    public static int getIntVolatile(Object receiver, Object o, long offset, TagFrame frame) {
        Tag oTag = frame.pop();
        Tag offsetTag = frame.pop();
        int result = UNSAFE.getIntVolatile(o, offset);
        frame.setReturnTag(getTag(o, offset));
        return result;
    }

    @Mask(owner = "jdk/internal/misc/Unsafe", name = "getLongVolatile")
    @Mask(owner = "sun/misc/Unsafe", name = "getLongVolatile")
    public static long getLongVolatile(Object receiver, Object o, long offset, TagFrame frame) {
        Tag oTag = frame.pop();
        Tag offsetTag = frame.pop();
        long result = UNSAFE.getLongVolatile(o, offset);
        frame.setReturnTag(getTag(o, offset));
        return result;
    }

    @Mask(owner = "jdk/internal/misc/Unsafe", name = "getShortVolatile")
    @Mask(owner = "sun/misc/Unsafe", name = "getShortVolatile")
    public static short getShortVolatile(Object receiver, Object o, long offset, TagFrame frame) {
        Tag oTag = frame.pop();
        Tag offsetTag = frame.pop();
        short result = UNSAFE.getShortVolatile(o, offset);
        frame.setReturnTag(getTag(o, offset));
        return result;
    }

    @Mask(owner = "jdk/internal/misc/Unsafe", name = "getObjectVolatile")
    @Mask(owner = "jdk/internal/misc/Unsafe", name = "getReferenceVolatile")
    @Mask(owner = "sun/misc/Unsafe", name = "getObjectVolatile")
    public static Object getObjectVolatile(Object receiver, Object o, long offset, TagFrame frame) {
        Tag oTag = frame.pop();
        Tag offsetTag = frame.pop();
        Object result = UNSAFE.getObjectVolatile(o, offset);
        frame.setReturnTag(getTag(o, offset));
        return result;
    }

    @Mask(owner = "sun/misc/Unsafe", name = "putOrderedInt")
    public static void putOrderedInt(Object receiver, Object o, long offset, int x, TagFrame frame) {
        Tag oTag = frame.pop();
        Tag offsetTag = frame.pop();
        Tag xTag = frame.pop();
        UNSAFE.putOrderedInt(o, offset, x);
        putTagVolatile(o, offset, xTag);
    }

    @Mask(owner = "sun/misc/Unsafe", name = "putOrderedLong")
    public static void putOrderedLong(Object receiver, Object o, long offset, long x, TagFrame frame) {
        Tag oTag = frame.pop();
        Tag offsetTag = frame.pop();
        Tag xTag = frame.pop();
        UNSAFE.putOrderedLong(o, offset, x);
        putTagVolatile(o, offset, xTag);
    }

    @Mask(owner = "sun/misc/Unsafe", name = "putOrderedObject")
    public static void putOrderedObject(Object receiver, Object o, long offset, Object x, TagFrame frame) {
        Tag oTag = frame.pop();
        Tag offsetTag = frame.pop();
        Tag xTag = frame.pop();
        UNSAFE.putOrderedObject(o, offset, x);
        putTagVolatile(o, offset, xTag);
    }

    @Mask(owner = "jdk/internal/misc/Unsafe", name = "compareAndSetInt")
    @Mask(owner = "sun/misc/Unsafe", name = "compareAndSwapInt")
    public static boolean compareAndSwapInt(
            Object receiver, Object o, long offset, int expected, int x, TagFrame frame) {
        Tag oTag = frame.pop();
        Tag offsetTag = frame.pop();
        Tag expectedTag = frame.pop();
        Tag xTag = frame.pop();
        boolean result = UNSAFE.compareAndSwapInt(o, offset, expected, x);
        frame.setReturnTag(Tag.getEmptyTag());
        if (result) {
            // Swap succeeded, update the tag
            // There is no way to swap both the tag and the value atomically
            putTagVolatile(o, offset, xTag);
        }
        return result;
    }

    @Mask(owner = "jdk/internal/misc/Unsafe", name = "compareAndSetLong")
    @Mask(owner = "sun/misc/Unsafe", name = "compareAndSwapLong")
    public static boolean compareAndSwapLong(
            Object receiver, Object o, long offset, int expected, long x, TagFrame frame) {
        Tag oTag = frame.pop();
        Tag offsetTag = frame.pop();
        Tag expectedTag = frame.pop();
        Tag xTag = frame.pop();
        boolean result = UNSAFE.compareAndSwapLong(o, offset, expected, x);
        frame.setReturnTag(Tag.getEmptyTag());
        if (result) {
            // Swap succeeded, update the tag
            // There is no way to swap both the tag and the value atomically
            putTagVolatile(o, offset, xTag);
        }
        return result;
    }

    @Mask(owner = "jdk/internal/misc/Unsafe", name = "compareAndSetObject")
    @Mask(owner = "jdk/internal/misc/Unsafe", name = "compareAndSetReference")
    @Mask(owner = "sun/misc/Unsafe", name = "compareAndSwapObject")
    public static boolean compareAndSwapObject(
            Object receiver, Object o, long offset, int expected, Object x, TagFrame frame) {
        Tag oTag = frame.pop();
        Tag offsetTag = frame.pop();
        Tag expectedTag = frame.pop();
        Tag xTag = frame.pop();
        boolean result = UNSAFE.compareAndSwapObject(o, offset, expected, x);
        frame.setReturnTag(Tag.getEmptyTag());
        if (result) {
            // Swap succeeded, update the tag
            // There is no way to swap both the tag and the value atomically
            putTagVolatile(o, offset, xTag);
        }
        return result;
    }

    @Mask(owner = "jdk/internal/misc/Unsafe", name = "compareAndExchangeInt")
    public static int compareAndExchangeInt(
            Object receiver, Object o, long offset, int expected, int x, TagFrame frame) {
        Tag oTag = frame.pop();
        Tag offsetTag = frame.pop();
        Tag expectedTag = frame.pop();
        Tag xTag = frame.pop();
        // Set the return tag to be the current value
        frame.setReturnTag(getTag(o, offset));
        int result = UNSAFE.compareAndExchangeInt(o, offset, expected, x);
        if (result == expected) {
            // Exchange succeeded, update the tag
            // There is no way to exchange both the tag and the value atomically
            putTagVolatile(o, offset, xTag);
        }
        return result;
    }

    @Mask(owner = "jdk/internal/misc/Unsafe", name = "compareAndExchangeLong")
    public static long compareAndExchangeLong(
            Object receiver, Object o, long offset, long expected, long x, TagFrame frame) {
        Tag oTag = frame.pop();
        Tag offsetTag = frame.pop();
        Tag expectedTag = frame.pop();
        Tag xTag = frame.pop();
        // Set the return tag to be the current value
        frame.setReturnTag(getTag(o, offset));
        long result = UNSAFE.compareAndExchangeLong(o, offset, expected, x);
        if (result == expected) {
            // Exchange succeeded, update the tag
            // There is no way to exchange both the tag and the value atomically
            putTagVolatile(o, offset, xTag);
        }
        return result;
    }

    @Mask(owner = "jdk/internal/misc/Unsafe", name = "compareAndExchangeObject")
    public static Object compareAndExchangeObject(
            Object receiver, Object o, long offset, Object expected, Object x, TagFrame frame) {
        Tag oTag = frame.pop();
        Tag offsetTag = frame.pop();
        Tag expectedTag = frame.pop();
        Tag xTag = frame.pop();
        // Set the return tag to be the current value
        frame.setReturnTag(getTag(o, offset));
        Object result = UNSAFE.compareAndExchangeObject(o, offset, expected, x);
        if (result == expected) {
            // Exchange succeeded, update the tag
            // There is no way to exchange both the tag and the value atomically
            putTagVolatile(o, offset, xTag);
        }
        return result;
    }

    public static byte[] copy(byte[] b, int off, int len) {
        if (b != null && off >= 0 && len >= 0 && off + len <= b.length) {
            byte[] buffer = new byte[len];
            System.arraycopy(b, off, buffer, 0, len);
            return buffer;
        }
        return null;
    }

    private static void putTag(Object o, long offset, Tag tag) {
        // TODO
    }

    private static void putTagVolatile(Object o, long offset, Tag tag) {
        // TODO
    }

    private static Tag getTag(Object o, long offset) {
        // TODO
        return Tag.getEmptyTag();
    }

    private static Tag getTagVolatile(Object o, long offset) {
        // TODO
        return Tag.getEmptyTag();
    }
}
