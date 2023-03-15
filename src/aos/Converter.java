package aos;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.BitSet;

public class Converter {
    private final int exponentLength;
    private final int mantissaLength;

    public Converter() {
        exponentLength = Program.exponentLength;
        mantissaLength = Program.mantissaLength;
    }

    boolean[] wholeToBinary(BigInteger n){
        ArrayList<Boolean> binary = new ArrayList<>(50);
        n = n.abs();
        final BigInteger base = new BigInteger("2");
        while(n.compareTo(BigInteger.ZERO) > 0){
            binary.add(0, n.mod(base).equals(BigInteger.ONE));
            n = n.divide(base);
        }
        for (int i = binary.size(); i < exponentLength ; i++) {
            binary.add(0, false);
        }
        return toPrimitiveArray(binary);
    }

    boolean[] toPrimitiveArray(final ArrayList<Boolean> booleanList) {
        final boolean[] primitives = new boolean[booleanList.size()];
        int index = 0;
        for (Boolean bool : booleanList) {
            primitives[index++] = bool;
        }
        return primitives;
    }

    boolean[] fractionalToBinary(BigDecimal fractional){
        fractional = fractional.remainder(BigDecimal.ONE);
        fractional = fractional.abs();
        BigDecimal current = BigDecimal.ONE;
        ArrayList<Boolean> binary = new ArrayList<>(20);
        final BigDecimal base = BigDecimal.valueOf(2);

        for (int setBits = 0; fractional.compareTo(BigDecimal.valueOf(0)) > 0 && setBits < mantissaLength; ) {
            current = current.divide(base);
            if (current.compareTo(fractional) <= 0){
                binary.add(true);
                fractional = fractional.subtract(current);
                setBits++;
            } else
                binary.add(false);
        }

        return toPrimitiveArray(binary);
    }

    int binaryToWhole(boolean[] binary){
        int sum = 0;
        for (int i = binary.length - 1, k = 0; i >= 0; i--, k++) {
            sum += (binary[i] ? 1 : 0 ) * Math.pow(2, k);
        }
        return sum;
    }
    BigDecimal mantissaToFraction(boolean[] binary){
        BigDecimal sum = BigDecimal.ZERO;

        // first bit is normal bit
        for (int i = 1; i < binary.length; i++) {
            sum = sum.add((binary[i] ? BigDecimal.ONE : BigDecimal.ZERO).divide(new BigDecimal(2).pow(i)));
        }
        return sum;
    }

    String binaryToString(boolean[] binary){
        StringBuilder sb = new StringBuilder();
        for (boolean b : binary){
            sb.append(b ? 1 : 0);
        }
        return sb.toString();
    }

}
