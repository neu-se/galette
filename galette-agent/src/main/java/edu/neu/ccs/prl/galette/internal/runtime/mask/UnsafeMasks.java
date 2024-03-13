package edu.neu.ccs.prl.galette.internal.runtime.mask;

import edu.neu.ccs.prl.galette.internal.runtime.Tag;
import edu.neu.ccs.prl.galette.internal.runtime.TagFrame;
import edu.neu.ccs.prl.galette.internal.transform.GaletteTransformer;
import java.security.ProtectionDomain;

@SuppressWarnings("unused")
public final class UnsafeMasks {
    @Mask(owner = "jdk/internal/misc/Unsafe", name = "defineAnonymousClass0")
    @Mask(owner = "sun/misc/Unsafe", name = "defineAnonymousClass")
    public static Class<?> defineAnonymousClass(Object unsafe, Class<?> hostClass, byte[] data, Object[] cpPatches) {
        byte[] instrumented = GaletteTransformer.getInstanceAndTransform(data, true);
        return UnsafeAccessor.defineAnonymousClass(hostClass, instrumented, cpPatches);
    }

    @Mask(owner = "jdk/internal/misc/Unsafe", name = "defineClass0")
    @Mask(owner = "sun/misc/Unsafe", name = "defineClass")
    public static Class<?> defineClass(
            Object unsafe, String name, byte[] b, int off, int len, ClassLoader loader, ProtectionDomain domain) {
        byte[] buffer = copy(b, off, len);
        if (buffer != null) {
            byte[] instrumented = GaletteTransformer.getInstanceAndTransform(buffer, false);
            return UnsafeAccessor.defineClass(name, instrumented, 0, instrumented.length, loader, domain);
        }
        return UnsafeAccessor.defineClass(name, b, off, len, loader, domain);
    }

    @Mask(owner = "jdk/internal/misc/Unsafe", name = "putBoolean")
    @Mask(owner = "sun/misc/Unsafe", name = "putBoolean")
    public static void putBoolean(Object o, long offset, boolean x, TagFrame frame) {
        Tag oTag = frame.pop();
        Tag offsetTag = frame.pop();
        Tag xTag = frame.pop();
        UnsafeAccessor.putBoolean(o, offset, x);
        putTag(o, offset, xTag);
    }

    @Mask(owner = "jdk/internal/misc/Unsafe", name = "putByte")
    @Mask(owner = "sun/misc/Unsafe", name = "putByte")
    public static void putByte(Object o, long offset, byte x, TagFrame frame) {
        Tag oTag = frame.pop();
        Tag offsetTag = frame.pop();
        Tag xTag = frame.pop();
        UnsafeAccessor.putByte(o, offset, x);
        putTag(o, offset, xTag);
    }

    @Mask(owner = "jdk/internal/misc/Unsafe", name = "putChar")
    @Mask(owner = "sun/misc/Unsafe", name = "putChar")
    public static void putChar(Object o, long offset, char x, TagFrame frame) {
        Tag oTag = frame.pop();
        Tag offsetTag = frame.pop();
        Tag xTag = frame.pop();
        UnsafeAccessor.putChar(o, offset, x);
        putTag(o, offset, xTag);
    }

    @Mask(owner = "jdk/internal/misc/Unsafe", name = "putDouble")
    @Mask(owner = "sun/misc/Unsafe", name = "putDouble")
    public static void putDouble(Object o, long offset, double x, TagFrame frame) {
        Tag oTag = frame.pop();
        Tag offsetTag = frame.pop();
        Tag xTag = frame.pop();
        UnsafeAccessor.putDouble(o, offset, x);
        putTag(o, offset, xTag);
    }

    @Mask(owner = "jdk/internal/misc/Unsafe", name = "putFloat")
    @Mask(owner = "sun/misc/Unsafe", name = "putFloat")
    public static void putFloat(Object o, long offset, float x, TagFrame frame) {
        Tag oTag = frame.pop();
        Tag offsetTag = frame.pop();
        Tag xTag = frame.pop();
        UnsafeAccessor.putFloat(o, offset, x);
        putTag(o, offset, xTag);
    }

    @Mask(owner = "jdk/internal/misc/Unsafe", name = "putInt")
    @Mask(owner = "sun/misc/Unsafe", name = "putInt")
    public static void putInt(Object o, long offset, int x, TagFrame frame) {
        Tag oTag = frame.pop();
        Tag offsetTag = frame.pop();
        Tag xTag = frame.pop();
        UnsafeAccessor.putInt(o, offset, x);
        putTag(o, offset, xTag);
    }

    @Mask(owner = "jdk/internal/misc/Unsafe", name = "putLong")
    @Mask(owner = "sun/misc/Unsafe", name = "putLong")
    public static void putLong(Object o, long offset, long x, TagFrame frame) {
        Tag oTag = frame.pop();
        Tag offsetTag = frame.pop();
        Tag xTag = frame.pop();
        UnsafeAccessor.putLong(o, offset, x);
        putTag(o, offset, xTag);
    }

    @Mask(owner = "jdk/internal/misc/Unsafe", name = "putShort")
    @Mask(owner = "sun/misc/Unsafe", name = "putShort")
    public static void putShort(Object o, long offset, short x, TagFrame frame) {
        Tag oTag = frame.pop();
        Tag offsetTag = frame.pop();
        Tag xTag = frame.pop();
        UnsafeAccessor.putShort(o, offset, x);
        putTag(o, offset, xTag);
    }

    @Mask(owner = "jdk/internal/misc/Unsafe", name = "putObject")
    @Mask(owner = "jdk/internal/misc/Unsafe", name = "putReference")
    @Mask(owner = "sun/misc/Unsafe", name = "putObject")
    public static void putObject(Object o, long offset, Object x, TagFrame frame) {
        Tag oTag = frame.pop();
        Tag offsetTag = frame.pop();
        Tag xTag = frame.pop();
        UnsafeAccessor.putObject(o, offset, x);
        putTag(o, offset, xTag);
    }

    @Mask(owner = "jdk/internal/misc/Unsafe", name = "putBooleanVolatile")
    @Mask(owner = "sun/misc/Unsafe", name = "putBooleanVolatile")
    public static void putBooleanVolatile(Object o, long offset, boolean x, TagFrame frame) {
        Tag oTag = frame.pop();
        Tag offsetTag = frame.pop();
        Tag xTag = frame.pop();
        UnsafeAccessor.putBooleanVolatile(o, offset, x);
        putTagVolatile(o, offset, xTag);
    }

    @Mask(owner = "jdk/internal/misc/Unsafe", name = "putByteVolatile")
    @Mask(owner = "sun/misc/Unsafe", name = "putByteVolatile")
    public static void putByteVolatile(Object o, long offset, byte x, TagFrame frame) {
        Tag oTag = frame.pop();
        Tag offsetTag = frame.pop();
        Tag xTag = frame.pop();
        UnsafeAccessor.putByteVolatile(o, offset, x);
        putTagVolatile(o, offset, xTag);
    }

    @Mask(owner = "jdk/internal/misc/Unsafe", name = "putCharVolatile")
    @Mask(owner = "sun/misc/Unsafe", name = "putCharVolatile")
    public static void putCharVolatile(Object o, long offset, char x, TagFrame frame) {
        Tag oTag = frame.pop();
        Tag offsetTag = frame.pop();
        Tag xTag = frame.pop();
        UnsafeAccessor.putCharVolatile(o, offset, x);
        putTagVolatile(o, offset, xTag);
    }

    @Mask(owner = "jdk/internal/misc/Unsafe", name = "putDoubleVolatile")
    @Mask(owner = "sun/misc/Unsafe", name = "putDoubleVolatile")
    public static void putDoubleVolatile(Object o, long offset, double x, TagFrame frame) {
        Tag oTag = frame.pop();
        Tag offsetTag = frame.pop();
        Tag xTag = frame.pop();
        UnsafeAccessor.putDoubleVolatile(o, offset, x);
        putTagVolatile(o, offset, xTag);
    }

    @Mask(owner = "jdk/internal/misc/Unsafe", name = "putFloatVolatile")
    @Mask(owner = "sun/misc/Unsafe", name = "putFloatVolatile")
    public static void putFloatVolatile(Object o, long offset, float x, TagFrame frame) {
        Tag oTag = frame.pop();
        Tag offsetTag = frame.pop();
        Tag xTag = frame.pop();
        UnsafeAccessor.putFloatVolatile(o, offset, x);
        putTagVolatile(o, offset, xTag);
    }

    @Mask(owner = "jdk/internal/misc/Unsafe", name = "putIntVolatile")
    @Mask(owner = "sun/misc/Unsafe", name = "putIntVolatile")
    public static void putIntVolatile(Object o, long offset, int x, TagFrame frame) {
        Tag oTag = frame.pop();
        Tag offsetTag = frame.pop();
        Tag xTag = frame.pop();
        UnsafeAccessor.putIntVolatile(o, offset, x);
        putTagVolatile(o, offset, xTag);
    }

    @Mask(owner = "jdk/internal/misc/Unsafe", name = "putLongVolatile")
    @Mask(owner = "sun/misc/Unsafe", name = "putLongVolatile")
    public static void putLongVolatile(Object o, long offset, long x, TagFrame frame) {
        Tag oTag = frame.pop();
        Tag offsetTag = frame.pop();
        Tag xTag = frame.pop();
        UnsafeAccessor.putLongVolatile(o, offset, x);
        putTagVolatile(o, offset, xTag);
    }

    @Mask(owner = "jdk/internal/misc/Unsafe", name = "putShortVolatile")
    @Mask(owner = "sun/misc/Unsafe", name = "putShortVolatile")
    public static void putShortVolatile(Object o, long offset, short x, TagFrame frame) {
        Tag oTag = frame.pop();
        Tag offsetTag = frame.pop();
        Tag xTag = frame.pop();
        UnsafeAccessor.putShortVolatile(o, offset, x);
        putTagVolatile(o, offset, xTag);
    }

    @Mask(owner = "jdk/internal/misc/Unsafe", name = "putObjectVolatile")
    @Mask(owner = "jdk/internal/misc/Unsafe", name = "putReferenceVolatile")
    @Mask(owner = "sun/misc/Unsafe", name = "putObjectVolatile")
    public static void putObjectVolatile(Object o, long offset, Object x, TagFrame frame) {
        Tag oTag = frame.pop();
        Tag offsetTag = frame.pop();
        Tag xTag = frame.pop();
        UnsafeAccessor.putObjectVolatile(o, offset, x);
        putTagVolatile(o, offset, xTag);
    }

    @Mask(owner = "jdk/internal/misc/Unsafe", name = "getBoolean")
    @Mask(owner = "sun/misc/Unsafe", name = "getBoolean")
    public static boolean getBoolean(Object o, long offset, TagFrame frame) {
        Tag oTag = frame.pop();
        Tag offsetTag = frame.pop();
        boolean result = UnsafeAccessor.getBoolean(o, offset);
        frame.setReturnTag(getTag(o, offset));
        return result;
    }

    @Mask(owner = "jdk/internal/misc/Unsafe", name = "getByte")
    @Mask(owner = "sun/misc/Unsafe", name = "getByte")
    public static byte getByte(Object o, long offset, TagFrame frame) {
        Tag oTag = frame.pop();
        Tag offsetTag = frame.pop();
        byte result = UnsafeAccessor.getByte(o, offset);
        frame.setReturnTag(getTag(o, offset));
        return result;
    }

    @Mask(owner = "jdk/internal/misc/Unsafe", name = "getChar")
    @Mask(owner = "sun/misc/Unsafe", name = "getChar")
    public static char getChar(Object o, long offset, TagFrame frame) {
        Tag oTag = frame.pop();
        Tag offsetTag = frame.pop();
        char result = UnsafeAccessor.getChar(o, offset);
        frame.setReturnTag(getTag(o, offset));
        return result;
    }

    @Mask(owner = "jdk/internal/misc/Unsafe", name = "getDouble")
    @Mask(owner = "sun/misc/Unsafe", name = "getDouble")
    public static double getDouble(Object o, long offset, TagFrame frame) {
        Tag oTag = frame.pop();
        Tag offsetTag = frame.pop();
        double result = UnsafeAccessor.getDouble(o, offset);
        frame.setReturnTag(getTag(o, offset));
        return result;
    }

    @Mask(owner = "jdk/internal/misc/Unsafe", name = "getFloat")
    @Mask(owner = "sun/misc/Unsafe", name = "getFloat")
    public static float getFloat(Object o, long offset, TagFrame frame) {
        Tag oTag = frame.pop();
        Tag offsetTag = frame.pop();
        float result = UnsafeAccessor.getFloat(o, offset);
        frame.setReturnTag(getTag(o, offset));
        return result;
    }

    @Mask(owner = "jdk/internal/misc/Unsafe", name = "getInt")
    @Mask(owner = "sun/misc/Unsafe", name = "getInt")
    public static int getInt(Object o, long offset, TagFrame frame) {
        Tag oTag = frame.pop();
        Tag offsetTag = frame.pop();
        int result = UnsafeAccessor.getInt(o, offset);
        frame.setReturnTag(getTag(o, offset));
        return result;
    }

    @Mask(owner = "jdk/internal/misc/Unsafe", name = "getLong")
    @Mask(owner = "sun/misc/Unsafe", name = "getLong")
    public static long getLong(Object o, long offset, TagFrame frame) {
        Tag oTag = frame.pop();
        Tag offsetTag = frame.pop();
        long result = UnsafeAccessor.getLong(o, offset);
        frame.setReturnTag(getTag(o, offset));
        return result;
    }

    @Mask(owner = "jdk/internal/misc/Unsafe", name = "getShort")
    @Mask(owner = "sun/misc/Unsafe", name = "getShort")
    public static short getShort(Object o, long offset, TagFrame frame) {
        Tag oTag = frame.pop();
        Tag offsetTag = frame.pop();
        short result = UnsafeAccessor.getShort(o, offset);
        frame.setReturnTag(getTag(o, offset));
        return result;
    }

    @Mask(owner = "jdk/internal/misc/Unsafe", name = "getObject")
    @Mask(owner = "jdk/internal/misc/Unsafe", name = "getReference")
    @Mask(owner = "sun/misc/Unsafe", name = "getObject")
    public static Object getObject(Object o, long offset, TagFrame frame) {
        Tag oTag = frame.pop();
        Tag offsetTag = frame.pop();
        Object result = UnsafeAccessor.getObject(o, offset);
        frame.setReturnTag(getTag(o, offset));
        return result;
    }

    @Mask(owner = "jdk/internal/misc/Unsafe", name = "getBooleanVolatile")
    @Mask(owner = "sun/misc/Unsafe", name = "getBooleanVolatile")
    public static boolean getBooleanVolatile(Object o, long offset, TagFrame frame) {
        Tag oTag = frame.pop();
        Tag offsetTag = frame.pop();
        boolean result = UnsafeAccessor.getBooleanVolatile(o, offset);
        frame.setReturnTag(getTag(o, offset));
        return result;
    }

    @Mask(owner = "jdk/internal/misc/Unsafe", name = "getByteVolatile")
    @Mask(owner = "sun/misc/Unsafe", name = "getByteVolatile")
    public static byte getByteVolatile(Object o, long offset, TagFrame frame) {
        Tag oTag = frame.pop();
        Tag offsetTag = frame.pop();
        byte result = UnsafeAccessor.getByteVolatile(o, offset);
        frame.setReturnTag(getTag(o, offset));
        return result;
    }

    @Mask(owner = "jdk/internal/misc/Unsafe", name = "getCharVolatile")
    @Mask(owner = "sun/misc/Unsafe", name = "getCharVolatile")
    public static char getCharVolatile(Object o, long offset, TagFrame frame) {
        Tag oTag = frame.pop();
        Tag offsetTag = frame.pop();
        char result = UnsafeAccessor.getCharVolatile(o, offset);
        frame.setReturnTag(getTag(o, offset));
        return result;
    }

    @Mask(owner = "jdk/internal/misc/Unsafe", name = "getDoubleVolatile")
    @Mask(owner = "sun/misc/Unsafe", name = "getDoubleVolatile")
    public static double getDoubleVolatile(Object o, long offset, TagFrame frame) {
        Tag oTag = frame.pop();
        Tag offsetTag = frame.pop();
        double result = UnsafeAccessor.getDoubleVolatile(o, offset);
        frame.setReturnTag(getTag(o, offset));
        return result;
    }

    @Mask(owner = "jdk/internal/misc/Unsafe", name = "getFloatVolatile")
    @Mask(owner = "sun/misc/Unsafe", name = "getFloatVolatile")
    public static float getFloatVolatile(Object o, long offset, TagFrame frame) {
        Tag oTag = frame.pop();
        Tag offsetTag = frame.pop();
        float result = UnsafeAccessor.getFloatVolatile(o, offset);
        frame.setReturnTag(getTag(o, offset));
        return result;
    }

    @Mask(owner = "jdk/internal/misc/Unsafe", name = "getIntVolatile")
    @Mask(owner = "sun/misc/Unsafe", name = "getIntVolatile")
    public static int getIntVolatile(Object o, long offset, TagFrame frame) {
        Tag oTag = frame.pop();
        Tag offsetTag = frame.pop();
        int result = UnsafeAccessor.getIntVolatile(o, offset);
        frame.setReturnTag(getTag(o, offset));
        return result;
    }

    @Mask(owner = "jdk/internal/misc/Unsafe", name = "getLongVolatile")
    @Mask(owner = "sun/misc/Unsafe", name = "getLongVolatile")
    public static long getLongVolatile(Object o, long offset, TagFrame frame) {
        Tag oTag = frame.pop();
        Tag offsetTag = frame.pop();
        long result = UnsafeAccessor.getLongVolatile(o, offset);
        frame.setReturnTag(getTag(o, offset));
        return result;
    }

    @Mask(owner = "jdk/internal/misc/Unsafe", name = "getShortVolatile")
    @Mask(owner = "sun/misc/Unsafe", name = "getShortVolatile")
    public static short getShortVolatile(Object o, long offset, TagFrame frame) {
        Tag oTag = frame.pop();
        Tag offsetTag = frame.pop();
        short result = UnsafeAccessor.getShortVolatile(o, offset);
        frame.setReturnTag(getTag(o, offset));
        return result;
    }

    @Mask(owner = "jdk/internal/misc/Unsafe", name = "getObjectVolatile")
    @Mask(owner = "jdk/internal/misc/Unsafe", name = "getReferenceVolatile")
    @Mask(owner = "sun/misc/Unsafe", name = "getObjectVolatile")
    public static Object getObjectVolatile(Object o, long offset, TagFrame frame) {
        Tag oTag = frame.pop();
        Tag offsetTag = frame.pop();
        Object result = UnsafeAccessor.getObjectVolatile(o, offset);
        frame.setReturnTag(getTag(o, offset));
        return result;
    }

    @Mask(owner = "sun/misc/Unsafe", name = "putOrderedInt")
    public static void putOrderedInt(Object o, long offset, int x, TagFrame frame) {
        Tag oTag = frame.pop();
        Tag offsetTag = frame.pop();
        Tag xTag = frame.pop();
        UnsafeAccessor.putOrderedInt(o, offset, x);
        putTagVolatile(o, offset, xTag);
    }

    @Mask(owner = "sun/misc/Unsafe", name = "putOrderedLong")
    public static void putOrderedLong(Object o, long offset, long x, TagFrame frame) {
        Tag oTag = frame.pop();
        Tag offsetTag = frame.pop();
        Tag xTag = frame.pop();
        UnsafeAccessor.putOrderedLong(o, offset, x);
        putTagVolatile(o, offset, xTag);
    }

    @Mask(owner = "sun/misc/Unsafe", name = "putOrderedObject")
    public static void putOrderedObject(Object o, long offset, Object x, TagFrame frame) {
        Tag oTag = frame.pop();
        Tag offsetTag = frame.pop();
        Tag xTag = frame.pop();
        UnsafeAccessor.putOrderedObject(o, offset, x);
        putTagVolatile(o, offset, xTag);
    }

    @Mask(owner = "jdk/internal/misc/Unsafe", name = "compareAndSetInt")
    @Mask(owner = "sun/misc/Unsafe", name = "compareAndSwapInt")
    public static boolean compareAndSwapInt(Object o, long offset, int expected, int x, TagFrame frame) {
        Tag oTag = frame.pop();
        Tag offsetTag = frame.pop();
        Tag expectedTag = frame.pop();
        Tag xTag = frame.pop();
        boolean result = UnsafeAccessor.compareAndSwapInt(o, offset, expected, x);
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
    public static boolean compareAndSwapLong(Object o, long offset, int expected, long x, TagFrame frame) {
        Tag oTag = frame.pop();
        Tag offsetTag = frame.pop();
        Tag expectedTag = frame.pop();
        Tag xTag = frame.pop();
        boolean result = UnsafeAccessor.compareAndSwapLong(o, offset, expected, x);
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
    public static boolean compareAndSwapObject(Object o, long offset, int expected, Object x, TagFrame frame) {
        Tag oTag = frame.pop();
        Tag offsetTag = frame.pop();
        Tag expectedTag = frame.pop();
        Tag xTag = frame.pop();
        boolean result = UnsafeAccessor.compareAndSwapObject(o, offset, expected, x);
        frame.setReturnTag(Tag.getEmptyTag());
        if (result) {
            // Swap succeeded, update the tag
            // There is no way to swap both the tag and the value atomically
            putTagVolatile(o, offset, xTag);
        }
        return result;
    }

    @Mask(owner = "jdk/internal/misc/Unsafe", name = "compareAndExchangeInt")
    public static int compareAndExchangeInt(Object o, long offset, int expected, int x, TagFrame frame) {
        Tag oTag = frame.pop();
        Tag offsetTag = frame.pop();
        Tag expectedTag = frame.pop();
        Tag xTag = frame.pop();
        // Set the return tag to be the current value
        frame.setReturnTag(getTag(o, offset));
        int result = UnsafeAccessor.compareAndExchangeInt(o, offset, expected, x);
        if (result == expected) {
            // Exchange succeeded, update the tag
            // There is no way to exchange both the tag and the value atomically
            putTagVolatile(o, offset, xTag);
        }
        return result;
    }

    @Mask(owner = "jdk/internal/misc/Unsafe", name = "compareAndExchangeLong")
    public static long compareAndExchangeLong(Object o, long offset, long expected, long x, TagFrame frame) {
        Tag oTag = frame.pop();
        Tag offsetTag = frame.pop();
        Tag expectedTag = frame.pop();
        Tag xTag = frame.pop();
        // Set the return tag to be the current value
        frame.setReturnTag(getTag(o, offset));
        long result = UnsafeAccessor.compareAndExchangeLong(o, offset, expected, x);
        if (result == expected) {
            // Exchange succeeded, update the tag
            // There is no way to exchange both the tag and the value atomically
            putTagVolatile(o, offset, xTag);
        }
        return result;
    }

    @Mask(owner = "jdk/internal/misc/Unsafe", name = "compareAndExchangeObject")
    public static Object compareAndExchangeObject(Object o, long offset, Object expected, Object x, TagFrame frame) {
        Tag oTag = frame.pop();
        Tag offsetTag = frame.pop();
        Tag expectedTag = frame.pop();
        Tag xTag = frame.pop();
        // Set the return tag to be the current value
        frame.setReturnTag(getTag(o, offset));
        Object result = UnsafeAccessor.compareAndExchangeObject(o, offset, expected, x);
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
