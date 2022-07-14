package solution;

import java.util.Arrays;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class TestWeightedSolution {

    Weight capacity;
    WeightedSolution solutionEmpty;
    WeightedSolution solutionWithElements;

    final List<Integer> elementList = Arrays.asList(2, 7);
    final List<Weight> weightList = Arrays.asList(
        new Weight(Arrays.asList(1, 2, 3)),
        new Weight(Arrays.asList(5, 14, 22))
    );

    @BeforeMethod
    public void init() {
        this.capacity = new Weight(Arrays.asList(10, 20, 30));
        this.solutionEmpty = new WeightedSolution(capacity);
        this.solutionWithElements = new WeightedSolution(capacity);
        for (int i = 0; i < this.elementList.size(); ++i) {
            this.solutionWithElements.addElement(
                this.elementList.get(i),
                this.weightList.get(i)
            );
        }
    }

    @Test
    public void testConstructor() {
        Assert.assertNotNull(solutionEmpty.getCapacity());
        Assert.assertNotNull(solutionEmpty.getWeight());
    }

    @Test
    public void testEqualityNoElement() {
        final var other = new WeightedSolution(capacity);
        Assert.assertEquals(solutionEmpty, other);
    }

    @Test
    public void testEquality() {
        final var other = new WeightedSolution(capacity);        for (int i = 0; i < this.elementList.size(); ++i) {
            other.addElement(
                this.elementList.get(i),
                this.weightList.get(i)
            );
        }
        Assert.assertEquals(solutionWithElements, other);
    }

    @Test
    public void testNotEqualsDifferentWeight() {
        final var other = new WeightedSolution(capacity);
        other.addElement(elementList.get(0), weightList.get(0));
        other.addElement(elementList.get(1), new Weight(Arrays.asList(5, 14, 3)));
        Assert.assertNotEquals(solutionWithElements, other);
    }

    @Test
    public void testNotEqualsDifferentCapacity() {
        final var otherCapacity = new Weight(Arrays.asList(10, 20, 99));
        final var other = new WeightedSolution(otherCapacity);
        Assert.assertNotEquals(solutionEmpty, other);
    }

    @Test
    public void testNotEqualsDifferentElements() {
        final var other = new WeightedSolution(capacity);
        other.addElement(elementList.get(0), weightList.get(0));
        other.addElement(elementList.get(1) + 1, weightList.get(1));
        Assert.assertNotEquals(solutionWithElements, other);
    }

}
