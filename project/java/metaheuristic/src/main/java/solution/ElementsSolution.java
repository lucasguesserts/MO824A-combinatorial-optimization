package solution;

import java.util.HashSet;
import java.util.Set;

import org.json.JSONObject;

public class ElementsSolution {

    private final Set<Integer> elements = new HashSet<Integer>();

    public ElementsSolution() {}

    protected ElementsSolution(final ElementsSolution other) {
        this.elements.addAll(other.elements);
    }

    public Set<Integer> getElements() {
        return this.elements;
    }

    public Integer getCost() {
        return this.elements.size();
    }

    public void addElement(final Integer element) {
        this.assertNoNewElement(element);
        this.assertNoNegativeValue(element);
        this.elements.add(element);
        return;
    }

    public void removeElement(final Integer element) {
        this.assertElementIsPresent(element);
        this.assertNoNegativeValue(element);
        this.elements.remove(element);
        return;
    }

    public void substituteElement(final Integer toRemove, final Integer toAdd) {
        this.assertNoEqualElements(toRemove, toAdd);
        this.removeElement(toRemove);
        this.addElement(toAdd);
        return;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ElementsSolution other = (ElementsSolution) obj;
        if (this.elements.size() != other.elements.size()) {
            return false;
        }
        return this.elements.containsAll(other.elements) && other.elements.containsAll(this.elements);
    }

    public JSONObject toJson() {
        final var obj = new JSONObject();
        obj.put("cost", this.getCost());
        obj.put("elements", this.getElements());
        return obj;
    }

    @Override
    public String toString() {
        return this.toJson().toString(4);
    }

    private void assertNoNewElement(final Integer element) {
        assert !this.getElements().contains(element)
            : String.format("Element already in the solution:\n\telement: %d\n\tsolution: %s",
                element,
                this.getElements().toString()
            );
        return;
    }

    private void assertNoNegativeValue(final Integer element) {
        assert element >= 0
            : String.format("Element must not be negative:\n\telement: %d",
                element
            );
        return;
    }

    private void assertElementIsPresent(final Integer element) {
        assert this.elements.contains(element)
            : String.format(
                "Element %d must be present in element list %s",
                element,
                this.elements.toString()
            );
        return;
    }

    private void assertNoEqualElements(final Integer lhs, final Integer rhs) {
        assert !lhs.equals(rhs)
            : String.format(
                "Elements %d and %d are equal",
                lhs,
                rhs
            );
        return;
    }

}

