package com.react.back.cartapp.backendcartapp.model.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.react.back.cartapp.backendcartapp.model.dao.ProductRepository;
import com.react.back.cartapp.backendcartapp.model.entity.Product;

@Service
public class ProductServiceImpl implements ProductService{

    @Override
    @Transactional(readOnly = true)
    public List<Product> findAll() {
        return (List<Product>) productRepo.findAll();
    }

    @Autowired
    private ProductRepository productRepo;
}
