public class Player {
    private String name;
    private int tokens;
    private Influence influence1;
    private Influence influence2;


    public Player(String name){
        this.name = name;
        this.tokens = 0;
        this.influence1 = Influence.INFLUENCES[(int) (Math.random()*5)];
        this.influence2 = Influence.INFLUENCES[(int) (Math.random()*5)];
    }
    private void addTokens(int token){
        this.tokens += token;
    }
    private void removeTokens(int token){
        this.tokens -= token;
    }
    public void income(){
        this.tokens++;
    }
    public void foreignAid(){
        this.tokens += 2;
    }
    public void tax(){
        this.tokens += 3;
    }
    public void steal(Player target){
        this.tokens += 2;
        target.removeTokens(2);
    }


}
