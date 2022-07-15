import java.io.IOException;

import org.json.JSONException;

import metaheuristic.ConstructiveGrasp;
import metaheuristic.greedy_criteria.GreedyCriteriaMax;
import problem.InvalidInputException;
import problem.ProblemData;

public class Main {

    static final String INSTANCES_DIR = "../instances/small_examples/";
    public static void main(final String[] args) throws JSONException, IOException, InvalidInputException {
        final var problem = new ProblemData(INSTANCES_DIR + "instance_1.json");
        final var greedyCriteria = new GreedyCriteriaMax();
        final Double greedyParameter = 0.2;
        final var grasp = new ConstructiveGrasp(problem, greedyCriteria, greedyParameter);
        final var solution = grasp.constructiveHeuristic();
        System.out.println("\n\nSolution:");
        System.out.println(solution);
    }
}
