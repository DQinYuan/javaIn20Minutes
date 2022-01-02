package org.example.util.numbermath;

public final class LongMath extends NumberMath {

    public static final LongMath INSTANCE = new LongMath();

    private LongMath() {}

    protected Number absImpl(Number number) {
        return Long.valueOf(Math.abs(number.longValue()));
    }

    public Number addImpl(Number left, Number right) {
        return Long.valueOf(left.longValue() + right.longValue());
    }

    public Number subtractImpl(Number left, Number right) {
        return Long.valueOf(left.longValue() - right.longValue());
    }

    public Number multiplyImpl(Number left, Number right) {
        return Long.valueOf(left.longValue() * right.longValue());
    }

    public Number divideImpl(Number left, Number right) {
        return BigDecimalMath.INSTANCE.divideImpl(left, right);
    }

    public int compareToImpl(Number left, Number right) {
        long leftVal = left.longValue();
        long rightVal = right.longValue();
        return (leftVal<rightVal ? -1 : (leftVal==rightVal ? 0 : 1));
    }

    protected Number intdivImpl(Number left, Number right) {
        return Long.valueOf(left.longValue() / right.longValue());
    }

    protected Number modImpl(Number left, Number right) {
        return Long.valueOf(left.longValue() % right.longValue());
    }

    protected Number unaryMinusImpl(Number left) {
        return Long.valueOf(-left.longValue());
    }

    protected Number unaryPlusImpl(Number left) {
        return Long.valueOf(left.longValue());
    }

    protected Number bitwiseNegateImpl(Number left) {
        return Long.valueOf(~left.longValue());
    }

    protected Number orImpl(Number left, Number right) {
        return Long.valueOf(left.longValue() | right.longValue());
    }

    protected Number andImpl(Number left, Number right) {
        return Long.valueOf(left.longValue() & right.longValue());
    }

    protected Number xorImpl(Number left, Number right) {
        return Long.valueOf(left.longValue() ^ right.longValue());
    }

    protected Number leftShiftImpl(Number left, Number right) {
        return Long.valueOf(left.longValue() << right.longValue());
    }

    protected Number rightShiftImpl(Number left, Number right) {
        return Long.valueOf(left.longValue() >> right.longValue());
    }

    protected Number rightShiftUnsignedImpl(Number left, Number right) {
        return Long.valueOf(left.longValue() >>> right.longValue());
    }

    protected Number bitAndImpl(Number left, Number right) {
        return Long.valueOf(left.longValue() & right.longValue());
    }
}
