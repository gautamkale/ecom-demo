package com.rest.ecommerce.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Validator;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import com.rest.ecommerce.hateoas.OrderResource;
import com.rest.ecommerce.models.Order;
import com.rest.ecommerce.services.EcommerceService;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/order")
public class OrderController extends CoreController {

    @Autowired
    private EcommerceService ecommerceService;

    @GetMapping
    public ResponseEntity<List<OrderResource>> index() {
        List<Order> orders = ecommerceService.getOrders();
        List<OrderResource> out = new ArrayList<OrderResource>();
        if(orders != null){
            orders.forEach(o -> {
                OrderResource orderResource = new OrderResource(o);
                orderResource.add(createHateoasLink(o.getId()));

                out.add(orderResource);
            });
        }
        return new ResponseEntity<>(out,HttpStatus.OK); 
    }

    @PostMapping
    public ResponseEntity<Order>  create(@RequestBody @Valid Order order){
        if(order.getItems() !=null){
            order.getItems().forEach(item -> item.setOrder(order));
        }
        return new ResponseEntity<>(ecommerceService.saveOrder(order),HttpStatus.OK); 
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResource> view(@PathVariable("id") long id){
        OrderResource orderResource = new OrderResource(ecommerceService.getOrder(id));
        orderResource.add(createHateoasLink(id));
        return new ResponseEntity<>(orderResource,HttpStatus.OK);
    }

    @PostMapping(value = "/{id}")
    public ResponseEntity<Order> edit(@PathVariable("id") long id, @RequestBody @Valid Order order){

        Order updatedOrder = ecommerceService.getOrder(id);

        if(updatedOrder== null){
        	new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(ecommerceService.saveOrder(updatedOrder),HttpStatus.OK);
    }
}
