package edu.neu.ccs.prl.galette.bench;

public class StringBuilderConcatITCase extends StringConcatBaseITCase {
    @Override
    ConcatAdapter getAdapter() {
        return new StringBuilderConcatAdapter();
    }
}
