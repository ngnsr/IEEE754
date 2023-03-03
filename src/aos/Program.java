package aos;

public class Program {
    protected static final byte exponentLength = 12;
    // With normal bit
    protected static final byte mantissaLength = 8;

    public static void main(String[] args) {
//        Reader reader = new Reader();
       // String number = reader.readExpNumber();
        Converter converter = new Converter();
        double num = 0.69;
        int wholeNumber = (int) num;
        double fractionalNumber = num - wholeNumber;

        boolean[] whole = converter.wholeToBinary(wholeNumber);
        boolean[] fractional = converter.fractionalToBinary(fractionalNumber);

        System.out.printf("exponent len: %s, mantissa len: %s\n", exponentLength, mantissaLength);
//        FloatingPointNumber fp = new FloatingPointNumber();
        FloatingPointNumber fp = new FloatingPointNumber(false, whole, fractional);

        Printer printer = new Printer(fp);
        printer.printInReadableForm();

    }

}
