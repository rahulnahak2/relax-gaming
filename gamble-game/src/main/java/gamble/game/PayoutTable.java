package gamble.game;

import java.util.Map;

public class PayoutTable {
    private static final Map<Symbol, int[]> payouts = Map.of(
            Symbol.H1, new int[]{5, 6, 7, 8, 10},
            Symbol.H2, new int[]{4, 5, 6, 7, 9},
            Symbol.H3, new int[]{4, 5, 6, 7, 9},
            Symbol.H4, new int[]{3, 4, 5, 6, 7},
            Symbol.L5, new int[]{1, 2, 3, 4, 5},
            Symbol.L6, new int[]{1, 2, 3, 4, 5},
            Symbol.L7, new int[]{1, 2, 3, 4, 5},
            Symbol.L8, new int[]{1, 2, 3, 4, 5}
    );

    public static int getPayout(Symbol symbol, int size) {
        if (!symbol.isPayable()) return 0;
        if (!payouts.containsKey(symbol)) return 0;
        if (size >= 21) return payouts.get(symbol)[4];
        else if (size >= 17) return payouts.get(symbol)[3];
        else if (size >= 13) return payouts.get(symbol)[2];
        else if (size >= 9) return payouts.get(symbol)[1];
        else if (size >= 5) return payouts.get(symbol)[0];
        return 0;
    }
}
