package com.morndao.gestionstock.service;

import com.morndao.gestionstock.model.Product;
import com.morndao.gestionstock.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {

    private final ProductRepository productRepository;

    public List<Product> findAll() {
        return productRepository.findAll();
    }

    public Product findById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produit introuvable avec l'id : " + id));
    }

    public List<Product> findByCategory(String category) {
        return productRepository.findByCategory(category);
    }

    @Transactional
    public Product create(Product product) {
        if (product.getReference() != null && productRepository.existsByReference(product.getReference())) {
            throw new RuntimeException("Référence déjà utilisée : " + product.getReference());
        }
        if (product.getStatus() == null) {
            product.setStatus(Product.Status.ACTIVE);
        }
        return productRepository.save(product);
    }

    @Transactional
    public Product update(Long id, Product updated) {
        Product existing = findById(id);
        existing.setName(updated.getName());
        existing.setDescription(updated.getDescription());
        existing.setPrice(updated.getPrice());
        existing.setCategory(updated.getCategory());
        existing.setStatus(updated.getStatus());
        return productRepository.save(existing);
    }

    @Transactional
    public void delete(Long id) {
        findById(id);
        productRepository.deleteById(id);
    }
}
