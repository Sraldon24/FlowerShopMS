package com.champsoft.services.inventory.MapperLayer;

import com.champsoft.services.inventory.DataLayer.Flowers.Option;
import com.champsoft.services.inventory.DataLayer.Flowers.Price;
import com.champsoft.services.inventory.DataLayer.Inventory.Inventory;
import com.champsoft.services.inventory.DataLayer.Inventory.InventoryIdentifier;
import com.champsoft.services.inventory.MapperLayer.Inventory_Flower.FlowerInventoryResponseMapper;
import com.champsoft.services.inventory.MapperLayer.Inventory_Flower.FlowerInventoryResponseMapperImpl;
import com.champsoft.services.inventory.PresentationLayer.Flower.FlowerResponseModel;
import com.champsoft.services.inventory.PresentationLayer.Inventory_Flower.InventoryFlowerResponseModel;
import com.champsoft.services.inventory.PresentationLayer.OptionDto;
import com.champsoft.services.inventory.utils.Currency;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = FlowerInventoryResponseMapperImpl.class)
public class MapperLayerTests {

    @Autowired
    private FlowerInventoryResponseMapper mapper;

    @Test
    public void testEntitiesToResponseModel() {

        Inventory inventory = new Inventory();
        inventory.setInventoryIdentifier(new InventoryIdentifier("INV-999"));
        inventory.setType("Main");

        FlowerResponseModel flower1 = new FlowerResponseModel();
        flower1.setFlowerName("Rose");
        flower1.setFlowerColor("Red");

        FlowerResponseModel flower2 = new FlowerResponseModel();
        flower2.setFlowerName("Tulip");
        flower2.setFlowerColor("Yellow");

        List<FlowerResponseModel> flowerList = List.of(flower1, flower2);

        // Act
        InventoryFlowerResponseModel response = mapper.entitiesToResponseModel(inventory, flowerList);

        // Assert
        assertEquals("INV-999", response.getInventoryId());
        assertEquals(2, response.getAvailableFlowers().size());
        assertEquals("Rose", response.getAvailableFlowers().get(0).getFlowerName());
        assertEquals("Tulip", response.getAvailableFlowers().get(1).getFlowerName());
    }

    private final OptionMapper optionMapper = Mappers.getMapper(OptionMapper.class);

    @Test
    public void testEntityToDto() {
        // Arrange
        Option option = new Option();
        option.setName("Gift Wrap");
        option.setDescription("Elegant wrapping");
        option.setPrice(BigDecimal.valueOf(3.50));

        // Act
        OptionDto dto = optionMapper.entityToDto(option);

        // Assert
        assertNotNull(dto);
        assertEquals("Gift Wrap", dto.getName());
        assertEquals("Elegant wrapping", dto.getDescription());
        assertEquals(BigDecimal.valueOf(3.50), dto.getPrice());
    }

    @Test
    public void testDtoToEntity() {
        // Arrange
        OptionDto dto = new OptionDto();
        dto.setName("Personal Note");
        dto.setDescription("Include a handwritten card");
        dto.setPrice(BigDecimal.valueOf(1.75));

        // Act
        Option option = optionMapper.dtoToEntity(dto);

        // Assert
        assertNotNull(option);
        assertEquals("Personal Note", option.getName());
        assertEquals("Include a handwritten card", option.getDescription());
        assertEquals(BigDecimal.valueOf(1.75), option.getPrice());
    }

    @Test
    public void testEntityListToDtoList() {
        // Arrange
        Option opt1 = new Option("Chocolates", "Box of chocolates", BigDecimal.valueOf(5));
        Option opt2 = new Option("Balloon", "Heart-shaped balloon", BigDecimal.valueOf(2));
        List<Option> entityList = List.of(opt1, opt2);

        // Act
        List<OptionDto> dtoList = optionMapper.entityListToDtoList(entityList);

        // Assert
        assertNotNull(dtoList);
        assertEquals(2, dtoList.size());

        assertEquals("Chocolates", dtoList.get(0).getName());
        assertEquals("Balloon", dtoList.get(1).getName());
    }

    @Test
    public void testDtoListToEntityList() {
        // Arrange
        OptionDto dto1 = new OptionDto("Scented Candle", "Lavender", BigDecimal.valueOf(4.99));
        OptionDto dto2 = new OptionDto("Tea Pack", "Assorted teas", BigDecimal.valueOf(3.25));
        List<OptionDto> dtoList = List.of(dto1, dto2);

        // Act
        List<Option> entityList = optionMapper.dtoListToEntityList(dtoList);

        // Assert
        assertNotNull(entityList);
        assertEquals(2, entityList.size());

        assertEquals("Scented Candle", entityList.get(0).getName());
        assertEquals("Tea Pack", entityList.get(1).getName());
    }

    private final PriceMapper priceMapper = Mappers.getMapper(PriceMapper.class);

    @Test
    public void testPriceToBigDecimal() {
        // Arrange
        Price price = Price.builder()
                .amount(BigDecimal.valueOf(19.99))
                .currency(Currency.USD)
                .build();

        // Act
        BigDecimal amount = priceMapper.priceToBigDecimal(price);

        // Assert
        assertNotNull(amount);
        assertEquals(BigDecimal.valueOf(19.99), amount);
    }

    @Test
    public void testPriceToBigDecimal_null() {
        // Arrange
        Price price = null;

        // Act
        BigDecimal amount = priceMapper.priceToBigDecimal(price);

        // Assert
        assertNull(amount);
    }

    @Test
    public void testBigDecimalToPrice() {
        // Arrange
        BigDecimal amount = BigDecimal.valueOf(12.50);

        // Act
        Price price = priceMapper.bigDecimalToPrice(amount);

        // Assert
        assertNotNull(price);
        assertEquals(amount, price.getAmount());
        assertEquals(Currency.CAD, price.getCurrency()); // Assuming default is CAD
    }

    @Test
    public void testBigDecimalToPrice_null() {
        // Arrange
        BigDecimal amount = null;

        // Act
        Price price = priceMapper.bigDecimalToPrice(amount);

        // Assert
        assertNull(price);
    }

    @Test
    public void testBigDecimalToPrice_edgeCase() {
        // Arrange
        BigDecimal amount = BigDecimal.ZERO;

        // Act
        Price price = priceMapper.bigDecimalToPrice(amount);

        // Assert
        assertNotNull(price);
        assertEquals(amount, price.getAmount());
        assertEquals(Currency.CAD, price.getCurrency());
    }
}
