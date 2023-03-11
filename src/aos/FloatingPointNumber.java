package aos;

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
    public double value;

    public final double MAX_FLOAT = 4080;
    public final double MIN_FLOAT = -4080;
    public final double SMALLEST_POSITIVE;

    public FloatingPointNumber(){
        converter = new Converter();
        exponentLength = Program.exponentLength;
        mantissaLength = Program.mantissaLength;
        SMALLEST_POSITIVE = Math.pow(2, -exponentLength);
        exponent = new boolean[exponentLength];
        mantissa = new boolean[mantissaLength];
    }

    public FloatingPointNumber(boolean sign, boolean[] whole, boolean[] fraction) {
        this();
        this.sign = sign;
        toFloatNumber(whole, fraction);
        toDecimalNumber();
    }

    public FloatingPointNumber(double value){
        this();
        this.sign = value < 0;
        toFloatNumber(converter.wholeToBinary((int) value), converter.fractionalToBinary(value - (int) value));
        toDecimalNumber();
    }
    private void toFloatNumber(boolean[] whole, boolean[] fraction){
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
        }else {
            int indexOfFractionOne = indexOfOne(fraction);
            if (indexOfFractionOne + 1 < -((int) Math.pow(2, exponentLength -1) - 2)){
                // subnormal
                normalBit = false;
                int exponentValue = -((int) Math.pow(2, exponentLength - 1) - 1);
                System.out.println("bias " + exponentValue);
                setExponent(exponentValue);
                for (int i = 1, k = indexOfFractionOne; i < mantissaLength; i++, k++) {
                    mantissa[i] = fraction[k];
                }
            } else if(indexOfFractionOne == -1){
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

    public void toDecimalNumber(){
        double fraction = converter.mantissaToFraction(mantissa);
        int exponentValue = converter.binaryToWhole(exponent);
        int bias = (int) Math.pow(2, exponentLength - 1) - 1;
        if(normalBit) {
            value = Math.pow(-1, (sign ? 1 : 0)) * (1 + fraction) * Math.pow(2, exponentValue - bias);
        } else{
            exponentValue = -bias + 1;
            value = Math.pow(-1, (sign ? 1 : 0)) * (fraction) * Math.pow(2, exponentValue - bias);
        }
    }

    public String getExpDec(){
        double fraction = converter.mantissaToFraction(mantissa);
        int exponentValue = converter.binaryToWhole(exponent);
        int bias = (int) Math.pow(2, exponentLength - 1) - 1;
        double x =  ((exponentValue - bias) * Math.log(2)/Math.log(10));
        return String.format("%s%s.%sE%s", sign ? "-" : "", normalBit ? "1": "0", String.valueOf(fraction).substring(2),x);
    }

    private void setExponent(int p){
        int exponentValue = p + (int)Math.pow(2, exponentLength - 1) - 1;
        exponent = converter.wholeToBinary(exponentValue);
    }

    private int indexOfOne(boolean[] binary){
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

    public void setSign(boolean sign){
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

    public boolean isInfinite(){
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

    public boolean isNan(){
        for (int i = 0; i < exponentLength; i++) {
            if(!exponent[i])
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

    public String getStringValue(){
        if (isInfinite())
            return (sign ? "-" : "") + "inf";
        else if(isNan())
            return "NaN";

        return String.format("%e", value);
    }

}
