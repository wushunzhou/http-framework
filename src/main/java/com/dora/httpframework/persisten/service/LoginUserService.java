package com.dora.httpframework.persisten.service;

import com.dora.httpframework.persisten.table.QaLoginUser;

/**
 * @Describe CRUD
 * @Author dora 1.0.1
 **/
public interface LoginUserService {

    QaLoginUser selectOne(String accid, String appKey, String token);

    int insertUser(QaLoginUser user);

    int updateUser(QaLoginUser user);

}
