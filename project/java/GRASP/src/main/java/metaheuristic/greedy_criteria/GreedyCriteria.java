package metaheuristic.greedy_criteria;

import problem.Problem;
import solution.Solution;
import solution.Weight;

public abstract class GreedyCriteria {

    public Double evaluateCombined(
        final Problem problem,
        final Solution solution,
        final Integer element
    ) {
        final var weightOfElement = problem.getWeightMap().get(element);
        final var combinedWeight = weightOfElement.add(solution.getWeight());
        return this.evaluate(combinedWeight);
    }

    public abstract Double evaluate(final Weight weight);

}
