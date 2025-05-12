import java.util.ArrayList;
import java.util.Arrays;

public class Deck {

    private ArrayList<Influence> deck;

    public Deck(){
        deck = new ArrayList<Influence>()  {{
            for (int i = 0; i < 3; i++) {
                addAll(Arrays.asList(Influence.getInfluences()[0], Influence.getInfluences()[1], Influence.getInfluences()[2], Influence.getInfluences()[3], Influence.getInfluences()[4]));
            }
        }};
    }

}
