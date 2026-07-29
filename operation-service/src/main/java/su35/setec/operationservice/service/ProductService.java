package su35.setec.operationservice.service;

import java.util.List;
import java.util.UUID;
import su35.setec.operationservice.dto.request.ProductRequest;
import su35.setec.operationservice.dto.response.ProductResponse;

public interface ProductService {

    ProductResponse create(ProductRequest request);

    List<ProductResponse> findAll();

    ProductResponse findById(UUID id);

    ProductResponse update(UUID id, ProductRequest request);

    void delete(UUID id);
}
