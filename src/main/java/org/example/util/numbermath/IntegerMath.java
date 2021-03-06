package org.example.util.numbermath;

public final class IntegerMath extends NumberMath {

    public static final IntegerMath INSTANCE = new IntegerMath();

    private IntegerMath() {}

    protected Number absImpl(Number number) {
        return Integer.valueOf(Math.abs(number.intValue()));
    }

    public Number addImpl(Number left, Number right) {
        return Integer.valueOf(left.intValue() + right.intValue());
    }

    public Number subtractImpl(Number left, Number right) {
        return Integer.valueOf(left.intValue() - right.intValue());
    }

    public Number multiplyImpl(Number left, Number right) {
        return Integer.valueOf(left.intValue() * right.intValue());
    }

    public Number divideImpl(Number left, Number right) {
        return BigDecimalMath.INSTANCE.divideImpl(left, right);
    }

    public int compareToImpl(Number left, Number right) {
        int leftVal = left.intValue();
        int rightVal = right.intValue();
        return (leftVal<rightVal ? -1 : (leftVal==rightVal ? 0 : 1));
    }

    protected Number orImpl(Number left, Number right) {
        return Integer.valueOf(left.intValue() | right.intValue());
    }

    protected Number andImpl(Number left, Number right) {
        return Integer.valueOf(left.intValue() & right.intValue());
    }

    protected Number xorImpl(Number left, Number right) {
        return Integer.valueOf(left.intValue() ^ right.intValue());
    }

    protected Number intdivImpl(Number left, Number right) {
        return Integer.valueOf(left.intValue() / right.intValue());
    }

    protected Number modImpl(Number left, Number right) {
        return Integer.valueOf(left.intValue() % right.intValue());
    }

    protected Number unaryMinusImpl(Number left) {
        return Integer.valueOf(-left.intValue());
    }

    protected Number unaryPlusImpl(Number left) {
        return Integer.valueOf(left.intValue());
    }

    protected Number bitwiseNegateImpl(Number left) {
        return Integer.valueOf(~left.intValue());
    }

    protected Number leftShiftImpl(Number left, Number right) {
        return Integer.valueOf(left.intValue() << right.intValue());
    }

    protected Number rightShiftImpl(Number left, Number right) {
        return Integer.valueOf(left.intValue() >> right.intValue());
    }

    protected Number rightShiftUnsignedImpl(Number left, Number right) {
        return Integer.valueOf(left.intValue() >>> right.intValue());
    }
}
