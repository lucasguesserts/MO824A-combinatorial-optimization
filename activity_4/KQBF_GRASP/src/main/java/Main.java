import java.io.IOException;

import grasp.GRASP_KQBF;
import solutions.Solution;

public class Main {

    private static Double alpha = 0.5;
    private static Integer iterations = 1000;
    private static Boolean firstImproving = false;
    private static String fileName = "";

    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            System.out.println("usage: gradle run --args=\"INSTANCE\"");
            System.out.println("INSTANCE: instances/qbf/*, instances/kqbf/*");
            return;
        }

        final long startTime = System.currentTimeMillis();
        fileName = args[0];
        final GRASP_KQBF grasp = new GRASP_KQBF(alpha, iterations, firstImproving, fileName);
        final Solution<Integer> bestSolution = grasp.solve();
        final Integer knapsackWeight = grasp.getKnapsackWeightOfSolution();
        final long endTime = System.currentTimeMillis();
        final long totalTime = endTime - startTime;
        System.out.println("");
        System.out.println("Best Solution Found: " + bestSolution);
        System.out.println("Knapsack Weight of Best Solution: " + knapsackWeight);
        System.out.println("RunningTime: " + (double) totalTime / (double) 1000 + " seconds");
    }

}
