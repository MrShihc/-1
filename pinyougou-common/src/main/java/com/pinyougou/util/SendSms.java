package com.pinyougou.util;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;

/**
 * 阿里云短信服务：
 * 注意：需要 签名名称、模版CODE 以及 RAM访问控制中的 AccessKeyID 和 AccessKeySecret
 */
public class SendSms {

    //产品名称:云通信短信API产品,开发者无需替换
    static final String product = "Dysmsapi";
    //产品域名,开发者无需替换
    static final String domain = "dysmsapi.aliyuncs.com";

    // TODO 此处需要替换成开发者自己的AK(在阿里云访问控制台寻找)
    static final String accessKeyId = "LTAI4G8EAswWP79XTNtHPG6o";  // TODO 修改成自己的
    static final String accessKeySecret = "83YK6bfb1AbEtuRn2kxubqjaeFpRQd";   // TODO 修改成自己的

    public static SendSmsResponse sendSms(String telephone, String code) throws ClientException {

        //可自助调整超时时间

        System.setProperty("sun.net.client.defaultConnectTimeout", "10000");

        System.setProperty("sun.net.client.defaultReadTimeout", "10000");

        //初始化acsClient,暂不支持region化

        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);

        DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);

        IAcsClient acsClient = new DefaultAcsClient(profile);

        //组装请求对象-具体描述见控制台-文档部分内容

        SendSmsRequest request = new SendSmsRequest();

        //必填:待发送手机号

        request.setPhoneNumbers(telephone);

        //必填:短信签名-可在短信控制台中找到

        request.setSignName("ABC商城");    // TODO 修改成自己的

        //必填:短信模板-可在短信控制台中找到

        request.setTemplateCode("SMS_205821039");    // TODO 修改成自己的

        //可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为

        //request.setTemplateParam("{\"name\":\"Tom\", \"code\":\"123\"}");

        request.setTemplateParam("{\"code\":\"" + code + "\"}");

        //选填-上行短信扩展码(无特殊需求用户请忽略此字段)

        //request.setSmsUpExtendCode("90997");

        //可选:outId为提供给业务方扩展字段,最终在短信回执消息中将此值带回给调用者

        request.setOutId("yourOutId");
        request.setOutId("yourOutId");

        //hint 此处可能会抛出异常，注意catch
        SendSmsResponse sendSmsResponse = acsClient.getAcsResponse(request);
        if(sendSmsResponse.getCode()!= null && sendSmsResponse.getCode().equals("OK")){
            System.out.println("短信发送成功！");
        }else {
            System.out.println("短信发送失败！");
        }
        return sendSmsResponse;
    }

    //以下为测试代码，随机生成验证码
    private static int newcode;

    public static int getNewcode() {
        return newcode;
    }

    public static void setNewcode(){
        newcode = (int)(Math.random()*9999)+100000;  //每次调用生成一位六位数的随机数
    }

    public static void main(String[] args) throws ClientException, InterruptedException {
        setNewcode();
        String code = Integer.toString(getNewcode());
        System.out.println("发送的验证码为："+code);
        //发短信
        SendSmsResponse response =sendSms("15932040663",code); // TODO 填写你需要测试的手机号码
        System.out.println("短信接口返回的数据----------------");
        System.out.println("Code=" + response.getCode());
        System.out.println("Message=" + response.getMessage());
        System.out.println("RequestId=" + response.getRequestId());
        System.out.println("BizId=" + response.getBizId());
    }
}
