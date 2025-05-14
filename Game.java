import java.util.Scanner;
public class Game {
    private Player[] players;
    public static String getInput(String toPrint){
        Scanner scanner = new Scanner(System.in);
        System.out.println(toPrint);
        return scanner.nextLine();
    }

    public void getPlayers(){
        int numPlayers = StringToInt.stringToInt("Number of players?");
        players = new Player[numPlayers];
        for (int i = 0; i<numPlayers; i++){
            String name = getInput("Name of player "+ (i+1));
            players[i] = new Player(name, i);
        }
    }
    public boolean checkWin(){
        int numOut = 0;
        for (int i = 0; i < players.length; i++){
            if (players[i].isOut()){
                numOut++;
            }
        }
        if (numOut == players.length -1){
            return true;
        }
        else{
            return false;
        }
    }
    public void Play(){
        getPlayers();
        boolean win = false;
        while (!win) {
            for (int i = 0; i < players.length; i++){
                //takeAction();
            }
            win = checkWin();
        }
    }

    public static void main(String[] args){
        Game game = new Game();
        game.Play();
    }
}
