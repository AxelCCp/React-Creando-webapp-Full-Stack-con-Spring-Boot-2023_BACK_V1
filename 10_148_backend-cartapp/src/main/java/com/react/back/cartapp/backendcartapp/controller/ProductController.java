package com.react.back.cartapp.backendcartapp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.react.back.cartapp.backendcartapp.model.entity.Product;
import com.react.back.cartapp.backendcartapp.model.service.ProductService;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
public class ProductController {

    @GetMapping("/products")
    List<Product>list(){
        return productService.findAll();
    }

    @Autowired
    private ProductService productService;

}
