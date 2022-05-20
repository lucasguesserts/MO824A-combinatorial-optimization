package solutions;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class SolutionInteger implements Solution<Integer> {

    private final Set<Integer> elements;

    public SolutionInteger() {
        super();
        this.elements = new HashSet<>();
    }

    public SolutionInteger(final SolutionInteger other) {
        this.elements = new HashSet<>(other.elements);
    }

    @Override
    public Collection<Integer> getElements() {
        return Collections.unmodifiableSet(this.elements);
    }

    @Override
    public Boolean isValidCandidate(final Integer element) {
        return ! this.elements.contains(element);
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
        return String.format("Solution: {size: %d, elements: %s",
            this.elements.size(),
            this.elements.toString()
        );
    }

}

