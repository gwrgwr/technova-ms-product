package com.technova.msproduct.repository;

import com.technova.msproduct.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, String> {
    Optional<List<Product>> findByVendorId(String vendorId);
    Optional<List<Product>> findByCompanyName(String productName);
}
