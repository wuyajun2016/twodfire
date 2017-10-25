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
 * @author sangye  2017年9月1日
 *
 * 找店页&搜索结果  /search/v1/shop
 * nearbyShopBusinessParam	选填	object	
    businessId	业务id	string	@mock=ewret23203543t565
    businessType	业务类型（企业卡：company_card_id）	string	@mock=coupon_id
nearbyShopConditionParam	必填	object	
    additionKey|2	选填	array<object>	
        id	搜索条件类型id	string	@mock=$order('1','4')
        typeContents	搜索条件id集合	array<string>	@mock=$order('4.1','4.2')
    latitude	选填	string	@mock=纬度
    longitude	选填	string	@mock=经度
    page	选填	number	@mock=1
    pageSize	选填	number	@mock=10
    searchContent
 */
public class SearchShopTest extends TestBase{
	
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
        	patUrl.add("/search/v1/shop");
        	

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
        Assert.assertEquals(resp.get("code").getAsInt(), exp_code);
       /* if ("19".equals(caseid) ) {
        	Assert.assertNotNull(resp.get("data").getAsJsonObject().get("orderCount").getAsJsonObject().get("cart_instance_count"), "购物车没菜");
    	}*/
	}
		 
}
