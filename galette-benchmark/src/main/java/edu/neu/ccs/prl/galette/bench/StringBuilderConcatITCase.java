package edu.neu.ccs.prl.galette.bench;

public class StringBuilderConcatITCase extends StringConcatITCase {
    @Override
    ConcatAdapter getAdapter() {
        return new StringBuilderConcatAdapter();
    }
}
