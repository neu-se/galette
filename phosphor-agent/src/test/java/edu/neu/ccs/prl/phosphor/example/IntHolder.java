package edu.neu.ccs.prl.phosphor.example;

public class IntHolder {
    private int x = 7;

    public void setX(int x) {
        this.x = x;
    }

    public int getX() {
        return x;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (!(o instanceof IntHolder)) {
            return false;
        }
        IntHolder intHolder = (IntHolder) o;
        return x == intHolder.x;
    }

    @Override
    public int hashCode() {
        return x;
    }

    public static IntHolder getInstance() {
        return new IntHolder();
    }
}
