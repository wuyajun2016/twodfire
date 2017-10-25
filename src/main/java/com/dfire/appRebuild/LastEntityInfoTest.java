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
 *
 * 获取上次消费店铺详情shop/info/v1/last_entity_info
 * param: entity_id、seat_code
 * http://10.1.24.109:8080/consumer-api/shop/info/v1/last_entity_info?uid=13675832423&format=json&appKey=100011&seat_code=A8&entity_id=99932333&timestamp=1504074928622&token=XIANMAOTEST
 */
public class LastEntityInfoTest extends TestBase{
	
	public static Logger logger = LoggerFactory.getLogger("biz");
    public static String caseid;
    public static String description;
    public static String entity_id;
    public static String seat_code;
    public static int exp_code;
    public static String msg;
    

    String http_url = com.dfire.testBase.TestBase.getConfigForAPP();
	@Test(dataProvider = "CsvDataProvider")
	public void test(Map<String, String> data) throws Exception{
		// 读case
        caseid = StringHelper.convert2String(data.get("caseid"));
        description = StringHelper.convert2String(data.get("description"));
        entity_id = StringHelper.convert2String(data.get("entity_id"));
        seat_code = StringHelper.convert2String(data.get("seat_code"));
        exp_code = StringHelper.convert2int(data.get("exp_code"));
        msg = StringHelper.convert2String(data.get("msg"));
        
        // 调用http服务
        Response response = null;
        try {
        	
        	List<String> patUrl = new ArrayList<String>();
        	patUrl.add(http_url);
        	patUrl.add("/shop/info/v1/last_entity_info");
        	

            Map<String, String> params = new HashMap<String, String>();
            params.put("appKey",Constants.APPKEY );
            params.put("format", Constants.FORMAT);
            params.put("uid", Constants.UID);
            params.put("timestamp", Long.toString(System.currentTimeMillis()));
            params.put("token",Constants.XTOKENFORAPP );
            //caseid为4时不传entity_id
            if (!"4".equals(caseid) ) {
            	params.put("entity_id",entity_id);
            }
            
            params.put("seat_code",seat_code);
            
            response = httpRequest.get(patUrl, params);
        } catch (Exception e) {
            logger.error("caseid: " + caseid + ",case描述: " + description + ",调用服务失败", e);
        }
        // 校验结果
        JsonObject resp = new JsonParser().parse(response.getResponseStr()).getAsJsonObject();
        Assert.assertEquals(resp.get("code").getAsInt(), exp_code);
        if("1".equals(resp.get("code"))){
        	if("1".equals(caseid) || "5".equals(caseid)){
        		Assert.assertEquals(resp.get("data").getAsJsonObject().get("entityId"), entity_id);
        	}
        	
        }
       
	}
		  
}
