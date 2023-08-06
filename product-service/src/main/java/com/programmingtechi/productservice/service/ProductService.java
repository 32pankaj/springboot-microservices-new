package com.programmingtechi.productservice.service;

import com.programmingtechi.productservice.dto.ProductRequest;
import com.programmingtechi.productservice.dto.ProductResponse;
import com.programmingtechi.productservice.model.Product;
import com.programmingtechi.productservice.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

@Service
@Slf4j
public class ProductService {


    @Autowired
    private  ProductRepository productRepository;

    public void createProduct(ProductRequest productRequest){
        Product product=Product.builder().
                name(productRequest.getName()).
                description(productRequest.getDescription()).
                price(productRequest.getPrice()).build();

        productRepository.save(product);

        log.info("Product  {} is save ",product.getId());

    }

    public List<ProductResponse> getAllProducts() {

        List<Product> products = productRepository.findAll();
     return    products.stream().map(this::mapToProductResponse).toList();
    }

    private ProductResponse mapToProductResponse(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice()).build();
    }
}
