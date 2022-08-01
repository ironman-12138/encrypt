package com.xtn.encrypt.controller;

import com.xtn.encrypt.annotation.CheckSign;
import com.xtn.encrypt.annotation.Decrypt;
import com.xtn.encrypt.annotation.Encrypt;
import com.xtn.encrypt.config.EncryptEnum;
import com.xtn.encrypt.utils.result.Result;
import com.xtn.encrypt.utils.result.ResultUtil;
import com.xtn.encrypt.vo.DataMark;
import com.xtn.encrypt.vo.TestSignVo;
import com.xtn.encrypt.vo.TestVo;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xCoder
 */
@RestController
@RequestMapping("/test")
public class TestController {

    @GetMapping("dataMask")
    public Result<List<DataMark>> dataMask() {
        List<DataMark> list = new ArrayList<>();
        DataMark dataMark1 = new DataMark();
        dataMark1.setName("张三");
        dataMark1.setPhone("17888999888");
        dataMark1.setIdCard("325627199610222731");
        DataMark dataMark2 = new DataMark();
        dataMark2.setName("啊啊啊");
        dataMark2.setPhone("15688999222");
        dataMark2.setIdCard("335627199610222738");
        DataMark dataMark3 = new DataMark();
        dataMark3.setName("欧阳数据");
        dataMark3.setPhone("19888999456");
        dataMark3.setIdCard("33562719961022290x");
        list.add(dataMark1);
        list.add(dataMark2);
        list.add(dataMark3);
        return ResultUtil.ok(list);
    }

    @Encrypt(encryptType = EncryptEnum.RSA)
    @GetMapping("encrypt")
    public Result<TestVo> encrypt(){
        TestVo testVo = new TestVo();
        testVo.setName("xxx");
        testVo.setTimestamp("11111123333");
        return ResultUtil.ok(testVo);
    }

    @GetMapping("decrypt")
    public Result<String> decrypt(@RequestBody @Decrypt(encryptType = EncryptEnum.RSA) TestVo vo){
        System.out.println(vo.getName());
        System.out.println(vo.getTimestamp());
        return ResultUtil.ok();
    }

    @CheckSign
    @GetMapping("checkSign")
    public Result<String> checkSign(@RequestBody TestSignVo vo){
        System.out.println(vo.getName());
        System.out.println(vo.getTimestamp());
        return ResultUtil.ok();
    }

}
