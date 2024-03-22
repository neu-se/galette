package edu.neu.ccs.prl.galette.internal.runtime.mask;

import edu.neu.ccs.prl.galette.internal.runtime.ArrayTagStore;
import edu.neu.ccs.prl.galette.internal.runtime.Tag;
import edu.neu.ccs.prl.galette.internal.runtime.TagFrame;
import java.lang.reflect.Array;

public final class ArrayMasks {
    @Mask(owner = "java/lang/reflect/Array", name = "newInstance", isStatic = true)
    public static Object newInstance(Class<?> componentType, int length, TagFrame frame)
            throws NegativeArraySizeException {
        return newArray(componentType, length, frame);
    }

    @Mask(owner = "java/lang/reflect/Array", name = "newInstance", isStatic = true)
    public static Object newInstance(Class<?> componentType, int[] dimensions, TagFrame frame)
            throws IllegalArgumentException, NegativeArraySizeException {
        return multiNewArray(componentType, dimensions, frame);
    }

    @Mask(owner = "java/lang/reflect/Array", name = "getLength", isStatic = true)
    public static int getLength(Object array, TagFrame frame) throws IllegalArgumentException {
        Tag arrayTag = frame.dequeue();
        int result = Array.getLength(array);
        frame.setReturnTag(ArrayTagStore.getLengthTag(array, arrayTag));
        return result;
    }

    @Mask(owner = "java/lang/reflect/Array", name = "get", isStatic = true)
    public static Object get(Object array, int index, TagFrame frame)
            throws IllegalArgumentException, ArrayIndexOutOfBoundsException {
        Tag arrayTag = frame.dequeue();
        Tag indexTag = frame.dequeue();
        Object result = Array.get(array, index);
        frame.setReturnTag(ArrayTagStore.getTag(array, index, arrayTag, indexTag));
        return result;
    }

    @Mask(owner = "java/lang/reflect/Array", name = "getBoolean", isStatic = true)
    public static boolean getBoolean(Object array, int index, TagFrame frame)
            throws IllegalArgumentException, ArrayIndexOutOfBoundsException {
        Tag arrayTag = frame.dequeue();
        Tag indexTag = frame.dequeue();
        boolean result = Array.getBoolean(array, index);
        frame.setReturnTag(ArrayTagStore.getTag(array, index, arrayTag, indexTag));
        return result;
    }

    @Mask(owner = "java/lang/reflect/Array", name = "getByte", isStatic = true)
    public static byte getByte(Object array, int index, TagFrame frame)
            throws IllegalArgumentException, ArrayIndexOutOfBoundsException {
        Tag arrayTag = frame.dequeue();
        Tag indexTag = frame.dequeue();
        byte result = Array.getByte(array, index);
        frame.setReturnTag(ArrayTagStore.getTag(array, index, arrayTag, indexTag));
        return result;
    }

    @Mask(owner = "java/lang/reflect/Array", name = "getChar", isStatic = true)
    public static char getChar(Object array, int index, TagFrame frame)
            throws IllegalArgumentException, ArrayIndexOutOfBoundsException {
        Tag arrayTag = frame.dequeue();
        Tag indexTag = frame.dequeue();
        char result = Array.getChar(array, index);
        frame.setReturnTag(ArrayTagStore.getTag(array, index, arrayTag, indexTag));
        return result;
    }

    @Mask(owner = "java/lang/reflect/Array", name = "getShort", isStatic = true)
    public static short getShort(Object array, int index, TagFrame frame)
            throws IllegalArgumentException, ArrayIndexOutOfBoundsException {
        Tag arrayTag = frame.dequeue();
        Tag indexTag = frame.dequeue();
        short result = Array.getShort(array, index);
        frame.setReturnTag(ArrayTagStore.getTag(array, index, arrayTag, indexTag));
        return result;
    }

    @Mask(owner = "java/lang/reflect/Array", name = "getInt", isStatic = true)
    public static int getInt(Object array, int index, TagFrame frame)
            throws IllegalArgumentException, ArrayIndexOutOfBoundsException {
        Tag arrayTag = frame.dequeue();
        Tag indexTag = frame.dequeue();
        int result = Array.getInt(array, index);
        frame.setReturnTag(ArrayTagStore.getTag(array, index, arrayTag, indexTag));
        return result;
    }

    @Mask(owner = "java/lang/reflect/Array", name = "getLong", isStatic = true)
    public static long getLong(Object array, int index, TagFrame frame)
            throws IllegalArgumentException, ArrayIndexOutOfBoundsException {
        Tag arrayTag = frame.dequeue();
        Tag indexTag = frame.dequeue();
        long result = Array.getLong(array, index);
        frame.setReturnTag(ArrayTagStore.getTag(array, index, arrayTag, indexTag));
        return result;
    }

    @Mask(owner = "java/lang/reflect/Array", name = "getFloat", isStatic = true)
    public static float getFloat(Object array, int index, TagFrame frame)
            throws IllegalArgumentException, ArrayIndexOutOfBoundsException {
        Tag arrayTag = frame.dequeue();
        Tag indexTag = frame.dequeue();
        float result = Array.getFloat(array, index);
        frame.setReturnTag(ArrayTagStore.getTag(array, index, arrayTag, indexTag));
        return result;
    }

    @Mask(owner = "java/lang/reflect/Array", name = "getDouble", isStatic = true)
    public static double getDouble(Object array, int index, TagFrame frame)
            throws IllegalArgumentException, ArrayIndexOutOfBoundsException {
        Tag arrayTag = frame.dequeue();
        Tag indexTag = frame.dequeue();
        double result = Array.getDouble(array, index);
        frame.setReturnTag(ArrayTagStore.getTag(array, index, arrayTag, indexTag));
        return result;
    }

    @Mask(owner = "java/lang/reflect/Array", name = "set", isStatic = true)
    public static void set(Object array, int index, Object value, TagFrame frame)
            throws IllegalArgumentException, ArrayIndexOutOfBoundsException {
        Tag arrayTag = frame.dequeue();
        Tag indexTag = frame.dequeue();
        Tag valueTag = frame.dequeue();
        Array.set(array, index, value);
        ArrayTagStore.setTag(array, index, arrayTag, indexTag, valueTag);
    }

    @Mask(owner = "java/lang/reflect/Array", name = "setBoolean", isStatic = true)
    public static void setBoolean(Object array, int index, boolean value, TagFrame frame)
            throws IllegalArgumentException, ArrayIndexOutOfBoundsException {
        Tag arrayTag = frame.dequeue();
        Tag indexTag = frame.dequeue();
        Tag valueTag = frame.dequeue();
        Array.setBoolean(array, index, value);
        ArrayTagStore.setTag(array, index, arrayTag, indexTag, valueTag);
    }

    @Mask(owner = "java/lang/reflect/Array", name = "setByte", isStatic = true)
    public static void setByte(Object array, int index, byte value, TagFrame frame)
            throws IllegalArgumentException, ArrayIndexOutOfBoundsException {
        Tag arrayTag = frame.dequeue();
        Tag indexTag = frame.dequeue();
        Tag valueTag = frame.dequeue();
        Array.setByte(array, index, value);
        ArrayTagStore.setTag(array, index, arrayTag, indexTag, valueTag);
    }

    @Mask(owner = "java/lang/reflect/Array", name = "setChar", isStatic = true)
    public static void setChar(Object array, int index, char value, TagFrame frame)
            throws IllegalArgumentException, ArrayIndexOutOfBoundsException {
        Tag arrayTag = frame.dequeue();
        Tag indexTag = frame.dequeue();
        Tag valueTag = frame.dequeue();
        Array.setChar(array, index, value);
        ArrayTagStore.setTag(array, index, arrayTag, indexTag, valueTag);
    }

    @Mask(owner = "java/lang/reflect/Array", name = "setShort", isStatic = true)
    public static void setShort(Object array, int index, short value, TagFrame frame)
            throws IllegalArgumentException, ArrayIndexOutOfBoundsException {
        Tag arrayTag = frame.dequeue();
        Tag indexTag = frame.dequeue();
        Tag valueTag = frame.dequeue();
        Array.setShort(array, index, value);
        ArrayTagStore.setTag(array, index, arrayTag, indexTag, valueTag);
    }

    @Mask(owner = "java/lang/reflect/Array", name = "setInt", isStatic = true)
    public static void setInt(Object array, int index, int value, TagFrame frame)
            throws IllegalArgumentException, ArrayIndexOutOfBoundsException {
        Tag arrayTag = frame.dequeue();
        Tag indexTag = frame.dequeue();
        Tag valueTag = frame.dequeue();
        Array.setInt(array, index, value);
        ArrayTagStore.setTag(array, index, arrayTag, indexTag, valueTag);
    }

    @Mask(owner = "java/lang/reflect/Array", name = "setLong", isStatic = true)
    public static void setLong(Object array, int index, long value, TagFrame frame)
            throws IllegalArgumentException, ArrayIndexOutOfBoundsException {
        Tag arrayTag = frame.dequeue();
        Tag indexTag = frame.dequeue();
        Tag valueTag = frame.dequeue();
        Array.setLong(array, index, value);
        ArrayTagStore.setTag(array, index, arrayTag, indexTag, valueTag);
    }

    @Mask(owner = "java/lang/reflect/Array", name = "setFloat", isStatic = true)
    public static void setFloat(Object array, int index, float value, TagFrame frame)
            throws IllegalArgumentException, ArrayIndexOutOfBoundsException {
        Tag arrayTag = frame.dequeue();
        Tag indexTag = frame.dequeue();
        Tag valueTag = frame.dequeue();
        Array.setFloat(array, index, value);
        ArrayTagStore.setTag(array, index, arrayTag, indexTag, valueTag);
    }

    @Mask(owner = "java/lang/reflect/Array", name = "setDouble", isStatic = true)
    public static void setDouble(Object array, int index, double value, TagFrame frame)
            throws IllegalArgumentException, ArrayIndexOutOfBoundsException {
        Tag arrayTag = frame.dequeue();
        Tag indexTag = frame.dequeue();
        Tag valueTag = frame.dequeue();
        Array.setDouble(array, index, value);
        ArrayTagStore.setTag(array, index, arrayTag, indexTag, valueTag);
    }

    @Mask(owner = "java/lang/reflect/Array", name = "newArray", isStatic = true)
    public static Object newArray(Class<?> componentType, int length, TagFrame frame)
            throws NegativeArraySizeException {
        frame.dequeue();
        Tag lengthTag = frame.dequeue();
        Object result = ArrayAccessor.newArray(componentType, length);
        ArrayTagStore.setLengthTag(result, lengthTag);
        return result;
    }

    @Mask(owner = "java/lang/reflect/Array", name = "newArray", isStatic = true)
    public static Object multiNewArray(Class<?> componentType, int[] dimensions, TagFrame frame)
            throws IllegalArgumentException, NegativeArraySizeException {
        Object result = ArrayAccessor.multiNewArray(componentType, dimensions);
        ArrayTagStore.setLengthTags(result, dimensions);
        return result;
    }
}
