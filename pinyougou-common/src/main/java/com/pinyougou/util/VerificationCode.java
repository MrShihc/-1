package com.pinyougou.util;

/**
 * 获取6位随机验证码  大小写字母和数字
 */
public class VerificationCode {

    public static String getRandomCode(){
        //从0到9的数字 48,49,50,51,52,53,54,55,56,57,
        //从A到Z的数字 65,66,67,68,69,70,71,72,73,74,75,76,77,78,79,80,81,82,83,84,85,86,87,88,89,90
        //从a到z的数字 97,98,99,100,101,102,103,104,105,106,107,108,109,110,111,112,113,114,115,116,117,118,119,120,121,122
        char[] arr = {48,49,50,51,52,53,54,55,56,57,65,66,67,68,69,70,71,72,73,74,75,76,77,78,79,80,81,82,83,84,85,86,87,88,89,90,97,98,99,100,101,102,103,104,105,106,107,108,109,110,111,112,113,114,115,116,117,118,119,120,121,122};
        int i=1;
        String cd = "";
        while(i<=6){ //循环六次，得到六位数的验证码
            char msg =arr[(int)(Math.random()*62)];
            cd+=msg;
            i++;
        }
        return cd;
    }

    public static void main(String[] args) {
        String randomCode = VerificationCode.getRandomCode();
        System.out.println(randomCode);
    }

}
