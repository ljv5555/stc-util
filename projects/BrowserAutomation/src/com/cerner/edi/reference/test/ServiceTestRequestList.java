package com.cerner.edi.reference.test;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ServiceTestRequestList {

	

	public List<String[]> getTestRequestsForService(String serviceName, String baseUrl)
	{
		List<String[]> rtn = new ArrayList<String[]>(); // each entry: { urlString, bodyString, headerString1..n }
		String[] rowTemplate = new String[]{ "/edi/reference/"+serviceName+"/{submitterId}/{SERVICE_NAME}", ""};
		File csvfile = new File(serviceName+".csv");
		List<String[]> csvRows = CSVUtils.readCsvFile(csvfile.getAbsolutePath());
		for(String[] csvrow : csvRows)
		{
			String[] row = new String[2];
			row[1] = "";
			row[0] = baseUrl + rowTemplate[0].replace("\\{submitterId\\}", csvrow[0]).replace("\\{SERVICE_NAME\\}", csvrow[1]);
			rtn.add(row);
		}
		
		
		return rtn;
	}
}
