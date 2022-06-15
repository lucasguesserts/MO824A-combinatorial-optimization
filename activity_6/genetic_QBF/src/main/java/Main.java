import java.io.IOException;

public class Main {

    private static final int numberOfGenerations = 1000;
    private static final int populationSize = 100;
    private static final double mutationRate = 1.0 / 100.0;
    private static final String problemInstance = "../instances/qbf/qbf100";

    public static void main(final String[] args) throws IOException {
        final var solver = new ProblemInstanceSolver(
            numberOfGenerations,
            populationSize,
            mutationRate,
            problemInstance
        );
        solver.solve();
        solver.log();
    }

}
