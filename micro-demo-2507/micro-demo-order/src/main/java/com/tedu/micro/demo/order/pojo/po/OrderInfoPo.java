package com.tedu.micro.demo.order.pojo.po;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("order_info")
public class OrderInfoPo {

    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;
    @TableField(value = "bill_id")
    private String billId;
    @TableField(value = "user_id")
    private Integer userId;
    @TableField(value = "gun_id")
    private Integer gunId;
    @TableField(value = "create_time",fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    @TableField(value = "update_time",fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
