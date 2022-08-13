package com.dora.httpframework.persisten.controller;

import com.dora.httpframework.persisten.service.LoginUserService;
import com.dora.httpframework.persisten.table.QaLoginUser;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @Describe 测试
 * @Author dora 1.0.1
 **/
@RestController
public class LoginUserController {

    @Resource
    private LoginUserService loginUserService;


    @RequestMapping("/selectLoginMsg")
    public Object selectLoginMsg(@RequestParam(value = "token", defaultValue = "VN8lVVB0ER5EStkiIQicaTJ03V3VURsy") String token,
                                 @RequestParam(value = "accid") String accid, @RequestParam(value = "appkey") String appKey) {
        return loginUserService.selectOne(accid, appKey, token);
    }


    @RequestMapping("/insertUser")
    public int insertUser(QaLoginUser user) {
        return loginUserService.insertUser(user);
    }

}
