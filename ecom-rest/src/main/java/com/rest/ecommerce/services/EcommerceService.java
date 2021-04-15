package com.rest.ecommerce.services;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rest.ecommerce.models.Order;
import com.rest.ecommerce.models.Product;
import com.rest.ecommerce.repositories.OrderRepository;
import com.rest.ecommerce.repositories.ProductRepository;

import java.util.List;

@Service
public class EcommerceService {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    OrderRepository orderRepository;


    /* PRODUCT */
    public List<Product> getProducts(){
        return productRepository.findAll();
    }
    public Product getProduct(long id){
        return productRepository.findOne(id);
    }
    public Product saveProduct(Product product){
        return productRepository.save(product);
    }

    /* ORDERS */
    public List<Order> getOrders(){
        return orderRepository.findAll();
    }
    public Order getOrder(long id){
        return orderRepository.findOne(id);
    }
    public Order saveOrder(Order order){
        return orderRepository.save(order);
    }
}
