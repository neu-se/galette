package edu.neu.ccs.prl.galette.internal.runtime.frame;

import edu.neu.ccs.prl.galette.internal.runtime.TagFrame;

class MatchingFrameAdjuster implements FrameAdjuster {
    private final TagFrame original;
    private final Object[] arguments;
    private int index = 0;

    MatchingFrameAdjuster(TagFrame original, Object[] arguments) {
        this.original = original;
        this.arguments = arguments;
    }

    @Override
    public FrameAdjuster process(boolean value) {
        if (index < arguments.length) {
            Object actual = arguments[index++];
            if (actual instanceof Boolean && (Boolean) actual == value) {
                return this;
            } else if (matchIntType(actual, value ? 1 : 0)) {
                return this;
            }
        }
        return new MismatchedFrameAdjuster(original, arguments);
    }

    @Override
    public FrameAdjuster process(byte value) {
        if (index < arguments.length) {
            Object actual = arguments[index++];
            if (actual instanceof Byte && (Byte) actual == value) {
                return this;
            } else if (matchIntType(actual, value)) {
                return this;
            }
        }
        return new MismatchedFrameAdjuster(original, arguments);
    }

    @Override
    public FrameAdjuster process(char value) {
        if (index < arguments.length) {
            Object actual = arguments[index++];
            if (actual instanceof Character && (Character) actual == value) {
                return this;
            } else if (matchIntType(actual, value)) {
                return this;
            }
        }
        return new MismatchedFrameAdjuster(original, arguments);
    }

    @Override
    public FrameAdjuster process(short value) {
        if (index < arguments.length) {
            Object actual = arguments[index++];
            if (actual instanceof Short && (Short) actual == value) {
                return this;
            } else if (matchIntType(actual, value)) {
                return this;
            }
        }
        return new MismatchedFrameAdjuster(original, arguments);
    }

    @Override
    public FrameAdjuster process(int value) {
        if (index < arguments.length) {
            Object actual = arguments[index++];
            if (matchIntType(actual, value)) {
                return this;
            }
        }
        return new MismatchedFrameAdjuster(original, arguments);
    }

    @Override
    public FrameAdjuster process(long value) {
        if (index < arguments.length) {
            Object actual = arguments[index++];
            if (actual instanceof Long && (Long) actual == value) {
                return this;
            }
        }
        return new MismatchedFrameAdjuster(original, arguments);
    }

    @Override
    public FrameAdjuster process(float value) {
        if (index < arguments.length) {
            Object actual = arguments[index++];
            if (actual instanceof Float && (Float) actual == value) {
                return this;
            }
        }
        return new MismatchedFrameAdjuster(original, arguments);
    }

    @Override
    public FrameAdjuster process(double value) {
        if (index < arguments.length) {
            Object actual = arguments[index++];
            if (actual instanceof Double && (Double) actual == value) {
                return this;
            }
        }
        return new MismatchedFrameAdjuster(original, arguments);
    }

    @Override
    public FrameAdjuster process(Object value) {
        if (index >= arguments.length) {
            // Possible added trailing suffix
            return this;
        } else {
            Object expected = arguments[index++];
            if (expected == value || index == 1 && value == IndirectTagFrameStore.getUninitializedThisMarker()) {
                // Since an uninitialized value cannot be passed as an argument, a placeholder is used instead
                return this;
            }
            return new MismatchedFrameAdjuster(original, arguments);
        }
    }

    @Override
    public TagFrame createFrame() {
        return new AdjustedTagFrame(original, arguments, original.copyTags());
    }

    private boolean matchIntType(Object actual, int value) {
        return actual instanceof Integer && (Integer) actual == value;
    }
}
