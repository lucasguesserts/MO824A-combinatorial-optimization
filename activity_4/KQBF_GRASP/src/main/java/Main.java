import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import grasp.AbstractGRASP.ConstructionMechanism;
import grasp.GRASP_KQBF;
import solutions.Solution;

public class Main {

    private static String INSTANCES_DIR = "../instances/kqbf/";
    private static List<String> instaceList = Arrays.asList(
        "kqbf020",
        "kqbf040",
        "kqbf060",
        "kqbf080",
        "kqbf100",
        "kqbf200",
        "kqbf400"
    );
    private static List<Double> ALPHA_LIST = Arrays.asList(
        0.2,
        0.5
    );

    private static Integer iterations = 1000;

    public static void main(String[] args) throws IOException {
        for (final var instance: instaceList)
        for (final var alpha: ALPHA_LIST)
        for (final var constructionMechanism: ConstructionMechanism.values())
        for (final Boolean firstImproving: new Boolean[] {true, false}) {
            final var fileName = INSTANCES_DIR + instance;
            run_algorithm(alpha, firstImproving, constructionMechanism, fileName);
        }
    }

    private static void run_algorithm(
        final Double alpha,
        final Boolean firstImproving,
        final ConstructionMechanism constructionMechanism,
        final String fileName
    ) throws IOException {
        final long startTime = System.currentTimeMillis();
        System.out.println("\n\n=============================");
        final GRASP_KQBF grasp = new GRASP_KQBF(alpha, iterations, firstImproving, constructionMechanism, fileName);
        final Solution<Integer> bestSolution = grasp.solve();
        final Integer knapsackWeight = grasp.getKnapsackWeightOfSolution();
        final long endTime = System.currentTimeMillis();
        final long totalTime = endTime - startTime;
        System.out.println("-----------------");
        System.out.println("instance: " + fileName);
        System.out.println("construction mechanist: " + constructionMechanism);
        System.out.println("alpha: " + alpha);
        System.out.println("firstImproving: " + firstImproving);
        System.out.println("iterations: " + iterations);
        System.out.println("Best Solution Found: " + bestSolution);
        System.out.println("Knapsack Weight of Best Solution: " + knapsackWeight);
        System.out.println("RunningTime: " + (double) totalTime / (double) 1000 + " seconds");
    }

}
