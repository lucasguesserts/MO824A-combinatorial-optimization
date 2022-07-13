package solution;

import java.util.Arrays;

import org.testng.Assert;
import org.testng.annotations.Test;

public class TestWeight {

    private static final Double TOLERANCE = 1.0e-12;

    @Test
    public void testConstructor() {
        final var weight = new Weight(Arrays.asList(10, 20, 30));
        Assert.assertNotNull(weight);
        Assert.assertEquals(weight.size(), (Integer) 3);
    }

    @Test
    public void testEquality() {
        final var first = new Weight(Arrays.asList(10, 20, 30));
        final var second = new Weight(Arrays.asList(10, 20, 30));
        final var third = new Weight(Arrays.asList(10, 20, 999));
        Assert.assertEquals(first, second);
        Assert.assertNotEquals(first, third);
    }

    @Test
    public void testExceeds() {
        final var low = new Weight(Arrays.asList(1, 2, 3));
        final var high = new Weight(Arrays.asList(10, 20, 30));
        Assert.assertTrue(high.exceeds(low));
        Assert.assertFalse(low.exceeds(high));
    }

    @Test
    public void testAdd() {
        final var lhs = new Weight(Arrays.asList(10, 20, 30));
        final var rhs = new Weight(Arrays.asList(1, 2, 3));
        final var result = lhs.add(rhs);
        final var expected = new Weight(Arrays.asList(11, 22, 33));
        Assert.assertEquals(result, expected);
    }

    @Test
    public void testSubtract() {
        final var lhs = new Weight(Arrays.asList(10, 20, 30));
        final var rhs = new Weight(Arrays.asList(1, 2, 3));
        final var result = lhs.subtract(rhs);
        final var expected = new Weight(Arrays.asList(9, 18, 27));
        Assert.assertEquals(result, expected);
    }

    @Test
    public void testMax() {
        Weight weight;
        weight = new Weight(Arrays.asList(10, 20, 30));
        assertApprox(weight.getMax(), 30.0);
        weight = new Weight(Arrays.asList(100, 20, 30));
        assertApprox(weight.getMax(), 100.0);
        weight = new Weight(Arrays.asList(10, 200, 30));
        assertApprox(weight.getMax(), 200.0);
    }

    @Test
    public void testNorm() {
        Weight weight;
        weight = new Weight(Arrays.asList(10, 20, 30));
        assertApprox(weight.getNorm2(), 37.416573867739416);
        weight = new Weight(Arrays.asList(79, 89, 10, 40));
        assertApprox(weight.getNorm2(), 125.94443219134381);

    }

    private void assertApprox(final Double actual, final Double expected) {
        final var difference = Math.abs(actual - expected);
        Assert.assertTrue(difference < TOLERANCE);
    }

}
