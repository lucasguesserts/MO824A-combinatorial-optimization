package problem;

import java.util.Collection;

import inputReader.InputReaderKQBF;
import objectiveFunction.qbf.QBF_Inverse;
import solutions.SolutionKnapsack;

public class ProblemKQBF implements Problem<Integer, Integer> {

    private SolutionKnapsack solution;
    private QBF_Inverse objectiveFunction;
    private Integer cost;

    public ProblemKQBF(final InputReaderKQBF input) {
        this.solution = new SolutionKnapsack(input.getKnapsackCapacity(), input.getKnapsackWeights());
        this.objectiveFunction = new QBF_Inverse(input, this.solution);
        this.cost = QBF_Inverse.INITIAL_COST;
    }

    protected ProblemKQBF(final ProblemKQBF other){
        this.cost = other.cost;
        this.solution = other.solution.clone();
        this.objectiveFunction = other.objectiveFunction.clone();
    }

    @Override
    public ProblemKQBF clone() {
        return new ProblemKQBF(this);
    }

    @Override
    public Collection<Integer> getElements() {
        return this.solution.getElements();
    }

    @Override
    public Integer getCost() {
        return this.cost;
    }

    @Override
    public Boolean isValid(final Collection<Integer> elementsToAdd, final Collection<Integer> elementsToRemove) {
        Boolean valid = Boolean.TRUE;
        valid &= this.validRange(elementsToAdd, elementsToRemove);
        valid &= this.validWeight(elementsToAdd, elementsToRemove);
        return valid;
    }

    private Boolean validRange(final Collection<Integer> elementsToAdd, final Collection<Integer> elementsToRemove) {
        Boolean valid = Boolean.TRUE;
        valid &= elementsToAdd.stream().anyMatch(
            element -> (element < 0 || element >= objectiveFunction.getDomainSize())
        );
        if (valid)
            valid &= elementsToRemove.stream().anyMatch(
                element -> (element < 0 || element >= objectiveFunction.getDomainSize())
            );
        return valid;
    }

    private Boolean validWeight(final Collection<Integer> elementsToAdd, final Collection<Integer> elementsToRemove) {
        Boolean valid = Boolean.TRUE;
        Integer weight = this.solution.getCurrentKnapsackWeight();
        for (final var element: elementsToRemove) {
            weight -= this.solution.getWeightOfElement(element);
        }
        for (final var element: elementsToAdd) {
            weight += this.solution.getWeightOfElement(element);
        }
        valid &= (weight < this.solution.getKnapsackCapacity());
        return valid;
    }

    @Override
    public void add(final Integer element) {
        if (this.solution.isValidCandidate(element)) {
            this.cost += this.objectiveFunction.evaluateInsertionCost(element);
            this.solution.add(element);
            this.objectiveFunction.addVariable(element);
        }
    }

    @Override
    public void remove(final Integer element) {
        if (this.solution.contains(element)) {
            this.cost += this.objectiveFunction.evaluateRemovalCost(element);
            this.solution.remove(element);
            this.objectiveFunction.removeVariable(element);
        }
    }

    @Override
    public void reset() {
        this.solution.reset();
        this.objectiveFunction.reset();
        this.cost = QBF_Inverse.INITIAL_COST;
    }

    @Override
    public Integer getDomainSize() {
        return this.objectiveFunction.getDomainSize();
    }

    @Override
    public Boolean isValidCandidate(final Integer element) {
        return this.objectiveFunction.isValidCandidate(element) && this.solution.isValidCandidate(element);
    }

    @Override
    public Integer evaluateInsertionCost(final Integer element) {
        if (this.isValidCandidate(element))
            return this.objectiveFunction.evaluateInsertionCost(element);
        else
            return 0;
    }

    @Override
    public Integer evaluateRemovalCost(final Integer element) {
        return this.objectiveFunction.evaluateRemovalCost(element);
    }

    @Override
    public Integer evaluateExchangeCost(final Integer elementToInsert, final Integer elementToRemove) {
        final Integer newKnapsackWeight =
            this.solution.getCurrentKnapsackWeight()
            + this.solution.getWeightOfElement(elementToInsert)
            - this.solution.getWeightOfElement(elementToRemove);
        if (newKnapsackWeight < this.solution.getKnapsackCapacity())
            return this.objectiveFunction.evaluateExchangeCost(elementToInsert, elementToRemove);
        else
            return Integer.MAX_VALUE; // it is a minimization algorithm so return the worst improvement in order to forbid such move
    }

    public Integer evaluate(
        final Collection<Integer> elementsToInsert,
        final Collection<Integer> elementsToRemove
    ) {
        if (this.isValid(elementsToInsert, elementsToRemove)) {
            return this.objectiveFunction.evaluate(elementsToInsert, elementsToRemove);
        } else {
            return 0;
        }
    }

    @Override
    public String toString(){
        return String.format(
            "SolutionCost {cost: %s, solutionKnapsack: %s}", this.cost.toString(), this.solution.toString());
    }

}
