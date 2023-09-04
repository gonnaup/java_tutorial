package org.gonnaup.tutorial.springdoc.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.gonnaup.tutorial.common.model.Result;
import org.gonnaup.tutorial.springdoc.entity.Goods;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.IntStream;

/**
 * @author gonnaup
 * @version created at 2023/9/3 10:57
 */
@RestController
@RequestMapping("/api/v1/goods")
public class GoodsController {

    @Operation(description = "返回一个随机商品")
    @GetMapping("/random")
    Result<Goods> randomGoods() {
        return new Result<>("ok", Goods.randomGoods());
    }

    @Operation(description = "返回随机商品列表")
    @GetMapping("/random/{quantity}")
    Result<List<Goods>> random10Goods(@PathVariable int quantity) {
        List<Goods> list = IntStream.range(0, quantity).mapToObj(i -> Goods.randomGoods()).toList();
        return new Result<>("ok", list);
    }
}
