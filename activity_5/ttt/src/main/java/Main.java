import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.stream.Collectors;

import org.json.JSONArray;
import org.json.JSONObject;

import inputReader.InputReaderKQBF;
import main.Parameters;
import main.Parameters.LocalSearchMethod;
import main.Parameters.Variation;
import problem.Problem;
import problem.ProblemKQBF;
import tabuSearch.TabuSearch;
import tabuSearch.TabuSearchBestImproving;
import tabuSearch.TabuSearchDiversificationByRestart;


public class Main {

    private long startTime;
    private long endTime;

    private TabuSearch<Integer, Integer> tabuSearch;
    private Double runningTime;
    private Problem<Integer, Integer> bestProblemSolution;

    protected Parameters parameters = new Parameters();

    private String instance;
    private LocalSearchMethod localSearch;
    private Double tenureRatio;
    private Variation methodVariation;
    private Integer numberOfIterations;
    private Integer targetValue;
    private final int numberOfRepetitions = 50;

    public static void main(String[] args) throws IOException {
        final var solver = new Main();
        solver.run();
    }

    public Main() throws IOException {
        this.makeInstanceList();
        this.makeLocalSearchList();
        this.makeTenureRatioList();
        this.makeMethodVariationList();
        this.makeNumberOfIterationsList();
        this.makeTargetValueList();
    }

    public void run() throws IOException {
        assert parameters.instanceList.size() == parameters.targetValueList.size()
            : "instanceList and TargetValueList do not have the same size";
        for (int instanceIndex = 0; instanceIndex < parameters.instanceList.size(); ++instanceIndex)
        for (final var localSearch: parameters.localSearchList)
        for (final var tenureRatio: parameters.tenureRatioList)
        for (final var methodVariation: parameters.methodVariationList)
        for (final var numberOfIterations: parameters.numberOfIterationsList) {
            final var runningTimeResults = new JSONArray();
            for (int runCount = 0; runCount < numberOfRepetitions; ++runCount) {
                this.instance = this.parameters.instanceList.get(instanceIndex);
                this.localSearch = localSearch;
                this.tenureRatio = tenureRatio;
                this.methodVariation = methodVariation;
                this.numberOfIterations = numberOfIterations;
                this.targetValue = this.parameters.targetValueList.get(instanceIndex);
                System.out.println("\n\n========== START ==========");
                start();
                solve();
                finish(runCount);
                runningTimeResults.put(this.runningTime / 1000);
                System.out.println(runningTimeResults.toString());
                System.out.println("========== END ==========\n\n");
            }
            saveResults(runningTimeResults);
        }
    }

    private void start() throws IOException {
        final var input = new InputReaderKQBF(instance);
        final var emptySolution = new ProblemKQBF(input);
        if (localSearch.equals(Parameters.LocalSearchMethod.BEST_IMPROVING) && methodVariation.equals(Parameters.Variation.NONE)) {
            tabuSearch = new TabuSearchBestImproving(emptySolution, tenureRatio, numberOfIterations, targetValue);
        } else if (localSearch.equals(Parameters.LocalSearchMethod.BEST_IMPROVING) && methodVariation.equals(Parameters.Variation.INTENSIFICATION_BY_RESTART_AND_DIVERSIFICATION_BY_RESTART)) {
            tabuSearch = new TabuSearchDiversificationByRestart(emptySolution, tenureRatio, numberOfIterations, targetValue);
        } else {
            throw new RuntimeException(String.format("invalid local search method inserted: %s", localSearch.toString()));
        }
    }

    private void solve() {
        startTime = System.currentTimeMillis();
        tabuSearch.solve();
        bestProblemSolution = tabuSearch.getBestSolution();
        endTime = System.currentTimeMillis();
        this.runningTime = (double) (endTime - startTime);
    }

    private void finish(final Integer runCount) {
        System.out.println(String.format("instance = %s", this.instance.toString()));
        System.out.println(String.format("localSearch = %s", this.localSearch.toString()));
        System.out.println(String.format("tenureRatio = %s", this.tenureRatio.toString()));
        System.out.println(String.format("methodVariation = %s", this.methodVariation.toString()));
        System.out.println(String.format("numberOfIterations = %s", this.numberOfIterations.toString()));
        System.out.println(String.format("runningTime = %f seconds", this.runningTime / 1000));
        System.out.println(String.format("bestProblemSolution = %s", this.bestProblemSolution.toString()));
        System.out.println(String.format("numberOfRepetitions: %d", this.numberOfRepetitions));
        System.out.println(String.format("targetValue: %d", this.targetValue));
        System.out.println(String.format("runCount: %d", runCount));
        System.out.flush();
        System.gc();
    }

    private void saveResults(final JSONArray runningTimeResults) {
        final JSONObject setup = this.makeSetupAsJsonObject();
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

    private JSONObject makeSetupAsJsonObject() {
        final var obj = new JSONObject();
        obj.put("instance", this.instance.toString());
        obj.put("localSearch", this.localSearch.toString());
        obj.put("tenureRatio", this.tenureRatio.toString());
        obj.put("methodVariation", this.methodVariation.toString());
        obj.put("numberOfIterations", this.numberOfIterations.toString());
        obj.put("runningTime", this.runningTime / 1000);
        obj.put("bestProblemSolution", this.bestProblemSolution.toString());
        obj.put("numberOfRepetitions", this.numberOfRepetitions);
        obj.put("targetValue", this.targetValue);
        return obj;
    }

    private void makeInstanceList() {
        parameters.instanceList = Arrays.asList(
            40,
            60,
            80
        ).stream().map(n -> makeInstanceName(n)).collect(Collectors.toList());
    }

    private void makeTargetValueList() {
        parameters.targetValueList = Arrays.asList(
            -275,
            -446,
            -729
        );
    }

    private void makeLocalSearchList() {
        this.parameters.localSearchList = Arrays.asList(
            Parameters.LocalSearchMethod.BEST_IMPROVING
        );
    }

    private void makeTenureRatioList() {
        this.parameters.tenureRatioList.add(0.4);
    }

    private void makeMethodVariationList() {
        this.parameters.methodVariationList = Arrays.asList(
            Parameters.Variation.NONE,
            Parameters.Variation.INTENSIFICATION_BY_RESTART_AND_DIVERSIFICATION_BY_RESTART
        );
    }

    private void makeNumberOfIterationsList() {
        this.parameters.numberOfIterationsList.add(1000);
    }

    private String makeInstanceName(final Integer instanceNumber) {
        final String DIR_PATH = "../instances";
        final String PROBLEM_PREFIX = "kqbf";
        return String.format(
            "%s/%s/%s%03d",
            DIR_PATH,
            PROBLEM_PREFIX,
            PROBLEM_PREFIX,
            instanceNumber
        );
    }

}
