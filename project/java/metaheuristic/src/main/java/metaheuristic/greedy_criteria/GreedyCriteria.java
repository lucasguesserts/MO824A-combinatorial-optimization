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

    public Double evaluateRemoval(
        final Problem problem,
        final Solution solution,
        final Integer element
    ) {
        assertElementIsInSolution(solution, element);
        final var weightOfElement = problem.getWeightMap().get(element);
        assertElementWeightDoesNotExceedSolutionWeight(weightOfElement, solution.getWeight());
        final var weightWithElementRemoved = solution.getWeight().subtract(weightOfElement);
        return this.evaluate(weightWithElementRemoved);
    }

    public abstract Double evaluate(final Weight weight);

    private static void assertElementIsInSolution(final Solution solution, final Integer element) {
        assert solution.getElements().contains(element)
            : String.format(
                "Element %d is not in the elements of solution %s",
                element,
                solution.getElements()
            );
        return;
    }

    private static void assertElementWeightDoesNotExceedSolutionWeight(
        final Weight elementWeight,
        final Weight solutionWeight
    ) {
        assert !elementWeight.exceeds(solutionWeight)
            : String.format(
                "Element weight %s exceeds solution weight %s",
                elementWeight.toString(),
                solutionWeight.toString()
            );
        return;
    }

}
