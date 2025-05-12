public class GameAction {
    private Player[] players;
    private Deck gameDeck;
    private String code;

    public void BlockAction(int i_target, int i_challenger, String action){

    }

    public void income(int i_player){
        this.players[i_player].addTokens(1);
    }
    public void foreignAid(int i_player){
        this.players[i_player].addTokens(2);
    }
    public void tax(int i_player){
        this.players[i_player].addTokens(3);
    }


}
