package com.dora.httpframework.persisten.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dora.httpframework.persisten.table.QaLoginUser;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Describe 数据层操作
 * @Author dora 1.0.1
 **/
@Mapper
public interface LoginUserMapper extends BaseMapper<QaLoginUser> {

//    @Select("select id from ")
//    Map<String, Object> get();
    
}
