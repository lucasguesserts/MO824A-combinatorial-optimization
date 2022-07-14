package metaheuristic;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import graph.GraphTools;
import metaheuristic.greedy_criteria.GreedyCriteria;
import problem.Problem;
import solution.Solution;
import solution.WeightedSolution;

public class ConstructiveGrasp {

    static final Random randomNumberGenerator = new Random(0);

    protected final Problem problem;
    protected final GreedyCriteria greedyCriteria;
    protected final Double greedyParameter;

    protected Solution currentSolution;
    protected Integer candidateToAdd;
    protected Set<Integer> candidateList;
    protected List<Integer> restrictedCandidateList;

    public ConstructiveGrasp(
        final Problem problem,
        final GreedyCriteria greedyCriteria,
        final Double greedyParameter) {
        this.problem = problem;
        this.greedyCriteria = greedyCriteria;
        this.greedyParameter = greedyParameter;
    }

    public Solution constructiveHeuristic() {
        createEmptySolution();
        this.initializeCandidateList();
        updateRestrictedCandidateList();
        while (!stopCriteriaMet()) {
            chooseCandidate();
            addCandidateToSolution();
            updateCandidateList();
            updateRestrictedCandidateList();
        }
        return currentSolution;
    }

    private void addCandidateToSolution() {
        final var elementWeight = this.problem.getWeightMap().get(this.candidateToAdd);
        this.currentSolution.addElement(this.candidateToAdd, elementWeight);
        return;
    }

    private void initializeCandidateList() {
        /**
         * The initial candidate list is composed by
         * all nodes which have no predecessor.
         * They are better know as "root nodes".
         * Filter and remove the elements which do
         * not fit in the solution.
         */
        final var rootNodes = GraphTools.findRootNodes(this.problem.getGraph());
        final var weightMap = this.problem.getWeightMap();
        final var capacity = this.problem.getCapacity();
        this.candidateList = new HashSet<>(rootNodes
            .stream()
            .filter(node -> !weightMap.get(node).exceeds(capacity))
            .toList()
        );
        return;
    }

    private void updateRestrictedCandidateList() {
        if (this.candidateList.isEmpty()) {
            this.restrictedCandidateList = new ArrayList<>();
            return;
        }
        // evaluate the greedy cost of all candidates
        final List<ElementValuePair<Double>> elementValuePairList = this.candidateList
            .stream()
            .map(node -> new ElementValuePair<Double>(node, greedyCriteria.evaluate(this.problem, this.currentSolution, node)))
            .sorted((lhs, rhs) -> (int) Math.signum(lhs.value - rhs.value))
            .toList();
        // find the minimum value threshold (we want values lower than it)
        final var minimumValue = elementValuePairList.get(elementValuePairList.size() - 1).value;
        final var maximumValue = elementValuePairList.get(elementValuePairList.size() - 1).value;
        assert maximumValue >= minimumValue
            : String.format(
                "maximum value %f is not greater or equals minimum value %f",
                maximumValue,
                minimumValue
            );
        final var maximumValueThreshold = minimumValue + this.greedyParameter * (maximumValue - minimumValue);
        // make the candidate list out of the elements with value below the threshold
        final Double TOLERANCE = 1.0e-6;
        this.restrictedCandidateList = elementValuePairList
            .stream()
            .filter(pair -> pair.value <= maximumValueThreshold + TOLERANCE)
            .map(pair -> pair.element)
            .toList();
        return;
    }

    private void updateCandidateList() {
        /**
         * Add new possible candidates
         * to the old ones and remove
         * all which exceeds the capacity.
         */
        final var possibleNewCandidates = this.problem.getGraph().successors(this.candidateToAdd)
            .stream()
            .filter(element -> this.currentSolution.getElements().containsAll(this.problem.getGraph().predecessors(element))) // those which have all predecessors in the current solution
            .toList();
        this.candidateList.addAll(possibleNewCandidates);
        final var restrictedCapacity = this.currentSolution.getCapacity().subtract(this.currentSolution.getWeight());
        this.candidateList = new HashSet<>(this.candidateList
            .stream()
            .filter(element -> !this.problem.getWeightMap().get(element).exceeds(restrictedCapacity)) // do not exceed the capacity
            .toList()
        );
    }

    private void createEmptySolution() {
        this.currentSolution = new WeightedSolution(this.problem.getCapacity());
    }

    private Boolean stopCriteriaMet() {
        return this.candidateList.isEmpty();
    }

    private void chooseCandidate() {
        final Integer randomIndex = randomNumberGenerator.nextInt(this.restrictedCandidateList.size());
        this.candidateToAdd = this.restrictedCandidateList.get(randomIndex);
        this.candidateList.remove(this.candidateToAdd);
        return;
    }

}

final class ElementValuePair<T> {

    public final Integer element;
    public final T value;

    public ElementValuePair(final Integer element, final T value) {
        this.element = element;
        this.value = value;
        return;
    }

}
