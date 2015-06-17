package vendingmachine.product.utilities;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ResourceBundle;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.uiautomation.ios.IOSCapabilities;

import vendingmachine.product.bean.Products;

public class ProductUtilities {

	public static void setProductDetails( String productName ) throws InterruptedException, MalformedURLException {

		// IOS Implementation
		// Google
		RemoteWebDriver driver = initiateIOSDriver();

		ResourceBundle locator = ResourceBundle.getBundle( "ObjectRepositories" );

		driver.get( "http://www.google.com" );
		Thread.sleep( 3000 );
		driver.findElement( By.xpath( locator.getString( "searchBox" ) ) ).click();
		Thread.sleep( 3000 );
		driver.findElement( By.xpath( locator.getString( "searchBox" ) ) ).sendKeys( productName );
		Thread.sleep( 3000 );
		driver.findElement( By.xpath( locator.getString( "searchBox.instant" ) ) ).sendKeys( Keys.RETURN );
		Thread.sleep( 3000 );
		driver.findElement( By.xpath( locator.getString( "next.button" ) ) ).click();
		Thread.sleep( 3000 );
		Products.setProductDetailLink( driver.findElement( By.xpath( locator.getString( "secondpage.result.first" ) ) ).getAttribute( "href" ) );
		Products.setProductDetailTitle( driver.findElement( By.xpath( locator.getString( "secondpage.result.first" ) ) ).getText() );
		driver.quit();
		printProductDetails();

	}

	public static void printProductDetails() {

		System.out.println( "You can get more details about the product at following:" );
		System.out.println( "Title-  " + Products.getProductDetailTitle() );
		System.out.println( "Link-  " + Products.getProductDetailLink() );
	}

	public static RemoteWebDriver initiateIOSDriver() throws MalformedURLException {

		DesiredCapabilities safari = IOSCapabilities.ipad( "Safari" );
		RemoteWebDriver driver = new RemoteWebDriver( new URL( "http://localhost:4445/wd/hub" ), safari );
		return driver;
	}
}
