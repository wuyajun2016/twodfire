package com.dfire.appRebuild;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.dfire.test.util.StringHelper;
import com.dfire.testBase.TestBase;
import com.dfire.utils.Constants;
import com.dfire.utils.Response;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * @author sangye  2017年8月30日
 * 获取个人中心数据/member/action/v1/fire_member_info
 * 
 */
public class FireMemberInfoTest extends TestBase{
	
	public static Logger logger = LoggerFactory.getLogger("biz");
    public static String caseid;
    public static String description;
    public static int exp_code;
    public static String msg;

    String http_url = com.dfire.testBase.TestBase.getConfigForAPP();
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
        	
        	List<String> patUrl = new ArrayList<String>();
        	patUrl.add(http_url);
        	patUrl.add("/member/action/v1/fire_member_info");
        	

            Map<String, String> params = new HashMap<String, String>();
            params.put("appKey",Constants.APPKEY );
            params.put("format", Constants.FORMAT);
            params.put("uid", Constants.UID);
            params.put("timestamp", Long.toString(System.currentTimeMillis()));
            params.put("token",Constants.XTOKENFORAPP );
    		
            response = httpRequest.get(patUrl, params);
            
         
        } catch (Exception e) {
            logger.error("caseid: " + caseid + ",case描述: " + description + ",调用服务失败", e);
        }
        // 校验结果
        JsonObject resp = new JsonParser().parse(response.getResponseStr()).getAsJsonObject();
        JsonObject resp2 = new JsonParser().parse(new FireMemberInfoTest().testGetMyLevel().getResponseStr()).getAsJsonObject();
        Assert.assertEquals(resp.get("code").getAsInt(), exp_code);
        Assert.assertEquals(resp.get("data").getAsJsonObject().get("customerRegisterId"), resp2.get("data").getAsJsonObject().get("customerId"), "customerId不一致");
        //校验火等级
        Assert.assertEquals(resp.get("data").getAsJsonObject().get("fireGrade"), resp2.get("data").getAsJsonObject().get("fireSeedLevel"), "fireSeedLevel不一致");
        
	}
	
	/*
	 * 我的火会员等级http://10.1.24.109:8080/consumer-api/fm/info/v1/level?appKey=100011&format=json&sign=7376f3ceb93d5f99f712a5c7b3bb713f&timestamp=1504073664981&uid=13675832423
	 */
	public Response testGetMyLevel() throws Exception{
		 Response response2 = null;
	        try {
	            List<String> pathTwoFireUrl = new ArrayList<String>();
	        	pathTwoFireUrl.add(http_url);
	        	pathTwoFireUrl.add("/fm/info/v1/level");
	        	
	        	//入参
	            Map<String, String> params2 = new HashMap<String, String>();
	            params2.put("appKey",Constants.APPKEY );
	            params2.put("format", Constants.FORMAT);
	            params2.put("uid", Constants.UID);
	            params2.put("timestamp", Long.toString(System.currentTimeMillis()));
	            params2.put("token",Constants.XTOKENFORAPP );
	            
	            response2 = httpRequest.get(pathTwoFireUrl, params2);
	            
	        } catch (Exception e) {
	            logger.error("caseid: " + caseid + ",case描述: " + description + ",调用服务失败", e);
	        }
	        return response2;
	}
		 
}
