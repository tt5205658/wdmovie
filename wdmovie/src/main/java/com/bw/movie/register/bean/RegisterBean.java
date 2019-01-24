package com.bw.movie.register.bean;
/**
  * @作者 GXY
  * @创建日期 2019/1/24 23:01
  * @描述 注册bean
  *
  */

public class RegisterBean {
    private String message;
    private String status;
    private final String SUCCESS_STATUS = "0000";
    public boolean isSuccess(){
        return status.equals(SUCCESS_STATUS);
    }
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
