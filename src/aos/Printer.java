package aos;

import java.util.Arrays;
import java.util.List;

public class Printer {
    private FloatingPointNumber fp;

    public Printer() {
        fp = new FloatingPointNumber();
    }

    public void printConvertedRow(List<String> row){
        printTable(Arrays.asList(getHeadersList(), row));
    }

    public void printListing(){
        printTable(getListing());
    }

    public void printTable(List<List<String>> rows ) {
        int[] maxLengths = new int[rows.get(0).size()];
        for (List<String> row : rows) {
            for (int i = 0; i < row.size(); i++) {
                maxLengths[i] = Math.max(maxLengths[i], row.get(i).length());
            }
        }

        StringBuilder formatBuilder = new StringBuilder();
        for (int maxLength : maxLengths) {
            formatBuilder.append("%-").append(maxLength + 2).append("s");
        }
        String format = formatBuilder.toString();

        StringBuilder result = new StringBuilder();
        for (List<String> row : rows) {
            result.append(String.format(format, row.toArray(new String[0]))).append("\n");
        }
        System.out.println(result);
    }

    private List<List<String>> getListing() {
        List<String> headers = getHeadersList();
        List<String> minimumNonZero = getMinimumNonZeroList();
        List<String> maximum = getMaximumList();
        List<String> minimum = getMinimumList();
        List<String> one = getOneList();
        List<String> positiveInfinity = getPositiveInfList();
        List<String> negativeInfinity = getNegativeInfList();
        List<String> denormalized = getDenormalizedList();
        List<String> NaN = getNaNList();
        return Arrays.asList(headers, minimumNonZero, maximum,
                minimum, one, positiveInfinity,
                negativeInfinity, denormalized, NaN);
    }

    // Normal
    private List<String> getMinimumNonZeroList() {
        fp.fillExponent(false);
        fp.setNormalBit(true);
        fp.fillMantissa(false);
        fp.exponent[fp.exponentLength - 1] = true;
        fp.calculateValue();
        return fp.getList();
    }

    private List<String> getMaximumList() {
        fp.fillExponent(true);
        fp.exponent[fp.exponentLength - 1] = false;
        fp.normalBit = true;
        fp.fillMantissa(true);
        fp.calculateValue();
        return fp.getList();
    }

    private List<String> getMinimumList() {
        fp.sign = true;
        fp.fillExponent(true);
        fp.exponent[fp.exponentLength - 1] = false;
        fp.normalBit = true;
        fp.fillMantissa(true);
        fp.calculateValue();
        return fp.getList();
    }

    private List<String> getOneList() {
        fp = new FloatingPointNumber("1.0E0");
        return fp.getList();
    }

    private List<String> getPositiveInfList() {
        fp.setInfinity(false);
        return fp.getList();
    }

    private List<String> getNegativeInfList() {
        fp.setInfinity(true);
        return fp.getList();
    }

    private List<String> getDenormalizedList() {
        fp = new FloatingPointNumber("1.0E-616");
        return fp.getList();
    }

    private List<String> getNaNList() {
        fp = new FloatingPointNumber();
        fp.setNaN();
        fp.calculateValue();
        return fp.getList();
    }

    private List<String> getHeadersList() {
        String sign = "Sign";
        String exponent = "Exponent";
        String normalBit = "Normal Bit";
        String mantissa = "Mantissa";
        String decimal = "Decimal";
        return Arrays.asList(sign, exponent, normalBit, mantissa, decimal);
    }
}
