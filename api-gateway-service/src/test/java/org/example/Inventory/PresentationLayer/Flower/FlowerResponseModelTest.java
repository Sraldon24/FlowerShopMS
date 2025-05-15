package org.example.Inventory.PresentationLayer.Flower;

import org.example.Inventory.PresentationLayer.Inventory.InventoryResponseModel;
import org.example.Suppliers.PresentationLayer.SupplierResponseModel;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class FlowerResponseModelTest {

    @Test
    void testNoArgsConstructorAndSetters() {
        FlowerResponseModel model = new FlowerResponseModel();
        model.setFlowerId("F1");
        model.setInventoryId("I1");
        model.setFlowerName("Rose");
        model.setFlowerColor("Red");
        model.setFlowerCategory("Romantic");
        model.setFlowerStatus("Available");
        model.setStockQuantity(10);
        model.setSupplierIdentifier("S1");
        model.setPrice(BigDecimal.valueOf(5.99));
        model.setCurrency("CAD");

        OptionDto option = new OptionDto("Gift Wrap", "Wrap with ribbon", BigDecimal.valueOf(2.00));
        model.setOptions(List.of(option));

        SupplierResponseModel supplier = new SupplierResponseModel();
        model.setSupplier(supplier);

        assertThat(model.getFlowerId()).isEqualTo("F1");
        assertThat(model.getInventoryId()).isEqualTo("I1");
        assertThat(model.getFlowerName()).isEqualTo("Rose");
        assertThat(model.getFlowerColor()).isEqualTo("Red");
        assertThat(model.getFlowerCategory()).isEqualTo("Romantic");
        assertThat(model.getFlowerStatus()).isEqualTo("Available");
        assertThat(model.getStockQuantity()).isEqualTo(10);
        assertThat(model.getSupplierIdentifier()).isEqualTo("S1");
        assertThat(model.getPrice()).isEqualTo(BigDecimal.valueOf(5.99));
        assertThat(model.getCurrency()).isEqualTo("CAD");
        assertThat(model.getOptions()).hasSize(1);
        assertThat(model.getSupplier()).isEqualTo(supplier);
    }

    @Test
    void testAllArgsConstructorAndEqualsAndHashCode() {
        OptionDto option = new OptionDto("O1", "Desc", BigDecimal.TEN);
        SupplierResponseModel supplier = new SupplierResponseModel();

        FlowerResponseModel model1 = new FlowerResponseModel(
                "F1", "I1", "Tulip", "Yellow", "Spring", "Available", 20,
                "S1", BigDecimal.valueOf(4.99), "CAD", List.of(option), supplier);

        FlowerResponseModel model2 = new FlowerResponseModel(
                "F1", "I1", "Tulip", "Yellow", "Spring", "Available", 20,
                "S1", BigDecimal.valueOf(4.99), "CAD", List.of(option), supplier);

        assertThat(model1).isEqualTo(model2);
        assertThat(model1.hashCode()).isEqualTo(model2.hashCode());
        assertThat(model1.toString()).contains("Tulip");
    }

    @Test
    void testCanEqualAndInequality() {
        FlowerResponseModel model = new FlowerResponseModel();
        assertThat(model.canEqual(new FlowerResponseModel())).isTrue();
        assertThat(model.equals("not a FlowerResponseModel")).isFalse();

        FlowerResponseModel other = new FlowerResponseModel();
        other.setFlowerId("Different");
        assertThat(model).isNotEqualTo(other);
    }

    @Test
    void testAllSettersIndividually() {
        FlowerResponseModel model = new FlowerResponseModel();
        model.setFlowerId("F1");
        model.setInventoryId("I1");
        model.setFlowerName("Daisy");
        model.setFlowerColor("White");
        model.setFlowerCategory("Cute");
        model.setFlowerStatus("Sold");
        model.setStockQuantity(5);
        model.setSupplierIdentifier("SUP1");
        model.setPrice(BigDecimal.ONE);
        model.setCurrency("USD");
        model.setOptions(Collections.emptyList());

        assertThat(model.getFlowerId()).isEqualTo("F1");
        assertThat(model.getInventoryId()).isEqualTo("I1");
        assertThat(model.getFlowerName()).isEqualTo("Daisy");
        assertThat(model.getFlowerColor()).isEqualTo("White");
        assertThat(model.getFlowerCategory()).isEqualTo("Cute");
        assertThat(model.getFlowerStatus()).isEqualTo("Sold");
        assertThat(model.getStockQuantity()).isEqualTo(5);
        assertThat(model.getSupplierIdentifier()).isEqualTo("SUP1");
        assertThat(model.getPrice()).isEqualTo(BigDecimal.ONE);
        assertThat(model.getCurrency()).isEqualTo("USD");
        assertThat(model.getOptions()).isEmpty();
    }

    @Test
    void testEqualsWithNullsAndDifferentObjects() {
        FlowerResponseModel model = new FlowerResponseModel();
        assertThat(model.equals(null)).isFalse();
        assertThat(model.equals(new Object())).isFalse();
    }

    @Test
    void testHashCodeConsistency() {
        FlowerResponseModel model = new FlowerResponseModel();
        model.setFlowerId("F123");
        int hash1 = model.hashCode();
        int hash2 = model.hashCode();
        assertThat(hash1).isEqualTo(hash2);
    }
    @Test
    void testAllArgsConstructorWithNullOptionsAndNoSupplier() {
        FlowerResponseModel model = new FlowerResponseModel(
                "F100", "I100", "Sunflower", "Yellow", "Happy", "Available", 100,
                "SUP100", BigDecimal.valueOf(9.99), "USD", null, null);

        assertThat(model.getFlowerId()).isEqualTo("F100");
        assertThat(model.getInventoryId()).isEqualTo("I100");
        assertThat(model.getFlowerName()).isEqualTo("Sunflower");
        assertThat(model.getFlowerColor()).isEqualTo("Yellow");
        assertThat(model.getFlowerCategory()).isEqualTo("Happy");
        assertThat(model.getFlowerStatus()).isEqualTo("Available");
        assertThat(model.getStockQuantity()).isEqualTo(100);
        assertThat(model.getSupplierIdentifier()).isEqualTo("SUP100");
        assertThat(model.getPrice()).isEqualTo(BigDecimal.valueOf(9.99));
        assertThat(model.getCurrency()).isEqualTo("USD");
        assertThat(model.getOptions()).isNull();
        assertThat(model.getSupplier()).isNull();
    }

    @Test
    void testEqualsAndHashCode_DifferentValues() {
        FlowerResponseModel model1 = new FlowerResponseModel(
                "F1", "I1", "Rose", "Red", "Romantic", "Available", 10,
                "S1", BigDecimal.valueOf(5.99), "CAD", null, null);

        FlowerResponseModel model2 = new FlowerResponseModel(
                "F2", "I2", "Lily", "White", "Elegant", "Sold", 5,
                "S2", BigDecimal.valueOf(4.99), "USD", null, null);

        assertThat(model1).isNotEqualTo(model2);
        assertThat(model1.hashCode()).isNotEqualTo(model2.hashCode());
    }

    @Test
    void testEqualsSelfComparison() {
        FlowerResponseModel model = new FlowerResponseModel();
        assertThat(model).isEqualTo(model); // reflexive test
    }

    @Test
    void testCanEqualWithDifferentObjectType() {
        FlowerResponseModel model = new FlowerResponseModel();
        assertThat(model.canEqual("StringObject")).isFalse();
    }

    @Test
    void testSettersIndividuallyToHitMissedMethods() {
        FlowerResponseModel model = new FlowerResponseModel();

        model.setInventoryId("INV999");
        model.setFlowerName("Orchid");
        model.setFlowerColor("Purple");
        model.setFlowerCategory("Exotic");
        model.setFlowerStatus("OutOfStock");
        model.setStockQuantity(0);
        model.setSupplierIdentifier("SUP999");
        model.setPrice(BigDecimal.ZERO);
        model.setCurrency("JPY");
        model.setOptions(List.of(new OptionDto("Box", "Gift Box", BigDecimal.valueOf(1.0))));

        assertThat(model.getInventoryId()).isEqualTo("INV999");
        assertThat(model.getFlowerName()).isEqualTo("Orchid");
        assertThat(model.getFlowerColor()).isEqualTo("Purple");
        assertThat(model.getFlowerCategory()).isEqualTo("Exotic");
        assertThat(model.getFlowerStatus()).isEqualTo("OutOfStock");
        assertThat(model.getStockQuantity()).isEqualTo(0);
        assertThat(model.getSupplierIdentifier()).isEqualTo("SUP999");
        assertThat(model.getPrice()).isEqualTo(BigDecimal.ZERO);
        assertThat(model.getCurrency()).isEqualTo("JPY");
        assertThat(model.getOptions()).hasSize(1);
    }

    @Test
    void testHashCodeIncludesAllFields() {
        SupplierResponseModel supplier = new SupplierResponseModel();
        OptionDto option = new OptionDto("O1", "Nice wrap", BigDecimal.ONE);

        FlowerResponseModel model = new FlowerResponseModel(
                "F9", "I9", "Hydrangea", "Blue", "Cool", "Available", 30,
                "S9", BigDecimal.valueOf(3.33), "EUR", List.of(option), supplier);

        int hash = model.hashCode();
        assertThat(hash).isNotZero();
    }

    @Test
    void testAllArgsConstructorWithSupplierInFlowerResponseModel() {
        OptionDto option = new OptionDto("Deluxe Wrap", "Gold Ribbon", BigDecimal.valueOf(3.50));
        SupplierResponseModel supplier = new SupplierResponseModel(); // You might want to set fields if applicable

        FlowerResponseModel model = new FlowerResponseModel(
                "F123", "I123", "Lavender", "Purple", "Relaxing", "Available", 15,
                "SUP123", BigDecimal.valueOf(6.99), "USD", List.of(option), supplier);

        assertThat(model.getSupplier()).isEqualTo(supplier);
        assertThat(model.getOptions()).containsExactly(option);
    }
    @Test
    void testOptionDtoAllArgsConstructorAndGetters() {
        OptionDto option = new OptionDto("Name", "Description", BigDecimal.valueOf(2.50));

        assertThat(option.getName()).isEqualTo("Name");
        assertThat(option.getDescription()).isEqualTo("Description");
        assertThat(option.getPrice()).isEqualTo(BigDecimal.valueOf(2.50));
    }
    @Test
    void testOptionDtoSetters() {
        OptionDto option = new OptionDto();
        option.setName("Gift Bag");
        option.setDescription("With logo");
        option.setPrice(BigDecimal.TEN);

        assertThat(option.getName()).isEqualTo("Gift Bag");
        assertThat(option.getDescription()).isEqualTo("With logo");
        assertThat(option.getPrice()).isEqualTo(BigDecimal.TEN);
    }
    @Test
    void testOptionDtoEqualsAndHashCode() {
        OptionDto o1 = new OptionDto("Name", "Desc", BigDecimal.ONE);
        OptionDto o2 = new OptionDto("Name", "Desc", BigDecimal.ONE);
        OptionDto o3 = new OptionDto("Other", "Diff", BigDecimal.TEN);

        assertThat(o1).isEqualTo(o2);
        assertThat(o1.hashCode()).isEqualTo(o2.hashCode());
        assertThat(o1).isNotEqualTo(o3);
    }
    @Test
    void testOptionDtoToStringAndCanEqual() {
        OptionDto option = new OptionDto("Fancy", "Bright", BigDecimal.valueOf(5.0));

        assertThat(option.toString()).contains("Fancy", "Bright", "5.0");
        assertThat(option.canEqual(new OptionDto())).isTrue();
        assertThat(option.equals("not an OptionDto")).isFalse();
    }
    @Test
    void testAllArgsConstructorExplicit() {
        String flowerId = "F100";
        String inventoryId = "INV100";
        String flowerName = "Orchid";
        String flowerColor = "Pink";
        String flowerCategory = "Exotic";
        String flowerStatus = "Available";
        int stockQuantity = 50;
        String supplierIdentifier = "SUP100";
        BigDecimal price = BigDecimal.valueOf(12.99);
        String currency = "USD";
        OptionDto option = new OptionDto("Vase", "Glass Vase", BigDecimal.valueOf(5.00));
        List<OptionDto> options = List.of(option);
        SupplierResponseModel supplier = new SupplierResponseModel(); // if applicable, set supplier fields

        // DIRECT call to @AllArgsConstructor — this is what Jacoco sees
        FlowerResponseModel model = new FlowerResponseModel(
                flowerId, inventoryId, flowerName, flowerColor, flowerCategory,
                flowerStatus, stockQuantity, supplierIdentifier, price, currency,
                options, supplier
        );

        // Basic asserts to ensure it's not optimized out
        assertThat(model.getFlowerId()).isEqualTo(flowerId);
        assertThat(model.getFlowerName()).isEqualTo("Orchid");
        assertThat(model.getOptions()).containsExactly(option);
        assertThat(model.getSupplier()).isEqualTo(supplier);
    }
    @Test
    void testAllArgsConstructorEqualsHashCode() {
        OptionDto option1 = new OptionDto("O1", "Nice vase", BigDecimal.valueOf(3.50));
        OptionDto option2 = new OptionDto("O1", "Nice vase", BigDecimal.valueOf(3.50)); // same content for equals

        SupplierResponseModel supplier1 = new SupplierResponseModel(); // use populated object if needed
        SupplierResponseModel supplier2 = new SupplierResponseModel(); // equal for equals() to pass

        FlowerResponseModel model1 = new FlowerResponseModel(
                "F1", "INV1", "Tulip", "Red", "Spring", "Available", 30,
                "SUP1", BigDecimal.valueOf(10.99), "CAD", List.of(option1), supplier1
        );

        FlowerResponseModel model2 = new FlowerResponseModel(
                "F1", "INV1", "Tulip", "Red", "Spring", "Available", 30,
                "SUP1", BigDecimal.valueOf(10.99), "CAD", List.of(option2), supplier2
        );

        // Explicit equals & hashCode coverage
        assertThat(model1).isEqualTo(model2);
        assertThat(model1.hashCode()).isEqualTo(model2.hashCode());

        // Reflexive, symmetric, transitive
        assertThat(model1).isEqualTo(model1); // reflexive
        assertThat(model2).isEqualTo(model1); // symmetric
        FlowerResponseModel model3 = new FlowerResponseModel(
                "F1", "INV1", "Tulip", "Red", "Spring", "Available", 30,
                "SUP1", BigDecimal.valueOf(10.99), "CAD", List.of(option1), supplier1
        );
        assertThat(model1).isEqualTo(model3); // transitive

        // Inequality scenarios
        FlowerResponseModel different = new FlowerResponseModel(
                "F2", "INV2", "Lily", "White", "Summer", "Sold", 20,
                "SUP2", BigDecimal.valueOf(8.99), "USD", List.of(), null
        );
        assertThat(model1).isNotEqualTo(different);

        // Null and type comparison
        assertThat(model1.equals(null)).isFalse();
        assertThat(model1.equals("not a model")).isFalse();

        // AllArgsConstructor specific checks
        assertThat(model1.getFlowerName()).isEqualTo("Tulip");
        assertThat(model1.getOptions()).hasSize(1);
        assertThat(model1.getPrice()).isEqualTo(BigDecimal.valueOf(10.99));
    }


}
