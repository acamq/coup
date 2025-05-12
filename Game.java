import java.util.Scanner;
public class Game {

    public static String getInput(String toPrint){
        Scanner scanner = new Scanner(System.in);
        System.out.println(toPrint);
        return scanner.nextLine();
    }

    public static void main(String[] args){
        int numPlayers = StringToInt.stringToInt(getInput("Number of players?"));
        for (int i = 0; i<numPlayers; i++){
             getInput("Name of player "+ (i+1));
        }
    }
}
