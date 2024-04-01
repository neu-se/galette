package edu.neu.ccs.prl.galette.bench;

public interface ConcatAdapter {
    String OPERAND = "<OPERAND>";

    String concat(boolean x, boolean prefix);

    String concat(byte x, boolean prefix);

    String concat(char x, boolean prefix);

    String concat(short x, boolean prefix);

    String concat(int x, boolean prefix);

    String concat(long x, boolean prefix);

    String concat(float x, boolean prefix);

    String concat(double x, boolean prefix);

    String concat(Object x, boolean prefix);

    String concatMany(char[] values);
}
