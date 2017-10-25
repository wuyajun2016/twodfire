package com.dfire.appRebuild;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.dfire.test.util.JsonHelper;
import com.dfire.test.util.StringHelper;
import com.dfire.testBase.TestBase;
import com.dfire.utils.Constants;
import com.dfire.utils.Response;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.twodfire.share.result.ResultMap;

/**
 * @author sangye  2017年8月30日
 *
 * 卡累计省钱接口  /member/card/v1/card_save_money  
 * http://10.1.24.109:8080/consumer-api/member/card/v1/card_save_money?uid=15068129031&format=json&appKey=100011&timestamp=1504061962511&token=XIANMAOTEST
 */
public class CardSaveMoneyTest extends TestBase{
	
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
        	
        	List<String> pathFortwoFireAPP = new ArrayList<String>();
        	pathFortwoFireAPP.add(http_url);
        	pathFortwoFireAPP.add("/member/card/v1/card_save_money");
        	
        	//入参
            Map<String, String> params = new HashMap<String, String>();
            params.put("appKey",Constants.APPKEY );
            params.put("format", Constants.FORMAT);
           // params.put("uid", "15068129031");
            params.put("uid", Constants.UID);
            params.put("timestamp", Long.toString(System.currentTimeMillis()));
            params.put("token",Constants.XTOKENFORAPP );
    		
            response = httpRequest.get(pathFortwoFireAPP, params);
            
        } catch (Exception e) {
            logger.error("caseid: " + caseid + ",case描述: " + description + ",调用服务失败", e);
        }
        ;
        // 校验结果
        JsonObject resp = new JsonParser().parse(response.getResponseStr()).getAsJsonObject();
        JsonObject resp2 = new JsonParser().parse(new CardSaveMoneyTest().testGetQueryCardDataList().getResponseStr()).getAsJsonObject();
        
        Assert.assertEquals(resp.get("code").getAsInt(), exp_code);
        System.out.println("省钱金额：----"+resp.get("data").getAsJsonObject().get("savedMoney")); 
        Assert.assertEquals(resp.get("data").getAsJsonObject().get("cardNum"), resp2.get("data").getAsJsonArray().size(), "我的会员卡张数不一致");
       
	}
	
	/*
	 * 我的会员卡-获取会员卡张数http://10.1.24.109:8080/consumer-api/member/card/v3/query_card_data_list?uid=13675832423&format=json&page=1&page_size=50&appKey=100011&timestamp=1504061962511&token=XIANMAOTEST
	 */
	public Response testGetQueryCardDataList() throws Exception{
		 Response response2 = null;
	        try {
	        	 List<String> pathTwoFireUrl = new ArrayList<String>();
	         	pathTwoFireUrl.add(http_url);
	         	pathTwoFireUrl.add("/member/card/v3/query_card_data_list");
	         	
	         	//入参
	             Map<String, String> params2 = new HashMap<String, String>();
	             params2.put("appKey",Constants.APPKEY );
	             params2.put("format", Constants.FORMAT);
	             // params2.put("uid", "15068129031");
	             params2.put("uid", Constants.UID);
	             params2.put("timestamp", Long.toString(System.currentTimeMillis()));
	             params2.put("token",Constants.XTOKENFORAPP );
	             params2.put("page",Constants.PAGE );
	             params2.put("page_size",Constants.PAGE_SIZE );
	             
	             response2 = httpRequest.get(pathTwoFireUrl, params2);
	            
	        } catch (Exception e) {
	            logger.error("caseid: " + caseid + ",case描述: " + description + ",调用服务失败", e);
	        }
	        return response2;
	}
		 
	
}
