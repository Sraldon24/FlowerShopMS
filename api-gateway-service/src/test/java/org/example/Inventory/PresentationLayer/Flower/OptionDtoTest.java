package org.example.Inventory.PresentationLayer.Flower;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class OptionDtoTest {

    @Test
    void testAllArgsConstructorAndGetters() {
        OptionDto option = new OptionDto("Vase", "Glass vase", BigDecimal.valueOf(3.50));

        assertThat(option.getName()).isEqualTo("Vase");
        assertThat(option.getDescription()).isEqualTo("Glass vase");
        assertThat(option.getPrice()).isEqualTo(BigDecimal.valueOf(3.50));
    }

    @Test
    void testSetters() {
        OptionDto option = new OptionDto();
        option.setName("Card");
        option.setDescription("Greeting card");
        option.setPrice(BigDecimal.ONE);

        assertThat(option.getName()).isEqualTo("Card");
        assertThat(option.getDescription()).isEqualTo("Greeting card");
        assertThat(option.getPrice()).isEqualTo(BigDecimal.ONE);
    }

    @Test
    void testEqualsHashCodeAndToString() {
        OptionDto o1 = new OptionDto("Box", "Gift box", BigDecimal.TEN);
        OptionDto o2 = new OptionDto("Box", "Gift box", BigDecimal.TEN);

        assertThat(o1).isEqualTo(o2);
        assertThat(o1.hashCode()).isEqualTo(o2.hashCode());
        assertThat(o1.toString()).contains("Gift box");
    }

    @Test
    void testCanEqualAndInequality() {
        OptionDto option = new OptionDto();
        assertThat(option.canEqual(new OptionDto())).isTrue();
        assertThat(option.equals("not an OptionDto")).isFalse();
    }
}
