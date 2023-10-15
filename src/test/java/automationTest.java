import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import util.utils;

import java.util.ArrayList;
import java.util.List;
import java.io.FileReader;
import java.io.IOException;


public class automationTest {
    public static void main(String[] args) throws IOException {
        //Initialize the WebDriver with Chrome
        WebDriver driver = new ChromeDriver();
        String url = "https://www.saucedemo.com/";

        driver.get(url);
        driver.manage().window().maximize();

        utils.delay(1000);
        try {
            //Step 1: Login SauceDemo via CSV file
            // Read the CSV file
            FileReader reader = new FileReader("D:\\trainingProject\\DemoCsv.csv");
            CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT);

            //Skip the header row
            csvParser.iterator().next();

            //Iterate through each record in the CSV file
            for (CSVRecord record : csvParser) {
                String username = record.get(0);
                String password = record.get(1);

                driver.findElement(By.id("user-name")).sendKeys(username);
                utils.delay(1000);
                driver.findElement(By.name("password")).sendKeys(password);
                utils.delay(1000);
                driver.findElement(By.xpath("//*[@id=\"login-button\"]")).click();
            }

            utils.delay(2000);
            //Step 2: Verify one of the product from the landing page using Assertion
            WebElement actualValue = driver.findElement(By.xpath("//*[text()='Sauce Labs Fleece Jacket']"));
            String actual = actualValue.getText();
            Assert.assertEquals("Sauce Labs Fleece Jacket", actual);

            //Step 3: Add any three products to the cart
            for (int i = 0; i < 3; i++) {
                WebElement addToCartButton = driver.findElement(By.xpath("(//button[text()='Add to cart'])[" + (i + 1) + "]"));
                addToCartButton.click();
                utils.delay(1000);
            }

            utils.delay(2000);
            // Step 4: Remove one product from the cart
            WebElement removeButton = driver.findElement(By.xpath("(//button[text()='Remove'])[1]"));
            removeButton.click();

            utils.delay(2000);
            // Step 5: Go to the Shopping Cart Link
            WebElement cartLink = driver.findElement(By.className("shopping_cart_link"));
            cartLink.click();

            utils.delay(2000);
            // Step 6: Verify the names of the products in "Your Cart" page using Assertion
            List<WebElement> cartProductNamesElements = driver.findElements(By.className("inventory_item_name"));
            List<String> expectedProductNames = List.of("Sauce Labs Bolt T-Shirt", "Sauce Labs Onesie");

            List<String> actualProductNames = new ArrayList<>();
            for (WebElement element : cartProductNamesElements) {
                String productName = element.getText();
                actualProductNames.add(productName);

                // Print the actual product name
                System.out.println("Actual Product Name: " + productName);

                // Check if the actual product name matches any of the expected names
                if (!expectedProductNames.contains(productName)) {
                    System.out.println("Warning: Unexpected product name found in the cart - " + productName);
                }
            }

            // Compare expected and actual product names
            Assert.assertEquals("Product names in the cart do not match the expected names.", expectedProductNames, actualProductNames);
            System.out.println("Product names in the cart have been verified.");


            utils.delay(2000);
            // Step 7: Remove one product from the cart
            WebElement removeCartItemButton = driver.findElement(By.xpath("(//button[text()='Remove'])[1]"));
            removeCartItemButton.click();

            utils.delay(2000);
            // Step 8: Proceed to Checkout
            WebElement checkoutButton = driver.findElement(By.xpath("(//button[text()='Checkout'])"));
            checkoutButton.click();

            utils.delay(2000);
            // Step 9: Input values in the Checkout Information page
            WebElement firstNameInput = driver.findElement(By.id("first-name"));
            WebElement lastNameInput = driver.findElement(By.id("last-name"));
            WebElement postalCodeInput = driver.findElement(By.id("postal-code"));
            WebElement continueButton = driver.findElement(By.xpath("//*[@id=\"continue\"]"));

            firstNameInput.sendKeys("Mahisma");
            utils.delay(1000);
            lastNameInput.sendKeys("Bajracharya");
            utils.delay(1000);
            postalCodeInput.sendKeys("44600");
            utils.delay(2000);
            continueButton.click();

            utils.delay(2000);
            // Step 10: Finish the checkout process
            WebElement finishButton = driver.findElement(By.xpath("//*[@id=\"finish\"]"));
            finishButton.click();

            utils.delay(2000);
            // Step 11: Verify your order is successfully placed
            WebElement confirmationMessage = driver.findElement(By.xpath("//*[text()='Thank you for your order!']"));

            if (confirmationMessage.isDisplayed()) {
                String confirmationText = confirmationMessage.getText();
                System.out.println("Order placed successfully.");
                System.out.println("Confirmation Message: " + confirmationText);

            // Use assertEquals to check the confirmation message
                String expectedMessage = "Thankyou for your order!";
                Assert.assertEquals(expectedMessage, confirmationText);
            } else {
                System.out.println("Order placement failed. Confirmation message not found.");
                Assert.fail("Order placement failed. Confirmation message not found.");

            }

            /*// Step 11: Verify your order is successfully placed using assertEquals
            WebElement confirmationMessage = driver.findElement(By.xpath("//*[text()='Thank you for your order!']"));

            String expectedMessage = "Thank you for your order!";
            String confirmationText = confirmationMessage.getText();

            System.out.println("Confirmation Message: " + confirmationText);

            // Use assertEquals to check the confirmation message
            Assert.assertEquals("Confirmation message is incorrect.", expectedMessage, confirmationText);*/


            utils.delay(2000);
            // Step 12: Go back to Home
            utils.delay(1000);
            WebElement backButton = driver.findElement(By.xpath("//button[text()='Back Home']"));
            backButton.click();

            // Step 13: Logout
            utils.delay(2000);
            WebElement burgerMenu = driver.findElement(By.id("react-burger-menu-btn"));
            burgerMenu.click();
            utils.delay(2000);
            WebElement logoutButton = driver.findElement(By.id("logout_sidebar_link"));
            logoutButton.click();

        } finally {
            utils.delay(1000);
            driver.close();
        }
    }
}
