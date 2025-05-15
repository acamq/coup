public class Player {
    private String name;
    private int tokens;
    private Influence influence1;
    private Influence influence2;
    private int index;
    private boolean out;

    public Player(String name, int index){
        this.name = name;
        this.tokens = 2;
        this.influence1 = Influence.INFLUENCES[(int) (Math.random()*5)];
        this.influence2 = Influence.INFLUENCES[(int) (Math.random()*5)];
        this.index = index;
    }
    public Player(String name, int index, int tokens){
        this.name = name;
        this.tokens = tokens;
        this.influence1 = Influence.INFLUENCES[(int) (Math.random()*5)];
        this.influence2 = Influence.INFLUENCES[(int) (Math.random()*5)];
        this.index = index;
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
        return "Name: " + name + ", Tokens: " + tokens + ", # of influences: " + numInfluences();
    }
    public void income(){
        tokens += 1;
    }
    public void foreignAid(){
        tokens += 2;
    }
    public void tax(){
        tokens += 3;
    }
    public void blockSteal(Player victim, Player thief){
        thief.tokens -=2;
        victim.tokens +=2;
    }
    public void steal(Player target){
        target.tokens -= 2;
        this.tokens += 2;
    }

    public String toString(){
        return name;
    }

    public boolean canCoup(){
        return tokens >= 7;
    }

    public boolean mustCoup(){
        return tokens >= 10;
    }
    public int numInfluences(){
        int count = 0;
        if (influence1 != null){count++;}
        if  (influence2 != null){count++;}
        return count;
    }

    public void coup(Player target){
        if (influence1.equals(null) || influence2.equals(null)){
            System.out.println(target + ", you are out!");
            influence1 = null;
            influence2 = null;
            out = true;
        }
        else{
            int toLose = StringToInt.stringToInt(target+", you have been targeted by a Coup. Please choose which influence to lose, 1 or 2.");
            boolean removed = false;
            while (!removed) {
                if (toLose == 1) {
                    influence1 = null;
                    removed = true;
                } else if (toLose == 2) {
                    influence2 = null;
                    removed = true;
                }
                else{
                    toLose = StringToInt.stringToInt("Invalid input. Please choose influence 1 or 2.");
                }
            }
        }
    }

    public void blockAid(Player blocked){
        blocked.tokens -= 2;
    }

    public void Challenge(Player target, String action){

    }

    public boolean canAssassinate(){
        return tokens >= 3;
    }

    public void assassinate(Player target){
        System.out.println(target + ", you are being assassinated. If you want to block, do it now.");

        if (influence1.equals(null) || influence2.equals(null)){
            System.out.println(target + ", you are out!");
            influence1 = null;
            influence2 = null;
            out = true;
        }
        else{
            int toLose = StringToInt.stringToInt(target+", you have been targeted by a assassination. Please choose which influence to lose, 1 or 2.");
            boolean removed = false;
            while (!removed) {
                if (toLose == 1) {
                    influence1 = null;
                    removed = true;
                } else if (toLose == 2) {
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
