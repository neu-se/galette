package edu.neu.ccs.prl.galette.bench;

public class SunUnsafeITCase extends UnsafeBaseITCase {
    @Override
    UnsafeAdapter getUnsafe() {
        return new SunUnsafeAdapter();
    }
}
