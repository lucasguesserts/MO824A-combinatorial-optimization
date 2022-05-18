package solutions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SolutionInteger implements Solution<Integer> {

    private final List<Integer> elements;

    public SolutionInteger() {
        super();
        this.elements = new ArrayList<>();
    }

    public SolutionInteger(final SolutionInteger other) {
        this.elements = new ArrayList<>(other.elements);
    }

    @Override
    public List<Integer> getElements() {
        return Collections.unmodifiableList(this.elements);
    }

    @Override
    public void add(final Integer element) {
        this.elements.add(element);
    }

    @Override
    public void remove(final Integer element) {
        this.elements.remove(element);
    }

    @Override
    public void reset() {
        this.elements.clear();
    }

    @Override
    public String toString() {
        return String.format("Solution: {size: %d, elements: [%s]",
            this.elements.size(),
            this.elements.toString()
        );
    }

}

