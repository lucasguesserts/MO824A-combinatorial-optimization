package SolutionCost;

import java.io.IOException;
import java.util.Collection;

import inputReader.InputReaderQBF;
import objectiveFunction.qbf.QBF_Inverse;
import solutions.SolutionInteger;

public class SolutionCostInteger implements SolutionCost<Integer, Integer> {

    private SolutionInteger solution;
    private QBF_Inverse objectiveFunction;
    private Integer cost;

    public SolutionCostInteger(final String fileName) throws IOException {
        this.solution = new SolutionInteger();
        final var input = new InputReaderQBF(fileName);
        this.objectiveFunction = new QBF_Inverse(input, this.solution);
        this.cost = QBF_Inverse.INITIAL_COST;
    }

    public SolutionCostInteger(final SolutionCostInteger other){
        this.cost = other.cost;
        this.solution = new SolutionInteger(other.solution);
        this.objectiveFunction = new QBF_Inverse(other.objectiveFunction);
    }

    @Override
    public SolutionCostInteger clone() {
        return new SolutionCostInteger(this);
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
    public String toString(){
        return String.format(
            "SolutionCost {cost: %s, %s}", this.cost.toString(), this.solution.toString());
    }

}
