package gunsmith;

import necesse.engine.util.InvalidLevelIdentifierException;
import necesse.engine.util.LevelIdentifier;

public class MyLevelIdentifiers extends LevelIdentifier {
    public static final LevelIdentifier AETHER_IDENTIFIER = new LevelIdentifier("aether");

    public MyLevelIdentifiers(String stringID) {
        super(stringID);
    }
    public boolean isAether() {
        return this.equals(AETHER_IDENTIFIER);
    }
}
