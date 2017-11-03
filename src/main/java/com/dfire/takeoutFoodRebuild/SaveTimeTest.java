package com.dfire.takeoutFoodRebuild;

import static org.testng.Assert.assertEquals;

import java.net.URLEncoder;
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
import com.dfire.utils.HttpRequestEx;
import com.dfire.utils.Response;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import net.sf.json.JSONObject;

import com.dfire.testBase.TestBase;
import com.dfire.utils.MD5Utils;

/**
 * 保存和修改外卖时段（  /takeout/v1/save_time）
 * @author xianmao
 * 用例：
 * 1:保存外卖时间段，查看返回信息正确；且查看下外卖时间段列表（ /takeout/v1/get_times）显示正确
 * 2：保存相同的外卖时间段，会有提示 
 * 3：结束时间如果小于开始时间，会提示；配送时间如果不在开始和结束之内会提示；
 * 4：库存无上限时候保存，查看返回正确；库存有上限时候查看返回正确；
 * 5：修改外卖时间段，修改后返回正确； 且查看下外卖时间段列表（ /takeout/v1/get_times）显示正确
 * X6：必填的不传，会提示
 */
public class SaveTimeTest extends TestBase{
	
	public static Logger logger = LoggerFactory.getLogger("biz");
    public static String caseid;
    public static String description;
    public static int exp_code;
    public static String msg;
    public static Long beginTime;
    public static Long endTime;
    public static int num;
    public static int lastVer;
    public static int isValid;
    public static int opTime;
    public static int createTime;
    public static int startDeliveryTime;

    private Gson gson = new Gson();
    String http_url = getConfigForZG().get("url");
    String entityid = getConfigForZG().get("bossentityid");
    
	
    @Test(dataProvider = "CsvDataProvider")
	public void test(Map<String, String> data) throws Exception{
    	// 读case
        description = StringHelper.convert2String(data.get("description"));
        exp_code = StringHelper.convert2int(data.get("exp_code"));
        msg = StringHelper.convert2String(data.get("msg"));
        caseid = StringHelper.convert2String(data.get("caseid"));
        beginTime = StringHelper.convert2Long(data.get("beginTime"));
        endTime = StringHelper.convert2Long(data.get("endTime"));
        num = StringHelper.convert2int(data.get("num"));
        lastVer = StringHelper.convert2int(data.get("lastVer"));
        isValid = StringHelper.convert2int(data.get("isValid"));
        opTime = StringHelper.convert2int(data.get("opTime"));
        createTime = StringHelper.convert2int(data.get("createTime"));
        startDeliveryTime = StringHelper.convert2int(data.get("startDeliveryTime"));
        
    	if(caseid.equals("1")||caseid.equals("4")){
    		//如先查看之前又没有时间段，如果存在就删光(只有第1/4条用例需要执行)
    		int numoftimes = (int) takeoutFoodUntils.gettimes().get("length"); //获取外卖时间段共有几条数据
    		
    		for(int i=1;i<numoftimes;i++){
    			takeoutFoodUntils.removetimes((String) takeoutFoodUntils.gettimes().get("id0"));
    		}
    	}
		
        // 调用http服务
        Response response = null;
        try {
        	
        	List<String> pathUrl = new ArrayList<String>();
        	pathUrl.add(http_url);
        	pathUrl.add("/takeout/v1/save_time");
        	
			Map<String, String> httpHeader = new HashMap<String, String>();		
		    httpHeader.put("version", Constants.bossVersion);
		    httpHeader.put("sessionId", Constants.bossSessionId);
		    httpHeader.put("isTest", Constants.bossIsTest);
        	
            Map<String, String> params = new HashMap<String, String>();
    		params.put("app_key", Constants.bossAppKey);
			params.put("app_version", Constants.bossAppVersion);
			params.put("device_id", Constants.bossDeviceId);

			params.put("format", Constants.bossFormat);
			params.put("s_apv", Constants.bossAppVersion);
			params.put("session_key", Constants.bossSessionKey);
			params.put("timeStamp", Long.toString(System.currentTimeMillis()));
			params.put("version_code", Constants.bossVersionCode);  
			params.put("entityId", entityid);  
    		

			JSONObject jsonObj_savetime = new JSONObject();
			jsonObj_savetime.put("beginTime", beginTime);
			jsonObj_savetime.put("endTime", endTime);
			jsonObj_savetime.put("num", num); //添加库存
			jsonObj_savetime.put("startDeliveryTime", startDeliveryTime); 
			jsonObj_savetime.put("lastVer", lastVer);
			jsonObj_savetime.put("isValid", isValid);
			jsonObj_savetime.put("opTime", opTime);
			jsonObj_savetime.put("createTime", createTime);
			if("5".equals(caseid)||"4".equals(caseid)){
			jsonObj_savetime.put("id", takeoutFoodUntils.gettimes().get("id0")); //修改外卖时间段时需传入id
			}
			params.put("time_json", gson.toJson(jsonObj_savetime));
			
			
			String sign = MD5Utils.generateSignForBossAPI(Constants.bossSecret, params);
			params.put("sign", sign);
			
			response = httpRequest.postWithHeaders(pathUrl, httpHeader,  new HashMap<String, String>(), params);
            
         
        } catch (Exception e) {
            logger.error("caseid: " + caseid + ",case描述: " + description + ",调用服务失败", e);
        }
        //校验结果
        JsonObject resp = new JsonParser().parse(response.getResponseStr()).getAsJsonObject();
        
        if("1".equals(caseid)){
        	Assert.assertEquals(resp.get("code").getAsInt(), 1);
            Assert.assertEquals(resp.get("data").getAsJsonObject().get("entityId").getAsString(), entityid,"店铺id返回与期望不一致");
            Assert.assertEquals(resp.get("data").getAsJsonObject().get("num").getAsString(), "-1","外卖限量值返回与期望不一致");
            Assert.assertEquals(resp.get("data").getAsJsonObject().get("beginTime").getAsString(), beginTime.toString(),"开始时间返回与期望不一致");
            Assert.assertEquals(resp.get("data").getAsJsonObject().get("endTime").getAsString(), endTime.toString(),"结束时间返回与期望不一致");
            Assert.assertEquals(resp.get("data").getAsJsonObject().get("startDeliveryTime").getAsString(), String.valueOf(startDeliveryTime),"开始配送时间返回与期望不一致");
        }
        if("2".equals(caseid)){
        	Assert.assertEquals(resp.get("code").getAsInt(), 0);
            Assert.assertEquals(resp.get("message").getAsString(), msg,"添加相同的时间段返回提示与期望不一致");
        }
        if("3".equals(caseid)){
        	Assert.assertEquals(resp.get("code").getAsInt(), 0);
            Assert.assertEquals(resp.get("message").getAsString(), msg,"结束时间应当大于开始时间");
        }
        if("4".equals(caseid)){
        	Assert.assertEquals(resp.get("code").getAsInt(), 1);
            Assert.assertEquals(takeoutFoodUntils.gettimes().get("num0"), 20,"外卖数量返回与期望设置的不一致");
        }
        if("5".equals(caseid)){
        	Assert.assertEquals(resp.get("code").getAsInt(), 1);
        	Map<String,Object> updateout = new HashMap<String,Object>();
        	updateout = takeoutFoodUntils.gettimes();	
        	Assert.assertEquals(takeoutFoodUntils.secToTime(beginTime.intValue()), updateout.get("beginTime1").toString(),"修改后的外卖开始时间与期望的不一致");  //校验修改后的开始时间值值跟查询出来一致
        	Assert.assertEquals(takeoutFoodUntils.secToTime(endTime.intValue()), updateout.get("endTime1").toString(),"修改后的外卖结束时间与期望的不一致");
        	Assert.assertEquals(null, updateout.get("num"),"修改后的外卖上限与期望的不一致");
        	Assert.assertEquals(startDeliveryTime, updateout.get("startDliveryTime1"),"修改后的外卖开始配送时间与期望的不一致");
        }
        
        
        

	}

	
}
