package cart.service;

import cart.controller.request.ProductRequestDto;
import cart.controller.response.ProductResponseDto;
import cart.dao.ProductDao;
import cart.domain.Product;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    private final ProductDao productDao;

    public ProductService(ProductDao productDao) {
        this.productDao = productDao;
    }

    public List<ProductResponseDto> getAllProducts() {
        List<Product> products = productDao.getAllProducts();
        return products.stream().map(ProductResponseDto::from).collect(Collectors.toList());
    }
    public List<ProductResponseDto> getAllProductsPagination(final Long limit, final Long scrollCount) {
        return productDao.getAllProductsPagination(limit, scrollCount)
                .stream()
                .map(ProductResponseDto::from)
                .collect(Collectors.toList());
    }

    public ProductResponseDto getProductById(Long productId) {
        Product product = productDao.getProductById(productId);
        return ProductResponseDto.from(product);
    }

    public Long createProduct(ProductRequestDto productRequest) {
        Product product = new Product(productRequest.getName(), productRequest.getPrice(), productRequest.getImageUrl());
        return productDao.createProduct(product);
    }

    public void updateProduct(Long productId, ProductRequestDto productRequest) {
        Product product = new Product(productRequest.getName(), productRequest.getPrice(), productRequest.getImageUrl());
        productDao.updateProduct(productId, product);
    }

    public void deleteProduct(Long productId) {
        productDao.deleteProduct(productId);
    }

}