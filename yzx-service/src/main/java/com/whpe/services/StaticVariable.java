package com.whpe.services;

public class StaticVariable {

	private static int orderSeq = 1;
	
	public static synchronized String getOrderSeq() {
		
		int temp = orderSeq;
		orderSeq = orderSeq + 1;
		
		StringBuffer orderSeqStr = new StringBuffer(temp +"");
		while(orderSeqStr.length() < 15)
		{
			orderSeqStr = orderSeqStr.insert(0, "0");
		}
		
		return orderSeqStr.toString();
	}

}
