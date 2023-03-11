package aos;

public class Program {
    protected static final byte exponentLength = 12;
    // Mantissa length with normal bit
    protected static final byte mantissaLength = 8;

    public Program() {
    }

    public static void main(String[] var0) {
        double value = 1.0E-30;
        System.out.printf("exponent len: %s, mantissa len: %s\n", exponentLength, mantissaLength);
        FloatingPointNumber fp = new FloatingPointNumber(value);
        Printer printer = new Printer(fp);
        printer.printInReadableForm();
    }
}