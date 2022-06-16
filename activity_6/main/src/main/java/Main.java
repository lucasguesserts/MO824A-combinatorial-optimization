import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;
import org.json.JSONArray;

public class Main {

    private static final JSONArray experimentResults = new JSONArray();

    private static final int numberOfGenerations = 1000;
    private static final int populationSize = 100;
    private static final double mutationRate = 1.0 / 100.0;
    private static final List<String> problemInstanceList = Arrays.asList(
        "../instances/kqbf/kqbf020",
        "../instances/kqbf/kqbf040",
        "../instances/kqbf/kqbf060",
        "../instances/kqbf/kqbf080",
        "../instances/kqbf/kqbf100",
        "../instances/kqbf/kqbf200",
        "../instances/kqbf/kqbf400"
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
            experimentResults.put(solver.getLogAsJson());
        }
        saveExperimentResults();
    }

    private static void saveExperimentResults() {
        try {
            final var timestamp = new Timestamp(System.currentTimeMillis());
            final var filePath = String.format(
                "../experiments/%s.json",
                timestamp.toString()
            ).replace(" ", "T");
            final var file = new FileWriter(filePath);
            file.write(experimentResults.toString(2));
            file.close();
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
        }
    }

}
