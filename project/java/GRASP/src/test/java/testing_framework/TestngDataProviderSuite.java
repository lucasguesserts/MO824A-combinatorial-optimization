package testing_framework;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public final class TestngDataProviderSuite
{

    @DataProvider(name = "data provider test")
    public static Integer[] case_file_name_list()
    {
        return new Integer[] {
            96,
            -16,
            35,
            -89,
        };
    }

    @Test(dataProvider = "data provider test")
    public void test_data_provider(final Integer number)
        throws Exception
    {
        Assert.assertNotNull(number);
        Assert.assertTrue(-1000   < number);
        Assert.assertTrue(number < 1000);
    }

}
