package com.cerner.edi.reference.test;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class DeployToWAS {

	public static String adminConsoleLoginUrl = "https://localhost:9043/ibm/console/logon.jsp";
	public static String[] adminConsoleCredentials = new String[]{"admin","admin"};
	public static long delay = 100;
	public static boolean success = true;
	public DeployToWAS(String[] args) 
	{
		ArrayList<String[]> rtn = new ArrayList<String[]>();
		StringWriter rtnb = new StringWriter();
		PrintWriter pw = new PrintWriter(rtnb);
		

			try {
				FirefoxDriver driver = new FirefoxDriver();
				driver.manage().timeouts().implicitlyWait(130, TimeUnit.SECONDS);
				WebDriver wdriver = (WebDriver) driver;
				wdriver.get(adminConsoleLoginUrl);
				Thread.sleep(5 * delay);
//				wdriver.findElement(By.id("j_username")).sendKeys(adminConsoleCredentials[0]);
//				Thread.sleep(5 * delay);
//				wdriver.findElement(By.id("j_password")).sendKeys(adminConsoleCredentials[1]);
//				Thread.sleep(5 * delay);
//				wdriver.findElement(By.className("loginButton")).click();
//				Thread.sleep(5 * delay);
//				Thread.sleep(50 * delay);
//				wdriver.findElement(By.partialLinkText("Logout")).click();
//				Thread.sleep(5 * delay);
				wdriver.close();
			//	wdriver.close();
			} catch (Exception e) {
				e.printStackTrace(System.out);
				success=false;
			}
		

		
	}

	public static void main(String[] args) 
	{
		new DeployToWAS(args);
	}

}
