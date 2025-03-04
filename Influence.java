public class Influence {
     private String name;
     private boolean tax;
     private boolean assassinate;
     private boolean steal;
     private boolean exchange;
     private boolean blockAssassin;
     private boolean blockSteal;
     private boolean blockAid;
     private Influence(String name, boolean tax, boolean assassinate, boolean steal, boolean exchange, boolean blockAssassin, boolean blockSteal, boolean blockAid) {
         this.name = name;
         this.tax = tax;
         this.assassinate = assassinate;
         this.steal = steal;
         this.exchange = exchange;
         this.blockAssassin = blockAssassin;
         this.blockSteal = blockSteal;
         this.blockAid = blockAid;

     }
    public static final Influence[] INFLUENCES = {
        new Influence("Duke", true, false, false, false,false,false, true),
        new Influence("Assassin", false, true, false, false, false, false, false),
        new Influence("Captain", false, false, false, true, false, false, true),
        new Influence("Ambassador", false, false, false, true, false, false, true),
        new Influence("Contessa", false, false, false, false, true, false, false)

    };
    public String getName() {return this.name;}
    public boolean canTax() {return this.tax;}
    public boolean canAssassinate() {return this.assassinate;}
    public boolean canSteal() {return this.steal;}
    public boolean canExchange() {return this.exchange;}
    public boolean canBlockAssassin() {return this.blockAssassin;}
    public boolean canBlockSteal() {return this.blockSteal;}
    public boolean canBlockAid() {return this.blockAid;}

}
