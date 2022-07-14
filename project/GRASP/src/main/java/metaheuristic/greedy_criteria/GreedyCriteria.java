package metaheuristic.greedy_criteria;

import problem.Problem;
import solution.Solution;

public interface GreedyCriteria {

    Double evaluate(
        final Problem problem,
        final Solution solution,
        final Integer element
    );

}
