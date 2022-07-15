import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.json.JSONException;

import metaheuristic.Tabu;
import metaheuristic.greedy_criteria.GreedyCriteriaMax;
import problem.InvalidInputException;
import problem.ProblemData;
import solution.Solution;

public class Main {

    static final String INSTANCES_DIR = "../../instances/";

    public static void main(final String[] args) throws JSONException, IOException, InvalidInputException {
        final var listOfInstances = listFiles(INSTANCES_DIR);
        for (final var instanceName : listOfInstances) {
            System.out.println("\n\n========== START ==========\n");
            System.out.println("Instance " + instanceName);
            try{
                final var solution = solve(instanceName);
                System.out.println("Solution cost: " + solution.getCost());
            } catch (final Exception exception) {
                System.out.println("\nerror processing instance");
                System.out.println(exception);
            }
            System.out.println("\n\n========== END ==========");
        }
    }

    private static Solution solve(final String instanceName) throws JSONException, IOException, InvalidInputException {
        final var problem = new ProblemData(instanceName);
        final var greedyCriteria = new GreedyCriteriaMax();
        final Double greedyParameter = 0.2;
        final Integer maximumNumberOfIterationsWithoutImprovement = 4;
        final Double tenureRatio = 0.4;
        final Double capacityExpansionRatio = 0.2;
        final var tabu = new Tabu(
            problem,
            greedyCriteria,
            greedyParameter,
            tenureRatio,
            capacityExpansionRatio,
            maximumNumberOfIterationsWithoutImprovement
        );
        final var solution = tabu.solve();
        return solution;
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
