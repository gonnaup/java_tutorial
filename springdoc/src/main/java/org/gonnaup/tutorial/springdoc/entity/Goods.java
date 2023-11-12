package org.gonnaup.tutorial.springdoc.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Id;
import org.gonnaup.tutorial.common.domain.Commodity;
import org.gonnaup.tutorial.springdoc.convert.EntityMapper;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.StringJoiner;

/**
 * 商品实体类
 *
 * @author gonnaup
 * @version created at 2023/9/3 10:46
 */
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

    @Override
    public String toString() {
        return new StringJoiner(", ", Goods.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("name='" + name + "'")
                .add("origin='" + origin + "'")
                .add("picture_url='" + picture_url + "'")
                .add("price=" + price)
                .add("originSite='" + originSite + "'")
                .add("discribe='" + discribe + "'")
                .toString();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getPicture_url() {
        return picture_url;
    }

    public void setPicture_url(String picture_url) {
        this.picture_url = picture_url;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getOriginSite() {
        return originSite;
    }

    public void setOriginSite(String originSite) {
        this.originSite = originSite;
    }

    public String getDiscribe() {
        return discribe;
    }

    public void setDiscribe(String discribe) {
        this.discribe = discribe;
    }
}
