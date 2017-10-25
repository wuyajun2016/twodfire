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
 * 领券列表 /bz/district/v1/promotions
 * http://10.1.24.109:8080/consumer-api/bz/district/v1/promotions?uid=13675832423&format=json&index=1&appKey=100011&timestamp=1504488084834&token=XIANMAOTEST
 */
public class PromotionsListTest extends TestBase{
	
	public static Logger logger = LoggerFactory.getLogger("biz");
    public static String caseid;
    public static String description;
    public static String index;
    public static int exp_code;
    public static String msg;
    

    String http_url = com.dfire.testBase.TestBase.getConfigForAPP();
	@Test(dataProvider = "CsvDataProvider")
	public void test(Map<String, String> data) throws Exception{
		// 读case
        caseid = StringHelper.convert2String(data.get("caseid"));
        description = StringHelper.convert2String(data.get("description"));
        index = StringHelper.convert2String(data.get("index"));
        exp_code = StringHelper.convert2int(data.get("exp_code"));
        msg = StringHelper.convert2String(data.get("msg"));
        
        // 调用http服务
        Response response = null;
        try {
        	
        	List<String> patUrl = new ArrayList<String>();
        	patUrl.add(http_url);
        	patUrl.add("/bz/district/v1/promotions");
        	

            Map<String, String> params = new HashMap<String, String>();
            params.put("appKey",Constants.APPKEY );
            params.put("format", Constants.FORMAT);
            params.put("uid", Constants.UID);
            params.put("timestamp", Long.toString(System.currentTimeMillis()));
            params.put("token",Constants.XTOKENFORAPP );
            if (!"4".equals(caseid) ){
            	params.put("index", index);
            }
    		
            response = httpRequest.get(patUrl, params);
        } catch (Exception e) {
            logger.error("caseid: " + caseid + ",case描述: " + description + ",调用服务失败", e);
        }
        // 校验结果
        JsonObject resp = new JsonParser().parse(response.getResponseStr()).getAsJsonObject();
        Assert.assertEquals(resp.get("code").getAsInt(), exp_code);
       /* if ("19".equals(caseid) ) {
        	Assert.assertNotNull(resp.get("data").getAsJsonObject().get("orderCount").getAsJsonObject().get("cart_instance_count"), "购物车没菜");
    	}*/
	}
		 
}
