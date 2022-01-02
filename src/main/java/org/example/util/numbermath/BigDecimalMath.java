package org.example.util.numbermath;

import java.math.BigDecimal;
import java.math.MathContext;

public final class BigDecimalMath extends NumberMath {

    // This is an arbitrary value, picked as a reasonable choice for a precision
    // for typical user math when a non-terminating result would otherwise occur.
    public static final int DIVISION_EXTRA_PRECISION = 10;

    //This is an arbitrary value, picked as a reasonable choice for a rounding point
    //for typical user math.
    public static final int DIVISION_MIN_SCALE = 10;

    public static final BigDecimalMath INSTANCE = new BigDecimalMath();

    private BigDecimalMath() {
    }

    protected Number absImpl(Number number) {
        return toBigDecimal(number).abs();
    }

    public Number addImpl(Number left, Number right) {
        return toBigDecimal(left).add(toBigDecimal(right));
    }

    public Number subtractImpl(Number left, Number right) {
        return toBigDecimal(left).subtract(toBigDecimal(right));
    }

    public Number multiplyImpl(Number left, Number right) {
        return toBigDecimal(left).multiply(toBigDecimal(right));
    }

    public Number divideImpl(Number left, Number right) {
        BigDecimal bigLeft = toBigDecimal(left);
        BigDecimal bigRight = toBigDecimal(right);
        try {
            return bigLeft.divide(bigRight);
        } catch (ArithmeticException e) {
            // set a DEFAULT precision if otherwise non-terminating
            int precision = Math.max(bigLeft.precision(), bigRight.precision()) + DIVISION_EXTRA_PRECISION;
            BigDecimal result = bigLeft.divide(bigRight, new MathContext(precision));
            int scale = Math.max(Math.max(bigLeft.scale(), bigRight.scale()), DIVISION_MIN_SCALE);
            if (result.scale() > scale) result = result.setScale(scale, BigDecimal.ROUND_HALF_UP);
            return result;
        }
    }

    public int compareToImpl(Number left, Number right) {
        return toBigDecimal(left).compareTo(toBigDecimal(right));
    }

    protected Number unaryMinusImpl(Number left) {
        return toBigDecimal(left).negate();
    }

    protected Number unaryPlusImpl(Number left) {
        return toBigDecimal(left);
    }
}
