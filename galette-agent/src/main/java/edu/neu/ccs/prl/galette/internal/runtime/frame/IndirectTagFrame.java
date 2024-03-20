package edu.neu.ccs.prl.galette.internal.runtime.frame;

import edu.neu.ccs.prl.galette.internal.runtime.Handle;
import edu.neu.ccs.prl.galette.internal.runtime.InvokedViaHandle;
import edu.neu.ccs.prl.galette.internal.runtime.TagFrame;
import edu.neu.ccs.prl.galette.internal.runtime.collection.Queue;

public class IndirectTagFrame extends TagFrame {
    private Queue<Object> arguments;

    @InvokedViaHandle(handle = Handle.INDIRECT_FRAME_CREATE_FOR_CALL)
    public static IndirectTagFrame createForCall(TagFrame callerFrame) {
        // TODO
        return new IndirectTagFrame();
    }

    void setArguments(Queue<Object> arguments) {
        this.arguments = arguments;
    }
}
