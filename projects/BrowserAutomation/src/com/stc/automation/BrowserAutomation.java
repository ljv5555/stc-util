/**
 * 
 */
package com.stc.automation;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.browserlaunchers.locators.InternetExplorerLocator;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

/**
 * @author l.j.verderber.iv
 *
 */
public class BrowserAutomation 
{
	

	/**
	 * 
	 */
	public static WebDriver getDriver(String url, Map<String,String> htmlInputElementCssSelectorsAndValues, WebDriver driver)
	{
		
		if(driver==null)
		{
			
//			if(!System.getProperties().keySet().contains("webdriver.ie.driver"))
//			{
//				System.setProperty("webdriver.ie.driver", new InternetExplorerLocator().findBrowserLocation().launcherFilePath());
//			}
//			System.out.println("webdriver.ie.driver="+System.getProperties().get("webdriver.ie.driver"));
//			driver = new InternetExplorerDriver();
		driver = new FirefoxDriver();
		}	
		driver.manage().timeouts().implicitlyWait(90, TimeUnit.SECONDS);
		driver.manage().timeouts().pageLoadTimeout(190, TimeUnit.SECONDS);
		driver.manage().timeouts().setScriptTimeout(290, TimeUnit.SECONDS);
		
		driver.get(url);
		String[] cssKeys = htmlInputElementCssSelectorsAndValues.keySet().toArray(new String[0]);
		for(String key : cssKeys)
		{
			String value = htmlInputElementCssSelectorsAndValues.get(key);
			driver.findElement(By.cssSelector(key)).sendKeys(""+value);
		}
		return driver;
	}

	public static void saveScreen(WebDriver driver, String filename)
	{
			File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
			try 
			{
				FileUtils.copyFile(scrFile, new File(""+filename));
			} catch (IOException e) 
			{
				e.printStackTrace();
			}
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) 
	{
		String stime = ""+new Date().getTime();
		if(!new File("results").exists()){new File("results").mkdirs();}
		//else{for(File f : new File("results").listFiles()){f.delete();}}
		
	while(true){	
		WebDriver driver = BrowserAutomation.getDriver("http://beta.speedtest.net",toMap(), null);
		try {Thread.sleep(5000);} catch (InterruptedException e) {e.printStackTrace();}
		if(driver instanceof FirefoxDriver)
		{
			FirefoxDriver.class.cast(driver).executeScript("jQuery('.start-text').click()");
			driver.manage().window().maximize();
		}
		try {Thread.sleep(50000);} catch (InterruptedException e) {e.printStackTrace();}
		long startTime = new java.util.Date().getTime();
		while(driver.getCurrentUrl().split("\\/")[driver.getCurrentUrl().split("\\/").length-1].replace("[0-9]*", "").trim().length()==0){ System.out.println("waiting"); try{Thread.sleep(1000);}catch(Exception ee){} }
		for(int i=0;i<1;i++)
		{
			try {Thread.sleep(1000);} catch (InterruptedException e) {e.printStackTrace();}
			System.out.println("."+(30-i));
			String name = stime+"_"+(new Date().getTime()-startTime)+"_"+new Date().getTime()+".png";
			saveScreen(driver,"results"+File.separator+name);
		}
		driver.close();
		try {Thread.sleep(5000);} catch (InterruptedException e) {e.printStackTrace();}
		
	}
		
	}

	public static Map<String,String> toMap(String...kvp)
	{
		Map<String,String> rtn = new LinkedHashMap<String,String>();
		for(int i=1;i<kvp.length;i++)
		{
			String n = ""+kvp[i-1];
			String v = ""+kvp[i];
			rtn.put(n,v);
		}
		return rtn;
	}
}
