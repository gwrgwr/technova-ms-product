package com.technova.msproduct.service;

import com.technova.Result;
import com.technova.exceptions.BaseException;
import com.technova.msproduct.entity.Product;
import com.technova.msproduct.repository.ProductRepository;
import com.technova.product.constants.RabbitProductConstants;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    @RabbitListener(queues = RabbitProductConstants.PRODUCT_GET_ID_QUEUE)
    public Result<Product> findById(String id) {
        Product product = productRepository.findById(id).orElse(null);
        if (product != null) {
            return Result.success(product);
        }
        return Result.error(new BaseException("Product not found"));
    }

    @RabbitListener(queues = RabbitProductConstants.PRODUCT_GET_COMPANY_NAME_QUEUE)
    public Result<List<Product>> findAll(String companyName) {
        List<Product> list = productRepository.findByCompanyName(companyName).orElse(null);
        return Result.success(Objects.requireNonNullElseGet(list, List::of));
    }

    @RabbitListener(queues = RabbitProductConstants.PRODUCT_GET_ID_QUEUE)
    public Result<List<Product>> findByVendorId(String vendorId) {
        List<Product> list = productRepository.findByVendorId(vendorId).orElse(null);
        return Result.success(Objects.requireNonNullElseGet(list, List::of));
    }

    @RabbitListener(queues = RabbitProductConstants.PRODUCT_SAVE_QUEUE)
    public Result<Product> save(Product product) {
        Product product1 = productRepository.save(product);
        if (product1 != null) {
            return Result.success(product1);
        }
        return Result.error(new BaseException("Product not saved"));
    }

    @RabbitListener(queues = RabbitProductConstants.PRODUCT_UPDATE_QUEUE)
    public Result<Product> update(Product product) {
        Product product1 = productRepository.save(product);
        if (product1 != null) {
            return Result.success(product1);
        }
        return Result.error(new BaseException("Product not updated"));
    }

    @RabbitListener(queues = RabbitProductConstants.PRODUCT_DELETE_QUEUE)
    public void delete(String id) {
        productRepository.deleteById(id);
    }
}
