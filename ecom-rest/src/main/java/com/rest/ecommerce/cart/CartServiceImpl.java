package com.rest.ecommerce.cart;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rest.ecommerce.cache.Cache;
import com.rest.ecommerce.models.Order;
import com.rest.ecommerce.models.OrderItem;
import com.rest.ecommerce.models.Product;
import com.rest.ecommerce.services.EcommerceService;

import java.util.*;

@Service
public class CartServiceImpl implements CartService{

    @Autowired
    private EcommerceService ecommerceService;

    @Autowired
    private Cache cache;

    @Override
    public String createNewCart() {
        return UUID.randomUUID().toString();
    }

    @Override
    public void addProduct(String cartId, CartItem cartItem) {
        cache.addItemToList(cartId, cartItem);
    }

    @Override
    public void removeProduct(String cartId, String productId) {
        CartItem removeItem = new CartItem();

        removeItem.setProductId(Long.parseLong(productId));

        cache.removeItemFromList(cartId, removeItem);
    }

    @Override
    public void setProductQuantity(String cartId, String productId, int quantity){

        List<CartItem> list = (List) cache.getList(cartId, CartItem.class);
        list.forEach( item -> {
            try{
                if(item.getProductId() == Long.parseLong(productId) ){
                    item.setQuantity( quantity );
                    cache.removeItemFromList(cartId, item);
                    cache.addItemToList(cartId, item);
                }
            } catch (Exception e){
                System.out.println(e.getMessage());
            }
        } );
    }

    @Override
    public Set<CartItem> getItems(String cartId){
        return new HashSet<CartItem>( (List) cache.getList(cartId, CartItem.class));
    }

    @Override
    public Order createOrder(String cartId, Order order) {
        List<CartItem> cartItems = (List)cache.getList(cartId, CartItem.class);

        order = addCartItemsToOrders(cartItems, order);
        if( order == null){
            System.out.println("Order not set.");
        }
        order = ecommerceService.saveOrder(order);

        cache.removeItem(cartId);

        return order;
    }

    private Order addCartItemsToOrders(List<CartItem> cartItems, Order order){

        cartItems.forEach(cartItem -> {

            Product prod = ecommerceService.getProduct(cartItem.getProductId());
            int qty = cartItem.getQuantity() > 0 ? cartItem.getQuantity() : 1;

            for(int i = 0; i < qty; i++) {
                OrderItem orderItem = new OrderItem();
                orderItem.setProduct(prod);
                orderItem.setOrder(order);
                order.getItems().add(orderItem);
            }

        } );

        return order;
    }

    private String generateCartRedisId(String cartId){
        return "cart#"+cartId+"#items";
    }
}
