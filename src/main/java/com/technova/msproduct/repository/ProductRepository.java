package com.technova.msproduct.repository;

import com.technova.msproduct.entity.Product;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends MongoRepository<Product, String> {
    Optional<List<Product>> findByVendorId(String vendorId);
    Optional<List<Product>> findByCompanyName(String productName);
}
