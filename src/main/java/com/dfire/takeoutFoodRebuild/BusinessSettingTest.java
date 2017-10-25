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
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.dfire.testBase.TestBase;
import com.dfire.utils.MD5Utils;

/**
 * 门店是否营业（  /takeout/v1/business_setting）
 * @author xianmao
 * 用例：
 * X1:如果配送设置和门店设置其中任何或都未设置，此时去打开，会出提示：请先完成“门店设置”和“配送设置”
 * 2：打开，查看返回信息正确；再查看get_takeout_setting这个接口返回的isOpenBusiness为1
 * 3:关闭，查看返回信息正确；再查看get_takeout_setting这个接口返回的isOpenBusiness为0
 */
public class BusinessSettingTest extends TestBase{
	
	public static Logger logger = LoggerFactory.getLogger("biz");
    public static String caseid;
    public static String description;
    public static int exp_code;
    public static String msg;
    public static int flag;

    String http_url = getConfigForZG().get("url");
    String entityid = getConfigForZG().get("bossentityid");
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
        	
        	
        	List<String> pathUrl = new ArrayList<String>();
        	pathUrl.add(http_url);
        	pathUrl.add("/takeout/v1/business_setting");
        	
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
    		
			if("1".equals(takeoutFoodUntils.gettakeoutsetting().get("isOpen").toString())){
				params.put("isOpen", "0");//关闭是否接单开关
				flag = 0;
			}else{
				params.put("isOpen", "1");//开通是否接单开关
				flag = 1;
			}
			
			params.put("id", (String) takeoutFoodUntils.gettakeoutsetting().get("id"));//通过获取外卖设置接口得到id
			
			String sign = MD5Utils.generateSignForBossAPI(Constants.bossSecret, params);
			params.put("sign", sign);			

            response = httpRequest.get(pathUrl, params, httpHeader);
            
         
        } catch (Exception e) {
            logger.error("caseid: " + caseid + ",case描述: " + description + ",调用服务失败", e);
        }
        // 校验结果
        JsonObject resp = new JsonParser().parse(response.getResponseStr()).getAsJsonObject();
        Assert.assertEquals(resp.get("code").getAsInt(), 1);
        Assert.assertEquals(flag, takeoutFoodUntils.gettakeoutsetting().get("isOpen"),"是否接单开关状态返回与期望不一致");//跟获取外卖设置接口返回的做比较
        

	}

	
}
