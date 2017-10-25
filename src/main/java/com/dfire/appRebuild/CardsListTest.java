package com.dfire.appRebuild;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dfire.test.util.StringHelper;
import com.dfire.testBase.TestBase;
import com.dfire.utils.Constants;
import com.dfire.utils.Response;
import com.dfire.utils.SignUtil;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * @author sangye  2017年8月30日
 *  http://10.1.24.109:8080/consumer-api/bz/district/v1/cards?uid=13675832423&format=json&index=1&appKey=100011&timestamp=1504494585814&token=XIANMAOTEST
 * 领卡列表 /bz/district/v1/cards
 * 
 */
public class CardsListTest extends TestBase{
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
        
        switch(caseid)
        {
        case "6":
        	//关注店铺
        	Response payAttentionResp =	new CardsListTest().testGetPayAttention(Constants.ENTITY_ID_Daily);
        	break;
        case "7":
        	/*//获取关注店铺的卡、券
        	JsonObject listResp = new JsonParser().parse(new CardsListTest().testEntityOtherInfo(Constants.ENTITY_ID_Daily).getResponseStr()).getAsJsonObject();
        	//领卡
    		JSONObject object = JSON.parseObject( listResp.get("data").getAsJsonObject().get("privilege").getAsJsonObject().getAsJsonArray("canFetchedCardList").get(0).toString());
    	       String kindCardId = object.getJSONObject("cardData").get("kindCardId").toString();
        	if("1".equals(listResp.get("code"))){
        		Response cardPaymentres =	new CardsListTest().testCardPayment(Constants.ENTITY_ID_Daily,kindCardId);
        	}*/
        	
        	JsonObject newApplyCardResp = new JsonParser().parse(new CardsListTest().testNewApplyCard(Constants.Kind_CARD_ID_Daily).getResponseStr()).getAsJsonObject();
        	
        	Response cardPaymentres =	new CardsListTest().testCardPayment(Constants.ENTITY_ID_Daily,newApplyCardResp.get("data").getAsJsonObject().get("model").getAsJsonObject().get("cardId").toString());
        	break;
        case "8":
        	/*//获取未未关注店铺的卡、券
        	JsonObject listResp2 = new JsonParser().parse(new CardsListTest().testEntityOtherInfo(Constants.ENTITY_ID_Daily).getResponseStr()).getAsJsonObject();
        	JSONObject object2 = JSON.parseObject( listResp2.get("data").getAsJsonObject().get("privilege").getAsJsonObject().getAsJsonArray("canFetchedCardList").get(0).toString());
 	       String kindCardId2 = object2.getJSONObject("cardData").get("kindCardId").toString();
        	if("1".equals(listResp2.get("code"))){
        		//删除卡
        		Response cardPaymentres =	new CardsListTest().testRemoveCard(kindCardId2);
        	}*/
        	//JsonObject newApplyCardResp2 = new JsonParser().parse(new CardsListTest().testNewApplyCard(Constants.Kind_CARD_ID_Daily).getResponseStr()).getAsJsonObject();
        	JsonObject listResp2 = new JsonParser().parse(new CardsListTest().testRemoveCard("999323335dbb13d5015dedfdd622012a").getResponseStr()).getAsJsonObject();
        	break;
        case "9":
        	//取消关注店铺
        	Response cancelPayAttentionResp =	new CardsListTest().testCancelPayAttention(Constants.ENTITY_ID_Daily);
        	break;
        }
        
        // 调用http服务
        Response response = null;
        try {
        	List<String> patUrl = new ArrayList<String>();
        	patUrl.add(http_url);
        	patUrl.add("/bz/district/v1/cards");

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
        if("1".equals(resp.get("code"))){
        	Assert.assertNotNull(resp.get("data"), "数据不为空");
        }
  
        if("6".equals(caseid) || "7".equals(caseid) || "8".equals(caseid) || "9".equals(caseid)){
        //获取店铺下未领取的卡
      /* JsonObject cardListResp = new JsonParser().parse(new CardsListTest().testEntityOtherInfo(Constants.ENTITY_ID_Daily).getResponseStr()).getAsJsonObject();
        JSONObject object = JSON.parseObject( cardListResp.get("data").getAsJsonObject().get("privilege").getAsJsonObject().getAsJsonArray("canFetchedCardList").get(0).toString());
       String kindCardId =  object.getJSONObject("cardData").get("kindCardId").toString();
    		  */
    		   //getAsJsonObject().get("0").getAsJsonObject().get("cardData").getAsJsonObject().get("kindCardId").getAsString();
       if("6".equals(caseid)){
    	   //列表中显示关联店铺未领取的卡
    		   Assert.assertEquals(JSON.parseObject(resp.get("data").getAsJsonObject().getAsJsonArray("businessEntries").get(0).toString()).get("kindCardId").toString(), Constants.Kind_CARD_ID_Daily, "关注店铺失败，未找到该卡");
       }
       if("7".equals(caseid)){
   		//卡已领取，不显示该卡   
    	   Assert.assertNotEquals(JSON.parseObject(resp.get("data").getAsJsonObject().getAsJsonArray("businessEntries").get(0).toString()).get("kindCardId").toString(), Constants.Kind_CARD_ID_Daily, "领取卡失败，显示该卡");
       }
       if("8".equals(caseid)){
    	   //卡已删除，显示该卡   
    	   Assert.assertEquals(JSON.parseObject(resp.get("data").getAsJsonObject().getAsJsonArray("businessEntries").get(0).toString()).get("kindCardId").toString(),  Constants.Kind_CARD_ID_Daily, "卡删除失败，未找到该卡");
        }
       if("9".equals(caseid)){
     		//取消关注店铺，不显示该卡   
    	   Assert.assertNotEquals(JSON.parseObject(resp.get("data").getAsJsonObject().getAsJsonArray("businessEntries").get(0).toString()).get("kindCardId").toString(), Constants.Kind_CARD_ID_Daily, "取消关注店铺失败，显示该卡");
       }
       }
      
	}
	
	//关注店铺
	public Response testGetPayAttention(String entity_id) throws Exception{

        // 调用http服务
        Response payAttentionResp = null;
        try {
        	String url = http_url+"/shop/attention/v1/pay_attention";
            Map<String, String> form = new HashMap<String, String>();
            form.put("appKey",Constants.APPKEY );
            form.put("format", Constants.FORMAT);
            form.put("uid", Constants.UID);
            form.put("timestamp", Long.toString(System.currentTimeMillis()));
            form.put("token",Constants.XTOKENFORAPP );
            form.put("entity_id", entity_id);
            payAttentionResp = httpRequest.postHandle(url, form, 200000);
        } catch (Exception e) {
            logger.error("caseid: " + caseid + ",case描述: " + description + ",调用服务失败", e);
        }
        
        return payAttentionResp;
	}
	
	//取消关注店铺  
	public Response testCancelPayAttention(String entity_id) throws Exception{

        // 调用http服务
        Response cancelPayAttentionResp = null;
        try {
        	String url = http_url+"/shop/attention/v1/cancel_pay_attention";
            Map<String, String> form = new HashMap<String, String>();
            form.put("appKey",Constants.APPKEY );
            form.put("format", Constants.FORMAT);
            form.put("uid", Constants.UID);
            form.put("timestamp", Long.toString(System.currentTimeMillis()));
            form.put("token",Constants.XTOKENFORAPP );
            form.put("entity_id", entity_id);
            cancelPayAttentionResp = httpRequest.postHandle(url, form, 200000);
        } catch (Exception e) {
            logger.error("caseid: " + caseid + ",case描述: " + description + ",调用服务失败", e);
        }
        return cancelPayAttentionResp;
	}
	
	/*
	 * http://10.1.24.5:8080/consumer-api/member/card/v1/new_apply_card
	 */
	public Response testNewApplyCard(String kind_card_id) throws Exception{

        // 调用http服务
        Response newApplyCardResp = null;
        try {
        	String url = http_url+"/member/card/v1/new_apply_card";
            Map<String, String> form = new HashMap<String, String>();
            form.put("appKey",Constants.APPKEY );
            form.put("format", Constants.FORMAT);
            form.put("uid", Constants.UID);
            form.put("timestamp", Long.toString(System.currentTimeMillis()));
            form.put("token",Constants.XTOKENFORAPP );
            form.put("kind_card_id", kind_card_id);
            newApplyCardResp = httpRequest.postHandle(url, form, 200000);
        } catch (Exception e) {
            logger.error("caseid: " + caseid + ",case描述: " + description + ",调用服务失败", e);
        }
        return newApplyCardResp;
	}
       
	
	/*
	 * 我关注的店，未领取的卡,券
	 * http://10.1.24.5:8080/consumer-api/shop/info/v3/entity_other_info?appKey=100011&entity_id=99932333&format=json&seat_code=&sign=0c52ec5985b13017a10db4cf4949164f&timestamp=1504158671042&uid=13675832423
	 */
	public Response testEntityOtherInfo(String entity_id) throws Exception{
        // 调用http服务
        Response cardPaymentres = null;
        try {
        	List<String> patUrl = new ArrayList<String>();
        	patUrl.add(http_url);
        	patUrl.add("/shop/info/v3/entity_other_info");

            Map<String, String> params = new HashMap<String, String>();
            params.put("appKey",Constants.APPKEY );
            params.put("format", Constants.FORMAT);
            params.put("uid", Constants.UID);
            params.put("timestamp", Long.toString(System.currentTimeMillis()));
            params.put("sign","a7c1bc80ef79b10aee12b56757b7db68" );
            params.put("entity_id", entity_id);
            params.put("seat_code", "");
            cardPaymentres = httpRequest.get(patUrl, params);
            
        } catch (Exception e) {
            logger.error("caseid: " + caseid + ",case描述: " + description + ",调用服务失败", e);
        }
        return cardPaymentres;
	}
		
	//领卡
	public Response testCardPayment(String entity_id,String card_id) throws Exception{
        // 调用http服务
        Response cardPaymentres = null;
        try {
        	List<String> patUrl = new ArrayList<String>();
        	patUrl.add(http_url);
        	patUrl.add("/member/card/v1/payment");

            Map<String, String> params = new HashMap<String, String>();
            params.put("appKey",Constants.APPKEY );
            params.put("format", Constants.FORMAT);
            params.put("uid", Constants.UID);
            params.put("timestamp", Long.toString(System.currentTimeMillis()));
            params.put("token",Constants.XTOKENFORAPP );
            params.put("entity_id", entity_id);
            params.put("card_id", card_id);
            cardPaymentres = httpRequest.get(patUrl, params);
            
        } catch (Exception e) {
            logger.error("caseid: " + caseid + ",case描述: " + description + ",调用服务失败", e);
        }
        return cardPaymentres;
	}
		
		
	//删除卡
	public Response testRemoveCard(String card_id) throws Exception{

        // 调用http服务
        Response removeCardResp = null;
        try {
        	String url = http_url+"/member/card/v1/remove_card";
            Map<String, String> form = new HashMap<String, String>();
            form.put("appKey",Constants.APPKEY );
            form.put("format", Constants.FORMAT);
            form.put("uid", Constants.UID);
            form.put("timestamp", Long.toString(System.currentTimeMillis()));
            form.put("token",Constants.XTOKENFORAPP );
            form.put("card_id", card_id);
            removeCardResp = httpRequest.postHandle(url, form, 200000);
        } catch (Exception e) {
            logger.error("caseid: " + caseid + ",case描述: " + description + ",调用服务失败", e);
        }
        return removeCardResp;
	}
		 
}
