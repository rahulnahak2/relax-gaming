package gamble.game;

import java.util.*;

public class Simulator {
    Symbol[][] grid = new Symbol[8][8];

    /**
     * generate initial grid for the game
     */
    public void generateGrid() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                grid[i][j] = SymbolGenerator.randomSymbol();
            }
        }
    }

    /**
     * find the cluster for each symbol present in grid
     * @return the list of symbols with their location in the grid
     */
    public List<Set<Position>> findClusters() {
        boolean[][] visited = new boolean[8][8];
        List<Set<Position>> clusters = new ArrayList<>();

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (!visited[i][j] && grid[i][j] != null && grid[i][j].isPayable()) {
                    Set<Position> cluster = matchSymbolInCluster(i, j, visited);
                    if (cluster.size() >= 5) clusters.add(cluster);
                }
            }
        }
        return clusters;
    }

    private Set<Position> matchSymbolInCluster(int i, int j, boolean[][] visited) {
        Symbol base = grid[i][j];
        Queue<Position> queue = new LinkedList<>();
        Set<Position> result = new HashSet<>();

        queue.add(new Position(i, j));
        visited[i][j] = true;

        while (!queue.isEmpty()) {
            Position p = queue.poll();
            result.add(p);

            for (int[] direction : new int[][]{{-1, 0}, {1, 0}, {0, -1}, {0, 1}}) {
                int directedRow = p.row + direction[0], directedCol = p.col + direction[1];
                if (directedRow >= 0 && directedRow < 8 && directedCol >= 0 && directedCol < 8 && !visited[directedRow][directedCol]) {
                    Symbol s = grid[directedRow][directedCol];
                    if (s == base || s == Symbol.WR) {
                        visited[directedRow][directedCol] = true;
                        queue.add(new Position(directedRow, directedCol));
                    }
                }
            }
        }
        return result;
    }

    public int destroyAndGetWin(Set<Position> positions, int bet) {
        Map<Symbol, Integer> counts = new HashMap<>();
        for (Position position : positions) {
            Symbol symbol = grid[position.row][position.col];
            if (symbol != Symbol.WR) counts.put(symbol, counts.getOrDefault(symbol, 0) + 1);
            grid[position.row][position.col] = null;
        }
        int totalWin = 0;
        for (Map.Entry<Symbol, Integer> entry : counts.entrySet()) {
            int win = PayoutTable.getPayout(entry.getKey(), entry.getValue());
            totalWin += win * (bet / 10);
        }
        destroyBlockers(positions);
        return totalWin;
    }

    public void destroyBlockers(Set<Position> winPositions) {
        for (Position position : winPositions) {
            for (int[] d : new int[][]{{-1, 0}, {1, 0}, {0, -1}, {0, 1}}) {
                int ni = position.row + d[0], nj = position.col + d[1];
                if (ni >= 0 && ni < 8 && nj >= 0 && nj < 8 && grid[ni][nj] == Symbol.BLOCKER) {
                    grid[ni][nj] = null;
                }
            }
        }
    }

    /**
     * perform avalanche in the grid
     */
    public void performAvalanche() {
        for (int col = 0; col < 8; col++) {
            int empty = 7;
            for (int row = 7; row >= 0; row--) {
                if (grid[row][col] != null) {
                    grid[empty][col] = grid[row][col];
                    if (empty != row) grid[row][col] = null;
                    empty--;
                }
            }
            for (int row = empty; row >= 0; row--) {
                grid[row][col] = SymbolGenerator.randomSymbol();
            }
        }
    }
}
