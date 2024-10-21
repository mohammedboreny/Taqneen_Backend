package com.example.demo.controller;

import com.example.demo.entity.Product;
import com.example.demo.exceptions.InvalidProductException;
import com.example.demo.exceptions.ProductNotFoundException;
import com.example.demo.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping
    public ResponseEntity<List<Product>> getProducts() {
        List<Product> products = productService.getAllProduct();
        if (products.isEmpty()) {
            throw new ProductNotFoundException("No products found");
        }
        return ResponseEntity.ok(products);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProduct(@PathVariable Long id) {
        Product product = productService.getProductById(id);
        if (product == null) {
            throw new ProductNotFoundException("Product not found with id: " + id);
        }
        return ResponseEntity.ok(product);
    }

    @PostMapping
    public ResponseEntity<Product> addProduct(@RequestBody Product product) {

        if (product == null || product.getName() == null || product.getPrice() <= 0) {
            throw new InvalidProductException("Invalid product details provided");
        }
        Product newProduct = productService.saveProduct(product);
        return ResponseEntity.ok(newProduct);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable long id, @RequestBody Product product) throws Exception {
        Product updatedProduct = productService.updateProduct(id, product);
        if (updatedProduct == null) {
            throw new ProductNotFoundException("Unable to update. Product not found with id: " + id);
        }
        return ResponseEntity.ok(updatedProduct);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long id) {
        boolean isDeleted = productService.deleteProduct(id);
        if (isDeleted) {
            return ResponseEntity.ok("Product deleted successfully");
        } else {
            throw new ProductNotFoundException("Product not found with id: " + id);
        }
    }
}
