package su35.setec.operationservice.service.impl;

import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import su35.setec.operationservice.dto.request.ProductRequest;
import su35.setec.operationservice.dto.response.ProductResponse;
import su35.setec.operationservice.entity.Product;
import su35.setec.operationservice.exception.BadRequestException;
import su35.setec.operationservice.exception.NotFoundException;
import su35.setec.operationservice.mapper.ProductMapper;
import su35.setec.operationservice.repository.ProductRepository;
import su35.setec.operationservice.service.ProductService;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @Override
    @Transactional
    public ProductResponse create(ProductRequest request) {
        if (productRepository.existsBySku(request.getSku())) {
            throw new BadRequestException("SKU already exists");
        }
        if (productRepository.existsByName(request.getName())) {
            throw new BadRequestException("Product name already exists");
        }

        Product product = Product.builder()
                .name(request.getName())
                .sku(request.getSku())
                .description(request.getDescription())
                .unitPrice(request.getUnitPrice())
                .quantityInStock(request.getQuantityInStock())
                .active(request.isActive())
                .build();
        return productMapper.toResponse(productRepository.save(product));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponse> findAll() {
        return productRepository.findAll().stream().map(productMapper::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ProductResponse findById(UUID id) {
        return productMapper.toResponse(productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Product not found: " + id)));
    }

    @Override
    @Transactional
    public ProductResponse update(UUID id, ProductRequest request) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Product not found: " + id));

        if (!product.getSku().equals(request.getSku()) && productRepository.existsBySkuAndIdNot(request.getSku(), id)) {
            throw new BadRequestException("SKU already exists");
        }
        if (!product.getName().equals(request.getName()) && productRepository.existsByNameAndIdNot(request.getName(), id)) {
            throw new BadRequestException("Product name already exists");
        }

        product.setName(request.getName());
        product.setSku(request.getSku());
        product.setDescription(request.getDescription());
        product.setUnitPrice(request.getUnitPrice());
        product.setQuantityInStock(request.getQuantityInStock());
        product.setActive(request.isActive());
        return productMapper.toResponse(productRepository.save(product));
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        if (!productRepository.existsById(id)) {
            throw new NotFoundException("Product not found: " + id);
        }
        productRepository.deleteById(id);
    }
}
