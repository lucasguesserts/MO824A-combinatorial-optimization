import java.io.IOException;

import org.json.JSONException;

import main.MetaheuristicSolver;
import metaheuristic.Greedy;
import metaheuristic.greedy_criteria.GreedyCriteriaMax;
import problem.InvalidInputException;
import problem.ProblemData;

public class Main {

    public static void main(final String[] args) throws Exception {
        final var solver = new GreedyRunner();
        solver.run();
    }

}

final class GreedyRunner extends MetaheuristicSolver {

    protected void initializeSolver(final String instanceName) throws JSONException, IOException, InvalidInputException {
        problem = new ProblemData(instanceName);
        greedyCriteria = new GreedyCriteriaMax();
        grasp = new Greedy(problem, greedyCriteria);
    }

    protected void solve() {
        solution = grasp.solve();
        return;
    }

    protected String getInstancesDir() {
        return "../../instances/medium/";
    }

    protected String getLogFileName() {
        return "../../greedy_solve.json";
    }


}
