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
 * 保存配送价格（  /takeout/v1/save_delivery_price）
 * @author xianmao
 * 用例：
 * 1:保存配送价格，查看返回信息正确；且查看下配送价格列表（  /takeout/v1/get_distribution_price_list）显示正确
 * 2：保存相同的配送距离，会有提示 
 * 3：修改配送价格，修改后返回正确； 且查看下配送价格列表（  /takeout/v1/get_distribution_price_list）显示正确
 * X4：保存多个配送价格，然后查看下配送价格列表（  /takeout/v1/get_distribution_price_list）显示正确
 * 5：必填的不传，会提示
 */
public class SaveDeliveryPriceTest extends TestBase{
	
	public static Logger logger = LoggerFactory.getLogger("biz");
    public static String caseid;
    public static String description;
    public static int exp_code;
    public static String msg;
    public static int startMeter;
    public static int endMeter;
    public static String price;
    public static int lastVer;
    public static int isValid;
    public static int opTime;
    public static int createTime;

    Gson gson = new Gson();
    String http_url = getConfigForZG().get("url");
    String entityid = getConfigForZG().get("bossentityid");
    @Test(dataProvider = "CsvDataProvider")
	public void test(Map<String, String> data) throws Exception{
    	// 读case
        description = StringHelper.convert2String(data.get("description"));
        exp_code = StringHelper.convert2int(data.get("exp_code"));
        msg = StringHelper.convert2String(data.get("msg"));
        caseid = StringHelper.convert2String(data.get("caseid"));
        startMeter = StringHelper.convert2int(data.get("startMeter"));
        endMeter = StringHelper.convert2int(data.get("endMeter"));
        price = StringHelper.convert2String(data.get("price"));
        lastVer = StringHelper.convert2int(data.get("lastVer"));
        isValid = StringHelper.convert2int(data.get("isValid"));
        opTime = StringHelper.convert2int(data.get("opTime"));
        createTime = StringHelper.convert2int(data.get("createTime"));
        
    	if(caseid.equals("1")){
    		//如先查看之前又没有时间段，如果存在就删
    		int numoftimes = (int) takeoutFoodUntils.getdistributionpricelist().get("length"); //获取价格列表中共有几条数据

    		for(int i=1;i<numoftimes;i++){
    			takeoutFoodUntils.removedeliveryprice((String) takeoutFoodUntils.getdistributionpricelist().get("id0"));
    		}
    	}
		
        // 调用http服务
        Response response = null;
        try {
        	
        	List<String> pathUrl = new ArrayList<String>();
        	pathUrl.add(http_url);
        	pathUrl.add("/takeout/v1/save_delivery_price");
        	
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
			jsonObj_savetime.put("startMeter", startMeter);
			jsonObj_savetime.put("endMeter", endMeter);
			jsonObj_savetime.put("price", price); 
			if("4".equals(caseid)){
				jsonObj_savetime.put("id", takeoutFoodUntils.getdistributionpricelist().get("id0")); //修改时候需要传id
			}
			if(Integer.valueOf(caseid)!=4){
			jsonObj_savetime.put("lastVer", lastVer);
			jsonObj_savetime.put("isValid", isValid);
			jsonObj_savetime.put("opTime", opTime);
			jsonObj_savetime.put("createTime", createTime);
			}
			params.put("delivery_price_json", gson.toJson(jsonObj_savetime));
			
			
			String sign = MD5Utils.generateSignForBossAPI(Constants.bossSecret, params);
			params.put("sign", sign);
			
			response = httpRequest.postWithHeaders(pathUrl, httpHeader,  new HashMap<String, String>(), params);
            
         
        } catch (Exception e) {
            logger.error("caseid: " + caseid + ",case描述: " + description + ",调用服务失败", e);
        }
        //校验结果
        JsonObject resp = new JsonParser().parse(response.getResponseStr()).getAsJsonObject();
        if("1".equals(caseid)){
        	Assert.assertEquals(resp.get("code").getAsInt(), 0);
        	Assert.assertEquals(resp.get("message").getAsString(), msg,"必填项没填时返回提示与期望不一致");//现在提示服务器异常！提示可以先不改
        }
        if("2".equals(caseid)){
        	Map<String,Object> deliverymap = new HashMap<String,Object>();
        	deliverymap = takeoutFoodUntils.getdistributionpricelist();
        	int last_record = Integer.valueOf(deliverymap.get("length").toString())-1;
        	int startMeter_exp = (int) deliverymap.get("startMeter"+last_record);//从配送价格列表中获取下起送距离
        	int endMeter_exp = (int) deliverymap.get("endMeter"+last_record);//从配送价格列表中获取下结束距离
        	Float price_exp = (float) deliverymap.get("price"+last_record);//从配送价格列表中获取配送价格
        	Assert.assertEquals(resp.get("code").getAsInt(), 1);
            Assert.assertEquals(startMeter, startMeter_exp,"开始距离返回与期望不一致");
            Assert.assertEquals(endMeter, endMeter_exp,"结束距离返回与期望不一致");
            Assert.assertEquals(Float.valueOf(price), price_exp,"配送价格返回与期望不一致");
        }
        if("3".equals(caseid)){
        	Assert.assertEquals(resp.get("code").getAsInt(), 0);
        	Assert.assertEquals(resp.get("message").getAsString(), msg,"添加相同的配送距离返回提示与期望不一致");
        }
        if("4".equals(caseid)){
        	Assert.assertEquals(resp.get("code").getAsInt(), 1);
        	takeoutFoodUntils.getdistributionpricelist().get("startMeter0");
        	Assert.assertEquals(String.valueOf(startMeter), takeoutFoodUntils.getdistributionpricelist().get("startMeter0").toString(),"修改后的起送距离与期望的不一致");  //校验修改后的起送距离值值跟查询出来一致
        }
        if("5".equals(caseid)){  //传非整数价格时，传price字段值为2023就代表了20.23元了
        	int last_record = Integer.valueOf(takeoutFoodUntils.getdistributionpricelist().get("length").toString())-1;
        	Float price_exp = (float) takeoutFoodUntils.getdistributionpricelist().get("price"+last_record);//从配送价格列表中获取配送价格
        	Assert.assertEquals(resp.get("code").getAsInt(), 1);
        	Assert.assertEquals(Float.valueOf(price), price_exp,"配送价格为小数时返回与期望不一致");
        }

	}


	
}
