/* ======================
   OptionMapper.java
   ====================== */
package com.champsoft.services.inventory.MapperLayer;

import com.champsoft.services.inventory.DataLayer.Flowers.Option;
import com.champsoft.services.inventory.PresentationLayer.OptionDto;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OptionMapper {

    @Named("entityToDto")
    OptionDto entityToDto(Option option);

    @Named("entityListToDtoList")
    List<OptionDto> entityListToDtoList(List<Option> options);

    @Named("dtoToEntity")
    Option dtoToEntity(OptionDto optionDto);

    @Named("dtoListToEntityList")
    List<Option> dtoListToEntityList(List<OptionDto> optionDtos);
}
