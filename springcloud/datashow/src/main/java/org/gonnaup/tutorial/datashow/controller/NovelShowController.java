package org.gonnaup.tutorial.datashow.controller;

import org.gonnaup.tutorial.common.entity.datafactory.Novel;
import org.gonnaup.tutorial.common.model.PageData;
import org.gonnaup.tutorial.common.model.Result;
import org.gonnaup.tutorial.datashow.feignclient.NovelClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author gonnaup
 * @version created at 2022/10/13 21:32
 */
@RestController
public class NovelShowController {

    private static final Logger logger = LoggerFactory.getLogger(NovelShowController.class);

    final NovelClient novelClient;

    public NovelShowController(NovelClient novelClient) {
        this.novelClient = novelClient;
    }


    @GetMapping("/show/novel/{id}")
    Result<Novel> showNovelThroughId(@PathVariable Integer id) {

        Novel novel = novelClient.fetchNovelThroughId(id);
        logger.info("远程查询小说 {} => {}", id, novel);
        return new Result<>("200", novel);
    }

    @GetMapping("/show/novel/search/paged")
    Result<PageData<List<Novel>>> searchNovelPaged(Novel novel, @RequestParam int currentPage, @RequestParam int pageSize) {
        logger.info("分页查询小说, 参数 {}, currentPage {}, pageSize {}", novel, currentPage, pageSize);
        return new Result<>("200", novelClient.pageSearch(novel, currentPage, pageSize));
    }

}
