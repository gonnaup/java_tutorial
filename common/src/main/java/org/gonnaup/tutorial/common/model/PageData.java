package org.gonnaup.tutorial.common.model;

import java.util.StringJoiner;

/**
 * @author gonnaup
 * @version created at 2022/10/14 19:54
 */
public class PageData<T> {

    private long total;

    private int totalPage;

    private T data;

    public PageData() {
    }

    public PageData(long total, int totalPage, T data) {
        this.total = total;
        this.totalPage = totalPage;
        this.data = data;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", PageData.class.getSimpleName() + "[", "]")
                .add("total=" + total)
                .add("totalPage=" + totalPage)
                .add("data=" + data)
                .toString();
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
