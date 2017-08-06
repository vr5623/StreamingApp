package com.streaming.kafka;

import java.util.ArrayList;
import java.util.List;


public class Receipt {
    
	private int store_id;
	private int receipt_id;
	private int customer_id;
	
	private List<Item> items = new ArrayList<>();
    

    public Receipt(){

    }


	public Receipt(int store_id, int receipt_id, int customer_id,
			List<Item> items) {
		super();
		this.store_id = store_id;
		this.receipt_id = receipt_id;
		this.customer_id = customer_id;
		this.items = items;
	}


	public int getStore_id() {
		return store_id;
	}


	public void setStore_id(int store_id) {
		this.store_id = store_id;
	}


	public int getReceipt_id() {
		return receipt_id;
	}


	public void setReceipt_id(int receiptno) {
		this.receipt_id = receiptno;
	}


	public int getCustomer_id() {
		return customer_id;
	}


	public void setCustomer_id(int customer_id) {
		this.customer_id = customer_id;
	}


	public List<Item> getItems() {
		return items;
	}


	public void setItems(List<Item> items) {
		this.items = items;
	}
    
    
    

    
}
