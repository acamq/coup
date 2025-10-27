import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**Deck class to mimic a real deck of cards*/
public class Deck {

    private final List<Influence> drawPile;
    private final List<Influence> discardPile;

    public Deck(){
        drawPile = new ArrayList<>();
        discardPile = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            drawPile.addAll(Arrays.asList(Influence.getInfluences()));
        }
        shuffle();
    }

    private void shuffle(){
        Collections.shuffle(drawPile);
    }

    private void replenishIfNeeded(){
        if (drawPile.isEmpty() && !discardPile.isEmpty()){
            drawPile.addAll(discardPile);
            discardPile.clear();
            shuffle();
        }
    }

    public Influence draw(){
        replenishIfNeeded();
        if (drawPile.isEmpty()){
            throw new IllegalStateException("No influences left to draw.");
        }
        return drawPile.remove(drawPile.size() - 1);
    }

    public void discard(Influence influence){
        if (influence != null){
            discardPile.add(influence);
        }
    }

    public void returnToDeck(Influence influence){
        if (influence != null){
            drawPile.add(influence);
            shuffle();
        }
    }

    public void returnToDeck(Collection<Influence> influences){
        boolean added = false;
        for (Influence influence : influences){
            if (influence != null){
                drawPile.add(influence);
                added = true;
            }
        }
        if (added){
            shuffle();
        }
    }
}
