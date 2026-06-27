package com.morndao.gestionstock.config;

import com.morndao.gestionstock.model.Product;
import com.morndao.gestionstock.model.User;
import com.morndao.gestionstock.service.ProductService;
import com.morndao.gestionstock.service.StockService;
import com.morndao.gestionstock.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final ProductService productService;
    private final StockService stockService;
    private final UserService userService;

    @Override
    public void run(String... args) {
        log.info("Initialisation des données de démonstration...");

        User admin = userService.create(User.builder()
                .username("admin")
                .email("admin@stock.fr")
                .password("admin123")
                .role(User.Role.ADMIN)
                .build());

        userService.create(User.builder()
                .username("manager")
                .email("manager@stock.fr")
                .password("manager123")
                .role(User.Role.MANAGER)
                .build());

        Product laptop = productService.create(Product.builder()
                .name("Laptop Pro 15")
                .description("Ordinateur portable haute performance")
                .price(new BigDecimal("1299.99"))
                .category("Informatique")
                .reference("LAP-001")
                .status(Product.Status.ACTIVE)
                .build());

        Product mouse = productService.create(Product.builder()
                .name("Souris sans fil")
                .description("Souris ergonomique Bluetooth")
                .price(new BigDecimal("29.90"))
                .category("Accessoires")
                .reference("SOU-001")
                .status(Product.Status.ACTIVE)
                .build());

        stockService.create(laptop.getId(), 50, 10);
        stockService.create(mouse.getId(), 3, 10);

        log.info("Données initialisées. {} produits, {} utilisateurs.", 2, 2);
    }
}
