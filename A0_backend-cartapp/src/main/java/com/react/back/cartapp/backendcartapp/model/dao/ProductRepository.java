package com.react.back.cartapp.backendcartapp.model.dao;

import org.springframework.data.repository.CrudRepository;
import com.react.back.cartapp.backendcartapp.model.entity.Product;

public interface ProductRepository extends CrudRepository <Product, Long>{

}
