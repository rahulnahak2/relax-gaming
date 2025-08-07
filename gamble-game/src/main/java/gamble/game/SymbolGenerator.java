package gamble.game;

import java.util.Random;

public class SymbolGenerator {
    private static final Symbol[] values = Symbol.values();
    private static final Random rand = new Random();

    public static Symbol randomSymbol() {
        return values[rand.nextInt(values.length)];
    }
}
