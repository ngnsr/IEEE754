package aos;

public class Printer {
    private final FloatingPointNumber floatNum;

    public Printer(FloatingPointNumber floatNum) {
        this.floatNum = floatNum;
    }

    public void printInReadableForm(){
        String[] labels = getLabelsStringArr();
        String[] values = floatNumToStringArr(floatNum);
        formatAndPrint(labels, values);
    }

    public void printListing(){

    }

    private String[] getLabelsStringArr(){
        String sign = "Sign";
        String exponent = "Exponent";
        String normalBit = "Normal Bit";
        String mantissa = "Mantissa";
        String decimal = "Decimal";
        return new String[]{sign, exponent, normalBit, mantissa, decimal};
    }

    private String[] floatNumToStringArr(FloatingPointNumber floatNum){
        String[] values = {boolToStr(floatNum.sign),
                boolArrToStr(floatNum.exponent, 0),
                boolToStr(floatNum.normalBit),
                boolArrToStr(floatNum.mantissa, 1),
                floatNum.getStringValue()};
        return values;
    }
    private void formatAndPrint(String[] labels, String[] values) {
        if (labels.length != values.length || labels.length < 2 || values.length < 2) {
            return;
        }
        for (int i = 0; i < values.length; i++) {
            int labelsElementLen = labels[i].length();
            int valuesElementLen = values[i].length();
            int len = Math.max(valuesElementLen, labelsElementLen);
            values[i] = fill(values[i], len);
            labels[i] = fill(labels[i], len);
        }

        for (int i = 0; i < labels.length - 1; i++) {
            System.out.print(labels[i] + " | ");
        }
        System.out.println(labels[labels.length - 1]);
        for (int i = 0; i < values.length - 1; i++) {
            System.out.print(values[i] + " | ");
        }
        System.out.println(values[values.length - 1]);
    }

    private String fill(String str, int len){
        return str + " ".repeat(Math.max(len - str.length(), 0));
    }

    private String boolToStr(boolean bool){
        return (bool ? "1" : "0");
    }

    private String boolArrToStr(boolean[] arr, int index){
        StringBuilder sb = new StringBuilder(arr.length - 1);
        for (int i = index; i < arr.length; i++) {
            sb.append(boolToStr(arr[i]));
        }
        return sb.toString();
    }
}
