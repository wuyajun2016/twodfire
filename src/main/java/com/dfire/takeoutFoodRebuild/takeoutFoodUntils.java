package com.dfire.takeoutFoodRebuild;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.dfire.test.util.DBHelper;
import com.dfire.testBase.TestBase;
import com.dfire.utils.Constants;
import com.dfire.utils.HttpRequestEx;
import com.dfire.utils.MD5Utils;
import com.dfire.utils.Response;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import net.sf.json.JSONObject;

/**
 * 外卖改造一期的公用类(把查询、删除的功能放到一起)
 * @author xianmao
 *
 */
public class takeoutFoodUntils extends TestBase{
	
	public static Logger logger = LoggerFactory.getLogger("biz");

	public static String http_url = getConfigForZG().get("url");

    public static String entityid = getConfigForZG().get("bossentityid");
	/**
	 * 获取外卖设置
	 * @return map
	 */
	public static Map<String, Object> gettakeoutsetting() {
		int brandBusinessStatus;  //打烊与否
		String id = null;   //外卖设置id
		int isFulfillBrandSetting; //门店设置情况
		int isFulfillDeliverySetting; //配送设置情况
		int isOpen; //是否营业
		int isOpenElectronicAccounts; //电子账户开通情况
		int isOut; //是否开通外卖
		int isSetBrandAddress; //店铺地址设置情况
		
        Response response = null;
        HttpRequestEx httpRequest = new HttpRequestEx();
        Map<String,Object> getts = new HashMap<String,Object>();
        try {
        	
        	List<String> pathUrl = new ArrayList<String>();
        	pathUrl.add(http_url);
        	pathUrl.add("/takeout/v1/get_takeout_setting");
        	
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
			params.put("entityId", entityid);  //抓包不会有这个字段，主要是为了模拟，这个店铺加入了白名单，必传
    		
			String sign = MD5Utils.generateSignForBossAPI(Constants.bossSecret, params);
			params.put("sign", sign);			

            response = httpRequest.get(pathUrl, params, httpHeader);
            Assert.assertEquals(response.getStatus(), 200);
            JsonObject resp = new JsonParser().parse(response.getResponseStr()).getAsJsonObject();
            Assert.assertEquals(resp.get("code").getAsInt(), 1,"获取外卖设置可能失败了！");
            
            brandBusinessStatus= resp.getAsJsonObject().get("data").getAsJsonObject().get("brandBusinessStatus").getAsInt();
            id= resp.getAsJsonObject().get("data").getAsJsonObject().get("id").getAsString();
            isFulfillBrandSetting= resp.getAsJsonObject().get("data").getAsJsonObject().get("isFulfillBrandSetting").getAsInt();
            isFulfillDeliverySetting= resp.getAsJsonObject().get("data").getAsJsonObject().get("isFulfillDeliverySetting").getAsInt();
            isOpen= resp.getAsJsonObject().get("data").getAsJsonObject().get("isOpen").getAsInt();
            isOpenElectronicAccounts= resp.getAsJsonObject().get("data").getAsJsonObject().get("isOpenElectronicAccounts").getAsInt();
            isOut= resp.getAsJsonObject().get("data").getAsJsonObject().get("isOut").getAsInt();
            isSetBrandAddress= resp.getAsJsonObject().get("data").getAsJsonObject().get("isSetBrandAddress").getAsInt();

            getts.put("brandBusinessStatus", brandBusinessStatus);
            getts.put("id", id);
            getts.put("isFulfillBrandSetting", isFulfillBrandSetting);
            getts.put("isFulfillDeliverySetting", isFulfillDeliverySetting);
            getts.put("isOpen", isOpen);
            getts.put("isOpenElectronicAccounts", isOpenElectronicAccounts);
            getts.put("isOut", isOut);
            getts.put("isSetBrandAddress", isSetBrandAddress);

		} catch (Exception e) {
			logger.error("获取外卖设置失败", e);
		}
		
		return getts;
	}
	
	/**
	 * 获取外卖时间段
	 * @return
	 */
	public static Map<String, Object> gettimes() {
		int beginTime;  //开始时间
		int endTime; //结束时间
		String  id; //一个时间区间对应一个id
		int num; //库存
		int startDliveryTime; //配送时间
		
        Response response = null;
        HttpRequestEx httpRequest = new HttpRequestEx();
        Map<String,Object> gett = new HashMap<String,Object>();
        try {
        	
        	List<String> pathUrl = new ArrayList<String>();
        	pathUrl.add(http_url);
        	pathUrl.add("/takeout/v1/get_times");
        	
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
			params.put("entityId", entityid);  //抓包不会有这个字段，主要是为了模拟，这个店铺加入了白名单，必传
    		
			String sign = MD5Utils.generateSignForBossAPI(Constants.bossSecret, params);
			params.put("sign", sign);			

            response = httpRequest.get(pathUrl, params, httpHeader);
            Assert.assertEquals(response.getStatus(), 200);
            JsonObject resp = new JsonParser().parse(response.getResponseStr()).getAsJsonObject();
            Assert.assertEquals(resp.get("code").getAsInt(), 1,"获取外卖时间段可能失败了！");

            for(int i = 0;i<resp.getAsJsonArray("data").size();i++){
            	beginTime= resp.getAsJsonArray("data").get(i).getAsJsonObject().get("beginTime").getAsInt();   
                endTime= resp.getAsJsonArray("data").get(i).getAsJsonObject().get("endTime").getAsInt();
                id= resp.getAsJsonArray("data").get(i).getAsJsonObject().get("id").getAsString();
                num= resp.getAsJsonArray("data").get(i).getAsJsonObject().get("num").getAsInt();
                startDliveryTime= resp.getAsJsonArray("data").get(i).getAsJsonObject().get("startDeliveryTime").getAsInt();
            	gett.put("beginTime"+i, secToTime(beginTime));
                gett.put("endTime"+i, secToTime(endTime));
                gett.put("id"+i, id);
                gett.put("num"+i, num);
                gett.put("startDliveryTime"+i, startDliveryTime);
            }
            gett.put("length", resp.getAsJsonArray("data").size());
            
            
		} catch (Exception e) {
			logger.error("获取可点外卖时段列表失败", e);
		}
		
		return gett;
	}
	
	/**
	 * 获取门店设置
	 * @return
	 */
	public static Map<String, Object> shopsetting() {
		String gpsmark_id;//gpsmark里对应派送范围id
		String gpsMark;  //派送范围
		int hasSetOpenTimes; //营业时间
		double  latitude; //店铺经度
		double  longitude; //店铺维度
		int orderAheadOfTime; //提前时间自动下厨房
		int pickupFlag; //是否支持客户到店自取
		int reserveTomorrowFlag; //次日外卖
		Long startPrice; //最低起送消费
		int hasOpenTimes;//是否营业
		
        Response response = null;
        HttpRequestEx httpRequest = new HttpRequestEx();
        Map<String,Object> getss = new HashMap<String,Object>();
        try {
        	
        	List<String> pathUrl = new ArrayList<String>();
        	pathUrl.add(http_url);
        	pathUrl.add("/takeout/v1/get_shop_setting");
        	
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
			params.put("entityId", entityid);  //抓包不会有这个字段，主要是为了模拟，这个店铺加入了白名单，必传
    		
			String sign = MD5Utils.generateSignForBossAPI(Constants.bossSecret, params);
			params.put("sign", sign);			

            response = httpRequest.get(pathUrl, params, httpHeader);
            Assert.assertEquals(response.getStatus(), 200);
            JsonObject resp = new JsonParser().parse(response.getResponseStr()).getAsJsonObject();
            Assert.assertEquals(resp.get("code").getAsInt(), 1,"获取门店设置可能失败了！");
//            hasSetOpenTimes= resp.getAsJsonArray("data").get(0).getAsJsonObject().get("takeOutTiemList").getAsInt();   
            orderAheadOfTime= resp.getAsJsonObject().get("data").getAsJsonObject().get("orderAheadOfTime").getAsInt();
            reserveTomorrowFlag= resp.getAsJsonObject().get("data").getAsJsonObject().get("reserveTomorrowFlag").getAsInt();
            startPrice= resp.getAsJsonObject().get("data").getAsJsonObject().get("startPrice").getAsLong();
            pickupFlag= resp.getAsJsonObject().get("data").getAsJsonObject().get("pickupFlag").getAsInt();
            gpsmark_id = resp.getAsJsonObject().get("data").getAsJsonObject().get("gpsMark").getAsJsonObject().get("id").getAsString();
            hasOpenTimes= resp.getAsJsonObject().get("data").getAsJsonObject().get("hasOpenTimes").getAsInt();
            gpsMark = resp.getAsJsonObject().get("data").getAsJsonObject().get("gpsMark").getAsJsonObject().get("gpsPointVOs").getAsJsonArray().get(0).getAsJsonObject().get("longitude").getAsString();
            
//            getss.put("beginTime", hasSetOpenTimes);
            getss.put("orderAheadOfTime", orderAheadOfTime);
            getss.put("reserveTomorrowFlag", reserveTomorrowFlag);
            getss.put("startPrice", startPrice);
            getss.put("pickupFlag", pickupFlag);
            getss.put("gpsmark_id", gpsmark_id);
            getss.put("hasOpenTimes", hasOpenTimes);
            getss.put("gpsMark", gpsMark);
            
		} catch (Exception e) {
			logger.error("获门店设置列表失败", e);
		}
		
		return getss;
	}
	
	/**
	 * 删除外卖时间段
	 * @param id
	 * @return
	 */
	public static void removetimes(String id) {
		
        HttpRequestEx httpRequest = new HttpRequestEx();
        Response response = null;
        try {
        	
        	List<String> pathUrl = new ArrayList<String>();
        	pathUrl.add(http_url);
        	pathUrl.add("/takeout/v1/remove_time");
        	
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
			params.put("entityId", entityid);  //抓包不会有这个字段，主要是为了模拟，这个店铺加入了白名单，必传
			params.put("id", id);
			
			String sign = MD5Utils.generateSignForBossAPI(Constants.bossSecret, params);
			params.put("sign", sign);			

            response = httpRequest.get(pathUrl, params, httpHeader);
            Assert.assertEquals(response.getStatus(), 200);
            JsonObject resp = new JsonParser().parse(response.getResponseStr()).getAsJsonObject();
            Assert.assertEquals(resp.get("code").getAsInt(), 1,"删除外卖时间段可能失败了！");
    		Assert.assertEquals(resp.get("data").getAsInt(), 1);
            
		} catch (Exception e) {
			logger.error("删除外卖时间段失败", e);
		}

	}
	
	/**
	 * 获取配送设置
	 * @param id
	 * @return
	 */
    public static Map<String, Object> getdeliverysetting() {
//		Integer arriveTime = null;//预计接单多久后送达
//    	int deliveryMode = 0; //收费模式
    	ArrayList<Object> deliveryPrices = new ArrayList<Object>();//配送价格
    	ArrayList<Object> deliveryMans = new ArrayList<Object>();//送货人
    	String id = null;
    	Long outFee = null;//外送费用
    	int outFeeMode = 0;//收费模式
    	String deliveryTime = null;//预计多久送达
    	int endMeter;
    	int startMeter;
    	String price;
    	String meterid; //deliveryPrices中对应一条记录有一个id
    	
        HttpRequestEx httpRequest = new HttpRequestEx();
        Map<String,Object> getds = new HashMap<String,Object>();
        JSONObject jsb = new JSONObject();
        Gson gson = new Gson();
        Response response = null;
        try {
        	
        	List<String> pathUrl = new ArrayList<String>();
        	pathUrl.add(http_url);
        	pathUrl.add("/takeout/v1/get_delivery_settings");
        	
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
			params.put("entityId", entityid);  //抓包不会有这个字段，主要是为了模拟，这个店铺加入了白名单，必传
			
			String sign = MD5Utils.generateSignForBossAPI(Constants.bossSecret, params);
			params.put("sign", sign);			

			
            response = httpRequest.get(pathUrl, params, httpHeader);
            Assert.assertEquals(response.getStatus(), 200);
            JsonObject resp = new JsonParser().parse(response.getResponseStr()).getAsJsonObject();
            Assert.assertEquals(resp.get("code").getAsInt(), 1,"获取配送设置可能失败了！");
            int length = resp.getAsJsonObject().get("data").getAsJsonObject().get("deliveryPrices").getAsJsonArray().size();
            
            outFeeMode = resp.getAsJsonObject().get("data").getAsJsonObject().get("outFeeMode").getAsInt();
            id = resp.getAsJsonObject().get("data").getAsJsonObject().get("id").getAsString();
    		outFee = resp.getAsJsonObject().get("data").getAsJsonObject().get("outFee").getAsLong();
    		deliveryTime = resp.getAsJsonObject().get("data").getAsJsonObject().get("deliveryTime").getAsString();
    		
            for(int i=0;i<length;i++){
        		startMeter = resp.getAsJsonObject().get("data").getAsJsonObject().get("deliveryPrices").getAsJsonArray().get(i).getAsJsonObject().get("startMeter").getAsInt(); 
        		endMeter = resp.getAsJsonObject().get("data").getAsJsonObject().get("deliveryPrices").getAsJsonArray().get(i).getAsJsonObject().get("endMeter").getAsInt(); 
        		price = resp.getAsJsonObject().get("data").getAsJsonObject().get("deliveryPrices").getAsJsonArray().get(i).getAsJsonObject().get("price").getAsString(); 
        		meterid = resp.getAsJsonObject().get("data").getAsJsonObject().get("deliveryPrices").getAsJsonArray().get(i).getAsJsonObject().get("id").getAsString(); 
        		jsb.put("startMeter"+i, startMeter);
        		jsb.put("endMeter"+i, endMeter);
        		jsb.put("price"+i, price);
        		jsb.put("meterid"+i, meterid);
        		deliveryPrices.add(jsb);	
            }
            getds.put("id", id);
    		getds.put("outFee", outFee);
    		getds.put("outFeeMode", outFeeMode);
    		getds.put("deliveryTime", deliveryTime);
    		getds.put("deliveryPrices", gson.toJson(deliveryPrices));
    		       
		} catch (Exception e) {
			logger.error("获取配送设置失败", e);
		}
		
		return getds;
	}
	
    /**
     * 获取配送价格列表
     * @return
     */
    public static Map<String, Object> getdistributionpricelist() {
		Integer endMeter;  //结束距离
		String id; 
		float  price; //价格
		Integer  startMeter; //开始距离
		
        Response response = null;
        HttpRequestEx httpRequest = new HttpRequestEx();
        Map<String,Object> getss = new HashMap<String,Object>();
        try {
        	
        	List<String> pathUrl = new ArrayList<String>();
        	pathUrl.add(http_url);
        	pathUrl.add("/takeout/v1/get_distribution_price_list");
        	
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
			params.put("entityId", entityid);  //抓包不会有这个字段，主要是为了模拟，这个店铺加入了白名单，必传
    		
			String sign = MD5Utils.generateSignForBossAPI(Constants.bossSecret, params);
			params.put("sign", sign);			

            response = httpRequest.get(pathUrl, params, httpHeader);
            Assert.assertEquals(response.getStatus(), 200);
            JsonObject resp = new JsonParser().parse(response.getResponseStr()).getAsJsonObject();
            Assert.assertEquals(resp.get("code").getAsInt(), 1,"获取配送价格列表可能失败了！");
            for(int i = 0;i<resp.getAsJsonArray("data").size();i++){
            	startMeter= resp.getAsJsonArray("data").get(i).getAsJsonObject().get("startMeter").getAsInt();   
                endMeter= resp.getAsJsonArray("data").get(i).getAsJsonObject().get("endMeter").getAsInt();
                id= resp.getAsJsonArray("data").get(i).getAsJsonObject().get("id").getAsString();
                price= resp.getAsJsonArray("data").get(i).getAsJsonObject().get("price").getAsFloat();
                getss.put("startMeter"+i, startMeter);
                getss.put("endMeter"+i, endMeter);
                getss.put("id"+i, id);
                getss.put("price"+i, price);
            }
            
            getss.put("length", resp.getAsJsonArray("data").size());
            
		} catch (Exception e) {
			logger.error("获门配送价格列表失败", e);
		}
		
		return getss;
	}
	
    /**
     * 删除配送价格
     * @param id
     * @return
     */
    public static void removedeliveryprice(String id) {
		
        HttpRequestEx httpRequest = new HttpRequestEx();
        Response response = null;
        try {
        	
        	List<String> pathUrl = new ArrayList<String>();
        	pathUrl.add(http_url);
        	pathUrl.add("/takeout/v1/remove_delivery_price");
        	
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
			params.put("entityId", entityid);  //抓包不会有这个字段，主要是为了模拟，这个店铺加入了白名单，必传
			params.put("id", id);
			
			String sign = MD5Utils.generateSignForBossAPI(Constants.bossSecret, params);
			params.put("sign", sign);			

            response = httpRequest.get(pathUrl, params, httpHeader);
            Assert.assertEquals(response.getStatus(), 200);
            JsonObject resp = new JsonParser().parse(response.getResponseStr()).getAsJsonObject();
            Assert.assertEquals(resp.get("code").getAsInt(), 1,"删除配送价格可能失败了！");
    		Assert.assertEquals(resp.get("data").getAsInt(), 1);
            
		} catch (Exception e) {
			logger.error("删除配送价格失败", e);
		}
		
	}


	/**
	 * 秒数转换成时间
	 * @param time
	 * @return
	 */
	  public static String secToTime(int time) {  
	        String timeStr = null;  
	        int hour = 0;  
	        int minute = 0;  
	        int second = 0;  
	        if (time <= 0)  
	            return "00:00";  
	        else {  
	            minute = time / 60;  
	            if (minute < 60) {  
	                second = time % 60;  
	                timeStr = unitFormat(minute) + ":" + unitFormat(second);  
	            } else {  
	                hour = minute / 60;  
	                if (hour > 99)  
	                    return "99:59:59";  
	                minute = minute % 60;  
	                second = time - hour * 3600 - minute * 60;  
	                timeStr = unitFormat(hour) + ":" + unitFormat(minute) + ":" + unitFormat(second);  
	            }  
	        }  
	        return timeStr;  
	    }  
	  
	    public static String unitFormat(int i) {  
	        String retStr = null;  
	        if (i >= 0 && i < 10)  
	            retStr = "0" + Integer.toString(i);  
	        else  
	            retStr = "" + i;  
	        return retStr;  
	    }  
	    
	    //查询数据库
        @Test
        public void test(){
                
                DBHelper.setdruidDataSource(druidDataSource);
                
                    Map<String, String> conditions = new HashMap<String, String>();
                    conditions.put("id", "0ea42c7e3bbb4e1f98d6c0b3348a9c11");
                
                System.out.println(DBHelper.query("delivery_price", conditions));
        }

}
