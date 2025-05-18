import java.util.Scanner;
/**Class to get input and convert it to an int*/
public class StringToInt {
    public static int convertToInt(String toConvert){
        int num = -1;
        String str = toConvert;
        try {
            num = Integer.parseInt(str);
        } catch (NumberFormatException e) {
            System.out.println("Error: Invalid string for Integer.parseInt()");
        }

        // Using Integer.valueOf()
        try {
            num = Integer.valueOf(str);
        } catch (NumberFormatException e) {
            System.out.println("Error: Invalid string for Integer.valueOf()");
        }
        if (num < 0){
            System.out.println("Error: Invalid input.");
            return stringToInt("Please enter the number again.");
        }
        return num;
    }
    public static int stringToInt(String toPrint) {
        Scanner scanner = new Scanner(System.in);
        System.out.println(toPrint);
        String str = scanner.nextLine();
        int num = -1;

        // Using Integer.parseInt()
        try {
            num = Integer.parseInt(str);
        } catch (NumberFormatException e) {
           // System.out.println("Error: Invalid string for Integer.parseInt()");
        }

        // Using Integer.valueOf()
        try {
            num = Integer.valueOf(str);
        } catch (NumberFormatException e) {
            //System.out.println("Error: Invalid string for Integer.valueOf()");
        }

        if (num < 0) {
            System.out.println("Error: Invalid string. Please enter an integer");
            return stringToInt(toPrint); // fixed by adding return
        }
        return num;
    }

}
