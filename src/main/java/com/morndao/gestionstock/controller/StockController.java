package com.morndao.gestionstock.controller;

import com.morndao.gestionstock.model.Stock;
import com.morndao.gestionstock.service.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/stocks")
@RequiredArgsConstructor
public class StockController {

    private final StockService stockService;

    @GetMapping
    public ResponseEntity<List<Stock>> getAll() {
        return ResponseEntity.ok(stockService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Stock> getById(@PathVariable Long id) {
        return ResponseEntity.ok(stockService.findById(id));
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<Stock> getByProduct(@PathVariable Long productId) {
        return ResponseEntity.ok(stockService.findByProduct(productId));
    }

    @GetMapping("/low")
    public ResponseEntity<List<Stock>> getLowStock() {
        return ResponseEntity.ok(stockService.findLowStock());
    }

    @GetMapping("/out-of-stock")
    public ResponseEntity<List<Stock>> getOutOfStock() {
        return ResponseEntity.ok(stockService.findOutOfStock());
    }

    @PostMapping("/product/{productId}")
    public ResponseEntity<Stock> create(
            @PathVariable Long productId,
            @RequestBody Map<String, Integer> body) {
        int quantity = body.getOrDefault("quantity", 0);
        int minQuantity = body.getOrDefault("minQuantity", 5);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(stockService.create(productId, quantity, minQuantity));
    }

    @PatchMapping("/product/{productId}/add")
    public ResponseEntity<Stock> addQuantity(
            @PathVariable Long productId,
            @RequestBody Map<String, Integer> body) {
        return ResponseEntity.ok(stockService.addQuantity(productId, body.get("quantity")));
    }

    @PatchMapping("/product/{productId}/remove")
    public ResponseEntity<Stock> removeQuantity(
            @PathVariable Long productId,
            @RequestBody Map<String, Integer> body) {
        return ResponseEntity.ok(stockService.removeQuantity(productId, body.get("quantity")));
    }
}
