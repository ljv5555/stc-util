package com.cerner.edi.reference.test;
import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.junit.Test;



/**
 * 
 */

/**
 * @author C_Verderber,Joe@cerner.com
 * test the rest reference service against the old version; fail if responses differ
 */
public class TestReferenceService {

	public static void main(String[] args)
	{
//		TestReferenceService trf = new TestReferenceService();
//		//trf.test();
//		String tu = "http://ipediwascrt01.northamerica.cerner.net/edi/reference/submitter/SUBMITTER_CERT/CREDIT_PROCESSING";
//		System.out.println();
//		List<String[]> tur = httpRequest(tu, (new ArrayList<String[]>()), "GET", "");
//		if(tur.size()>0 && tur.get(tur.size()-1)!=null && tur.get(tur.size()-1).length>0)
//		{
//			System.out.println("Body: "+tur.get(tur.size()-1)[0]);
//		}
	}
	
	@Test
	public void test() {
		//"/edi/reference/"+"submitter"+"/{submitterId}/{SERVICE_NAME}"
		DEPServiceResponsesToCSVs t = new DEPServiceResponsesToCSVs(new String[]{"http://ipediuststweb01.northamerica.cerner.net/edi/dep"});
		assertTrue(t.success);
		String newCsv = CSVUtils.appendColumnToEveryRowInCsvString(
				CSVUtils.listToCsvString(CSVUtils.readCsvFile("submitter.csv")),
				"http://ipediwascrt01.northamerica.cerner.net/edi/reference/submitter/{0}/{1}"
				);
		newCsv = CSVUtils.appendColumnToEveryRowInCsvString(
				newCsv,
				"http://ipediwascrt01.northamerica.cerner.net/edi/reference/submitter/{0}/{1}"
				);
		
		CSVUtils.writeCsvFile(CSVUtils.csvStringToList(newCsv),"submitter.csv");
		List<String[]> l = CSVUtils.readCsvFile("submitter.csv");
		for(int i = 0;i<l.size(); i++)
		{
			String[] row = l.get(i);
			if(row!=null && row.length>1)
			{
				List<String[]> h1 = httpRequest(row[row.length-2], new ArrayList<String[]>(), "GET", "");
				String h1s = CSVUtils.listToCsvString(h1).trim();
				System.out.println("Got "+row[row.length-2]+", size="+h1s.length());
				
				List<String[]> h2 = httpRequest(row[row.length-1], new ArrayList<String[]>(), "GET", "");
				String h2s = CSVUtils.listToCsvString(h2).trim();
				System.out.println("Got "+row[row.length-1]+", size="+h2s.length());
				JSONArray ja = new JSONArray();
				ja.add(h1);
				ja.add(""+h1s.length());
				ja.add(h2);
				ja.add(""+h2s.length());
				
				assertTrue("[h1url,h2url,h1size,h2size]="+ja.toJSONString(),/*TODO: remove this ! */ h1s.equals(h2s));
				
			}
		}
	}

	/**
	 * 
	 * @param url
	 * @param headers
	 * @param method
	 * @param body
	 * @return [  [headername1, headervalue1], [headername2, headervalue2], [bodyValue,""] ]
	 */
	public static List<String[]> httpRequest(String url, ArrayList<String[]> headers, String method, String body)
	{
		if(method==null){method="GET";}
		if(body==null){body="";}
		if(headers==null){headers=new ArrayList<String[]>();}
		ArrayList<String[]> rtn = new ArrayList<String[]>();
		StringWriter rtnb = new StringWriter();
		PrintWriter pw = new PrintWriter(rtnb);
		
		try {
			URL url1 = new URL(url);
			HttpURLConnection conn = HttpURLConnection.class.cast(url1.openConnection());
			conn.setRequestMethod(method);
			conn.setInstanceFollowRedirects(true);
			for(String[] sa : headers){conn.setRequestProperty(sa[0], sa[1]);}
			conn.connect();
			//Object content = conn.getContent();
			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			List<String> lines = new ArrayList<String>();
			String line = ".";
			String lastLine = "";
			boolean headerdone = false;
			while(true)
			{
				lastLine = line;
				line = br.readLine();
				if(line==null){break;}
				else{if(lastLine!=null && lastLine.length()==0 && line.length()==0){headerdone = true;}}
				lines.add(line);
				pw.println(line);
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		rtn.add(new String[]{rtnb.toString(),""});
		
		return rtn;
	}
	public static String join(String[] args, String join){StringBuilder rtn = new StringBuilder();for(int i=0;i<args.length;i++){rtn.append(args[i]);if(i!=(args.length-1)){rtn.append(join);}}return rtn.toString();}
	
}
