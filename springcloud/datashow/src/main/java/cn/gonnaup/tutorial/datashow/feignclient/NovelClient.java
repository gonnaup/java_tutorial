package cn.gonnaup.tutorial.datashow.feignclient;

import cn.gonnaup.tutorial.common.ServerName;
import cn.gonnaup.tutorial.common.entity.datafactory.Novel;
import cn.gonnaup.tutorial.common.model.PageData;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author gonnaup
 * @version created at 2022/10/13 21:57
 */
@FeignClient(ServerName.DATA_FACTORY)
public interface NovelClient {

    @GetMapping("/novel/{id}")
    Novel fetchNovelThroughId(@PathVariable("id") Integer id);

    @GetMapping("/novel/pageSearch")
    PageData<List<Novel>> pageSearch(@SpringQueryMap Novel novel, @RequestParam("currentPage") int currentPage, @RequestParam("pageSize") int pageSize);

}
