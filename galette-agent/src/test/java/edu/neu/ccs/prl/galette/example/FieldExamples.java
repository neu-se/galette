package edu.neu.ccs.prl.galette.example;

@SuppressWarnings("unused")
public class FieldExamples {
    public static int x = 7;
    public int y = 99;
    public static long x2 = 7L;
    public long y2 = 99L;

    public int getStaticInt() {
        return x;
    }

    public int getFieldInt() {
        return y;
    }

    public void putStaticInt(int x) {
        FieldExamples.x = x;
    }

    public void putFieldInt(int y) {
        this.y = y;
    }

    public long getStaticLong() {
        return x2;
    }

    public long getFieldLong() {
        return y2;
    }

    public void putStaticLong(long x2) {
        FieldExamples.x2 = x2;
    }

    public void putFieldLong(long y2) {
        this.y2 = y2;
    }

    public static FieldExamples getInstance() {
        return new FieldExamples();
    }
}
