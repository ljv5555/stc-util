package com.cerner.edi.reference.test;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

/**
 * 
 */

/**
 * @author root
 *
 */
public class DEPServiceResponsesToCSVs {

	/**
	 * base "DEP service" url, username, password
	 */
	String[] cmdLineArgs = {"http://ipediuststweb01.northamerica.cerner.net/edi/dep","test","Cerner2010","http://ipediwascrt01.northamerica.cerner.net/edi/reference/submitter/{0}/{1}______http://ipediwastst01.northamerica.cerner.net/edi/reference/submitter/{0}/{1}"};
	/**
	 * 
	 * @param args -- DEP base url, username, password, additionalColumnTemplateStrings delimited by "______"
	 */
	public static void main(String[] args) {
		new DEPServiceResponsesToCSVs(args);
	}
	
	
	public String[] linkResponses = {"","",""};
	public String linkUrlBase = "http://ipediuststweb01.northamerica.cerner.net/edi/dep";
	public String linkUrls[] = {
			linkUrlBase+"/submitter/ajax-read.html?autoForward=false&_retrieveInactive=off",
			linkUrlBase+"/payer/ajax-payer-read.html?payerName=&cernPayerId=&ediPayerId=&_retrieveInactive=off",
			linkUrlBase+"/organization/ajax-read.html?autoForward=false&_retrieveInactive=off"
	};
	public String linkNames[]={"submitter","payer","organization"};
	public Map<String,String> csvStrings = new java.util.Hashtable<String,String>();
	public boolean success = true;
	/**
	 * 
	 */
	public DEPServiceResponsesToCSVs(String[] args) {
		
		if(args!=null)
		{
			if(args.length>0){cmdLineArgs[0]=args[0];}
			if(args.length>1){cmdLineArgs[1]=args[1];}
			if(args.length>2){cmdLineArgs[2]=args[2];}
			if(args.length>3){cmdLineArgs[3]=args[3];}
			
		}
		String[] additionalColumnTemplateStrings=cmdLineArgs[3].split("______");
		int delay = 30;
		try {
			FirefoxDriver driver = new FirefoxDriver();
			driver.manage().timeouts().implicitlyWait(130, TimeUnit.SECONDS);
			WebDriver wdriver = (WebDriver) driver;
			wdriver.get(linkUrlBase+"/edi/dep/index.html");
			Thread.sleep(5 * delay);
			wdriver.findElement(By.id("j_username")).sendKeys("test");
			Thread.sleep(5 * delay);
			wdriver.findElement(By.id("j_password")).sendKeys("Cerner2010");
			Thread.sleep(5 * delay);
			wdriver.findElement(By.id("Login")).click();
			Thread.sleep(5 * delay);
			
			String linkTemplate = linkUrlBase+"/edi/reference/"+"submitter"+"/{0}/{1}"; 
				
			for(int urli = 0;urli<linkUrls.length;urli++)
			{
				wdriver.navigate().to(linkUrls[urli]);
				Thread.sleep(5*delay);
				wdriver.findElement(By.tagName("table"));
				Thread.sleep(5*delay);
				linkResponses[urli]=wdriver.getPageSource();
				Thread.sleep(5*delay);
				File f = new File(linkNames[urli]+".html");
				System.out.println("Writing file: "+f.getAbsolutePath());
				stringToFile(linkResponses[urli], f.getAbsolutePath());
				File f2 = new File(linkNames[urli]+".csv");
				System.out.println("Writing file: "+f2.getAbsolutePath());
				HtmlTableToCsv(f.getAbsolutePath(), f2.getAbsolutePath());
				System.out.println("--- --- --- "+f2.getAbsolutePath()+" --- --- --- ");
				csvStrings.put(linkNames[urli], readFile(f2.getAbsolutePath()));
				// read in csv add link, write back
				for(String templateString : additionalColumnTemplateStrings)
				{
					System.out.println("CSVUtils.appendColumnToEveryRowInCsvString(csvStrings.get(linkNames[urli]), \""+templateString+"\");");
					CSVUtils.appendColumnToEveryRowInCsvString(csvStrings.get(linkNames[urli]), templateString);
				}
				
			}
			Thread.sleep(5 * delay);
			wdriver.navigate().to(linkUrlBase+"/edi/dep/j_spring_security_logout");
			Thread.sleep(5 * delay);
			wdriver.close();
		} catch (Exception e) {
			e.printStackTrace();
			success=false;
		}

		ServiceTestRequestList strl = new ServiceTestRequestList();
		for(String[] sa : strl.getTestRequestsForService("submitter", linkUrlBase))
		{
			System.out.println(sa[0]);
		}
		
			
		
	}
	public static void HtmlTableToCsv(String htmlPath, String csvPath) throws ParserConfigurationException
	{
		
		String html = readFile(htmlPath);
		try{
			DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document document = db.parse(htmlPath);
			NodeList rows = document.getElementsByTagName("tr");
			List<String[]> rowsStrings = new LinkedList<String[]>();
			for(int i=0;i<rows.getLength();i++)
			{
				NodeList rowCells = rows.item(i).getChildNodes();
				List<String> currentRow = new ArrayList<String>();
				for(int j=0;j<rowCells.getLength();j+=1)
				{
					if(rowCells.item(j).getNodeType()==org.w3c.dom.html.HTMLElement.ELEMENT_NODE)
					{
						currentRow.add(String.valueOf(rowCells.item(j).getTextContent()).trim());
					}
				}
				System.out.println();
				//currentRow.add("");
				rowsStrings.add(currentRow.toArray(new String[]{}));
				
			}
			CSVWriter csvw = new CSVWriter(new FileWriter(csvPath));
			csvw.writeAll(rowsStrings);
			csvw.close();
			System.out.println("Wrote file: "+new File(htmlPath).getAbsolutePath());
		}catch(Exception ex){ex.printStackTrace();}
	}
	
	public static String readFile(String path)
	{
		StringWriter rtn = new StringWriter();
		PrintWriter pw = new PrintWriter(rtn);
		try{
			BufferedReader br = new BufferedReader(new FileReader(path));
			String line = br.readLine();
			while(true)
			{
				if(line==null){break;}
				pw.println(line);
				line = br.readLine();
			}
			br.close();
			pw.close();
		}catch(Exception e){e.printStackTrace();}
		return rtn.toString();
	}
	public static void stringToFile(String s, String file)
	{
		try{
			FileWriter pw = new FileWriter(file,false);
			pw.write(s);
			pw.close();
		}catch(Exception e){e.printStackTrace();}
	}
	public static WebElement[] getDescendantElementsByCssSelector(WebElement e, String cssSelector)
	{
		LinkedList<WebElement> rtn = new LinkedList<WebElement>();
		rtn.addAll(e.findElements(By.cssSelector(cssSelector)));
		return rtn.toArray(new WebElement[]{});
	}
	public static String join(String[] args, String join){StringBuilder rtn = new StringBuilder();for(int i=0;i<args.length;i++){rtn.append(args[i]);if(i!=(args.length-1)){rtn.append(join);}}return rtn.toString();}
	public static String padTo(String s, int len){String rtn = ""+s;while(rtn.length()<len){rtn = " "+rtn;}return rtn;}
//	public static List<String[]> tableToArrayRowList(WebElement table)
//	{
//		List<String[]> rtn = new ArrayList<String[]>();
//		WebElement[] trelements = getDescendantElementsByCssSelector(table,"tr");
//		for(WebElement tr : trelements)
//		{
//			List<String> rtnRow = new ArrayList<String>();
//			WebElement tds[] = getDescendantElementsByCssSelector(tr,"td");
//			int cycles = 0;
//			for(WebElement td : tds)
//			{
//				if(cycles>0){System.out.print(", ");}
//				System.out.print(td.getText());System.out.flush();
//				rtnRow.add(td.getText());
//				cycles++;
//			}
//			System.out.println();
//			rtn.add(rtnRow.toArray(new String[]{}));
//		}
//		return rtn;
//	}
//	public static String arrayToString(String[][] array)
//	{
//		int padSize = 0;
//		List<String> rows = new ArrayList<String>();
//		for(String[] sa : array)
//		{
//			rows.add(join(sa,", "));
//		}
//		String rtn = join(rows.toArray(new String[]{}),"\n");
//		return rtn;
//	}
	
	public static String webElementToString(WebElement we)
	{
		String rtn = we.getText();
//		arrayToString(tableToArrayRowList(we).toArray(new String[][]{}));
		return rtn;
	}
	public static String htmlTableToString(String tablesrc)
	{
		StringBuilder rtn = new StringBuilder();return rtn.toString();
	}
	List<String> webElementsAsStrings(List<WebElement> wel)
	{
		List<String> rtn = new ArrayList<String>();
		for(WebElement we : wel){rtn.add(we.getText());}
		return rtn;
	}
	
}
