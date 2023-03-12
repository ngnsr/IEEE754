package aos;

import java.math.BigDecimal;
import java.util.BitSet;
public class Converter {
    private final int exponentLength;
    private final int mantissaLength;
    public static final byte base = 10;

    public Converter() {
        exponentLength = Program.exponentLength;
        mantissaLength = Program.mantissaLength;
    }

    BigDecimal expDecToBigDecimal(String value){
        return new BigDecimal(value);
    }
    double expDecToDec(String value){
        String[] parts = value.split("[eE]");
        double M = Double.parseDouble(parts[0]);
        byte p = Byte.parseByte(parts[1]);
        M *= Math.pow(base, p);
        return M;
    }
    boolean[] wholeToBinary(int n){
        n = Math.abs(n);
        final int base = 2;
        boolean[] binary = new boolean[exponentLength];
//        if(n > Math.pow(base, exponentLength) - 1) {
////            for (int i = 0; i < h; i++) {
////               binary[i] = true;
////            }
//            return binary;
//        }

        for (int i = exponentLength - 1; n > 0 && i >= 0; i--) {
            binary[i] = n % base == 1;
            n /= base;
        }
        return binary;
    }

    boolean[] fractionalToBinary(BigDecimal fractional){
        fractional = fractional.remainder(BigDecimal.ONE);
        fractional = fractional.abs();
        BigDecimal c = BigDecimal.ONE;
        BitSet binary = new BitSet();
        BigDecimal TWO = BigDecimal.valueOf(2);

        for (int i = 0, setBits = 0; fractional.compareTo(BigDecimal.valueOf(0)) > 0 && setBits < mantissaLength; i++) {
            c = c.divide(TWO);
            if (c.compareTo(fractional) <= 0){
                binary.set(i, true);
                fractional = fractional.subtract(c);
                setBits++;
            }
        }

        return bitSetToBoolArr(binary);
    }

    int binaryToWhole(boolean[] binary){
        int sum = 0;
        for (int i = binary.length - 1, k = 0; i >= 0; i--, k++) {
            sum += (binary[i] ? 1 : 0 ) * Math.pow(2, k);
        }
        return sum;
    }
    double mantissaToFraction(boolean[] binary){
        double sum = 0;

        // first bit is normal bit
        for (int i = 1; i < binary.length; i++) {
            sum += (binary[i] ? 1 : 0 ) * Math.pow(2, -i);
        }
        return sum;
    }

    boolean[] bitSetToBoolArr(BitSet bitSet){
        int length = bitSet.length();
        boolean[] binary = new boolean[length];
        for (int i = 0; i < length; i++) {
            binary[i] = bitSet.get(i);
        }
        return binary;
    }

    String binaryToString(boolean[] binary){
        StringBuilder sb = new StringBuilder();
        for (boolean b : binary){
            sb.append(b ? 1 : 0);
        }
        return sb.toString();
    }

}
