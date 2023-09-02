package org.gonnaup.tutorial.common.entity.datafactory;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.StringJoiner;

/**
 * @author gonnaup
 * @version created at 2022/10/13 20:42
 */
@Entity
@Getter
@Setter
@Table(name = "crawl_doubannovel_data")
public class Novel {

    /**
     * ID
     */
    @Id
    private Integer id;

    /**
     * 书名
     */
    @Column(length = 50)
    private String title;

    /**
     * 作者
     */
    @Column(length = 100)
    private String author;

    /**
     * 作者主页
     */
    @Column(length = 500)
    private String authorUrl;

    /**
     * 小说类型
     */
    @Column(length = 50)
    private String kind;

    /**
     * 小说字数
     */
    @Column(length = 20)
    private String words;

    /**
     * 小说状态
     */
    @Column(length = 20)
    private String status;

    /**
     * 标签
     */
    @Column(length = 20)
    private String tag;

    /**
     * 小说简介
     */
    @Column(length = 2000)
    private String introduce;

    /**
     * 小说主页
     */
    @Column(length = 500)
    private String novelUrl;

    /**
     * 封面图片地址
     */
    @Column(length = 500)
    private String coverUrl;


    @Override
    public String toString() {
        return new StringJoiner(", ", Novel.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("title='" + title + "'")
                .add("author='" + author + "'")
                .add("kind='" + kind + "'")
                .add("worlds='" + words + "'")
                .add("status='" + status + "'")
                .add("tag='" + tag + "'")
                .toString();
    }

}
