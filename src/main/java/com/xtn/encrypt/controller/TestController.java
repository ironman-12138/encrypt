package com.xtn.encrypt.controller;

import com.xtn.encrypt.annotation.CheckSign;
import com.xtn.encrypt.annotation.Decrypt;
import com.xtn.encrypt.annotation.Encrypt;
import com.xtn.encrypt.config.EncryptEnum;
import com.xtn.encrypt.utils.result.Result;
import com.xtn.encrypt.utils.result.ResultUtil;
import com.xtn.encrypt.vo.TestVo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author xCoder
 */
@RestController
@RequestMapping("/test")
public class TestController {

    @Encrypt
    @GetMapping("encrypt")
    public Result<TestVo> encrypt(){
        TestVo testVo = new TestVo();
        testVo.setName("xxx");
        return ResultUtil.ok(testVo);
    }

    @CheckSign
    @GetMapping("decrypt")
    public Result<String> decrypt(@RequestBody TestVo vo){
        System.out.println(vo.getName());
        return ResultUtil.ok();
    }

}
