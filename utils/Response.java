package com.personal.utils;

import lombok.Data;

/**
 * 描述：返回类型的数据结构
 * @author
 * @date 2020/06/24 15:43
 **/
@Data
public class Response {
    private String code;
    private String msg;
    private Object data;
    public Response() {
        this.code = "-200";
        this.msg = "SUCCESS";
    }
    public Response(String code, String msg){
        this.code = code;
        this.msg = msg;
    }
    public Response buildSuccessResponse(){
        this.code = "-200";
        this.msg = "SUCCESS";
        return this;
    }
    public Response buildFailedResponse(){
        this.code = "-400";
        this.msg = "FAILED";
        return this;
    }
    public Response buildSuccessResponse(String msg){
        this.code = "-200";
        this.msg = msg;
        return this;
    }
    public Response buildFailedResponse(String msg){
        this.code = "-400";
        this.msg = msg;
        return this;
    }
    public Response buildFailedResponse(String code, String msg){
        this.code = code;
        this.msg = msg;
        return this;
    }
    public Response buildSuccessResponse(String code, String msg){
        this.code = code;
        this.msg =  msg;
        return this;
    }
}

