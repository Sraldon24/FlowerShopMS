package org.example.Inventory.PresentationLayer.Inventory;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class InventoryResponseModelTest {

    @Test
    void testNoArgsConstructorAndSetters() {
        InventoryResponseModel model = new InventoryResponseModel();
        model.setInventoryId("INV001");
        model.setType("Warehouse");

        assertThat(model.getInventoryId()).isEqualTo("INV001");
        assertThat(model.getType()).isEqualTo("Warehouse");
    }
    @Test
    void testAllArgsConstructorEqualsHashCode() {
        InventoryResponseModel m1 = new InventoryResponseModel("INV001", "Warehouse");
        InventoryResponseModel m2 = new InventoryResponseModel("INV001", "Warehouse");

        assertThat(m1).isEqualTo(m2);
        assertThat(m1.hashCode()).isEqualTo(m2.hashCode());
    }


    @Test
    void testInequalityWithDifferentType() {
        InventoryResponseModel model = new InventoryResponseModel();
        assertThat(model.equals("not an InventoryResponseModel")).isFalse();
    }
}
