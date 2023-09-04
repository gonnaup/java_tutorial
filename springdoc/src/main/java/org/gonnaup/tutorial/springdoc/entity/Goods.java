package org.gonnaup.tutorial.springdoc.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Id;
import lombok.Data;
import org.gonnaup.tutorial.common.domain.Commodity;
import org.gonnaup.tutorial.springdoc.convert.EntityMapper;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

/**
 * 商品实体类
 *
 * @author gonnaup
 * @version created at 2023/9/3 10:46
 */
@Data
@Document
@Schema(description = "商品详情")
public class Goods {

    /**
     * ID
     */
    @Id
    private Integer id;

    /**
     * 商品名称
     */
    @Schema(description = "商品名称", example = "电脑")
    private String name;

    /**
     * 商品产地
     */
    private String origin;

    /**
     * 商品图片地址
     */
    private String picture_url;

    /**
     * 商品价格
     */
    private BigDecimal price;

    /**
     * 商品原网站
     */
    private String originSite;

    /**
     * 商品描述
     */
    private String discribe;

    public static Goods randomGoods() {
        return EntityMapper.INSTANCE.commodityToGoods(Commodity.newRandomCommodity());
    }
}
