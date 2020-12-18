package com.httpclient.entity;

import java.io.Serializable;

/**
 * 用户实体
 */
public class User implements Serializable {

    //姓名
    private String name;

    //年龄
    private Integer age;

    //性别
    private String gender;

    //座右铭
    private String motto;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getMotto() {
        return motto;
    }

    public void setMotto(String motto) {
        this.motto = motto;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", gender='" + gender + '\'' +
                ", motto='" + motto + '\'' +
                '}';
    }
}
