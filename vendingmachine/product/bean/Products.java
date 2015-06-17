package vendingmachine.product.bean;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ResourceBundle;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.uiautomation.ios.IOSCapabilities;

public abstract class Products {

	private String productName;
	private int productCost;
	private int productQuantity;
	private static String productDetailTitle;
	private static String productDetailLink;
	private static int productsCount = 0;

	public Products( String productName, int productCost, int productQuantity ) {

		this.productName = productName;
		this.productCost = productCost;
		this.productQuantity = productQuantity;
		this.setProductsCount( this.getProductsCount() + 1 );
	}

	public String getProductName() {

		return productName;
	}

	public void setProductName( String productName ) {

		this.productName = productName;
	}

	public int getProductCost() {

		return productCost;
	}

	public void setProductCost( int productCost ) {

		this.productCost = productCost;
	}

	public int getProductQuantity() {

		return productQuantity;
	}

	public void setProductQuantity( int productQuantity ) {

		this.productQuantity = productQuantity;
	}

	public static String getProductDetailTitle() {

		return productDetailTitle;
	}

	public static void setProductDetailTitle( String productDetailTitle ) {

		Products.productDetailTitle = productDetailTitle;
	}

	public static String getProductDetailLink() {

		return productDetailLink;
	}

	public static void setProductDetailLink( String productDetailLink ) {

		Products.productDetailLink = productDetailLink;
	}

	public static int getProductsCount() {

		return productsCount;
	}

	public static void setProductsCount( int productsCount ) {

		Products.productsCount = productsCount;
	}

}
