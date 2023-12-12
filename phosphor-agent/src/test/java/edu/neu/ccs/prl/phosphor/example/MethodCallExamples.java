package edu.neu.ccs.prl.phosphor.example;

@SuppressWarnings("unused")
public class MethodCallExamples {
    public int invokeVirtualIntReturn(int i) {
        return i;
    }

    public long invokeVirtualLongReturn(long l) {
        return l;
    }

    public long invokeVirtualMixedParameterTypes(int x1, long x2, int x3) {
        return x1 + invokeVirtualLongReturn(x2) + invokeVirtualIntReturn(x3);
    }

    public static MethodCallExamples getInstance() {
        return new MethodCallExamples();
    }
}
