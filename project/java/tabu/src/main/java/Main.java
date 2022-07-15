import java.io.IOException;

import org.json.JSONException;

import main.MetaheuristicSolver;
import metaheuristic.Tabu;
import metaheuristic.greedy_criteria.GreedyCriteriaMax;
import problem.InvalidInputException;
import problem.ProblemData;

public class Main {

    public static void main(final String[] args) throws Exception {
        final var solver = new TabuRunner();
        solver.run();
    }

}

final class TabuRunner extends MetaheuristicSolver {

    private Double tenureRatio;
    private Double capacityExpansionRatio;
    private Integer maximumNumberOfIterationsWithoutImprovement;

    protected void initializeSolver(final String instanceName) throws JSONException, IOException, InvalidInputException {
        problem = new ProblemData(instanceName);
        greedyCriteria = new GreedyCriteriaMax();
        greedyParameter = 0.2;
        tenureRatio = 0.4;
        capacityExpansionRatio = 0.2;
        maximumNumberOfIterationsWithoutImprovement = 10;
        metaheuristic = new Tabu(
            problem,
            greedyCriteria, greedyParameter,
            tenureRatio,
            capacityExpansionRatio,
            maximumNumberOfIterationsWithoutImprovement
        );
    }

    protected void solve() {
        solution = metaheuristic.solve();
        return;
    }

    protected String getInstancesDir() {
        return "../../instances/medium/";
    }

    protected String getLogFileName() {
        return "../../tabu_solve.json";
    }

}
