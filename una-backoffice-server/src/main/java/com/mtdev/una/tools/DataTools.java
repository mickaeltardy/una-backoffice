package com.mtdev.una.tools;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;

public class DataTools {

	public static String formatDate(Date pDate){
		
		SimpleDateFormat lSdf = new SimpleDateFormat("dd/MM/yyyy");
		
		if(pDate != null){
			
			return lSdf.format(pDate);
			
		}
		
		return "";
	}
	
	public static String getLabelFromMap(Map<Object, Object> pMap, String pCode) {
		String lResult = "";
		if (pMap != null && pMap.size() > 0 && pCode != null) {
			for (Entry<Object, Object> pItem : pMap.entrySet()) {
				if (((String) pItem.getKey()).compareTo(pCode) == 0) {
					lResult = (String) pItem.getValue();
				}

			}
		}
		return lResult;
	}

}
