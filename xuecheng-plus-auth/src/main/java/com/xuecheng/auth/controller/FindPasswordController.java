package com.xuecheng.auth.controller;

import com.xuecheng.base.model.RestResponse;
import com.xuecheng.ucenter.model.dto.AuthParamsDto;
import com.xuecheng.ucenter.service.FindPasswordAuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class FindPasswordController {

    @Autowired
    FindPasswordAuthService findPasswordAuthService;

    @RequestMapping("/findpassword")
    public RestResponse findPassword(@RequestBody AuthParamsDto authParamsDto) {
        return findPasswordAuthService.findPasswordAuth(authParamsDto);
    }
}
