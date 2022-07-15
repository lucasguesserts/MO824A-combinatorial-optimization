package testing_framework;

import org.testng.Assert;
import org.testng.annotations.Test;

public final class TestngThrowTestSuite
{

    @Test
    public void test_expect_throw()
    {
        Assert.expectThrows(
            Exception.class,
            () ->
            {
                throw new Exception("this exception must be properly detected");
            }
        );
    }

    @Test(expectedExceptions = Exception.class)
    public void test_expected_exception() throws Exception
    {
        throw new Exception("this exception must be properly detected");
    }

}
