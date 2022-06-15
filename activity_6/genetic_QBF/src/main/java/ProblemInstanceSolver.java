import java.io.IOException;
import problems.qbf.solvers.GaQbf;
import solutions.Solution;

public class ProblemInstanceSolver {

    private final int numberOfGenerations;
    private final int populationSize;
    private final double mutationRate;
    private final String problemInstance;

    private Solution<Integer> bestSol;

    private long startTime = System.currentTimeMillis();
    private long endTime = System.currentTimeMillis();
    private long totalTime = System.currentTimeMillis();

    public ProblemInstanceSolver(
            final int numberOfGenerations,
            final int populationSize,
            final double mutationRate,
            final String problemInstance
    ) {
        this.numberOfGenerations = numberOfGenerations;
        this.populationSize = populationSize;
        this.mutationRate = mutationRate;
        this.problemInstance = problemInstance;
    }

    public void solve() throws IOException {
        this.startTime = System.currentTimeMillis();
        final var ga = new GaQbf(
            numberOfGenerations,
            populationSize,
            mutationRate,
            problemInstance);
        this.bestSol = ga.solve();
        this.endTime = System.currentTimeMillis();
        this.totalTime = this.endTime - this.startTime;
    }

    public void log() {
        System.out.println(String.format("maxVal = %s", this.bestSol.toString()));
        System.out.println(String.format(
            "Time = %f seconds",
            (double) this.totalTime / 1000.0
        ));
    }

}
