package org.mixer2.sample.integrationtest;

import static org.junit.Assert.assertTrue;

import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.mixer2.sample.Server;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.remote.DesiredCapabilities;

/**
 * Integration test with Selenium WebDriver.<br>
 * To run this test, install Firefox 38 or later and un-comment out the line
 * with "@Ignore".
 *
 */
public class PurchaseTest {

    protected WebDriver driver;

    protected static Server server;

    @Ignore // !!! un-comment out here !!!
    @Test
    public void purchase() throws Exception {
        // go top page and assert
        driver.get("http://localhost:8080");
        assertTrue(driver.findElement(By.id("content")).getText().contains("Hello World !"));

        // go catgory page ("berry") and assert
        driver.findElement(By.id("categoryList")).findElements(By.tagName("a")).get(0).click();

        // go item page ("stwawberry") and assert
        driver.findElement(By.id("itemTable")).findElements(By.tagName("td")).get(0).findElements(By.tagName("a"))
                .get(0).click();

        Thread.sleep(5000);
    }

    @BeforeClass
    @SuppressWarnings("static-access")
    public static void beforeClass() throws Exception {
        server = new Server();
        server.main(new String[] {});
    }

    @AfterClass
    @SuppressWarnings("static-access")
    public static void afterClass() throws Exception {
        server.close();
    }

    @Before
    public void before() throws Exception {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        FirefoxProfile profile = new FirefoxProfile();
        profile.setPreference("browser.private.browsing.autostart", true);
        profile.setPreference("browser.privatebrowsing.dont_prompt_on_enter", true);
        profile.setEnableNativeEvents(false);
        FirefoxBinary ffbin = new FirefoxBinary();
        driver = new FirefoxDriver(ffbin, profile, capabilities);

        driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
        driver.manage().timeouts().pageLoadTimeout(15, TimeUnit.SECONDS);
        driver.manage().timeouts().setScriptTimeout(15, TimeUnit.SECONDS);
        int width = 1000;
        int height = 600;
        driver.manage().window().setSize(new Dimension(width, height));
    }

    @After
    public void after() throws Exception {
        if (driver != null) {
            driver.quit();
        }
    }

}
