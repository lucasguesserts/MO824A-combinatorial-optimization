package solution;

import java.util.Arrays;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class TestElementSolution {

    ElementsSolution solutionEmpty;
    ElementsSolution solutionWithElements;

    final List<Integer> elementList = Arrays.asList(2, 7);

    @BeforeMethod
    public void init() {
        this.solutionEmpty = new ElementsSolution();
        this.solutionWithElements = new ElementsSolution();
        this.elementList.forEach(
            element -> this.solutionWithElements.addElement(element)
        );
    }

    @Test
    public void testConstructor() {
        Assert.assertEquals(solutionEmpty.getCost(), (Integer) 0);
        Assert.assertNotNull(solutionEmpty.getElements());
        Assert.assertTrue(solutionEmpty.getElements().isEmpty());
    }

    @Test
    public void testAddElement() {
        solutionEmpty.addElement(1);
        solutionEmpty.addElement(2);
        final var expected = Arrays.asList(1, 2);
        Assert.assertFalse(solutionEmpty.getElements().isEmpty());
        Assert.assertEquals(solutionEmpty.getElements(), expected);
    }

    @Test
    public void testRemoveElement() {
        solutionWithElements.removeElement(elementList.get(0));
        Assert.assertFalse(solutionWithElements.getElements().isEmpty());
        Assert.assertEquals(solutionWithElements.getElements(), elementList.subList(1, elementList.size()));
    }

    @Test
    public void testCost() {
        final var solution = new ElementsSolution();
        solution.addElement(1);
        solution.addElement(2);
        Assert.assertEquals(solution.getCost(), (Integer) 2);
    }

    @Test
    public void testNoRepetition() {
        final var solution = new ElementsSolution();
        solution.addElement(1);
        Assert.expectThrows(
            AssertionError.class,
            () -> {
                solution.addElement(1);
            }
        );
    }

    @Test
    public void testNoNegativeValue() {
        final var solution = new ElementsSolution();
        Assert.expectThrows(
            AssertionError.class,
            () -> {
                solution.addElement(-1);
            }
        );
    }

    @Test
    public void testEquals() {
        final var first = new ElementsSolution();
        first.addElement(1);
        first.addElement(2);
        final var second = new ElementsSolution();
        second.addElement(1);
        second.addElement(2);
        Assert.assertEquals(first, second);
    }

}
