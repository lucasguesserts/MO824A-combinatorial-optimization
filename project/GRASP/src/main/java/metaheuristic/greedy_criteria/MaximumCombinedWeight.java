package metaheuristic.greedy_criteria;

import problem.Problem;
import solution.Solution;

public class MaximumCombinedWeight implements GreedyCriteria {

    public MaximumCombinedWeight() {}

    public Double evaluate(
        final Problem problem,
        final Solution solution,
        final Integer element
    ) {
        final var weightOfElement = problem.getWeightMap().get(element);
        final var combinedWeight = weightOfElement.add(solution.getWeight());
        return combinedWeight.getMax();
    }

}
