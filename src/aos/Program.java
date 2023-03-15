package aos;

public class Program {
    protected static final byte exponentLength = 12;
    // Mantissa length with normal bit
    protected static final byte mantissaLength = 8;

    public static void main(String[] args) {
        Printer printer = new Printer();
        printer.printListing();

        Reader reader = new Reader();
        String stringValue = reader.readExpNumber();
//        String stringValue = "6969";
        FloatingPointNumber fp = new FloatingPointNumber(stringValue);

        printer.printConvertedRow(fp.getList());
    }

}