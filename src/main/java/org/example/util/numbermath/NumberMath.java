package org.example.util.numbermath;

import java.math.BigDecimal;
import java.math.BigInteger;

public abstract class NumberMath {

    public static Number abs(Number number) {
        return getMath(number).absImpl(number);
    }

    public static Number add(Number left, Number right) {
        return getMath(left, right).addImpl(left,right);
    }

    public static Number subtract(Number left, Number right) {
        return getMath(left,right).subtractImpl(left,right);
    }

    public static Number multiply(Number left, Number right) {
        return getMath(left,right).multiplyImpl(left,right);
    }

    public static Number divide(Number left, Number right) {
        return getMath(left,right).divideImpl(left,right);
    }

    public static int compareTo(Number left, Number right) {
        return getMath(left,right).compareToImpl(left, right);
    }

    public static Number or(Number left, Number right) {
        return getMath(left,right).orImpl(left, right);
    }

    public static Number and(Number left, Number right) {
        return getMath(left,right).andImpl(left, right);
    }

    public static Number xor(Number left, Number right) {
        return getMath(left,right).xorImpl(left, right);
    }

    public static Number intdiv(Number left, Number right) {
        return getMath(left,right).intdivImpl(left,right);
    }

    public static Number mod(Number left, Number right) {
        return getMath(left,right).modImpl(left, right);
    }

    /**
     * For this operation, consider the operands independently.  Throw an exception if the right operand
     * (shift distance) is not an integral type.  For the left operand (shift value) also require an integral
     * type, but do NOT promote from Integer to Long.  This is consistent with Java, and makes sense for the
     * shift operators.
     */
    public static Number leftShift(Number left, Number right) {
        if (isFloatingPoint(right) || isBigDecimal(right)) {
            throw new UnsupportedOperationException("Shift distance must be an integral type, but " +  right + " (" + right.getClass().getName() + ") was supplied");
        }
        return getMath(left).leftShiftImpl(left,right);
    }

    /**
     * For this operation, consider the operands independently.  Throw an exception if the right operand
     * (shift distance) is not an integral type.  For the left operand (shift value) also require an integral
     * type, but do NOT promote from Integer to Long.  This is consistent with Java, and makes sense for the
     * shift operators.
     */
    public static Number rightShift(Number left, Number right) {
        if (isFloatingPoint(right) || isBigDecimal(right)) {
            throw new UnsupportedOperationException("Shift distance must be an integral type, but " +  right + " (" + right.getClass().getName() + ") was supplied");
        }
        return getMath(left).rightShiftImpl(left,right);
    }

    /**
     * For this operation, consider the operands independently.  Throw an exception if the right operand
     * (shift distance) is not an integral type.  For the left operand (shift value) also require an integral
     * type, but do NOT promote from Integer to Long.  This is consistent with Java, and makes sense for the
     * shift operators.
     */
    public static Number rightShiftUnsigned(Number left, Number right) {
        if (isFloatingPoint(right) || isBigDecimal(right)) {
            throw new UnsupportedOperationException("Shift distance must be an integral type, but " +  right + " (" + right.getClass().getName() + ") was supplied");
        }
        return getMath(left).rightShiftUnsignedImpl(left,right);
    }

    public static Number bitwiseNegate(Number left) {
        return getMath(left).bitwiseNegateImpl(left);
    }

    public static Number unaryMinus(Number left) {
        return getMath(left).unaryMinusImpl(left);
    }

    public static Number unaryPlus(Number left) {
        return getMath(left).unaryPlusImpl(left);
    }

    public static boolean isFloatingPoint(Number number) {
        return number instanceof Double || number instanceof Float;
    }

    public static boolean isInteger(Number number) {
        return number instanceof Integer;
    }

    public static boolean isLong(Number number) {
        return number instanceof Long;
    }

    public static boolean isBigDecimal(Number number) {
        return number instanceof BigDecimal;
    }

    public static boolean isBigInteger(Number number) {
        return number instanceof BigInteger;
    }

    public static BigDecimal toBigDecimal(Number n) {
        return (n instanceof BigDecimal ? (BigDecimal) n : new BigDecimal(n.toString()));
    }

    public static BigInteger toBigInteger(Number n) {
        return (n instanceof BigInteger ? (BigInteger) n : new BigInteger(n.toString()));
    }

    /**
     * Determine which NumberMath instance to use, given the supplied operands.  This method implements
     * the type promotion rules discussed in the documentation.  Note that by the time this method is
     * called, any Byte, Character or Short operands will have been promoted to Integer.  For reference,
     * here is the promotion matrix:
     *    bD bI  D  F  L  I
     * bD bD bD  D  D bD bD
     * bI bD bI  D  D bI bI
     *  D  D  D  D  D  D  D
     *  F  D  D  D  D  D  D
     *  L bD bI  D  D  L  L
     *  I bD bI  D  D  L  I
     *
     * Note that for division, if either operand isFloatingPoint, the result will be floating.  Otherwise,
     * the result is BigDecimal
     */
    public static NumberMath getMath(Number left, Number right) {
        if (isFloatingPoint(left) || isFloatingPoint(right)) {
            return FloatingPointMath.INSTANCE;
        }
        if (isBigDecimal(left) || isBigDecimal(right)) {
            return BigDecimalMath.INSTANCE;
        }
        if (isBigInteger(left) || isBigInteger(right)) {
            return BigIntegerMath.INSTANCE;
        }
        if (isLong(left) || isLong(right)){
            return LongMath.INSTANCE;
        }
        return IntegerMath.INSTANCE;
    }

    private static NumberMath getMath(Number number) {
        if (isLong(number)) {
            return LongMath.INSTANCE;
        }
        if (isFloatingPoint(number)) {
            return FloatingPointMath.INSTANCE;
        }
        if (isBigDecimal(number)) {
            return BigDecimalMath.INSTANCE;
        }
        if (isBigInteger(number)) {
            return BigIntegerMath.INSTANCE;
        }
        return IntegerMath.INSTANCE;
    }

    //Subclasses implement according to the type promotion hierarchy rules
    protected abstract Number absImpl(Number number);
    public abstract Number addImpl(Number left, Number right);
    public abstract Number subtractImpl(Number left, Number right);
    public abstract Number multiplyImpl(Number left, Number right);
    public abstract Number divideImpl(Number left, Number right);
    public abstract int compareToImpl(Number left, Number right);
    protected abstract Number unaryMinusImpl(Number left);
    protected abstract Number unaryPlusImpl(Number left);

    protected Number bitwiseNegateImpl(Number left) {
        throw createUnsupportedException("bitwiseNegate()", left);
    }

    protected Number orImpl(Number left, Number right) {
        throw createUnsupportedException("or()", left);
    }

    protected Number andImpl(Number left, Number right) {
        throw createUnsupportedException("and()", left);
    }

    protected Number xorImpl(Number left, Number right) {
        throw createUnsupportedException("xor()", left);
    }

    protected Number modImpl(Number left, Number right) {
        throw createUnsupportedException("mod()", left);
    }

    protected Number intdivImpl(Number left, Number right) {
        throw createUnsupportedException("intdiv()", left);
    }

    protected Number leftShiftImpl(Number left, Number right) {
        throw createUnsupportedException("leftShift()", left);
    }

    protected Number rightShiftImpl(Number left, Number right) {
        throw createUnsupportedException("rightShift()", left);
    }

    protected Number rightShiftUnsignedImpl(Number left, Number right) {
        throw createUnsupportedException("rightShiftUnsigned()", left);
    }

    protected UnsupportedOperationException createUnsupportedException(String operation, Number left) {
        return new UnsupportedOperationException("Cannot use " + operation + " on this number type: " + left.getClass().getName() + " with value: " + left);
    }
}
