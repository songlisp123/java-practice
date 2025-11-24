package com.todo.demo.base.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JsonResult {
    private Integer code;
    private String message;
    private Object data;

    public JsonResult(Status status) {
        this.code = status.getCode();
        this.message = status.getMessage();
    }

    public JsonResult(Status status, Object data) {
        this.code = status.getCode();
        this.message = status.getMessage();
        this.data = data;
    }

    public static JsonResult ok() {
        return new JsonResult(Status.OPERATION_SUCCESS);
    }

    public static JsonResult ok(Object data) {
        return new JsonResult(Status.OPERATION_SUCCESS,data);
    }

    public static JsonResult failed(Object data) {
        return new JsonResult(Status.OPERATION_FAILED,data);
    }
}
