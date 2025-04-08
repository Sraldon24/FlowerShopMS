package com.champsoft.services.inventory.DataLayer.Flowers;

import com.champsoft.services.inventory.DataLayer.Inventory.InventoryIdentifier;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "flowers")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Flower {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "flowerId", column = @Column(name = "flower_identifier"))
    })
    private FlowersIdentifier flowersIdentifier;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "inventoryId", column = @Column(name = "inventory_id"))
    })
    private InventoryIdentifier inventoryIdentifier;

    @Column(name = "flower_name", nullable = false)
    private String flowerName;

    @Column(name = "flower_color")
    private String flowerColor;

    @Column(name = "flower_category")
    private String flowerCategory;

    @Enumerated(EnumType.STRING)
    @Column(name = "flower_status")
    private Status status;

    @Column(name = "stock_quantity")
    private Integer stockQuantity;

    @Column(name = "supplier_id", nullable = false)
    private String supplierIdentifier;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "flower_options", joinColumns = @JoinColumn(name = "flower_id"))
    @AttributeOverrides({
            @AttributeOverride(name = "name", column = @Column(name = "option_name")),
            @AttributeOverride(name = "description", column = @Column(name = "option_description")),
            @AttributeOverride(name = "price", column = @Column(name = "option_price"))
    })
    private List<Option> options;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "amount", column = @Column(name = "price_amount")),
            @AttributeOverride(name = "currency", column = @Column(name = "price_currency"))
    })
    private Price price;

    @PrePersist
    public void ensureFlowerNumberExists() {
        if (this.flowersIdentifier == null) {
            this.flowersIdentifier = new FlowersIdentifier();
        }
        this.flowersIdentifier.generateIfMissing();
    }
}
