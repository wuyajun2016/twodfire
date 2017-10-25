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
 * @author sangye  2017年9月1日
 *获取时间线信息       /food/dairy/v1/shop_track
 * http://10.1.24.109:8080/consumer-api/food/dairy/v1/shop_track?uid=13675832423&format=json&appKey=100011&page=1&current_page=1&timestamp=1504489398951&token=XIANMAOTEST&page_size=20
 */
public class ShopTrackTest extends TestBase{
	
	public static Logger logger = LoggerFactory.getLogger("biz");
    public static String caseid;
    public static String description;
    public static String current_page;
    public static String page_size;
    public static int exp_code;
    public static String msg;
    

    String http_url = com.dfire.testBase.TestBase.getConfigForAPP();
    @Test(dataProvider = "CsvDataProvider")
	public void test(Map<String, String> data) throws Exception{
		// 读case
        caseid = StringHelper.convert2String(data.get("caseid"));
        description = StringHelper.convert2String(data.get("description"));
        current_page = StringHelper.convert2String(data.get("current_page"));
        page_size = StringHelper.convert2String(data.get("page_size"));
        exp_code = StringHelper.convert2int(data.get("exp_code"));
        msg = StringHelper.convert2String(data.get("msg"));
        
        // 调用http服务
        Response response2 = null;
        try {
        	
            List<String> patUrl = new ArrayList<String>();
            patUrl.add(http_url);
            patUrl.add("/food/dairy/v1/shop_track");
        	
        	//入参
            Map<String, String> params = new HashMap<String, String>();
            params.put("appKey",Constants.APPKEY );
            params.put("format", Constants.FORMAT);
            params.put("uid", Constants.UID);
            params.put("timestamp", Long.toString(System.currentTimeMillis()));
            params.put("token",Constants.XTOKENFORAPP );
            params.put("page",Constants.PAGE );
            if(!"4".equals(caseid)){
            	params.put("page_size",page_size);
                params.put("current_page",current_page);
            }
            
            response2 = httpRequest.get(patUrl, params);
        } catch (Exception e) {
            logger.error("caseid: " + caseid + ",case描述: " + description + ",调用服务失败", e);
        }
       
        JsonObject resp = new JsonParser().parse(response2.getResponseStr()).getAsJsonObject();
        Assert.assertEquals(resp.get("code").getAsInt(), exp_code);
	}
		 
	
}
