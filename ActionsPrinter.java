public class ActionsPrinter {
    /**String methods to avoid clutter*/
    public static int numActions = 9;
    public static String welcomeMessage(){
        return "Welcome to Coup! You can read the rules here. https://www.qugs.org/rules/r131357.pdf.\n" +
                "Please use your keyboard as prompted to take actions. Unless otherwise specified, decline an action by not inputting anything and pressing enter. Have fun!";
    }
    public static String instructions(){
        return "You can read the rules here. https://www.qugs.org/rules/r131357.pdf.\n" +
                "Please use your keyboard as prompted to take actions. Unless otherwise specified, decline an action by not inputting anything and pressing enter.";
    }
    public static String getActions(){
        String actions = """
                1. Income
                2. Foreign Aid
                3. Coup (needs 7 tokens)
                4. Tax
                5. Assassinate
                6. Steal
                7. Exchange
                8. View your influences
                9. View instructions and rules""";
        return actions;
    }
}
