import java.io.IOException;

import org.json.JSONException;

import metaheuristic.ConstructiveGrasp;
import metaheuristic.greedy_criteria.MaximumCombinedWeight;
import problem.InvalidInputException;
import problem.ProblemData;

public class Main {
    public static void main(final String[] args) throws JSONException, IOException, InvalidInputException {
        final var problem = new ProblemData("../instances/instance_1.json");
        final var greedyCriteria = new MaximumCombinedWeight();
        final Double greedyParameter = 0.2;
        final var grasp = new ConstructiveGrasp(problem, greedyCriteria, greedyParameter);
        final var solution = grasp.constructiveHeuristic();
        System.out.println("\n\nSolution:");
        System.out.println(solution);
    }
}
