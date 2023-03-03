package aos;

import java.util.Scanner;

public class Reader {
    Scanner scanner = new Scanner(System.in);
    String readExpNumber(){
        while (true){
            System.out.print("Enter decimal number in exponential form (±n.n…nЕ±n…n): \n > ");
            String in = scanner.nextLine();
            System.out.println();
            if(expNumIsValid(in)){
                return in;
            }
        }
    }

    private boolean expNumIsValid(String input) {
        String regex = "[+-]?\\d\\.\\d+[eE][+-]?\\d+";
        return input.matches(regex);
    }
}
