package edu.neu.ccs.prl.galette.internal.runtime;

public final class TagFrameFactory {
    private TagFrameFactory() {
        throw new AssertionError();
    }

    public static TagFrame acquire(TagFrame frame) {
        return frame.acquire(0);
    }

    public static TagFrame acquire(TagFrame frame, Tag t0) {
        return frame.acquire(1).set(0, t0);
    }

    public static TagFrame acquire(TagFrame frame, Tag t0, Tag t1) {
        return frame.acquire(2).set(0, t0).set(1, t1);
    }

    public static TagFrame acquire(TagFrame frame, Tag t0, Tag t1, Tag t2) {
        return frame.acquire(3).set(0, t0).set(1, t1).set(2, t2);
    }

    public static TagFrame acquire(TagFrame frame, Tag t0, Tag t1, Tag t2, Tag t3) {
        return frame.acquire(4).set(0, t0).set(1, t1).set(2, t2).set(3, t3);
    }
}
