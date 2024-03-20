package edu.neu.ccs.prl.galette.internal.runtime.frame;

import edu.neu.ccs.prl.galette.internal.runtime.collection.Queue;

class IndirectTagFrameAnnotater extends AbstractFrameAdjuster {
    private final Queue<Object> arguments = new Queue<>();
    private final IndirectTagFrame frame;

    IndirectTagFrameAnnotater(IndirectTagFrame frame) {
        if (frame == null) {
            throw new NullPointerException();
        }
        this.frame = frame;
    }

    @Override
    protected void processInternal(Object value) {
        arguments.enqueue(value);
    }

    @Override
    public IndirectTagFrame createFrame() {
        frame.setArguments(arguments);
        return frame;
    }
}
