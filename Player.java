public class Player {
    private String name;
    private int tokens;
    private Influence influence1;
    private Influence influence2;
    private int index;
    private boolean out;

    public Player(String name, int index){
        this.name = name;
        this.tokens = 0;
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



}
