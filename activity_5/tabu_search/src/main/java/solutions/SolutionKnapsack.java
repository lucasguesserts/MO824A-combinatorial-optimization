package solutions;

import java.util.Collections;
import java.util.List;

public class SolutionKnapsack extends SolutionInteger {

    public static Integer NULL_CANDIDATE = -1; // this is a value that will never be in the solution, but will be used to fill the Tabu List

    private final Integer knapsackCapacity;
    private final List<Integer> knapsackWeights;
    private Integer currentKnapsackWeight;

    public SolutionKnapsack(final Integer knapsackCapacity, final List<Integer> knapsackWeights) {
        super();
        this.knapsackCapacity = knapsackCapacity;
        this.knapsackWeights = Collections.unmodifiableList(knapsackWeights);
        this.currentKnapsackWeight = 0;
    }

    protected SolutionKnapsack(final SolutionKnapsack other) {
        super(other);
        this.knapsackCapacity = other.knapsackCapacity;
        this.knapsackWeights = Collections.unmodifiableList(other.knapsackWeights);
        this.currentKnapsackWeight = other.currentKnapsackWeight;
    }

    @Override
    public SolutionKnapsack clone() {
        return new SolutionKnapsack(this);
    }

    @Override
    public Boolean isValidCandidate(final Integer element) {
        return super.isValidCandidate(element) && this.fitsIntoKnapsack(element);
    }

    private Boolean fitsIntoKnapsack(final Integer element) {
        final var elementWeight = this.knapsackWeights.get(element);
        return this.currentKnapsackWeight + elementWeight <= this.knapsackCapacity;
    }

    @Override
    public void add(final Integer element) {
        if (!super.contains(element)) {
            super.add(element);
            final var elementWeight = this.knapsackWeights.get(element);
            this.currentKnapsackWeight += elementWeight;
        }
    }

    @Override
    public void remove(final Integer element) {
        if (super.contains(element)) {
            super.remove(element);
            final var elementWeight = this.knapsackWeights.get(element);
            this.currentKnapsackWeight -= elementWeight;
        }
    }

    @Override
    public void reset() {
        super.reset();
        this.currentKnapsackWeight = 0;
    }

    @Override
    public String toString() {
        final var str = new StringBuilder();
        str.append(String.format(
            "{currentKnapsackWeight: %d, knapsackCapacity: %d, knapsackWeights: %s, solution: %s}",
            this.currentKnapsackWeight,
            this.knapsackCapacity,
            this.knapsackWeights.toString(),
            super.toString()
        ));
        return new String(str);
    }

}

