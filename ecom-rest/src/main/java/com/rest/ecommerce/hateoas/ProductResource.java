package com.rest.ecommerce.hateoas;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.rest.ecommerce.models.Product;

import org.springframework.hateoas.ResourceSupport;

public class ProductResource extends ResourceSupport {

    @JsonProperty
    public long id;
    public String name;
    public String price;
    public String description;
    public Object group;

    public ProductResource(Product model){
        id = model.getId();
        name = model.getName();
        price = model.getPrice();
        description = model.getDescription();
    }

}
