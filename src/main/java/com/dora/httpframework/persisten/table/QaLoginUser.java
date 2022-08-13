package com.dora.httpframework.persisten.table;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @Describe 登陆信息
 * @Author dora 1.0.1
 **/
@Data
public class QaLoginUser implements Serializable {

    @TableId(type = IdType.AUTO)
    private Integer id;

    private String appKey;

    private String accid;

    private Long uin;

    private String session;

    private String token;

    private Integer flag;

    private Long effectiveTime;

    @TableField(fill = FieldFill.INSERT, value = "create_time")
    private Date createTime;
    @TableField(fill = FieldFill.INSERT_UPDATE, value = "update_time")
    private Date updateTime;


}
