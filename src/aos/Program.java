package aos;

import java.math.BigDecimal;

public class Program {
    protected static final byte exponentLength = 12;
    // Mantissa length with normal bit
    protected static final byte mantissaLength = 10;

    public static void main(String[] args) {
        Reader reader = new Reader();
//        String stringValue = reader.readExpNumber();
//        String stringValue = String.valueOf(Math.pow(2, -130));
        String stringValue = "121.65";

//        double value = 1.0E-300;
        System.out.printf("num: %s, exponent len: %s, mantissa len: %s\n",stringValue, exponentLength, mantissaLength);
        FloatingPointNumber fp = new FloatingPointNumber(stringValue);
        Printer printer = new Printer(fp);
        printer.printInReadableForm();
    }
}