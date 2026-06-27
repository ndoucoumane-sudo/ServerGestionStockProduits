package com.morndao.gestionstock.service;

import com.morndao.gestionstock.model.Product;
import com.morndao.gestionstock.model.Stock;
import com.morndao.gestionstock.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StockService {

    private final StockRepository stockRepository;
    private final ProductService productService;

    public List<Stock> findAll() {
        return stockRepository.findAll();
    }

    public Stock findById(Long id) {
        return stockRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Stock introuvable avec l'id : " + id));
    }

    public Stock findByProduct(Long productId) {
        return stockRepository.findByProductId(productId)
                .orElseThrow(() -> new RuntimeException("Aucun stock pour le produit id : " + productId));
    }

    public List<Stock> findLowStock() {
        return stockRepository.findByStockStatus(Stock.StockStatus.LOW);
    }

    public List<Stock> findOutOfStock() {
        return stockRepository.findByStockStatus(Stock.StockStatus.OUT_OF_STOCK);
    }

    @Transactional
    public Stock create(Long productId, int quantity, int minQuantity) {
        Product product = productService.findById(productId);
        if (stockRepository.findByProductId(productId).isPresent()) {
            throw new RuntimeException("Un stock existe déjà pour ce produit");
        }
        Stock stock = Stock.builder()
                .product(product)
                .quantity(quantity)
                .minQuantity(minQuantity)
                .stockStatus(Stock.StockStatus.AVAILABLE)
                .build();
        stock.refreshStatus();
        return stockRepository.save(stock);
    }

    @Transactional
    public Stock addQuantity(Long productId, int quantity) {
        Stock stock = findByProduct(productId);
        stock.setQuantity(stock.getQuantity() + quantity);
        stock.refreshStatus();
        return stockRepository.save(stock);
    }

    @Transactional
    public Stock removeQuantity(Long productId, int quantity) {
        Stock stock = findByProduct(productId);
        if (stock.getQuantity() < quantity) {
            throw new RuntimeException("Stock insuffisant. Disponible : " + stock.getQuantity());
        }
        stock.setQuantity(stock.getQuantity() - quantity);
        stock.refreshStatus();
        return stockRepository.save(stock);
    }
}
