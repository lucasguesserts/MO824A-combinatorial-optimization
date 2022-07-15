import java.io.IOException;

import org.json.JSONException;

import main.MetaheuristicSolver;
import metaheuristic.Grasp;
import metaheuristic.greedy_criteria.GreedyCriteriaMax;
import problem.InvalidInputException;
import problem.ProblemData;

public class Main {

    public static void main(final String[] args) throws Exception {
        final var solver = new GraspRunner();
        solver.run();
    }

}

final class GraspRunner extends MetaheuristicSolver {

    protected void initializeSolver(final String instanceName) throws JSONException, IOException, InvalidInputException {
        problem = new ProblemData(instanceName);
        greedyCriteria = new GreedyCriteriaMax();
        greedyParameter = 0.2;
        grasp = new Grasp(
            problem,
            greedyCriteria,
            greedyParameter
        );
    }

    protected void solve() {
        solution = grasp.solve();
        return;
    }

    protected String getInstancesDir() {
        return "../../instances/medium/";
    }

    protected String getLogFileName() {
        return "../../grasp_solve.json";
    }

}
