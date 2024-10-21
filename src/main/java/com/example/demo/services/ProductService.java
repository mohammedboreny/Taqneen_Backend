package com.example.demo.services;

import com.example.demo.dao.ProductDao;
import com.example.demo.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    @Autowired
    ProductDao productDao;

    public List<Product> getAllProduct() {
        return productDao.findAll();
    }
    @Cacheable(value = "products", key = "#id")
    public Product getProductById(Long id) {
        return productDao.findById(id).orElse(null);
    }

    public Product saveProduct(Product product) {
        return productDao.save(product);
    }

    @CachePut(value = "products", key = "#id")
    public Product updateProduct(long id,Product product) throws Exception {
        Product existProduct=productDao.findById(id).orElseThrow(() -> new Exception("Product not found with id: " + id));
        existProduct.setName(product.getName());
        existProduct.setDescription(product.getDescription());
        existProduct.setPrice(product.getPrice());
        return productDao.save(existProduct);
    }

    @CacheEvict(value = "products", key = "#id")
    public boolean deleteProduct(Long id) {
        Optional<Product> product = productDao.findById(id);
        if (product.isPresent()) {
            productDao.deleteById(id);
            return true;  // Product was deleted successfully
        } else {
            return false;  // Product was not found
        }
    }

}
