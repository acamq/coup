public class Player {
    private String name;
    private int tokens;
    private Influence influence1;
    private Influence influence2;
    private int playerNum;
    private boolean out;

    public Player(String name, int playerNum){
        this.name = name;
        this.tokens = 2;
        this.influence1 = Influence.random();
        this.influence2 = Influence.random();
        this.playerNum = playerNum;
    }
    public Player(String name, int playerNum, int tokens){
        this.name = name;
        this.tokens = tokens;
        this.influence1 = Influence.random();
        this.influence2 = Influence.random();
        this.playerNum = playerNum;
    }
    public void addTokens(int token){
        this.tokens += token;
    }
    public void removeTokens(int token){
        this.tokens -= token;
    }
    public boolean isOut(){
        return out;
    }
    public String getName(){return name;}
    public String getInfo(){
        if (isOut()){
            return String.format(
                    "Name: %s (Player #%d) is out",
                    name, playerNum
            );
        }
        return String.format(
                "Name: %s (Player #%d), Tokens: %d, # of influences: %d",
                name, playerNum, tokens, numInfluences()
        );
    }
    public int getTokens(){
        return tokens;
    }
    public Influence getInfluence1(){
        return influence1;
    }
    public Influence getInfluence2(){
        return influence2;
    }
    public void setInfluence1(Influence influence){
        influence1 = influence;
    }
    public void setInfluence2(Influence influence){
        influence2 = influence;
    }

    public String toString(){
        return name;
    }

/*public boolean canCoup(){
        return tokens >= 7;
    }

 */

    public boolean mustCoup(){
        return tokens >= 10;
    }
    public int numInfluences(){
        int count = 0;
        if (influence1 != null){count++;}
        if  (influence2 != null){count++;}
        return count;
    }

    public void loseInfluence(String reason){
        if (influence1 == null || influence2 == null){
            System.out.println(this.name + ", you are out!");
            influence1 = null;
            influence2 = null;
            out = true;
        }
        else{
            int toLose = StringToInt.stringToInt(this.name+reason+"\nPlease choose an influence to lose by inputting 1 or 2.");
            boolean removed = false;
            while (!removed) {
                if (toLose == 1) {
                    System.out.println(this.name + " lost a " + influence1);
                    influence1 = null;
                    removed = true;
                } else if (toLose == 2) {
                    System.out.println(this.name + " lost a " + influence2);
                    influence2 = null;
                    removed = true;
                }
                else{
                    toLose = StringToInt.stringToInt("Invalid input. Please choose influence 1 or 2.");
                }
            }
        }
    }
}
