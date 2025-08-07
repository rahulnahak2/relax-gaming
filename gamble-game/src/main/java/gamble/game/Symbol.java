package gamble.game;

/**
 * define types of symbols
 */
public enum Symbol {
    H1, H2, H3, H4, L5, L6, L7, L8, WR, BLOCKER;

    public boolean isPayable() {
        return this != WR && this != BLOCKER;
    }
}
