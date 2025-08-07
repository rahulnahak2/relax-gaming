package gamble.game;

import java.util.*;

public class GridGame {
    public static void main(String[] args) {
        System.out.println("Please enter the number of rounds : ");
        Scanner sc = new Scanner(System.in);
        int rounds=sc.nextInt();
        sc.nextLine();

        int totalBet = 0, totalWin = 0;
        for (int i = 0; i < rounds; i++) {
            int betPerRound = 10;
            int winPerRound = playRound(betPerRound);
            totalBet += betPerRound;
            totalWin += winPerRound;
        }
        System.out.println("Total round played: " + rounds);
        System.out.println("Total Bet: " + totalBet + ", Total Win: " + totalWin);
        System.out.println("Do you want to take a chance to double the win? ");
        String isGamble=sc.nextLine();
        if(isGamble.equalsIgnoreCase("yes")){
              totalWin=gamble(totalWin);
        }
        System.out.printf("Return to Player: %.2f%%\n", (totalWin * 100.0) / totalBet);
    }

    public static int playRound(int bet) {
        Simulator simulator = new Simulator();
        simulator.generateGrid();

        int totalWin = 0;
        while (true) {
            List<Set<Position>> clusters = simulator.findClusters();
            if (clusters.isEmpty()) break;
            for (Set<Position> cluster : clusters) {
                totalWin += simulator.destroyAndGetWin(cluster, bet);
            }
            simulator.performAvalanche();
        }
        return totalWin;
    }

    public static int gamble(int currentWin) {
        return new Random().nextBoolean() ? currentWin * 2 : 0;
    }
}