package solutions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SolutionInteger implements Solution<Integer, Integer> {

    private Integer cost;
    private final List<Integer> elements;
    private List<Integer> unmodifiableElements;

    public SolutionInteger(final Integer initial_cost) {
        super();
        this.elements = new ArrayList<>();
        this.unmodifiableElements = Collections.unmodifiableList(this.elements);
        this.cost = initial_cost;
    }

    @Override
    public List<Integer> getElements() {
        return this.unmodifiableElements;
    }

    @Override
    public Integer getCost() {
        return this.cost;
    }

    @Override
    public void add(final Integer element, final Integer costIncrement) {
        this.elements.add(element);
        this.unmodifiableElements = Collections.unmodifiableList(this.elements);
        this.cost += costIncrement;
    }

    @Override
    public void remove(final Integer element, final Integer costDecrement) {
        this.elements.remove(element);
        this.unmodifiableElements = Collections.unmodifiableList(this.elements);
        this.cost -= costDecrement;
    }

    @Override
    public SolutionInteger clone() {
        return new SolutionInteger(this);
    }

    private SolutionInteger(final SolutionInteger other) {
        this.elements = new ArrayList<>(other.elements);
        this.unmodifiableElements = Collections.unmodifiableList(this.elements);
        this.cost = other.cost;
    }

    @Override
    public String toString() {
        return String.format("Solution: {cost: %f, size: %f, elements: [%s]",
            this.cost,
            this.elements.size(),
            this.elements.toString()
        );
    }

}

