package aos;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

public class FloatingPointNumber {

    Converter converter;
    public boolean sign;
    public boolean[] exponent;
    public boolean normalBit;
    public boolean[] mantissa;

    public final int exponentLength;

    // With normal bit
    public final int mantissaLength;
    public BigDecimal value;
    public final BigDecimal MAX_FLOAT;
    public final BigDecimal MIN_FLOAT;
//    public final double SMALLEST_POSITIVE;

    public FloatingPointNumber() {
        converter = new Converter();
        exponentLength = Program.exponentLength;
        mantissaLength = Program.mantissaLength;
        BigDecimal base = new BigDecimal(2);
        MAX_FLOAT = base.pow((int) Math.pow(2, exponentLength - 1)).multiply(base.subtract(BigDecimal.ONE.divide(base.pow(mantissaLength))));
        MIN_FLOAT = MAX_FLOAT.negate();

        exponent = new boolean[exponentLength];
        mantissa = new boolean[mantissaLength];
        value = BigDecimal.ZERO;
    }

    public FloatingPointNumber(String stringValue) {
        this();
        BigDecimal value = new BigDecimal(stringValue);
        this.sign = value.compareTo(new BigDecimal(0)) < 0;

        if (value.compareTo(MAX_FLOAT) > 0)
            setInfinity(false);
        else if (value.compareTo(MIN_FLOAT) < 0)
            setInfinity(true);
        else {
            convertTo(converter.wholeToBinary(value.toBigInteger()), converter.fractionalToBinary(value));
            calculateValue();
        }
    }

    private void convertTo(boolean[] whole, boolean[] fraction) {
        int bias;
        int indexOfWholeOne = indexOfOne(whole);
        if (indexOfWholeOne != -1) {
            normalBit = true;
            bias = whole.length - (indexOfWholeOne + 1);
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
                setExponent(exponentValue);
                for (int i = 1, k = Math.abs(exponentValue) - 1; i < mantissaLength && k < fraction.length; i++, k++) {
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
                setExponent(bias);
                for (int i = 0, k = indexOfFractionOne; i < mantissaLength && k < fraction.length; i++, k++) {
                    mantissa[i] = fraction[k];
                }
            }
        }
    }

    public void calculateValue() {
        BigDecimal fraction = converter.mantissaToFraction(mantissa);
        int exponentValue = converter.binaryToWhole(exponent);
        int bias = (int) Math.pow(2, exponentLength - 1) - 1;
        if (normalBit) {
            value = BigDecimal.ONE.add(fraction);

            if (exponentValue - bias < 0)
                value = value.divide(new BigDecimal(2).pow(Math.abs(exponentValue - bias)));
            else
                value = value.multiply(new BigDecimal(2).pow(exponentValue - bias));

        } else {
            exponentValue = bias - 1;
            value = fraction;
            value = value.divide(new BigDecimal(2).pow(exponentValue));
        }
        value = value.multiply(BigDecimal.valueOf(Math.pow(-1, (sign ? 1 : 0))));
    }

    private void setExponent(int bias) {
        int exponentValue = bias + (int) Math.pow(2, exponentLength - 1) - 1;
        exponent = converter.wholeToBinary(BigInteger.valueOf(exponentValue));
    }

    private int indexOfOne(boolean[] binary) {
        for (int i = 0; i < binary.length; i++) {
            if (binary[i])
                return i;
        }
        return -1;
    }

    public void fillExponent(boolean value) {
        Arrays.fill(exponent, value);
    }

    public void setSign(boolean sign) {
        this.sign = sign;
    }

    public void setNormalBit(boolean normalBit) {
        this.normalBit = normalBit;
    }

    public void fillMantissa(boolean value) {
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

        return String.format("%.10e", value);
    }

    public void setInfinity(boolean sign) {
        fillExponent(true);
        fillMantissa(false);
        setSign(sign);
    }

    public List<String> getList(){
        String exponentString = converter.binaryToString(exponent);
        String mantissaString = converter.binaryToString(mantissa).substring(1);
        List<String> list = Arrays.asList((sign ? "1" : "0"),
                exponentString,
                (normalBit ? "1" : "0"),
                mantissaString,
                getStringValue());
        return list;
    }

    public void setNaN() {
        fillExponent(true);
        mantissa[1] = true;
        mantissa[2] = false;
    }

}
