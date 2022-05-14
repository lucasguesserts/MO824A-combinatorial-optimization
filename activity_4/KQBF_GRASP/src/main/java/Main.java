import java.io.IOException;

import grasp.GRASP_KQBF;
import grasp.AbstractGRASP.ConstructionMechanism;
import solutions.Solution;

public class Main {

    private static String INSTANCES_DIR = "../instances/kqbf/";

    private static Double alpha = 0.5;
    private static Integer iterations = 1000;
    private static Boolean firstImproving = false;
    private static ConstructionMechanism constructionMechanism = ConstructionMechanism.DEFAULT;

    public static void main(String[] args) throws IOException {
        final String instance = INSTANCES_DIR + "kqbf020";
        final long startTime = System.currentTimeMillis();
        final GRASP_KQBF grasp = new GRASP_KQBF(alpha, iterations, firstImproving, constructionMechanism, instance);
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
