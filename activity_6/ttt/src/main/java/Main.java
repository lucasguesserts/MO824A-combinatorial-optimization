import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

public class Main {

    private static final JSONArray runningTimeResults = new JSONArray();
    private static final JSONObject experimentResults = new JSONObject();

    private static final int numberOfRepetitions = 50;
    private static final int numberOfGenerations = 1000;
    private static final int populationSize = 100;
    private static final double mutationRate = 1.0 / 100.0;
    private static final List<String> problemInstanceList = Arrays.asList(
        "../instances/kqbf/kqbf020"
    );
    private static final List<Integer> targetValueList = Arrays.asList(
        120
    );

    public static void main(final String[] args) throws IOException {
        for (int instanceIndex = 0; instanceIndex < problemInstanceList.size(); ++instanceIndex) {
            final var problemInstance = problemInstanceList.get(instanceIndex);
            final int targetValue = targetValueList.get(instanceIndex);
            for (int runCount = 0; runCount < numberOfRepetitions; ++runCount) {
                final var solver = new Solver(
                    numberOfGenerations,
                    populationSize,
                    mutationRate,
                    problemInstance,
                    targetValue
                );
                solver.logStart();
                solver.solve();
                solver.log();
                solver.logEnd();
                collectExperimentResults(solver);
            }
        }
        saveExperimentResults();
    }

    private static void collectExperimentResults(final Solver solver) {
        final var setupAsJson = solver.getSetup();
        setupAsJson.put("numberOfRepetitions", numberOfRepetitions);
        experimentResults.put("setup", setupAsJson);
        runningTimeResults.put(solver.totalTime);
    }

    private static void saveExperimentResults() {
        try {
            final var timestamp = new Timestamp(System.currentTimeMillis());
            final var filePath = String.format(
                "../experiments/%s.json",
                timestamp.toString()
            ).replace(" ", "T");
            final var file = new FileWriter(filePath);
            experimentResults.put("time", runningTimeResults);
            file.write(experimentResults.toString(2));
            file.close();
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
        }
    }

}
