import java.util.Scanner;

public class Game {
    private Player[] players;
    public static String getInput(String toPrint){
        Scanner scanner = new Scanner(System.in);
        System.out.println(toPrint);
        return scanner.nextLine();
    }

    private final String CHALLENGEREASON = ", you lost a challenge. Please choose an influence to lose.";

    public void foreignAid(int player_i){
        boolean canAid = blockAndChallenge(player_i, "foreign aid", Influence.INFLUENCES[0]);
        if (canAid){
            players[player_i].addTokens(2);
        }
    }

    public void coup(int player_i){
        players[player_i].loseInfluence(" you have been targeted by a coup.");
    }

    public void tax(int player_i){
        boolean canTax = challengeCurrent(player_i, "tax", Influence.INFLUENCES[0]);
        if (canTax){
            players[player_i].addTokens(3);
        }
    }

    public void assassination(int player_killer, int player_target){
        int choice = StringToInt.stringToInt("Block (1) or challenge (2)");
        while (!(choice == 1 || choice == 2)) {
            choice = StringToInt.stringToInt("Invalid input. Block (1) or challenge (2)");
        }
        boolean canKill = false;
        if (choice == 1){
            canKill = blockAndChallenge(player_killer, "assassination", Influence.INFLUENCES[4]);
        }
        else if (choice == 2){
            canKill = challengeCurrent(player_killer, "assassination", Influence.INFLUENCES[1]);
        }
        if (canKill){
            players[player_target].loseInfluence(", you have been targeted by an assassination.");
        }
    }

    public void steal(int player_thief, int player_target){
        String willBlock = Game.getInput("If you want to block, please declare an influence to block with.");
        if (!willBlock.isEmpty()){
            if (willBlock.equals("Captain")){
                boolean canSteal = blockAndChallenge(player_thief, "steal", Influence.INFLUENCES[2]);
            }
            if (willBlock.equals("Ambassador")){
                boolean canSteal = blockAndChallenge(player_thief, "steal", Influence.INFLUENCES[3]);
            }
        }
        boolean canSteal; //= blockAndChallenge(player_thief, "robbery", Influence.INFLUENCES[3]);
        if (canSteal){
            players[player_thief].addTokens(2);
            players[player_target].removeTokens(2);
        }
    }

    public boolean blockAndChallenge(int player_i, String action, Influence influence){
        Player current = players[player_i];
        String blocked = Game.getInput("Block?");
        if (!blocked.isEmpty()){
            int blocker = StringToInt.convertToInt(blocked.substring(0,1));
            while (blocker -1 == player_i || blocker > players.length || blocker < 1){
                blocker = StringToInt.convertToInt("Invalid player number.");
            }
            Player counter = players[blocker-1];
            String challengeBlock = Game.getInput("Challenge block?");
            if (!challengeBlock.isEmpty()){
                if (counter.getInfluence1().equals(influence)||counter.getInfluence2().equals(influence)){
                    System.out.println(
                            String.format("%1$s had an %2$s. %3$s, you lose the challenge and an influence.",
                                    counter, influence, current)
                    );
                    current.loseInfluence(CHALLENGEREASON);
                    System.out.println(
                            String.format("%s, your %s has been blocked by %s",
                                    current, action, counter)
                    );
                    return false;
                }
                else {
                    System.out.println(
                            String.format(
                                    "%1$s did not have a %2$s! %1$s, you lose the challenge and an influence.",
                                    counter, influence
                            )
                    );
                    counter.loseInfluence(CHALLENGEREASON);
                    System.out.println(
                            String.format("%s, your %s was successful.",
                                    current, action)
                    );
                    return true;
                }
            }
            else {
                System.out.println(
                        String.format("%s, your %s has been blocked by %s",
                                current, action, counter)
                );

                return false;
            }
        }
        else{
            System.out.println(
                    String.format("%s, your %s was successful.",
                            current, action)
            );
            return true;
        }
    }
    
    public boolean challengeCurrent(int player_i, String action, Influence influence){
        Player current = players[player_i];
        String challenged = getInput("Challenge?");
        if (!challenged.isEmpty()){
            int blocker = StringToInt.convertToInt(challenged.substring(0,1));
            while (blocker -1 == player_i || blocker > players.length || blocker < 1){
                blocker = StringToInt.convertToInt("Invalid player number.");
            }
            Player challenger = players[blocker-1];
            if (current.getInfluence1().equals(influence)||current.getInfluence2().equals(influence)){
                System.out.println(
                        String.format("%1$s had an %2$s. %3$s, you lose the challenge and an influence.",
                                current, influence, challenger)
                );

                challenger.loseInfluence(CHALLENGEREASON);
                return true;
            }
            else {
                System.out.println(
                        String.format(
                                "%1$s did not have a %2$s! %1$s, you lose the challenge and an influence.",
                                current, influence
                        )
                );
                current.loseInfluence(CHALLENGEREASON);
                return false;
            }
        }
        return true;
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
