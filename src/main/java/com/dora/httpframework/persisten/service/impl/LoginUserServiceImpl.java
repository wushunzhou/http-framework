package com.dora.httpframework.persisten.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.dora.httpframework.persisten.mapper.LoginUserMapper;
import com.dora.httpframework.persisten.service.LoginUserService;
import com.dora.httpframework.persisten.table.QaLoginUser;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Describe 数据库交互
 * @Author dora 1.0.1
 **/
@Service
public class LoginUserServiceImpl implements LoginUserService {
    @Resource
    private LoginUserMapper loginUserMapper;


    @Override
    public QaLoginUser selectOne(String accid, String appKey, String token) {
        return loginUserMapper.selectOne(new QueryWrapper<QaLoginUser>().select("id", "uin", "session", "flag", "effective_time")
                .eq("accid", accid).eq("app_key", appKey).eq("token", token).last("limit 1"));
    }

    @Override
    public int insertUser(QaLoginUser user) {
        return loginUserMapper.insert(user);
    }

    @Override
    public int updateUser(QaLoginUser user) {
        QaLoginUser entity = new QaLoginUser();
        entity.setAccid(user.getAccid());
        entity.setAppKey(user.getAppKey());
        entity.setToken(user.getToken());

        LambdaUpdateWrapper<QaLoginUser> wrapper = Wrappers.<QaLoginUser>lambdaUpdate()
                .set(QaLoginUser::getUin, user.getUin())
                .set(QaLoginUser::getSession, user.getSession())
                .set(QaLoginUser::getEffectiveTime, user.getEffectiveTime())
                .set(QaLoginUser::getFlag, user.getFlag())
                .setEntity(entity);

        return loginUserMapper.update(null, wrapper);
    }

}
