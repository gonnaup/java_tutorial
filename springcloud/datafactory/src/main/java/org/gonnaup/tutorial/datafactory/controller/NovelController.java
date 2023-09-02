package org.gonnaup.tutorial.datafactory.controller;

import org.gonnaup.tutorial.common.entity.datafactory.Novel;
import org.gonnaup.tutorial.common.model.PageData;
import org.gonnaup.tutorial.datafactory.repository.NovelRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.data.domain.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author gonnaup
 * @version created at 2022/10/13 21:06
 */
@RestController
public class NovelController {

    private static final Logger logger = LoggerFactory.getLogger(NovelController.class);

    final NovelRepository novelRepository;

    final CircuitBreakerFactory<?, ?> circuitBreakerFactory;

    public NovelController(NovelRepository novelRepository, CircuitBreakerFactory<?, ?> circuitBreakerFactory) {
        this.novelRepository = novelRepository;
        this.circuitBreakerFactory = circuitBreakerFactory;
    }

    @GetMapping("/novel/{id}")
    public Novel searchThroughId(@PathVariable Integer id) {
        logger.info("返回id为 {} 的小说信息", id);
        return circuitBreakerFactory.create("slow").run(() -> novelRepository.findById(id).orElse(null));
    }

    @GetMapping("/novel/pageSearch")
    PageData<List<Novel>> pageSearch(Novel novel, @RequestParam int currentPage, @RequestParam int pageSize) {
        logger.info("分页查询小说数据 novel {}, currentPage {}, pageSize {}", novel, currentPage, pageSize);
        PageRequest pageRequest = PageRequest.of(currentPage - 1, pageSize, Sort.by(Sort.Order.desc("title")));
        ExampleMatcher exampleMatcher = ExampleMatcher.matchingAll()
                .withIgnorePaths("novelUrl", "coverUrl")
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
        Page<Novel> novelPage = novelRepository.findAll(Example.of(novel, exampleMatcher), pageRequest);
        return new PageData<>(novelPage.getTotalElements(), novelPage.getTotalPages(), novelPage.getContent());
    }

}
