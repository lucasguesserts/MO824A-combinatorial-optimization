import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;

import org.json.JSONObject;
import org.json.JSONArray;

import grasp.AbstractGRASP.ConstructionMechanism;
import grasp.GRASP_KQBF;
import solutions.Solution;

public class Main {

    private static String INSTANCES_DIR = "../instances/kqbf/";
    private static final List<String> INSTANCE_LIST = Arrays.asList(
        "kqbf040",
        "kqbf060",
        "kqbf080"
    );
    private static final List<Integer> TARGET_VALUE_LIST = Arrays.asList(
        -275,
        -446,
        -729
    );
    private static final List<Double> ALPHA_LIST = Arrays.asList(
        0.2
    );
    private static final List<Boolean> FIRST_IMPROVIND_LIST = Arrays.asList(
        Boolean.TRUE,
        Boolean.FALSE
    );
    private static final List<ConstructionMechanism> CONSTRUCTION_MECHANISM_LIST = Arrays.asList(
        ConstructionMechanism.DEFAULT
    );

    private static final Integer NUMBER_OF_REPETITIONS = 50;

    private static final Integer NUMBER_OF_ITERATIONS = 1000;

    public static void main(String[] args) throws IOException {
        for (int instanceIndex = 0; instanceIndex < INSTANCE_LIST.size(); ++instanceIndex)
        for (final var alpha: ALPHA_LIST)
        for (final var constructionMechanism: CONSTRUCTION_MECHANISM_LIST)
        for (final Boolean firstImproving: FIRST_IMPROVIND_LIST) {
            final var instance = INSTANCE_LIST.get(instanceIndex);
            final var targetValue = TARGET_VALUE_LIST.get(instanceIndex);
            final var fileName = INSTANCES_DIR + instance;
            final var setup = getSetup(alpha, firstImproving, targetValue, constructionMechanism, fileName);
            final var runningTimeResults = new JSONArray();
            for (int runCount = 0; runCount < NUMBER_OF_REPETITIONS; ++runCount) {
                final var runningTime = run_algorithm(alpha, firstImproving, targetValue, constructionMechanism, fileName);
                runningTimeResults.put(runningTime);
            }
            saveResults(setup, runningTimeResults);
        }
    }

    private static long run_algorithm(
        final Double alpha,
        final Boolean firstImproving,
        final Integer targetValue,
        final ConstructionMechanism constructionMechanism,
        final String fileName
    ) throws IOException {
        final long startTime = System.currentTimeMillis();
        System.out.println("\n\n=============================");
        final GRASP_KQBF grasp = new GRASP_KQBF(alpha, NUMBER_OF_ITERATIONS, firstImproving, constructionMechanism, targetValue, fileName);
        final Solution<Integer> bestSolution = grasp.solve();
        final long endTime = System.currentTimeMillis();
        final long runningTime = endTime - startTime;
        final Integer knapsackWeight = grasp.getKnapsackWeightOfSolution();
        System.out.println("-----------------");
        System.out.println("instance: " + fileName);
        System.out.println("construction mechanist: " + constructionMechanism);
        System.out.println("alpha: " + alpha);
        System.out.println("firstImproving: " + firstImproving);
        System.out.println("iterations: " + NUMBER_OF_ITERATIONS);
        System.out.println("targetValue: " + targetValue);
        System.out.println("Best Solution Found: " + bestSolution);
        System.out.println("Knapsack Weight of Best Solution: " + knapsackWeight);
        System.out.println("RunningTime: " + (double) runningTime / (double) 1000 + " seconds");
        return runningTime;
    }

    private static JSONObject getSetup(
        final Double alpha,
        final Boolean firstImproving,
        final Integer targetValue,
        final ConstructionMechanism constructionMechanism,
        final String fileName
    ) {
        final var obj = new JSONObject();
        obj.put("instance", fileName);
        obj.put("construction mechanist", constructionMechanism);
        obj.put("alpha", alpha);
        obj.put("firstImproving", firstImproving);
        obj.put("targetValue", targetValue);
        obj.put("iterations", NUMBER_OF_ITERATIONS);
        return obj;
    }

    private static void saveResults(
        final JSONObject setup,
        final JSONArray runningTimeResults
    ) {
        final var obj = new JSONObject();
        obj.put("setup", setup);
        obj.put("time", runningTimeResults);
        try {
            final var timestamp = new Timestamp(System.currentTimeMillis());
            final var filePath = String.format(
                "../experiments/%s.json",
                timestamp.toString()
            ).replace(" ", "T");
            final var file = new FileWriter(filePath);
            file.write(obj.toString(2));
            file.close();
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
        }
    }



}
