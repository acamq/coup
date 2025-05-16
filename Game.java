import java.util.Scanner;

public class Game {
    private Player[] players;
    public static String getInput(String toPrint){
        Scanner scanner = new Scanner(System.in);
        System.out.println(toPrint);
        return scanner.nextLine();
    }

    private final String CHALLENGEREASON = ", you lost a challenge. Please choose an influence to lose.";

    public boolean income(int player_i){
        players[player_i].addTokens(1);
        System.out.println("You gained a token.");
        return true;
    }
    public boolean foreignAid(int player_i){
        boolean canAid = blockAndChallenge(player_i, "foreign aid", Influence.INFLUENCES[0]);
        if (canAid){
            players[player_i].addTokens(2);
            System.out.println("You gained 2 tokens.");
        }
        return true;
    }

    public boolean coup(int player_i, int player_target){
        if (players[player_i].getTokens()<7){
            System.out.println("Not enough tokens!");
            return false;
        }
        players[player_target].loseInfluence(" you have been targeted by a coup.");
        players[player_i].removeTokens(7);
        System.out.println("You successfully launched a coup!");
        return true;
    }

    public boolean tax(int player_i){
        boolean canTax = challengeCurrent(player_i, "tax", Influence.INFLUENCES[0]);
        if (canTax){
            players[player_i].addTokens(3);
            System.out.println("You gained 3 tokens.");
        }
        return true;
    }

    public boolean assassination(int player_killer, int player_target){
        if (players[player_killer].getTokens()<3){
            System.out.println("Not enough tokens!");
            return false;
        }
        boolean canKill = true;
        String choice = Game.getInput("Block (1) or challenge (2)");
        while (!(choice.equals("1") || choice.equals("2")|| choice.isEmpty())) {
            choice = Game.getInput("Invalid input. Block (1) or challenge (2)");
        }
        if (!choice.isEmpty()) {
            if (choice.equals("1")) {
                canKill = blockAndChallenge(player_killer, "assassination", Influence.INFLUENCES[4]);
            } else if (choice.equals("2")){
                canKill = challengeCurrent(player_killer, "assassination", Influence.INFLUENCES[1]);
            }
        }
        if (canKill) {
            players[player_target].loseInfluence(", you have been targeted by an assassination.");
            System.out.println("You successfully assassinated someone!");
        }
        players[player_killer].removeTokens(3);
        return true;
    }

    public boolean steal(int player_thief, int player_target){
        boolean canSteal = true;
        String choice = Game.getInput("Block (1) or challenge (2)");
        while (!(choice.equals("1") || choice.equals("2")|| choice.isEmpty())) {
            choice = Game.getInput("Invalid input. Block (1) or challenge (2)");
        }
        if (!choice.isEmpty()) {
            if (choice.equals("1")) {
                String willBlock = Game.getInput("Please choose Captain or Ambassador to block with.");
                while (!(willBlock.equals("Captain")||willBlock.equals("Ambassador")||willBlock.isEmpty())){
                    willBlock = Game.getInput("Invalid input. Please choose Captain or Ambassador");
                }
                if (!willBlock.isEmpty()){
                    if (willBlock.equals("Captain")){
                        canSteal = blockAndChallenge(player_thief, "steal", Influence.INFLUENCES[2]);
                    }
                    if (willBlock.equals("Ambassador")){
                        canSteal = blockAndChallenge(player_thief, "steal", Influence.INFLUENCES[3]);
                    }
                }
            } else if (choice.equals("2")){
                canSteal = challengeCurrent(player_thief, "steal", Influence.INFLUENCES[2]);
            }
        }
        if (canSteal){
            players[player_thief].addTokens(2);
            players[player_target].removeTokens(2);
            System.out.println("You successfully stole 2 tokens!");
        }
        return true;
    }

    public boolean exchange(int player_i){
        boolean canExchange = challengeCurrent(player_i, "exchange", Influence.INFLUENCES[3]);
        if (canExchange) {
            Player exchanging = players[player_i];
            boolean has1 = !exchanging.getInfluence1().equals(null);
            boolean has2 = !exchanging.getInfluence2().equals(null);
            Influence[] exchange;
            if (has1 && has2) {
                exchange = new Influence[]{exchanging.getInfluence1(), exchanging.getInfluence2(), Influence.random(), Influence.random()};
                SecretInfluenceViewer.showInfluences(exchange, true);
            } else {
                if (has1) {
                    exchange = new Influence[]{exchanging.getInfluence1(), Influence.random(), Influence.random()};
                } else {
                    exchange = new Influence[]{exchanging.getInfluence2(), Influence.random(), Influence.random()};
                }
            }
            SecretInfluenceViewer.showInfluences(exchange, true);
            String chosen = Game.getInput("Choose two influences by entering the number of the influence, a space, and another number.");
            int chosen1 = StringToInt.convertToInt(chosen.substring(0, 1)) - 1;
            int chosen2 = StringToInt.convertToInt(chosen.substring(2)) - 1;
            exchanging.setInfluence1(exchange[chosen1]);
            exchanging.setInfluence2(exchange[chosen2]);
            System.out.println("You have exchanged.");
        }
        return true;
    }



    public boolean blockAndChallenge(int player_i, String action, Influence influence){
        Player current = players[player_i];
        String blocked = Game.getInput("Blocking player number?");
        if (!blocked.isEmpty()){
            int blocker = StringToInt.convertToInt(blocked.substring(0,1));
            while (blocker -1 == player_i || blocker > players.length || blocker < 1){
                blocker = StringToInt.convertToInt("Invalid player number.");
            }
            Player counter = players[blocker-1];
            String challengeBlock = Game.getInput("Challenge the block? Input anything to challenge.");
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
                    System.out.println(
                            String.format("%s, you will receive a random influence to replace the revealed one.",
                                    counter)
                    );
                    if (counter.getInfluence1().equals(influence)){
                        Influence toGive = Influence.random();
                        SecretInfluenceViewer.showInfluences(new Influence[]{toGive}, false);
                        counter.setInfluence1(toGive);
                    }
                    else if (counter.getInfluence2().equals(influence)){
                        Influence toGive = Influence.random();
                        SecretInfluenceViewer.showInfluences(new Influence[]{toGive}, false);
                        counter.setInfluence2(toGive);
                    }
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
        String challenged = getInput("Challenging player number?");
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
                System.out.println(
                        String.format("%s, you will receive a random influence to replace the revealed one.",
                                current)
                );
                if (current.getInfluence1().equals(influence)){
                    Influence toGive = Influence.random();
                    SecretInfluenceViewer.showInfluences(new Influence[]{toGive, current.getInfluence2()}, false);
                    current.setInfluence1(toGive);
                }
                else if (current.getInfluence2().equals(influence)){
                    Influence toGive = Influence.random();
                    SecretInfluenceViewer.showInfluences(new Influence[]{current.getInfluence1(), toGive}, false);
                    current.setInfluence2(toGive);
                }
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
                players[i] = new Player(name, i+1);
            }
        }
        SecretInfluenceViewer.show(players);
        System.out.println("Please view your influences");
        System.out.println("Game setup complete! Please check your tabs for the game UI.");
    }
    public boolean checkWin(){
        int numOut = 0;
        for (Player player : players) {
            if (player.isOut()) {
                numOut++;
            }
        }
        if (numOut >= players.length -1){
            return true;
        }
        else{
            return false;
        }
    }

    public int checkValidTarget(int target) {
        int newTarget = target;
        while (newTarget < 0
                || newTarget >= players.length
                || players[newTarget].isOut()) {
            newTarget = StringToInt.stringToInt("Invalid target. Please choose a new target.");
        }
        return newTarget;
    }

    public void Play(){
        getPlayers();
        System.out.println(ActionsPrinter.welcomeMessage());
        boolean win = false;
        int player_i = 0;
        int max = players.length;
        while (!win){
            boolean tookAction = false;
            Player currentPlayer = players[player_i];
            boolean mustCoup = currentPlayer.mustCoup();
            for (Player p : players){
                System.out.print(p.getInfo() + ", ");
            }
            System.out.println();
            System.out.println("It is " + currentPlayer + "'s turn.");
            while(!tookAction) {
                if (mustCoup){
                    int target = StringToInt.stringToInt(("You must launch a coup. Please choose a target."))-1;
                    target = checkValidTarget(target);
                    tookAction = coup(player_i, target);
                    continue;
                }
                int action = StringToInt.stringToInt(ActionsPrinter.getActions());
                while (action < 0 || action > ActionsPrinter.numActions) {
                    action = StringToInt.stringToInt("Invalid input. Please choose an action");
                }
                if (action == 1) {
                    tookAction = income(player_i);
                }
                if (action == 2) {
                    tookAction = foreignAid(player_i);
                }
                if (action == 3) {
                    int target = StringToInt.stringToInt("Please enter the target player number") - 1;
                    target = checkValidTarget(target);
                    tookAction = coup(player_i, target);
                }
                if (action == 4){
                    tookAction = tax(player_i);
                }
                if (action == 5){
                    int target = StringToInt.stringToInt("Please enter the target player number") - 1;
                    target = checkValidTarget(target);
                    tookAction = assassination(player_i, target);
                }
                if (action == 6){
                    int target =  StringToInt.stringToInt("Please enter the target player number") - 1;
                    target = checkValidTarget(target);
                    tookAction = steal(player_i, target);
                }
                if (action == 7){
                    tookAction = exchange(player_i);
                }
                if (action == 8){
                    SecretInfluenceViewer.showInfluences(new Influence[]{currentPlayer.getInfluence1(), currentPlayer.getInfluence2()},false);
                }
                if (action == 9){
                    System.out.println(ActionsPrinter.instructions());
                }
                if (!tookAction){
                    System.out.println("Please choose another action.");
                }
            }
            win = checkWin();
            player_i = (player_i + 1) % max;
        }
        for (int i = 0; i<players.length;i++){
            if (!players[i].isOut()){
                System.out.println("Player " + (i+1) + " wins!");
            }
        }

    }

    public static void main(String[] args){
        Game game = new Game();
        game.Play();
    }
}
