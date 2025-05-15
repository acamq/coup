import java.util.Scanner;
public class Game {
    private Player[] players;
    public static String getInput(String toPrint){
        Scanner scanner = new Scanner(System.in);
        System.out.println(toPrint);
        return scanner.nextLine();
    }

    public void getPlayers(){
        int numPlayers = StringToInt.stringToInt("Number of players? 2-6");
        while (numPlayers == 1 || numPlayers > 6){
            numPlayers = StringToInt.stringToInt("Invalid input. Must have 2 to 6 players");
        }
        players = new Player[numPlayers];
        if (numPlayers == 2){
            System.out.println("2 player modified version: each player starts with 1 token.");
            for (int i = 0; i<numPlayers; i++) {
                String name = getInput("Name of player " + (i + 1));
                players[i] = new Player(name, i, 1);
            }
        }
        else{
            for (int i = 0; i<numPlayers; i++){
                String name = getInput("Name of player " + (i + 1));
                players[i] = new Player(name, i);
            }
        };
        SecretInfluenceViewer.show(players);
        System.out.println("Please view your influences");
        System.out.println("Game setup complete! Please check your tabs for the game UI.");
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

        MultipleButtonsFrame.showUI(players);
        while (!win) {
            //for (int i = 0; i < players.length; i++){
                //takeAction();
          //  }
            win = checkWin();
        }
    }

    public static void main(String[] args){
        Game game = new Game();
        game.Play();
    }
}
