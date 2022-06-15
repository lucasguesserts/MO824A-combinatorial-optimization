import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class Main {

    private static final int numberOfGenerations = 1000;
    private static final int populationSize = 100;
    private static final double mutationRate = 1.0 / 100.0;
    private static final List<String> problemInstanceList = Arrays.asList(
        "../instances/kqbf/kqbf020"
    );

    public static void main(final String[] args) throws IOException {
        for (final var problemInstance : problemInstanceList) {
            final var solver = new ProblemInstanceSolver(
                numberOfGenerations,
                populationSize,
                mutationRate,
                problemInstance
            );
            solver.logStart();
            solver.solve();
            solver.log();
            solver.logEnd();
        }
    }

}
