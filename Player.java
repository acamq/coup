import java.util.HashMap;
import java.util.Map;

public class Player {
    private String name;
    private int tokens;
    private Influence influence1;
    private Influence influence2;


    public Player(String name) {
        this.name = name;
        this.tokens = 0;
        this.influence1 = Influence.deck.remove((int) (Math.random() * Influence.deck.size()-1));
        this.influence2 = Influence.deck.remove((int) (Math.random() * Influence.deck.size()-1));
    }

    private void addTokens(int token){
        this.tokens += token;
    }
    private void removeTokens(int token){
        this.tokens -= token;
    }

    // Core player actions
    public void income(){
        this.tokens++;
    }
    public void foreignAid(){
        this.tokens += 2;
        return
    }
    public void coup(Player target){
        //front end to get target's chosen influence
        this.tokens -= 7;
        int chosen;
        if (chosen == 1) target.influence1 = Influence.INFLUENCES[5];
        if (chosen == 2) target.influence2 = Influence.INFLUENCES[5];
    }
    public boolean mustCoup(){
        return this.tokens >= 10;
    }

    //Character dependent
    public boolean tax(){
        this.tokens += 3;
        return this.influence1.canTax() || this.influence2.canTax();
    }
    public boolean steal(Player target){
        if (target.tokens >= 2){
            this.tokens += 2;
            target.tokens -=2;
        }
        else if (target.tokens == 0){
            //placeholder print, replace with front end
            System.out.println("Cannot steal from " + target.name);
        }
        else{
            this.tokens+=1;
            target.tokens -= 1;
        }
        return this.influence1.canSteal() || this.influence2.canSteal();
    }
    public boolean exchange(){
        Influence exchange1 = Influence.deck.remove((int) (Math.random() * Influence.deck.size()-1));
        Influence exchange2 = Influence.deck.remove((int) (Math.random() * Influence.deck.size()-1));
        Map<String, Influence> options = new HashMap<>();
        options.put("exchange1", exchange1);
        options.put("exchange2", exchange2);
        options.put("influence1", this.influence1);
        options.put("influence2", this.influence2);
        // code to get user input on which to keep
        String user1, user2;
        this.influence1 = options.get(user1);
        this.influence2 = options.get(user2);

    }

    public String toString(){
        return this.name + " Tokens: " + tokens + " Influence 1: " + influence1.getName() + ", Influence 2: " + influence2.getName();
    }
    public static void main(String[] args){
        Player player1 = new Player("Alice");
        Player player2 = new Player("Bob");
        player1.tax();
        System.out.println(player1);
        player2.steal(player1);
        System.out.println(player2);
        System.out.println(player1);
    }

}
