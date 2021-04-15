package com.rest.ecommerce.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.rest.ecommerce.models.Product;

import javax.persistence.*;

@Entity
@Table(name = "order_items")
public class OrderItem {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String price;

	@ManyToOne
	@JoinColumn(name = "order_id")
	@JsonBackReference
	private Order order;
	
	@ManyToOne
	@JoinColumn(name = "product_id")
	private Product product;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

}
