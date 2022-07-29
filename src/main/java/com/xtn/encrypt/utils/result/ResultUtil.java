package com.xtn.encrypt.utils.result;

/**
 * 返回值工具包
 * @author hefeiyu
 */
public class ResultUtil {

    private static final String SUCCESS_MESSAGE = "操作成功";

    private static final String FAIL_MESSAGE = "操作失败";

    //*****成功*****

    public static <T> Result<T> ok() {
        return ResultUtil.ok(SUCCESS_MESSAGE, null);
    }

    public static <T> Result<T> ok(T data) {
        return ResultUtil.ok(SUCCESS_MESSAGE, data);
    }


    public static <T> Result<T> ok(String message, T data) {

        Result<T> result = new Result<>();
        result.setCode(ResultCodeEnum.SUCCESS.getValue());
        result.setMessage(message);
        result.setData(data);
        return result;
    }

    //*******失败*********


    public static <T> Result<T> fail() {
        return ResultUtil.fail(FAIL_MESSAGE, null);
    }


    public static <T> Result<T> fail(String message) {
        return ResultUtil.fail(message, null);
    }

    public static <T> Result<T> fail(String message, T data) {
        Result<T> result = new Result<>();
        result.setCode(ResultCodeEnum.FAIL.getValue());
        result.setMessage(message);
        result.setData(data);
        return result;
    }

    //*********未准备好，用于轮询*********

    public static <T> Result<T> notReady() {
        Result<T> result = new Result<>();
        result.setCode(ResultCodeEnum.FILE_NOT_READY.getValue());
        result.setMessage("未准备好");
        result.setData(null);
        return result;
    }

    /**
     * 自定义
     */
    public static <T> Result<T> custom(ResultCodeEnum code, String message, T data) {
        Result<T> result = new Result<>();
        result.setCode(code.getValue());
        result.setMessage(message);
        result.setData(data);
        return result;
    }
}
