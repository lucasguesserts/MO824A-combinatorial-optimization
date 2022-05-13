import java.io.IOException;

import problems.KQBF;
import solutions.Solution;

public class Main {

    private static KQBF kqbf;
    private final static Integer numberOfIterations = 10000000;
    private static Solution<Integer> bestSolution = new Solution<Integer>();
    private static Solution<Integer> currentSolution = new Solution<Integer>();

    public static void main(String[] args) throws IOException {
        // check args
        if (args.length != 1) {
            System.out.println("usage: gradle run --args=\"INSTANCE\"");
            System.out.println("INSTANCE: instances/qbf/*, instances/kqbf/*");
            return;
        }

        long startTime = System.currentTimeMillis();
        initKQBF(args[0]);
        randomizedBruteForce();
        Integer knapsackWeight = kqbf.evaluateKnapsackWeight(bestSolution);
        long endTime = System.currentTimeMillis();
        long totalTime = endTime - startTime;
        System.out.println("BestSolution: " + bestSolution);
        System.out.println("KnapsackWeight: " + knapsackWeight);
        System.out.println("RunningTime = " + (double) totalTime / (double) 1000 + " seconds");
    }

    private static void initKQBF(final String fileName)
        throws IOException {
        kqbf = new KQBF(fileName);
    }

    private static void randomizedBruteForce()
        throws IOException {
        bestSolution.cost = kqbf.evaluate(bestSolution);
        for (int i = 0; i < numberOfIterations; ++i) {
            createRandomizedSolution();
            updateBestSolution();
        }
    }

    private static void createRandomizedSolution() {
        currentSolution = new Solution<Integer>();
        for (int i = 0; i < kqbf.size; i++) {
            if (Math.random() < 0.5)
                currentSolution.add(i);
        }
        currentSolution.cost = kqbf.evaluate(currentSolution);
    }

    private static void updateBestSolution() {
        final Integer knapsackWeight = kqbf.evaluateKnapsackWeight(currentSolution);
        if (bestSolution.cost < currentSolution.cost && knapsackWeight < kqbf.maximumWeight)
            bestSolution = currentSolution;
    }

}
