package com.satan.healthy.entitiy;

import java.util.List;

/**
 * Created by Satan on 2016/08/17.
 */

public class HospitalListResponse extends CommonResponse {
    private int total;
    private List<Hospital> tngou;

    public HospitalListResponse(boolean status, int total, List<Hospital> tngou) {
        super(status);
        this.total = total;
        this.tngou = tngou;
    }

    public HospitalListResponse() {
    }

    public List<Hospital> getTngou() {
        return tngou;
    }

    public void setTngou(List<Hospital> tngou) {

        this.tngou = tngou;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getTotal() {
        return total;
    }
}
