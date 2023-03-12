package aos;

import java.math.BigDecimal;
import java.util.Arrays;

public class FloatingPointNumber {

    // delete
    Converter converter;
    public boolean sign;
    public boolean[] exponent;
    public boolean normalBit;
    public boolean[] mantissa;
    public final int exponentLength;

    // With normal bit
    public final int mantissaLength;
    public BigDecimal value;

    public final double MAX_FLOAT = 4080;
    public final double MIN_FLOAT = -4080;
    public final double SMALLEST_POSITIVE;

    public FloatingPointNumber() {
        converter = new Converter();
        exponentLength = Program.exponentLength;
        mantissaLength = Program.mantissaLength;
        SMALLEST_POSITIVE = Math.pow(2, -exponentLength);
        exponent = new boolean[exponentLength];
        mantissa = new boolean[mantissaLength];
        toDecimalNumber();
    }

    public FloatingPointNumber(boolean sign, boolean[] whole, boolean[] fraction) {
        this();
        this.sign = sign;
        if(isInfinite()){
            setInfinite(sign);
        } else if (isNan()) {
            setNan();
        }
        toFloatNumber(whole, fraction);
        toDecimalNumber();
    }

    private void setNan() {
        setExponent(true);
        mantissa[1] = true;
        mantissa[2] = false;
    }

    public FloatingPointNumber(String stringValue) {
        this();
        value = converter.expDecToBigDecimal(stringValue);
        this.sign = value.compareTo(new BigDecimal(0)) < 0;

        if (value.compareTo(new BigDecimal(MAX_FLOAT)) > 0)
            setInfinite(false);
        else if (value.compareTo(new BigDecimal(MIN_FLOAT)) < 0)
            setInfinite(true);
        else {
            toFloatNumber(converter.wholeToBinary(value.intValue()), converter.fractionalToBinary(value));
            toDecimalNumber();
        }
    }

    private void toFloatNumber(boolean[] whole, boolean[] fraction) {
        int bias;
        int indexOfWholeOne = indexOfOne(whole);
        if (indexOfWholeOne != -1) {
            normalBit = true;
            bias = exponentLength - (indexOfWholeOne + 1);
            setExponent(bias);
            int i = 0;
            for (int k = indexOfWholeOne; i < mantissaLength && k < whole.length; i++, k++) {
                mantissa[i] = whole[k];
            }
            for (int k = 0; i < mantissaLength && k < fraction.length; k++, i++) {
                mantissa[i] = fraction[k];
            }
        } else {
            int indexOfFractionOne = indexOfOne(fraction);
            if (indexOfFractionOne + 1 > ((int) Math.pow(2, exponentLength - 1) - 2)) {
                // subnormal
                normalBit = false;
                int exponentValue = -((int) Math.pow(2, exponentLength - 1) - 1);
                System.out.println("bias " + exponentValue);
                setExponent(exponentValue);
                for (int i = 1, k = Math.abs(exponentValue) - 1; i < mantissaLength; i++, k++) {
                    mantissa[i] = fraction[k];
                }
            } else if (indexOfFractionOne == -1) {
                // zero
                normalBit = false;
                int exponentValue = -((int) Math.pow(2, exponentLength - 1) - 1);
                setExponent(exponentValue);
            } else {
                // < 1
                normalBit = true;
                bias = -(indexOfFractionOne + 1);
                System.out.println("bias " + bias);
                setExponent(bias);
                for (int i = 0, k = indexOfFractionOne; i < mantissaLength && k < fraction.length; i++, k++) {
                    mantissa[i] = fraction[k];
                }
            }
        }
    }

    public void toDecimalNumber() {
        double fraction = converter.mantissaToFraction(mantissa);
        int exponentValue = converter.binaryToWhole(exponent);
        int bias = (int) Math.pow(2, exponentLength - 1) - 1;
        // todo bigdecimal pow can be only positive
//        value = new BigDecimal(2);
        if (normalBit) {
            value = new BigDecimal(1 + fraction);

            if (exponentValue - bias < 0)
                value = value.divide(new BigDecimal(2).pow(Math.abs(exponentValue - bias)));
            else
                value = value.multiply(new BigDecimal(2).pow(exponentValue - bias));

        } else {
            exponentValue = bias - 1;
            value = new BigDecimal(fraction);
            value = value.divide(new BigDecimal(2).pow(exponentValue));
        }
        value = value.multiply(BigDecimal.valueOf(Math.pow(-1, (sign ? 1 : 0))));
    }

    private void setExponent(int p) {
        int exponentValue = p + (int) Math.pow(2, exponentLength - 1) - 1;
        exponent = converter.wholeToBinary(exponentValue);
    }

    private int indexOfOne(boolean[] binary) {
        for (int i = 0; i < binary.length; i++) {
            if (binary[i])
                return i;
        }
        return -1;
    }

    public void setExponent(boolean[] exponent) {
        this.exponent = exponent;
    }

    public void setExponent(boolean value) {
        Arrays.fill(exponent, value);
    }

    public void setSign(boolean sign) {
        this.sign = sign;
    }

    public void setNormalBit(boolean normalBit) {
        this.normalBit = normalBit;
    }

    public void setMantissa(boolean[] mantissa) {
        this.mantissa = mantissa;
    }

    public void setMantissa(boolean value) {
        Arrays.fill(mantissa, value);
    }

    public boolean isInfinite() {
        for (int i = 0; i < exponentLength; i++) {
            if (!exponent[i])
                return false;
        }
        // mantissa[0] -- normal bit
        for (int i = 1; i < mantissaLength; i++) {
            if (mantissa[i])
                return false;
        }
        return true;
    }

    public boolean isNan() {
        for (int i = 0; i < exponentLength; i++) {
            if (!exponent[i])
                return false;
        }

        boolean one = false;
        boolean zero = false;
        for (int i = 1; i < mantissaLength; i++) {
            if (mantissa[i])
                one = true;
            else zero = true;
        }
        return one && zero;
    }

    public String getStringValue() {
        if (isInfinite())
            return (sign ? "-" : "+") + "inf";
        else if (isNan())
            return "NaN";

        return String.format("%e", value);
    }

    private void setInfinite(boolean sign) {
        setExponent(true);
        setMantissa(false);
        setSign(sign);
    }

}
