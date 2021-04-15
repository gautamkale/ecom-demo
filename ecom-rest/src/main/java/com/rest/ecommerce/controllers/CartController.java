package com.rest.ecommerce.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import com.rest.ecommerce.cart.CartItem;
import com.rest.ecommerce.cart.CartService;
import com.rest.ecommerce.hateoas.OrderResource;
import com.rest.ecommerce.models.Order;

import javax.validation.Valid;
import org.springframework.validation.Validator;
import java.util.Set;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;


@RestController
@RequestMapping("/cart")
public class CartController extends CoreController{

    @Autowired
    CartService cartService;

    @PostMapping("/")
    public ResponseEntity<String> create(){
        return new ResponseEntity<>(cartService.createNewCart(),HttpStatus.OK);
    }

    @PostMapping("/{id}")
    public ResponseEntity<HttpStatus> addProduct(@PathVariable("id") String cartId, @RequestBody CartItem cartItem){
        cartService.addProduct(cartId, cartItem);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Set<CartItem>>  getCartItems(@PathVariable("id") String cartId){
        return new ResponseEntity<>(cartService.getItems(cartId),HttpStatus.OK);
    }

    @DeleteMapping("{id}/{product_id}")
    public ResponseEntity<HttpStatus>  removeItem(@PathVariable("id") String cartId, @PathVariable("product_id") String productId){
        cartService.removeProduct(cartId,productId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("{id}/quantity")
    public ResponseEntity<HttpStatus> setProductQuantity(@PathVariable("id") String cartId,@RequestBody CartItem cartItem){
        String productId = Long.toString(cartItem.getProductId());
        cartService.setProductQuantity(cartId, productId, cartItem.getQuantity());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("{id}/order")
    public ResponseEntity<OrderResource> createOrder(@PathVariable("id") String cartId, @RequestBody @Valid Order order){
        if(order == null){
            System.out.println("Order not in POST");
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        OrderResource orderResource = new OrderResource(
            cartService.createOrder(cartId, order)
        );
        Link link = linkTo(OrderController.class).slash(order.getId()).withSelfRel();
        orderResource.add(link);

        if(orderResource.id < 1){
            System.out.println("Resource not created");
            return null;
        }
        return new ResponseEntity<>(orderResource,HttpStatus.OK);

    }

}
