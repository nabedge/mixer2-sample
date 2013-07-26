package org.mixer2.sample.selenium;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.fail;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.excel.XlsDataSet;
import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.rules.ErrorCollector;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.springframework.util.ResourceUtils;

@Ignore // TODO delete  if you run test!
@RunWith(Theories.class)
public class SeleniumTest {

    private String topUrl = "http://localhost:8080/mixer2-fruitshop-springmvc/";
    private WebDriver driver;
    private static ITable paramTable;
    private static List<AssertionError> assertionErrors = new ArrayList<AssertionError>();
    private static class TestParameter {
        public String number;
        public String memo;
        public String categoryName;
        public String itemName;
        public String itemPrice;
    }

    @Rule
    public ErrorCollector ec = new ErrorCollector();

    @BeforeClass
    public static void beforeClass() throws Exception {
        File file = ResourceUtils
                .getFile("classpath:testdata/SeleniumParams.xls");
        IDataSet dataSet = new XlsDataSet(file);
        paramTable = dataSet.getTable("params");
    }

    @DataPoints
    public static TestParameter[] dataPoints() throws Exception {
        ArrayList<TestParameter> parameterList = new ArrayList<TestParameter>();
        for (int i=0; i < paramTable.getRowCount(); i++) {
            TestParameter p = new TestParameter();
            p.number = paramTable.getValue(i, "number").toString();
            p.memo = paramTable.getValue(i, "memo").toString();
            p.categoryName = paramTable.getValue(i, "categoryName").toString();
            p.itemName = paramTable.getValue(i, "itemName").toString();
            p.itemPrice = paramTable.getValue(i, "itemPrice").toString();
            parameterList.add(p);
        }
        return parameterList.toArray(new TestParameter[parameterList.size()]);
    }

    @Before
    public void setUp() throws Exception {
        driver = new FirefoxDriver();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
    }

    @Theory
    public void カテゴリと商品詳細の表示(TestParameter p) {
        driver.get(topUrl);
        // カテゴリのページへ
        driver.findElement(By.linkText(p.categoryName)).click();
        assertThat(p.number, driver.findElement(By.id("categoryName")).getText(), is(p.categoryName));
        // 商品詳細のページへ
        driver.findElement(By.linkText(p.itemName)).click();
        assertThat(p.number, driver.findElement(By.id("itemName")).getText(), is(p.itemName));
        assertThat(p.number, driver.findElement(By.id("itemPrice")).getText(), is(p.itemPrice));
    }

    private <T> void assertThat(String message, T actual, Matcher<? super T> matcher) {
        try {
            Assert.assertThat(message, actual, matcher);
        } catch (AssertionError e) {
            assertionErrors.add(e);
        }
    }

    @After
    public void after() throws Exception {
        driver.quit();// shutdown browser
    }

    @AfterClass
    public static void afterClass() throws Exception {
        if (assertionErrors.size() > 0) {
            for (AssertionError e: assertionErrors) {
                System.out.println(e.getMessage());
            }
            fail(assertionErrors.size() + "件のAssertionErrorが発生");
        }
    }

}
