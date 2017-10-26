package com.dfire.takeoutFoodRebuild;

import static org.testng.Assert.assertEquals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.dfire.appRebuild.FireMemberInfoTest;
import com.dfire.test.util.StringHelper;
import com.dfire.utils.Constants;
import com.dfire.utils.Response;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.dfire.testBase.TestBase;
import com.dfire.utils.MD5Utils;

/**
 * xianmao test
 * @author xianmao
 *
 */
public class zgTest extends TestBase{
	
	public static Logger logger = LoggerFactory.getLogger("biz");
    public static String caseid;
    public static String description;
    public static int exp_code;
    public static String msg;
	public static String msg6;

    String http_url = getConfigForZG().get("url");
    @Test(dataProvider = "CsvDataProvider")
	public void test(Map<String, String> data) throws Exception{
		// 读case
        caseid = StringHelper.convert2String(data.get("caseid"));
        description = StringHelper.convert2String(data.get("description"));
        exp_code = StringHelper.convert2int(data.get("exp_code"));
        msg = StringHelper.convert2String(data.get("msg"));
        
        // 调用http服务
        Response response = null;
        try {
//	    	System.out.println("获取外卖设置1---------"+takeoutFoodUntils.gettakeoutsetting()); 
//	    	System.out.println("获取外卖设置5---------"+takeoutFoodUntils.shopsetting());
	    	
//	    	System.out.println("获取门店设置2---------"+takeoutFoodUntils.gettimes());  
//	    	System.out.println("获取配送设置3---------"+takeoutFoodUntils.getdeliverysetting());  
	    	System.out.println("获取配送价格列表4---------"+takeoutFoodUntils.getdistributionpricelist());    
	    	
//	    	takeoutFoodUntils.removetimes("id");//删除外卖时间段
//	    	takeoutFoodUntils.removedeliveryprice("id");//删除配送价格
//        	List<String> pathUrl = new ArrayList<String>();
//        	pathUrl.add(http_url);
//        	pathUrl.add("/takeout/v1/get_times");
//        	
//			Map<String, String> httpHeader = new HashMap<String, String>();		
//		    httpHeader.put("version", Constants.bossVersion);
//		    httpHeader.put("sessionId", Constants.bossSessionId);
//		    httpHeader.put("isTest", Constants.bossIsTest);
//        	
//            Map<String, String> params = new HashMap<String, String>();
//    		params.put("app_key", Constants.bossAppKey);
//			params.put("app_version", Constants.bossAppVersion);
//			params.put("device_id", Constants.bossDeviceId);
//
//			params.put("format", Constants.bossFormat);
//			params.put("s_apv", Constants.bossAppVersion);
//			params.put("session_key", Constants.bossSessionKey);
//			params.put("timeStamp", Long.toString(System.currentTimeMillis()));
//			params.put("version_code", Constants.bossVersionCode);  
//			params.put("entityId", Constants.bossentityid);  
//    		
//			String sign = MD5Utils.generateSignForBossAPI(Constants.bossSecret, params);
//			params.put("sign", sign);			
//
//            response = httpRequest.get(pathUrl, params, httpHeader);
//            System.out.println("111111111111111请求返回:"+response);
            
         
        } catch (Exception e) {
            logger.error("caseid: " + caseid + ",case描述: " + description + ",调用服务失败", e);
        }
//        // 校验结果
//        JsonObject resp = new JsonParser().parse(response.getResponseStr()).getAsJsonObject();
//        Assert.assertEquals(resp.get("code").getAsInt(), 1);
//        System.out.println("------------"+resp.get("data").getAsJsonArray().get(0).getAsJsonObject().get("entityId").getAsString());
//        Assert.assertEquals(resp.get("data").getAsJsonArray().get(0).getAsJsonObject().get("entityId").getAsString(), "99932390");

	}

	
}
