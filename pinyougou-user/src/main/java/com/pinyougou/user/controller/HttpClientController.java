package com.pinyougou.user.controller;

import com.httpclient.entity.User;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * httpClient测试
 */
@RestController
@RequestMapping("/httpClient")
public class HttpClientController {

    /**
     * GET无参
     *
     * 测试数据
     */
    @RequestMapping("/doGetControllerOne")
    public void doGetControllerOne(){
        System.out.println("get无参无返回值!");
    }

    /**
     *GET有参
     */
    @RequestMapping("/doGetControllerTwo")
    public String doGetControllerTwo(String name,Integer age){
        return "没想到["+name+"]都"+age+"岁了!";
    }

    /**
     * post无参
     */
    @RequestMapping(value = "/doPostControllerOne",method = RequestMethod.POST)
    public void doPostControllerOne(){
        System.out.println("post无参测试");
    }

    /**
     *post有参(普通参数)
     *
     * 测试数据
     */
    @RequestMapping(value = "/doPostControllerFour",method = RequestMethod.POST)
    public String doPostControllerFour(String name,Integer age){
        return "name="+name+age;
    }

    /**
     * POST有参(对象参数)
     */
    @RequestMapping(value="/doPostControllerTwo",method=RequestMethod.POST)
    public String doPostControllerTwo(@RequestBody User user){
        return user.toString();
    }

    /**
     *POST有参(普通参数+对象参数)
     */
    @RequestMapping(value="doPostControllerThree",method=RequestMethod.POST)
    public String doPostControllerThree(@RequestBody User user,Integer flag,String meaning){
        System.out.println(flag);
        System.out.println(meaning);
        return user.toString()+"\n"+flag+"\n"+meaning;
    }

}
