public class StringToInt {
    public static int stringToInt(String input) {
        String str = input;
        int num = -1;

        // Using Integer.parseInt()
        try {
            num = Integer.parseInt(str);
            System.out.println("Integer.parseInt(): " + num);
        } catch (NumberFormatException e) {
            System.out.println("Error: Invalid string for Integer.parseInt()");
        }

        // Using Integer.valueOf()
        try {
            num = Integer.valueOf(str);
            System.out.println("Integer.valueOf(): " + num);
        } catch (NumberFormatException e) {
            System.out.println("Error: Invalid string for Integer.valueOf()");
        }
        return num;
    }
}