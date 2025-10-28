import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class Game {
    private Player[] players;
    private static Scanner inputScanner = new Scanner(System.in);
    private final Deck deck;

    public Game(){
        deck = new Deck();
    }
    public static String getInput(String toPrint){
        System.out.println(toPrint);
        if (inputScanner.hasNextLine()) {
            return inputScanner.nextLine();
        }
        return "";
    }

    static void resetInputScanner(){
        inputScanner = new Scanner(System.in);
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
        Influence lost = players[player_target].loseInfluence(" you have been targeted by a coup.");
        deck.discard(lost);
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
        String choice = Game.getInput("Block (1) or challenge (2) (only the target may block)");
        while (!(choice.equals("1") || choice.equals("2")|| choice.isEmpty())) {
            choice = Game.getInput("Invalid input. Block (1) or challenge (2) (only the target may block)");
        }
        if (!choice.isEmpty()) {
            if (choice.equals("1")) {
                canKill = blockAndChallenge(player_killer, "assassination", Influence.INFLUENCES[4], player_target);
            } else if (choice.equals("2")){
                canKill = challengeCurrent(player_killer, "assassination", Influence.INFLUENCES[1]);
            }
        }
        if (canKill) {
            Influence lost = players[player_target].loseInfluence(", you have been targeted by an assassination.");
            deck.discard(lost);
            System.out.println("You successfully assassinated someone!");
        }
        players[player_killer].removeTokens(3);
        return true;
    }

    public boolean steal(int player_thief, int player_target){
        boolean canSteal = true;
        String choice = Game.getInput("Block (1) or challenge (2) (only the target may block)");
        while (!(choice.equals("1") || choice.equals("2")|| choice.isEmpty())) {
            choice = Game.getInput("Invalid input. Block (1) or challenge (2) (only the target may block)");
        }
        if (!choice.isEmpty()) {
            if (choice.equals("1")) {
                String willBlock = Game.getInput("Target player: choose Captain or Ambassador to block with.");
                while (!(willBlock.equals("Captain")||willBlock.equals("Ambassador")||willBlock.isEmpty())){
                    willBlock = Game.getInput("Invalid input. Target player: choose Captain or Ambassador");
                }
                if (!willBlock.isEmpty()){
                    if (willBlock.equals("Captain")){
                        canSteal = blockAndChallenge(player_thief, "steal", Influence.INFLUENCES[2], player_target);
                    }
                    if (willBlock.equals("Ambassador")){
                        canSteal = blockAndChallenge(player_thief, "steal", Influence.INFLUENCES[3], player_target);
                    }
                }
            } else if (choice.equals("2")){
                canSteal = challengeCurrent(player_thief, "steal", Influence.INFLUENCES[2]);
            }
        }
        if (canSteal){
            int amount = Math.min(2, players[player_target].getTokens());
            if (amount > 0) {
                players[player_thief].addTokens(amount);
                players[player_target].removeTokens(amount);
            }
            System.out.println("You successfully stole " + amount + (amount == 1 ? " token!" : " tokens!"));
        }
        return true;
    }

    public boolean exchange(int player_i){
        boolean canExchange = challengeCurrent(player_i, "exchange", Influence.INFLUENCES[3]);
        if (canExchange) {
            Player exchanging = players[player_i];
            boolean has1 = !(exchanging.getInfluence1() == null);
            boolean has2 = !(exchanging.getInfluence2() == (null));
            Influence[] exchange;
            if (has1 && has2) {
                List<Influence> pool = new ArrayList<>();
                pool.add(exchanging.getInfluence1());
                pool.add(exchanging.getInfluence2());
                Influence draw1 = deck.draw();
                Influence draw2 = deck.draw();
                pool.add(draw1);
                pool.add(draw2);
                exchange = pool.toArray(new Influence[0]);
                SecretInfluenceViewer.showInfluences(exchange, true);
                int chosen1 = StringToInt.stringToInt("Choose one influence to keep.");
                while (chosen1 > exchange.length || chosen1 < 1){
                    chosen1 = StringToInt.stringToInt("Invalid input. Please enter a number between 1 and " + exchange.length);
                }
                int chosen2 = StringToInt.stringToInt("Choose another influence to keep");
                while (chosen2 > exchange.length || chosen2 < 1 || chosen1 == chosen2){
                    chosen2 = StringToInt.stringToInt("Invalid input. Please enter a number between 1 and " + exchange.length + ", and do not choose the same influence.");
                }
                Set<Integer> chosenIdx = new HashSet<>();
                chosenIdx.add(chosen1 - 1);
                chosenIdx.add(chosen2 - 1);
                exchanging.setInfluence1(exchange[chosen1-1]);
                exchanging.setInfluence2(exchange[chosen2-1]);
                List<Influence> toReturn = new ArrayList<>();
                for (int idx = 0; idx < exchange.length; idx++){
                    if (!chosenIdx.contains(idx)){
                        toReturn.add(exchange[idx]);
                    }
                }
                deck.returnToDeck(toReturn);
            } else {
                if (has1) {
                    List<Influence> pool = new ArrayList<>();
                    pool.add(exchanging.getInfluence1());
                    Influence draw1 = deck.draw();
                    Influence draw2 = deck.draw();
                    pool.add(draw1);
                    pool.add(draw2);
                    exchange = pool.toArray(new Influence[0]);
                    SecretInfluenceViewer.showInfluences(exchange, true);
                    int chosen = StringToInt.stringToInt("Choose the influence you would like to keep.");
                    while (chosen > exchange.length || chosen < 1){
                        chosen = StringToInt.stringToInt("Invalid input. Please enter a number between 1 and " + exchange.length);
                    }
                    exchanging.setInfluence1(exchange[chosen-1]);
                    List<Influence> toReturn = new ArrayList<>();
                    for (int idx = 0; idx < exchange.length; idx++){
                        if (idx != chosen - 1){
                            toReturn.add(exchange[idx]);
                        }
                    }
                    deck.returnToDeck(toReturn);
                } else {
                    List<Influence> pool = new ArrayList<>();
                    pool.add(exchanging.getInfluence2());
                    Influence draw1 = deck.draw();
                    Influence draw2 = deck.draw();
                    pool.add(draw1);
                    pool.add(draw2);
                    exchange = pool.toArray(new Influence[0]);
                    SecretInfluenceViewer.showInfluences(exchange, true);
                    int chosen = StringToInt.stringToInt("Choose the influence you would like to keep.");
                    while (chosen > exchange.length || chosen < 1){
                        chosen = StringToInt.stringToInt("Invalid input. Please enter a number between 1 and " + exchange.length);
                    }
                    exchanging.setInfluence2(exchange[chosen-1]);
                    List<Influence> toReturn = new ArrayList<>();
                    for (int idx = 0; idx < exchange.length; idx++){
                        if (idx != chosen - 1){
                            toReturn.add(exchange[idx]);
                        }
                    }
                    deck.returnToDeck(toReturn);
                }
            }
            System.out.println("You have exchanged.");
        }
        return true;
    }

    public boolean blockAndChallenge(int player_i, String action, Influence influence){
        return blockAndChallenge(player_i, action, influence, null);
    }

    public boolean blockAndChallenge(int player_i, String action, Influence influence, Integer allowedBlocker){
        Player current = players[player_i];
        String prompt = "Blocking player number?";
        if (allowedBlocker != null){
            prompt = String.format("Blocking player number? (Only player %d may block.)", allowedBlocker + 1);
        }
        String blocked = Game.getInput(prompt);
        if (!blocked.isEmpty()){
            String trimmedBlock = blocked.trim();
            while (trimmedBlock.isEmpty()){
                trimmedBlock = Game.getInput(prompt).trim();
            }
            int blocker = StringToInt.convertToInt(trimmedBlock);
            while (blocker -1 == player_i || blocker > players.length || blocker < 1 || players[blocker-1].isOut()
                    || (allowedBlocker != null && blocker - 1 != allowedBlocker)){
                String invalidMessage = "Invalid player number.";
                if (allowedBlocker != null){
                    invalidMessage = String.format("Invalid player number. Only player %d may block this action.", allowedBlocker + 1);
                }
                String retry = Game.getInput(invalidMessage).trim();
                while (retry.isEmpty()){
                    retry = Game.getInput(invalidMessage).trim();
                }
                blocker = StringToInt.convertToInt(retry);
            }
            Player counter = players[blocker-1];
            System.out.println("Challenge the block?");
            boolean challengeStatus = challengeCurrent(blocker -1, action, influence);
            if (challengeStatus){
                System.out.printf("%s, your %s has been blocked by %s%n",
                        current, action, counter);
                return false;
            }
            else {
                System.out.printf("%s, your %s was successful.%n",
                        current, action);
                return true;
            }
        }
        else{
            System.out.printf("%s, your %s was successful.%n",
                    current, action);
            return true;
        }
    }

    public boolean challengeCurrent(int player_i, String action, Influence influence){
        Player current = players[player_i];
        String challenged = getInput("Challenging player number?");
        if (!challenged.isEmpty()){
            int challengePlayer = StringToInt.convertToInt(challenged.substring(0,1));
            while (challengePlayer -1 == player_i || challengePlayer > players.length || challengePlayer < 1 || players[challengePlayer-1].isOut()){
                challengePlayer = StringToInt.convertToInt("Invalid player number.");
            }
            Player challenger = players[challengePlayer-1];
            if (influence.equals(current.getInfluence1())||influence.equals(current.getInfluence2())){
                System.out.printf("%1$s had an %2$s. %3$s, you lose the challenge and an influence.%n",
                        current, influence, challenger);
                Influence challengerLost = challenger.loseInfluence(CHALLENGEREASON);
                deck.discard(challengerLost);
                System.out.printf("%s, you will receive a random influence to replace the revealed one.\nPlease check your tabs to see your influences.%n",
                        current);
                if (influence.equals(current.getInfluence1())){
                    Influence revealed = current.getInfluence1();
                    deck.returnToDeck(revealed);
                    Influence replacement = deck.draw();
                    current.setInfluence1(replacement);
                    SecretInfluenceViewer.showInfluences(new Influence[]{replacement, current.getInfluence2()}, false);
                }
                else if (influence.equals(current.getInfluence2())){
                    Influence revealed = current.getInfluence2();
                    deck.returnToDeck(revealed);
                    Influence replacement = deck.draw();
                    current.setInfluence2(replacement);
                    SecretInfluenceViewer.showInfluences(new Influence[]{current.getInfluence1(), replacement}, false);
                }
                return true;
            }
            else {
                System.out.printf(
                        "%1$s did not have a %2$s! %1$s, you lose the challenge and an influence.%n",
                        current, influence
                );
                Influence lost = current.loseInfluence(CHALLENGEREASON);
                deck.discard(lost);
                return false;
            }
        }
        return true;
    }
    public void setPlayers(){
        int numPlayers = StringToInt.stringToInt("Number of players? 2-6");
        while (numPlayers < 2 || numPlayers > 6){
            numPlayers = StringToInt.stringToInt("Invalid input. Must have 2 to 6 players");
        }
        players = new Player[numPlayers];
        if (numPlayers == 2){
            System.out.println("2 player modified version: each player starts with 1 token.");
            for (int i = 0; i<numPlayers; i++) {
                String name = getInput("Name of player " + (i + 1));
                while (name==null || name.isEmpty()){
                    name = getInput("Name cannot be empty");
                }
                players[i] = new Player(name, i, 1);
                players[i].setInfluence1(deck.draw());
                players[i].setInfluence2(deck.draw());
            }
        }
        else{
            for (int i = 0; i<numPlayers; i++){
                String name = getInput("Name of player " + (i + 1));
                while (name==null || name.isEmpty()){
                    name = getInput("Name cannot be empty");
                }
                players[i] = new Player(name, i+1);
                players[i].setInfluence1(deck.draw());
                players[i].setInfluence2(deck.draw());
            }
        }
        SecretInfluenceViewer.show(players);
        System.out.println("Please view your influences");
        System.out.println("Game setup complete! Please check your tabs to view your influences .");
    }
    public boolean checkWin(){
        int numOut = 0;
        for (Player player : players) {
            if (player.isOut()) {
                numOut++;
            }
        }
        return numOut >= players.length - 1;
    }
    //checks for validity and converts to index
    public int checkValidTarget(int current_p, int initialPlayerNumber) {
        int idx = initialPlayerNumber - 1;
        while (idx < 0
                || idx >= players.length
                || players[idx].isOut()
                || idx == current_p)
        {
            int pick = StringToInt.stringToInt(
                    "Invalid target. Please choose a new target (1–"
                            + players.length + "):");
            idx = pick - 1;
        }
        return idx;
    }
    //Main loop that runs through people's turns
    public void play(){
        setPlayers();
        System.out.println(ActionsPrinter.welcomeMessage());
        boolean win = false;
        int player_i = 0;
        int max = players.length;
        while (!win){
            boolean tookAction = false;
            Player currentPlayer = players[player_i];
            boolean canPlay = currentPlayer.isOut();
            boolean mustCoup = currentPlayer.mustCoup();
            if (!canPlay){
                for (Player p : players){
                    System.out.print(p.getInfo() + ", ");
                }
                System.out.println();
                System.out.println("It is " + currentPlayer + "'s turn.");
                while(!tookAction) {
                    if (mustCoup){
                        int target = StringToInt.stringToInt(("You must launch a coup. Please choose a target."));
                        target = checkValidTarget(player_i, target);
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
                        int target = StringToInt.stringToInt("Please enter the target player number");
                        target = checkValidTarget(player_i, target);
                        tookAction = coup(player_i, target);
                    }
                    if (action == 4){
                        tookAction = tax(player_i);
                    }
                    if (action == 5){
                        int target = StringToInt.stringToInt("Please enter the target player number");
                        target = checkValidTarget(player_i, target);
                        tookAction = assassination(player_i, target);
                    }
                    if (action == 6){
                        int target =  StringToInt.stringToInt("Please enter the target player number");
                        target = checkValidTarget(player_i, target);
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
        game.play();
    }
}
