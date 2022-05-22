package problem;

import java.util.Collection;

import inputReader.InputReaderQBF;
import objectiveFunction.qbf.QBF_Inverse;
import solutions.SolutionInteger;

public class ProblemQBF implements Problem<Integer, Integer> {

    private SolutionInteger solution;
    private QBF_Inverse objectiveFunction;
    private Integer cost;

    public ProblemQBF(final InputReaderQBF input) {
        this.solution = new SolutionInteger();
        this.objectiveFunction = new QBF_Inverse(input, this.solution);
        this.cost = QBF_Inverse.INITIAL_COST;
    }

    protected ProblemQBF(final ProblemQBF other){
        this.cost = other.cost;
        this.solution = other.solution.clone();
        this.objectiveFunction = other.objectiveFunction.clone();
    }

    @Override
    public ProblemQBF clone() {
        return new ProblemQBF(this);
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
    public void add(final Integer element) {
        this.cost += this.objectiveFunction.evaluateInsertionCost(element);
        this.solution.add(element);
        this.objectiveFunction.addVariable(element);
    }

    @Override
    public void remove(final Integer element) {
        this.cost += this.objectiveFunction.evaluateRemovalCost(element);
        this.solution.remove(element);
        this.objectiveFunction.removeVariable(element);
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
        return this.objectiveFunction.evaluateInsertionCost(element);
    }

    @Override
    public Integer evaluateRemovalCost(final Integer element) {
        return this.objectiveFunction.evaluateRemovalCost(element);
    }

    @Override
    public Integer evaluateExchangeCost(final Integer elementToInsert, final Integer elementToRemove) {
        return this.objectiveFunction.evaluateExchangeCost(elementToInsert, elementToRemove);
    }

    @Override
    public Integer evaluateTwoAdditionOneRemovalCost(
        final Integer firstElementToInsert,
        final Integer secondElementToInsert,
        final Integer elementToRemove
    ) {
        return this.objectiveFunction.evaluateTwoAdditionOneRemovalCost(firstElementToInsert, secondElementToInsert, elementToRemove);
    }

    @Override
    public Integer evaluateOneAdditionTwoRemovalCost(
        final Integer elementToInsert,
        final Integer firstElementToRemove,
        final Integer secondElementToRemove
    ) {
        return this.objectiveFunction.evaluateOneAdditionTwoRemovalCost(elementToInsert, firstElementToRemove, secondElementToRemove);
    }

    @Override
    public String toString(){
        return String.format(
            "SolutionCost {cost: %s, %s}", this.cost.toString(), this.solution.toString());
    }

}
