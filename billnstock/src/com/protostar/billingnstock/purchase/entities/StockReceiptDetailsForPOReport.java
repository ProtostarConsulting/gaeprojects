package com.protostar.billingnstock.purchase.entities;

import java.util.ArrayList;
import java.util.List;

public class StockReceiptDetailsForPOReport {

	private List<Integer> receiptNumbers = new ArrayList<Integer>();
	private List<Integer> stockReceiptProductQts = new ArrayList<Integer>();

	public List<Integer> getReceiptNumbers() {
		return receiptNumbers;
	}

	public void setReceiptNumbers(List<Integer> receiptNumbers) {
		this.receiptNumbers = receiptNumbers;
	}

	public List<Integer> getStockReceiptProductQts() {
		return stockReceiptProductQts;
	}

	public void setStockReceiptProductQts(List<Integer> stockReceiptProductQts) {
		this.stockReceiptProductQts = stockReceiptProductQts;
	}

}
