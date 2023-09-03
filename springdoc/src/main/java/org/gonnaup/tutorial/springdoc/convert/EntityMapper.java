package org.gonnaup.tutorial.springdoc.convert;

import org.gonnaup.tutorial.common.domain.Commodity;
import org.gonnaup.tutorial.springdoc.entity.Goods;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author gonnaup
 * @version created at 2023/9/3 10:49
 */
@Mapper
public interface EntityMapper {

    EntityMapper INSTANCE = Mappers.getMapper(EntityMapper.class);

    Goods commodityToGoods(Commodity commodity);

}
