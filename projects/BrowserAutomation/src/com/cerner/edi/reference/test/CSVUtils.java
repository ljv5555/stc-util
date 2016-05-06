package com.cerner.edi.reference.test;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import org.json.simple.JSONObject;

import java.util.Map;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

public class CSVUtils{
	
	public static void writeCsvFile(List<String[]> rows, String path) 
	{
		try{
			CSVWriter w = new CSVWriter(new BufferedWriter(new FileWriter(path)));
			w.writeAll(rows);
			w.close();
			System.out.println("Wrote file: "+(new File(path)).getAbsolutePath());
		}catch(Exception x){x.printStackTrace();} 
		
	}
	public static List<String[]> readCsvFile(String path)
	{
		List<String[]> rtn = new ArrayList<String[]>();
		try{
			CSVReader r = new CSVReader(new BufferedReader(new FileReader(path)));
			rtn.addAll(r.readAll());
			r.close();
			System.out.println("Wrote file: "+(new File(path)).getAbsolutePath());
		}catch(Exception x){x.printStackTrace();}
		return rtn;
	}
	public static String listToCsvString(List<String[]> rows) 
	{
		StringWriter sw = new StringWriter();
		try{
			CSVWriter w = new CSVWriter(new BufferedWriter(sw));
			w.writeAll(rows);
			w.close();
		}catch(Exception x){x.printStackTrace();} 
		return sw.toString();
	}
	public static List<String[]> csvStringToList(String csvdata)
	{
		List<String[]> rtn = new ArrayList<String[]>();
		try{
			CSVReader r = new CSVReader(new BufferedReader(new StringReader(csvdata)));
			rtn.addAll(r.readAll());
			r.close();
		}catch(Exception x){x.printStackTrace();}
		return rtn;
	}
	/**
	 * 
	 * @param csvdata
	 * @param columnValue
	 * @return
	 */
	public static String appendColumnToEveryRowInCsvString(String csvdata, String columnValue)
	{
		List<String[]> list = csvStringToList(csvdata);
		int ncolumns = 0;
		if(list.size()>0 && list.get(0)!=null){ncolumns = list.get(0).length;}
		List<String[]> rtnList = new LinkedList<String[]>();
		for(String[] row : list)
		{
			List<String> rowList = new LinkedList<String>();
			for(String s : row){rowList.add(s);}
			String tColumnValue = columnValue;
			for(int ic=0; ic<ncolumns;ic++)
			{
				tColumnValue=tColumnValue.replaceAll("\\{"+ic+"\\}", row[ic]);
			}
			rowList.add(tColumnValue);
			rtnList.add(rowList.toArray(new String[]{}));
		}
		return listToCsvString(rtnList);
	}
	// HTTP POST request
	/**
	 * @return [HTTP code as string, header1name,header1value...headerNname,headerNvalue,bodyContent];
	 */
	public static String[] sendPost(String url, Map<String,List<String>> requestHeaders, String bodyContent) {

		List<String> rtn = new LinkedList<String>();
		try{
		URL obj = new URL(url);
		HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

		//add reuqest header
		con.setRequestMethod("POST");
		con.setRequestProperty("User-Agent", "Mozilla/5.0");
		con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

		
		
		// Send post request
		con.setDoOutput(true);
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.writeBytes(bodyContent);
		wr.flush();
		wr.close();

		int responseCode = con.getResponseCode();
		rtn.add(""+responseCode);
		BufferedReader in = new BufferedReader(
		        new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
		for(String s : con.getHeaderFields().keySet()){for(String headerKey : con.getHeaderFields().get(s)){for(int i=0;i<con.getHeaderFields().get(headerKey).size();i++){con.setRequestProperty(headerKey,con.getHeaderFields().get(headerKey).get(i)); }}}
		
			
		}catch(Exception ex){ex.printStackTrace();}
		return rtn.toArray(new String[]{});

	}
}