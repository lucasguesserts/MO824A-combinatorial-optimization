package metaheuristic.greedy_criteria;

import solution.Weight;

public class GreedyCriteriaMax extends GreedyCriteria {

    public GreedyCriteriaMax() {}

    public Double evaluate(final Weight weight) {
        return weight.getMax();
    }

}
