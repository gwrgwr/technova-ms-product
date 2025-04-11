package com.technova.msproduct.mapper;

import com.technova.msproduct.entity.Product;
import com.technova.product.dto.ProductDTO;

public class ProductMapper {
    public static ProductDTO toProductDTO(Product product) {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(product.getId().toHexString());
        productDTO.setName(product.getName());
        productDTO.setDescription(product.getDescription());
        productDTO.setCategory(product.getCategory());
        productDTO.setImageURL(product.getImageURL());
        productDTO.setPrice(product.getPrice());
        productDTO.setVendorId(product.getVendorId());
        productDTO.setCompanyName(product.getCompanyName());
        return productDTO;
    }

    public static Product toProduct(ProductDTO productDTO) {
        Product product = new Product();
        product.setId(null);
        product.setName(productDTO.getName());
        product.setDescription(productDTO.getDescription());
        product.setCategory(productDTO.getCategory());
        product.setImageURL(productDTO.getImageURL());
        product.setPrice(productDTO.getPrice());
        product.setVendorId(productDTO.getVendorId());
        product.setCompanyName(productDTO.getCompanyName());
        return product;
    }
}
