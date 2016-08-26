package com.satan.healthy.entitiy;

/**
 * Created by Satan on 2016/08/16.
 */

public class CommonResponse {
    boolean status;

    public CommonResponse(boolean status) {
        this.status = status;
    }

    public CommonResponse() {
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public boolean isStatus() {
        return status;
    }

}
