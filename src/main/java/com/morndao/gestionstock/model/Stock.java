package com.morndao.gestionstock.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "stocks")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false, unique = true)
    private Product product;

    @Min(0)
    @Column(nullable = false)
    private int quantity;

    @Min(0)
    @Column(nullable = false)
    private int minQuantity;

    @UpdateTimestamp
    private LocalDateTime lastUpdated;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StockStatus stockStatus;

    @PostLoad
    @PreUpdate
    void refreshStatus() {
        if (quantity == 0) {
            this.stockStatus = StockStatus.OUT_OF_STOCK;
        } else if (quantity <= minQuantity) {
            this.stockStatus = StockStatus.LOW;
        } else {
            this.stockStatus = StockStatus.AVAILABLE;
        }
    }

    public enum StockStatus {
        AVAILABLE, LOW, OUT_OF_STOCK
    }
}
