package com.tedu.micro.demo.common.protocol;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JsonResult<T> {
    private Integer code;//0成功其他表示失败
    private String message;
    private T data;
    //封装一些静态方法 方便业务调用
    public static <T> JsonResult<T> success(T data) {
        return new JsonResult<>(0,"ok",data);
    }
    //也可以表示失败
    public static <T> JsonResult<T> fail(String message) {
        return new JsonResult<>(-1,message,null);
    }
}
