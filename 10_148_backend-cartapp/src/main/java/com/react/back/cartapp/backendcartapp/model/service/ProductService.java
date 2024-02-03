package com.react.back.cartapp.backendcartapp.model.service;

import java.util.List;

import com.react.back.cartapp.backendcartapp.model.entity.Product;

public interface ProductService {

    List<Product>findAll();
    
}
