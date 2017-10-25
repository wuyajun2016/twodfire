package com.dfire.homepageopt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dfire.test.util.JsonHelper;
import com.dfire.test.util.StringHelper;
import com.dfire.testBase.TestBase;
import com.dfire.utils.Constants;
import com.dfire.utils.Response;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.twodfire.share.result.ResultMap;

/**
 * 扫码首页提速优化-H5扫码外卖接口  2017/8/14
 * @author sangye 
 * 
 */
public class GetStateForTakeoutTest extends TestBase{
	
	public static Logger logger = LoggerFactory.getLogger("biz");
    public static String caseid;
    public static String description;
    public static String entity_id;
    //public static String qr_code;
    public static int exp_code;
    public static String msg;
    

    String http_url = com.dfire.testBase.TestBase.getConfig();
    String xtoken = com.dfire.utils.TokenUtil.getToken();
    @Test(dataProvider = "CsvDataProvider")
    public void test(Map<String, String> data) throws Exception{
    	// 读case
        caseid = StringHelper.convert2String(data.get("caseid"));
        description = StringHelper.convert2String(data.get("description"));
        entity_id = StringHelper.convert2String(data.get("entity_id"));
     //   qr_code = StringHelper.convert2String(data.get("qr_code"));
        exp_code = StringHelper.convert2int(data.get("exp_code"));
        msg = StringHelper.convert2String(data.get("msg"));
        
        // 调用http服务
        Response response = null;
        try {
        	List<String> pathUrl = new ArrayList<String>();
        	pathUrl.add(http_url);
        	pathUrl.add("/initial/v1/");
        	pathUrl.add("get_state_for_takeout");
        	
            Map<String, String> params = new HashMap<String, String>();
            // params
            params.put("xtoken",xtoken);
            if(!"7".equals(caseid)){
           	 params.put("entity_id", entity_id);
           }
          //  params.put("qr_code", qr_code);
            response = httpRequest.get(pathUrl, params);
            System.out.println("response: " + JsonHelper.objectToString(response.getResponseStr()));
            
        } catch (Exception e) {
            logger.error("caseid: " + caseid + ",case描述: " + description + ",调用服务失败", e);
        }
        
        // 校验结果
        JsonObject resp = new JsonParser().parse(response.getResponseStr()).getAsJsonObject();
        Assert.assertEquals(resp.get("code").getAsInt(), exp_code);
        
        switch(caseid)
        {
        case "1":
        	Assert.assertEquals(resp.get("data").getAsJsonObject().get("takeout").getAsJsonObject().get("can_takeout").getAsInt(),Constants.TREU_FLAG,"外卖未开启");
        	break;
        case "2":
        	Assert.assertEquals(resp.get("data").getAsJsonObject().get("takeout").getAsJsonObject().get("can_takeout").getAsInt(),Constants.FLASE_FLAG,"外卖已开启");
        	break;
        }
        
    }
 
}
