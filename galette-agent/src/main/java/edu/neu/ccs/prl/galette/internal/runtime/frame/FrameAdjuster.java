package edu.neu.ccs.prl.galette.internal.runtime.frame;

import edu.neu.ccs.prl.galette.internal.runtime.Handle;
import edu.neu.ccs.prl.galette.internal.runtime.InvokedViaHandle;
import edu.neu.ccs.prl.galette.internal.runtime.Tag;
import edu.neu.ccs.prl.galette.internal.runtime.TagFrame;

public interface FrameAdjuster {
    @InvokedViaHandle(handle = Handle.FRAME_ADJUSTER_PROCESS_BOOLEAN)
    FrameAdjuster process(boolean value);

    @InvokedViaHandle(handle = Handle.FRAME_ADJUSTER_PROCESS_BYTE)
    FrameAdjuster process(byte value);

    @InvokedViaHandle(handle = Handle.FRAME_ADJUSTER_PROCESS_CHAR)
    FrameAdjuster process(char value);

    @InvokedViaHandle(handle = Handle.FRAME_ADJUSTER_PROCESS_SHORT)
    FrameAdjuster process(short value);

    @InvokedViaHandle(handle = Handle.FRAME_ADJUSTER_PROCESS_INT)
    FrameAdjuster process(int value);

    @InvokedViaHandle(handle = Handle.FRAME_ADJUSTER_PROCESS_LONG)
    FrameAdjuster process(long value);

    @InvokedViaHandle(handle = Handle.FRAME_ADJUSTER_PROCESS_FLOAT)
    FrameAdjuster process(float value);

    @InvokedViaHandle(handle = Handle.FRAME_ADJUSTER_PROCESS_DOUBLE)
    FrameAdjuster process(double value);

    @InvokedViaHandle(handle = Handle.FRAME_ADJUSTER_PROCESS_OBJECT)
    FrameAdjuster process(Object value);

    @InvokedViaHandle(handle = Handle.FRAME_ADJUSTER_CREATE_FRAME)
    TagFrame createFrame();

    @InvokedViaHandle(handle = Handle.FRAME_ADJUSTER_COPY_TAGS)
    Tag[] copyTags();
}
