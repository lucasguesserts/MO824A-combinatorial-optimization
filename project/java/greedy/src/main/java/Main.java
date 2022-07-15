import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import localsearch.LocalOptimal;
import metaheuristic.ConstructiveGrasp;
import metaheuristic.greedy_criteria.GreedyCriteria;
import metaheuristic.greedy_criteria.GreedyCriteriaMax;
import problem.InvalidInputException;
import problem.Problem;
import problem.ProblemData;
import solution.Solution;

public class Main {

    private static final String INSTANCES_DIR = "../../instances/medium/";

    private static final JSONArray logs = new JSONArray();

    private static Problem problem;
    private static GreedyCriteria greedyCriteria;
    private static Double greedyParameter;

    private static ConstructiveGrasp grasp;
    private static LocalOptimal localOptimalRunner;

    private static Solution solution;

    private static Long startTimeMilliseconds;
    private static Long endTimeMilliseconds;
    private static Double totalTimeSeconds;

    public static void main(final String[] args) throws JSONException, IOException, InvalidInputException {
        final var listOfInstances = listFiles(INSTANCES_DIR);
        for (final var instanceName : listOfInstances) {
            try{
                initializeSolver(instanceName);
                solve();
                log();
            } catch (final Exception exception) {
            }
        }
        writeLogFile();
    }

    private static void initializeSolver(final String instanceName) throws JSONException, IOException, InvalidInputException {
        problem = new ProblemData(instanceName);
        greedyCriteria = new GreedyCriteriaMax();
        greedyParameter = 0.0;
        grasp = new ConstructiveGrasp(problem, greedyCriteria, greedyParameter);
        localOptimalRunner = new LocalOptimal(problem, greedyCriteria);
    }

    private static void solve() throws JSONException, IOException, InvalidInputException {
        startTimeMilliseconds = System.currentTimeMillis();
        solution = grasp.constructiveHeuristic();
        solution = localOptimalRunner.search(solution);
        endTimeMilliseconds = System.currentTimeMillis();
        totalTimeSeconds = ((double) (endTimeMilliseconds - startTimeMilliseconds)) / 1000;
        return;
    }

    private static void log() {
        final var obj = new JSONObject();
        obj.put("problem", problem.getName());
        obj.put("cost", solution.getCost());
        obj.put("runningTime", totalTimeSeconds);
        logs.put(obj);
    }

    private static void writeLogFile() {
        final String logFileName = "../../greedy_solve.json";
        try {
            final FileWriter file = new FileWriter(logFileName);
            logs.write(file, 4, 4);
            file.close();
        } catch (final IOException exception) {
            System.out.println("Could not write file " + logFileName);
            exception.printStackTrace();
        }
    }

    private static List<String> listFiles(final String dir) {
        final File directoryPath = new File(dir);
        final File filesList[] = directoryPath.listFiles();
        final List<String> listOfFiles = Arrays.asList(filesList)
            .stream()
            .map(file -> file.getAbsolutePath())
            .filter(fileName -> fileName.contains(".json"))
            .toList();
        return listOfFiles;
    }

}
