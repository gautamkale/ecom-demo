package com.rest.ecommerce.controllers;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rest.ecommerce.hateoas.ProductResource;
import com.rest.ecommerce.models.Product;
import com.rest.ecommerce.services.EcommerceService;

@RestController
@RequestMapping("/product")
public class ProductController extends CoreController{

    @Autowired
    private EcommerceService ecommerceService;


    @GetMapping
    public ResponseEntity<List<ProductResource>> index() {
        List<Product> res = ecommerceService.getProducts();
        List<ProductResource> output = new ArrayList<ProductResource>();
        res.forEach((p)->{
            ProductResource pr = new ProductResource(p);
            pr.add(createHateoasLink(p.getId()));

            output.add(pr);
        });
        return new ResponseEntity<>(output,HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping
    public ResponseEntity<Product> create(@RequestBody @Valid Product product){
        return new ResponseEntity<>(ecommerceService.saveProduct(product),HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResourceSupport> view(@PathVariable("id") long id){
        Product p = ecommerceService.getProduct(id);

        ProductResource pr = new ProductResource(p);
        pr.add(createHateoasLink(p.getId()));

        return new ResponseEntity<>(pr,HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping(value = "/{id}")
    public ResponseEntity<Product> edit(@PathVariable("id") long id, @RequestBody @Valid Product product){

        Product updatedProduct = ecommerceService.getProduct(id);

        if(updatedProduct == null){
            return null;
        }

        updatedProduct.setName(product.getName());
        updatedProduct.setPrice(product.getPrice());
        updatedProduct.setDescription(product.getDescription());

        return new ResponseEntity<>(ecommerceService.saveProduct(updatedProduct),HttpStatus.OK);
    }



}