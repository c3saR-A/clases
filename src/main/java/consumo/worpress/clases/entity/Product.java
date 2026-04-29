package consumo.worpress.clases.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "products")
@Getter
@Setter
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(precision = 10, scale = 2)
    private BigDecimal price;

    @Column(unique = true) // SKU único
    private String sku;

    // Un producto tiene muchos logs
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<IntegrationLog> logs = new ArrayList<>();

}