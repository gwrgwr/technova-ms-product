package com.technova.msproduct.service;

import com.technova.Result;
import com.technova.exceptions.BaseException;
import com.technova.msproduct.entity.Product;
import com.technova.msproduct.mapper.ProductMapper;
import com.technova.msproduct.repository.ProductRepository;
import com.technova.product.constants.RabbitProductConstants;
import com.technova.product.dto.ProductDTO;
import com.technova.product.enums.ProductStatus;
import com.technova.vendor.constants.RabbitVendorConstants;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final RabbitTemplate rabbitTemplate;

    public ProductService(ProductRepository productRepository, RabbitTemplate rabbitTemplate) {
        this.productRepository = productRepository;
        this.rabbitTemplate = rabbitTemplate;
    }

    @RabbitListener(queues = RabbitProductConstants.PRODUCT_GET_ID_QUEUE)
    @Transactional(readOnly = true)
    public Result<Product> findById(String id) {
        Product product = productRepository.findById(id).orElse(null);
        if (product != null) {
            return Result.success(product);
        }
        return Result.error(new BaseException("Product not found"));
    }

    @RabbitListener(queues = RabbitProductConstants.PRODUCT_GET_COMPANY_NAME_QUEUE)
    @Transactional(readOnly = true)
    public Result<List<Product>> findAll(String companyName) {
        List<Product> list = productRepository.findByCompanyName(companyName).orElse(null);
        return Result.success(Objects.requireNonNullElseGet(list, List::of));
    }

    @RabbitListener(queues = RabbitProductConstants.PRODUCT_GET_ID_QUEUE)
    @Transactional(readOnly = true)
    public Result<List<Product>> findByVendorId(String vendorId) {
        List<Product> list = productRepository.findByVendorId(vendorId).orElse(null);
        return Result.success(Objects.requireNonNullElseGet(list, List::of));
    }

    @RabbitListener(queues = RabbitProductConstants.PRODUCT_SAVE_QUEUE)
    public Result<ProductDTO> save(ProductDTO product) {
        this.rabbitTemplate.convertSendAndReceive(RabbitVendorConstants.VENDOR_EXCHANGE, RabbitVendorConstants.VENDOR_FIND_BY_ID_REQUEST_ROUTING_KEY, product.getVendorId());
        return Result.success(ProductMapper.toProductDTO(productRepository.save(ProductMapper.toProduct(product))));
    }

    @RabbitListener(queues = RabbitProductConstants.PRODUCT_UPDATE_QUEUE)
    public Result<Product> update(Product product) {
        Product product1 = productRepository.save(product);
        return Result.success(product1);
    }

    @RabbitListener(queues = RabbitProductConstants.PRODUCT_DELETE_QUEUE)
    public void delete(String id) {
        Result<Product> productResult = findById(id);
        if (productResult.isHasError()) {
            throw new BaseException("Product not found for deletion");
        }
        Product product = productResult.getData();
        product.setStatus(ProductStatus.UNAVAILABLE);
        productRepository.save(product);
    }

}
