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
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import net.sf.json.JSONObject;

import com.dfire.testBase.TestBase;
import com.dfire.utils.MD5Utils;

/**
 * 保存门店设置（ /takeout/v2/save_settings）
 * @author xianmao
 * 用例：
 * 1:保存门店时候，必填字段（范围、营业时间、消费额、提前多久下厨）没填写，会提示
 * X2：不打开到店自取和预约次日开关，保存门店设置后，查看门店设置（ /takeout/v2/get_settings） 都同填写的一致
 * 3：打开到店自取和预约次日开关，保存门店设置后，查看门店设置（ /takeout/v2/get_settings） 都同填写的一致
 * 4：校验下提前多久下到厨房只能输入正整数（其他会报错）、最低起送金额（只能输入最多2为小数）
 * 5：保存门店设置后，查看外卖设置（/takeout/v1/get_takeout_setting）中的isFulfillBrandSetting应当为1
 */
public class SaveShopSettingTest extends TestBase{
	
	public static Logger logger = LoggerFactory.getLogger("biz");
    public static String caseid;
    public static String description;
    public static int exp_code;
    public static String msg;
    public static String latitude;
    public static String longitude;
    public static String latitude1;
    public static String longitude1;
    public static String latitude2;
    public static String longitude2;
    public static String latitude3;
    public static String longitude3;
    public static String orderAheadOfTime;
    public static int pickupFlag;
    public static int reserveTomorrowFlag;
    public static Long startPrice;

    Gson gson = new Gson();
    String http_url = getConfigForZG().get("url");
    String entityid = getConfigForZG().get("bossentityid");
    @Test(dataProvider = "CsvDataProvider")
	public void test(Map<String, String> data) throws Exception{
    	try{
    		// 读case
            caseid = StringHelper.convert2String(data.get("caseid"));
            description = StringHelper.convert2String(data.get("description"));
            exp_code = StringHelper.convert2int(data.get("exp_code"));
            msg = StringHelper.convert2String(data.get("msg"));
            latitude = StringHelper.convert2String(data.get("latitude"));
            longitude = StringHelper.convert2String(data.get("longitude"));
            latitude1 = StringHelper.convert2String(data.get("latitude1"));
            longitude1 = StringHelper.convert2String(data.get("longitude1"));
            latitude2 = StringHelper.convert2String(data.get("latitude2"));
            longitude2 = StringHelper.convert2String(data.get("longitude2"));
            latitude3 = StringHelper.convert2String(data.get("latitude3"));
            longitude3 = StringHelper.convert2String(data.get("longitude3"));
            orderAheadOfTime = StringHelper.convert2String(data.get("orderAheadOfTime"));
            pickupFlag = StringHelper.convert2int(data.get("pickupFlag"));
            reserveTomorrowFlag = StringHelper.convert2int(data.get("reserveTomorrowFlag"));
            startPrice = StringHelper.convert2Long(data.get("startPrice"));
    	}catch(Exception e){
    		e.printStackTrace();
    	}
		
        
        // 调用http服务
        Response response = null;
        try {
        	
        	List<String> pathUrl = new ArrayList<String>();
        	pathUrl.add(http_url);
        	pathUrl.add("/takeout/v1/save_shop_setting");
        	
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
    		
			JSONObject jsonObj_shop_setting = new JSONObject();//shop_setting对象
			JSONObject jsonObj_gpsMark = new JSONObject();//gpsMark对象
			JSONObject jsonObj_gpsData = new JSONObject();//gpsData对象
			JSONObject jsonObj_gpsData1 = new JSONObject();//gpsData对象1
			JSONObject jsonObj_gpsData2 = new JSONObject();//gpsData对象2,因为至少三个坐标
			JSONObject jsonObj_gpsData3 = new JSONObject();//gpsData对象3,因为至少三个坐标
			ArrayList<String> gpsarray = new ArrayList<String>();	
			jsonObj_gpsData.put("latitude", latitude);
			jsonObj_gpsData.put("longitude", longitude);
			jsonObj_gpsData1.put("latitude", latitude1);
			jsonObj_gpsData1.put("longitude", longitude1);
			jsonObj_gpsData2.put("latitude", latitude2);
			jsonObj_gpsData2.put("longitude", longitude2);
			jsonObj_gpsData3.put("latitude", latitude3);
			jsonObj_gpsData3.put("longitude", longitude3);
			gpsarray.add(jsonObj_gpsData.toString());
			gpsarray.add(jsonObj_gpsData1.toString());
			gpsarray.add(jsonObj_gpsData2.toString());
			gpsarray.add(jsonObj_gpsData3.toString());
			jsonObj_gpsMark.put("gpsPointVOs", gpsarray);
			jsonObj_gpsMark.put("id", takeoutFoodUntils.shopsetting().get("gpsmark_id"));   //gpsid，估计要自动生成一串数字
//			jsonObj_gpsMark.put("entityId", "99932390");
//			jsonObj_gpsMark.put("gpsName", "");
//			jsonObj_gpsMark.put("opTime", "20171018165835");
			jsonObj_shop_setting.put("gpsMark", jsonObj_gpsMark);
			jsonObj_shop_setting.put("id", takeoutFoodUntils.getdeliverysetting().get("id")); //保存门店需要的配送id，从配送接口获取
			jsonObj_shop_setting.put("orderAheadOfTime", orderAheadOfTime);//提前多久下单到厨房
			jsonObj_shop_setting.put("startPrice", startPrice); //起送价格
			jsonObj_shop_setting.put("pickupFlag", pickupFlag);//是否开启到店自取
			jsonObj_shop_setting.put("reserveTomorrowFlag", reserveTomorrowFlag);//是否开启预约次日
//			jsonObj_shop_setting.put("longitude", "120.134");
//			jsonObj_shop_setting.put("latitude", "30.2959");
//			jsonObj_shop_setting.put("hasOpenTimes", "1");
			params.put("shop_setting", gson.toJson(jsonObj_shop_setting));
			
			
			String sign = MD5Utils.generateSignForBossAPI(Constants.bossSecret, params);
			params.put("sign", sign);			

            response = httpRequest.postWithHeaders(pathUrl, httpHeader, new HashMap<String,String>(), params);
            
         
        } catch (Exception e) {
            logger.error("caseid: " + caseid + ",case描述: " + description + ",调用服务失败", e);
        }
        // 校验结果
        JsonObject resp = new JsonParser().parse(response.getResponseStr()).getAsJsonObject();
        if("1".equals(caseid)){
          	 Assert.assertEquals(resp.get("code").getAsInt(), 0);
          	 Assert.assertEquals(resp.get("message").getAsString(), msg,"必填项没填时候返回结果与期望不一致");
          }
        if("2".equals(caseid)){
        	 Map<String, Object> shopsettingmap = new HashMap<String, Object>(); 
        	 shopsettingmap = takeoutFoodUntils.shopsetting();//获取门店设置
        	 Assert.assertEquals(resp.get("code").getAsInt(), 1);
        	 Assert.assertEquals(resp.get("data").getAsString(), "success");
        	 Assert.assertEquals(startPrice.toString(), shopsettingmap.get("startPrice").toString()); //价格传入3位小数，应当只会取两位
        	 Assert.assertEquals(orderAheadOfTime.toString(), shopsettingmap.get("orderAheadOfTime").toString());
        	 Assert.assertEquals(reserveTomorrowFlag, shopsettingmap.get("reserveTomorrowFlag"));
        	 Assert.assertEquals(pickupFlag, shopsettingmap.get("pickupFlag"));
        	 Assert.assertEquals(longitude, shopsettingmap.get("gpsMark"));//校验下坐标设进去了
        	 
        }
        if("3".equals(caseid)){
       	 Assert.assertEquals(resp.get("code").getAsInt(), 0);
       	 Assert.assertEquals(resp.get("message").getAsString(), msg,"金额或起送价格格式不对的提示返回与期望不一致");
       }
       
	}

	
}
