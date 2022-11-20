package cn.gonnaup.tutorial.common.model;

import lombok.*;

/**
 * @author gonnaup
 * @version created at 2022/10/14 19:54
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PageData<T> {

    private long total;

    private int totalPage;

    private T data;

}
