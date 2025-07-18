package edu.neu.ccs.prl.galette.internal.runtime.frame;

import edu.neu.ccs.prl.galette.internal.runtime.PrimitiveBoxer;
import edu.neu.ccs.prl.galette.internal.runtime.PrimitiveBoxer.*;
import edu.neu.ccs.prl.galette.internal.runtime.Tag;
import edu.neu.ccs.prl.galette.internal.runtime.TagFrame;

class MatchingFrameAdjuster implements FrameAdjuster {
    private final TagFrame original;
    /**
     * Arguments passed by the caller.
     */
    private final Object[] arguments;
    /**
     * The number of arguments received by the callee that have been checked.
     */
    private int index = 0;

    MatchingFrameAdjuster(TagFrame original, Object[] arguments) {
        this.original = original;
        this.arguments = arguments;
    }

    @Override
    public FrameAdjuster process(boolean value) {
        if (index < arguments.length) {
            Object actual = arguments[index++];
            if (actual instanceof BoxedBoolean && ((BoxedBoolean) actual).getValue() == value) {
                return this;
            } else if (actual instanceof BoxedInt && ((BoxedInt) actual).getValue() == (value ? 1 : 0)) {
                return this;
            }
        }
        return new EmptyFrameAdjuster();
    }

    @Override
    public FrameAdjuster process(byte value) {
        if (index < arguments.length) {
            Object actual = arguments[index++];
            if (actual instanceof BoxedByte && ((BoxedByte) actual).getValue() == value) {
                return this;
            } else if (actual instanceof BoxedInt && ((BoxedInt) actual).getValue() == value) {
                return this;
            }
        }
        return new EmptyFrameAdjuster();
    }

    @Override
    public FrameAdjuster process(char value) {
        if (index < arguments.length) {
            Object actual = arguments[index++];
            if (actual instanceof BoxedChar && ((BoxedChar) actual).getValue() == value) {
                return this;
            } else if (actual instanceof BoxedInt && ((BoxedInt) actual).getValue() == value) {
                return this;
            }
        }
        return new EmptyFrameAdjuster();
    }

    @Override
    public FrameAdjuster process(short value) {
        if (index < arguments.length) {
            Object actual = arguments[index++];
            if (actual instanceof BoxedShort && ((BoxedShort) actual).getValue() == value) {
                return this;
            } else if (actual instanceof BoxedInt && ((BoxedInt) actual).getValue() == value) {
                return this;
            }
        }
        return new EmptyFrameAdjuster();
    }

    @Override
    public FrameAdjuster process(int value) {
        if (index < arguments.length) {
            Object actual = arguments[index++];
            if (actual instanceof BoxedStackInt && ((BoxedStackInt) actual).getIntValue() == value) {
                return this;
            }
        }
        return new EmptyFrameAdjuster();
    }

    @Override
    public FrameAdjuster process(long value) {
        if (index < arguments.length) {
            Object actual = arguments[index++];
            if (actual instanceof BoxedLong && ((BoxedLong) actual).getValue() == value) {
                return this;
            }
        }
        return new EmptyFrameAdjuster();
    }

    @Override
    public FrameAdjuster process(float value) {
        if (index < arguments.length) {
            Object actual = arguments[index++];
            if (actual instanceof BoxedFloat && ((BoxedFloat) actual).getValue() == value) {
                return this;
            }
        }
        return new EmptyFrameAdjuster();
    }

    @Override
    public FrameAdjuster process(double value) {
        if (index < arguments.length) {
            Object actual = arguments[index++];
            if (actual instanceof BoxedDouble && ((BoxedDouble) actual).getValue() == value) {
                return this;
            }
        }
        return new EmptyFrameAdjuster();
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
            return new EmptyFrameAdjuster();
        }
    }

    @Override
    public TagFrame createFrame() {
        return checkRemaining() ? original : SpareFrameStore.getAndClear();
    }

    @Override
    public Tag[] copyTags() {
        return checkRemaining() ? original.copyTags() : null;
    }

    private boolean checkRemaining() {
        for (int i = index; i < arguments.length; i++) {
            if (PrimitiveBoxer.isBoxed(arguments[i])) {
                // Unexpected trailing primitive value in caller arguments
                return false;
            }
        }
        return true;
    }
}
