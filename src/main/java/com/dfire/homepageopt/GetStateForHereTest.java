package com.dfire.homepageopt;

import org.testng.annotations.Test;

import com.dfire.test.util.StringHelper;
import com.dfire.testBase.TestBase;
import com.dfire.utils.Constants;
import com.dfire.utils.Response;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.testng.annotations.BeforeTest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
/**
 * 扫码首页提速优化-H5扫码堂食接口  2017/8/14
 * @author sangye 
 * 
 */
public class GetStateForHereTest extends TestBase{
	
	public static Logger logger = LoggerFactory.getLogger("biz");
    public static String caseid;
    public static String description;
    public static String entity_id;
    public static String seat_code;
    public static String order_id;
    public static String qr_code;
    public static int exp_code;
    public static String msg;
    
    public static String url;

    String http_url = com.dfire.testBase.TestBase.getConfig();
    String xtoken = com.dfire.utils.TokenUtil.getToken();
    @Test(dataProvider = "CsvDataProvider")
    public void test(Map<String, String> data) throws Exception{
    	// 读case
        caseid = StringHelper.convert2String(data.get("caseid"));
        description = StringHelper.convert2String(data.get("description"));
        entity_id = StringHelper.convert2String(data.get("entity_id"));
        seat_code = StringHelper.convert2String(data.get("seat_code"));
        order_id = StringHelper.convert2String(data.get("order_id"));
        qr_code = StringHelper.convert2String(data.get("qr_code"));
        exp_code = StringHelper.convert2int(data.get("exp_code"));
        msg = StringHelper.convert2String(data.get("msg"));
        
        // 调用http服务
        Response response = null;
        try {
        	
        	List<String> pathUrl = new ArrayList<String>();
        	pathUrl.add(http_url);
        	pathUrl.add("/initial/v1/get_state_for_here");
        	
            Map<String, String> params = new HashMap<String, String>();
            // params
            params.put("xtoken", xtoken);
            params.put("entity_id", entity_id);
            params.put("seat_code", seat_code);
            params.put("order_id", order_id);
            params.put("qr_code", qr_code);
            response = httpRequest.get(pathUrl, params);
        } catch (Exception e) {
            logger.error("caseid: " + caseid + ",case描述: " + description + ",调用服务失败", e);
        }
        // 校验结果
        JsonObject resp = new JsonParser().parse(response.getResponseStr()).getAsJsonObject();
        Assert.assertEquals(resp.get("code").getAsInt(), exp_code);
        if ("19".equals(caseid) ) {
        	Assert.assertNotNull(resp.get("data").getAsJsonObject().get("orderCount").getAsJsonObject().get("cart_instance_count"), "购物车没菜");
    	}
        if ("20".equals(caseid) ) {
        	Assert.assertNotNull(resp.get("data").getAsJsonObject().get("orderCount").getAsJsonObject().get("waiting_order_id"), "没有待付款的订单");
    	}
        if ("21".equals(caseid) ) {
        	Assert.assertEquals(resp.get("data").getAsJsonObject().get("orderCount").getAsJsonObject().get("approved").getAsBoolean(),false, "没有待审核的订单");
    	}
        
    }
    
    @BeforeTest
    public void beforeTest() {
    }

    @AfterTest
    public void afterTest() {
    }
  

}
