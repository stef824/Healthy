package com.satan.healthy.entitiy;

import java.util.List;

/**
 * Created by Satan on 2016/08/24.
 */
public class NewsResponse extends CommonResponse{
    private int total;
    private List<News> tngou;

    public NewsResponse(boolean status, int total, List<News> tngou) {
        super(status);
        this.total = total;
        this.tngou = tngou;
    }

    public NewsResponse() {
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<News> getTngou() {
        return tngou;
    }

    public void setTngou(List<News> tngou) {
        this.tngou = tngou;
    }
}
