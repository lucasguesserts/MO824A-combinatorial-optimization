package main;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import metaheuristic.Metaheuristic;
import metaheuristic.greedy_criteria.GreedyCriteria;
import problem.InvalidInputException;
import problem.Problem;
import solution.Solution;

public abstract class MetaheuristicSolver {

    protected final String INSTANCES_DIR = "../../instances/medium/";

    protected final JSONArray logs = new JSONArray();

    protected Problem problem;
    protected GreedyCriteria greedyCriteria;
    protected Double greedyParameter;

    protected Metaheuristic grasp;

    protected Solution solution;

    protected Long startTimeMilliseconds;
    protected Long endTimeMilliseconds;
    protected Double totalTimeSeconds;

    protected abstract void initializeSolver(final String instanceName) throws Exception;

    protected abstract void solve();

    protected abstract String getInstancesDir();

    protected abstract String getLogFileName();

    public void run() throws JSONException, IOException, InvalidInputException {
        final var listOfInstances = listFiles(getInstancesDir());
        for (final var instanceName : listOfInstances) {
            try{
                initializeSolver(instanceName);
                startTimer();
                solve();
                stopTimer();
                log();
            } catch (final Exception exception) {
                System.out.println("Error processing " + problem.getName());
                System.out.println(exception);
            }
        }
        writeLogFile();
    }

    protected void startTimer() {
        startTimeMilliseconds = System.currentTimeMillis();
    }

    protected void stopTimer() {
        endTimeMilliseconds = System.currentTimeMillis();
        totalTimeSeconds = ((double) (endTimeMilliseconds - startTimeMilliseconds)) / 1000;
    }

    protected void log() {
        final var obj = new JSONObject();
        obj.put("problem", problem.getName());
        obj.put("cost", solution.getCost());
        obj.put("runningTime", totalTimeSeconds);
        logs.put(obj);
    }

    protected void writeLogFile() {
        final String logFileName = getLogFileName();
        try {
            final FileWriter file = new FileWriter(logFileName);
            logs.write(file, 4, 4);
            file.close();
        } catch (final IOException exception) {
            System.out.println("Could not write file " + logFileName);
            exception.printStackTrace();
        }
    }

    protected List<String> listFiles(final String dir) {
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
