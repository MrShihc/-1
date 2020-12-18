package com.pinyougou.util;


import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;

/**
 * 创作时间：2019/10/22 16:30
 * 作者：李增强
 */
public class SendInfo {

    /*
    pom.xml
    <dependency>
      <groupId>com.aliyun</groupId>
      <artifactId>aliyun-java-sdk-core</artifactId>
      <version>4.0.3</version>
    </dependency>

    解析json字符串的方式:
            String data = SendInfo.test01(tbSeller.getMobile(),newcode);
            Map map = JSON.parseObject(data, Map.class);
            String code1 = map.get("Code").toString();
    */
    public static String test01(String tel,String code) {
        DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou", "" +
                "", "");
        IAcsClient client = new DefaultAcsClient(profile);

        CommonRequest request = new CommonRequest();
        request.setMethod(MethodType.POST);
        request.setDomain("dysmsapi.aliyuncs.com");
        request.setVersion("2017-05-25");
        request.setAction("SendSms");
        request.putQueryParameter("RegionId", "cn-hangzhou");
        request.putQueryParameter("PhoneNumbers", tel);
        request.putQueryParameter("SignName", "Java短信验证");
        request.putQueryParameter("TemplateCode", "SMS_175573893");
        request.putQueryParameter("TemplateParam", "{'code':'" + code + "'}");
        try {
            CommonResponse response = client.getCommonResponse(request);
            return response.getData();
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            e.printStackTrace();
        }
        return null;
    }
}