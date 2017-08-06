package com.streaming.kafka;

public class Item {
	
	private int item_id;
	private int quantity;
	private float total_price_paid;
	
	
	
	public Item(int item_id, int quantity, float total_price_paid) {
		this.item_id = item_id;
		this.quantity = quantity;
		this.total_price_paid = total_price_paid;
	}
	
	
	public Item() {
	}


	public int getItem_id() {
		return item_id;
	}
	public void setItem_id(int item_id) {
		this.item_id = item_id;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public float getTotal_price_paid() {
		return total_price_paid;
	}
	public void setTotal_price_paid(float total_price_paid) {
		this.total_price_paid = total_price_paid;
	}


}
