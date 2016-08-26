package com.satan.healthy.entitiy;

import java.util.List;

/**
 * Created by Satan on 2016/08/23.
 */
public class RecipeResponse extends CommonResponse{
    private int total;
    private List<Recipe> tngou;

    public RecipeResponse(boolean status, int total, List<Recipe> tngou) {
        super(status);
        this.total = total;
        this.tngou = tngou;
    }

    public RecipeResponse() {
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<Recipe> getTngou() {
        return tngou;
    }

    public void setTngou(List<Recipe> tngou) {
        this.tngou = tngou;
    }
}
