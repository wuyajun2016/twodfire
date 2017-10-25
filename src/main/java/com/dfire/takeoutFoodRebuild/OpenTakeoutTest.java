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
 * 开通外卖（ /takeout/v1/open_takeout）
 * @author xianmao
 * 用例：
 * 1:开通，查看返回正确；再查看get_takeout_setting这个接口返回的 所有字段值正确 
 * 2：关闭，查看返回正确；再查看get_takeout_setting这个接口返回的所有字段值正确 
 * X3：如果未开通电子账户，此时开通外卖时会提示：您还未开通电子账户，请先开通电子账户。
 * X4：如果未设置店铺地址，此时开通外卖时会提示：您还未设置店铺地址，请先设置店铺地址并提交审核。 
 * （备注：电子账户、店铺地址只能去手动开启，接口不知道是那个）
 */
public class OpenTakeoutTest extends TestBase{
	
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
        //获取门店设置列表中的数据，用来对比
        int takeout_setting_isout = (int) takeoutFoodUntils.gettakeoutsetting().get("isOut");
        try {
        	
        	List<String> pathUrl = new ArrayList<String>();
        	pathUrl.add(http_url);
        	pathUrl.add("/takeout/v1/open_takeout");
        	
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
			if(takeout_setting_isout==0){
				params.put("isOut", "1");
				flag = 1;
			}else if(takeout_setting_isout==1){
				params.put("isOut", "0");
				flag = 0;
			}
			params.put("id", (String) takeoutFoodUntils.gettakeoutsetting().get("id"));
			
    		
			String sign = MD5Utils.generateSignForBossAPI(Constants.bossSecret, params);
			params.put("sign", sign);			

            response = httpRequest.get(pathUrl, params, httpHeader);
            
         
        } catch (Exception e) {
            logger.error("caseid: " + caseid + ",case描述: " + description + ",调用服务失败", e);
        }
        // 校验结果
        JsonObject resp = new JsonParser().parse(response.getResponseStr()).getAsJsonObject();
        Assert.assertEquals(resp.get("code").getAsInt(), 1);
        Assert.assertEquals(flag, takeoutFoodUntils.gettakeoutsetting().get("isOut"),"外卖开通情况返回和预期不一致");

	}

	
}
