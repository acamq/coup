public class ActionsPrinter {
    public static int numActions = 9;
    public static String welcomeMessage(){
        String welcomeMessage =
                "Welcome to Coup! You can read the rules here. https://www.qugs.org/rules/r131357.pdf.\n" +
                        "Please use your keyboard as prompted to take actions. Unless otherwise specified, decline an action by not inputting anything and pressing enter. Have fun!";
        return welcomeMessage;
    }
    public static String instructions(){
        String instructions =
                "You can read the rules here. https://www.qugs.org/rules/r131357.pdf.\n" +
                        "Please use your keyboard as prompted to take actions. Unless otherwise specified, decline an action by not inputting anything and pressing enter.";
        return instructions;
    }
    public static String getActions(){
        String actions = "1. Income\n"
                + "2. Foreign Aid\n"
                + "3. Coup (needs 7 tokens)\n"
                + "4. Tax\n"
                + "5. Assassinate\n"
                + "6. Steal\n"
                + "7. Exchange\n"
                + "8. View your influences\n"
                + "9. View instructions and rules";
        return actions;
    }
}
