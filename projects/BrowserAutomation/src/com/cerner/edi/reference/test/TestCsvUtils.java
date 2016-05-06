package com.cerner.edi.reference.test;

import static org.junit.Assert.*;

import org.junit.Test;

public class TestCsvUtils {
	@Test
	public void testCsvUtils1()
	{
		String csv1 =      "\"a\",\"b\""
				+ "\r\n" + "\"a2\",\"b2\"";
		String templateString = "{0}__{1}";
		String expectedResult =      "\"a\",\"b\",\"a__b\""
				+ "\r\n" + "\"a2\",\"b2\",\"a2__b2\"";
		String actualResult = CSVUtils.appendColumnToEveryRowInCsvString(csv1, templateString);
		expectedResult = expectedResult.trim().replaceAll("\\r", "");
		actualResult = actualResult.trim().replaceAll("\\r", "");
		System.out.println(expectedResult+"\r\n---\r\n"+actualResult+"\r\n\r\n");
		assertEquals(expectedResult,actualResult);
	}
	@Test
	public void testCsvUtils2()
	{
		String csv1 =      "\"a\",\"b\""
				+ "\n" + "\"a2\",\"b2\"";
		String templateString = "{0}__{1}";
		String expectedResult =      "\"a\",\"b\",\"a__b\""
				+ "\n" + "\"a2\",\"b2\",\"a2__b2\"";
		String actualResult = CSVUtils.appendColumnToEveryRowInCsvString(csv1, templateString);
		expectedResult = expectedResult.trim().replaceAll("\\r", "");
		actualResult = actualResult.trim().replaceAll("\\r", "");
		System.out.println(expectedResult+"\r\n---\r\n"+actualResult+"\r\n\r\n");
		assertEquals(expectedResult,actualResult);
	}

}
