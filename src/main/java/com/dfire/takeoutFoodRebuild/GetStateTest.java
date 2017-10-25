package com.dfire.takeoutFoodRebuild;

import org.testng.annotations.Test;

import com.dfire.test.util.StringHelper;
import com.dfire.testBase.TestBase;
import com.dfire.utils.Constants;
import com.dfire.utils.Response;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.testng.annotations.BeforeTest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.AfterTest;


/**
 * 小二端-店铺首页展示外卖文案（ shop/v1/get_state）
 * @author xianmao
 * 用例：
 * 1:店铺有经纬度，此时查看下接口返回takeout对象中派送范围takeoutRangeList有值、是否开通外卖can_takeout跟掌柜设置一致
 * X2：店铺没有经纬度，此时查看下接口返回takeout对象中takeoutRangeList为空
 * 3:检验下起送金额和外送时间（多个/单个）返回正确（跟 /takeout/v1/get_times返回的对比）
 * X4：检查其他字段，如in_delivery_time\ takeoutFee\takeoutRange显示正确
 */
public class GetStateTest extends TestBase{
	
	public static Logger logger = LoggerFactory.getLogger("biz");
    public static String caseid;
    public static String description;
    public static int exp_code;
    public static String msg;
    public static Map<String, Object> get_takeout_setting;
    
    
    StringBuffer st_end =  new StringBuffer();
    String http_url = com.dfire.testBase.TestBase.getConfig();
    String xtoken = com.dfire.utils.TokenUtil.getToken();
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
//        	pathUrl.add("/takeout_optimize/api/initial/v1/get_state_for_takeout");
        	pathUrl.add("/initial/v1/get_state_for_takeout");
        	
            Map<String, String> params = new HashMap<String, String>();
            // params
            params.put("xtoken", xtoken);
            params.put("entity_id", entityid);
            
            response = httpRequest.get(pathUrl, params);
        } catch (Exception e) {
            logger.error("caseid: " + caseid + ",case描述: " + description + ",调用服务失败", e);
        }
        //校验结果
        JsonObject resp = new JsonParser().parse(response.getResponseStr()).getAsJsonObject();
        Assert.assertEquals(resp.get("code").getAsInt(), exp_code);
        if(response.getStatus()==200){
        	if("1".equals(caseid)){
        		//获取掌柜端是否外卖开关isOut打开情况
        		int get_takeout_setting_isout = (int) takeoutFoodUntils.gettakeoutsetting().get("isOut");
  
        		if(get_takeout_setting_isout!=0){ //外卖开通情况下进行校验
              		//对掌柜端返回的外卖时间段进行拼接
            		int length =(int) takeoutFoodUntils.gettimes().get("length");
            		for(int i=length-1;i>=0;i--){
            				String time1 = takeoutFoodUntils.gettimes().get("beginTime"+i+"")+"-"+takeoutFoodUntils.gettimes().get("endTime"+i+"");
            				st_end.append(time1);
            				if(i>=1){
            					st_end.append(" ");
            				}	
            		}
	        		//获取掌柜端门店设置中的最低起送价格startPrice
	        		String get_shop_setting_startprice =takeoutFoodUntils.shopsetting().get("startPrice").toString();
	//        		Assert.assertNotNull(resp.get("data").getAsJsonObject().get("takeout").getAsJsonObject().get("takeoutRangeList"));
	        		Assert.assertEquals(resp.get("data").getAsJsonObject().get("takeout").getAsJsonObject().get("delivery_amount").getAsString(), get_shop_setting_startprice,"最低起送金额返回与掌柜设置不一致");
	        		Assert.assertEquals(resp.get("data").getAsJsonObject().get("takeout").getAsJsonObject().get("delivery_open_hours").getAsString(), st_end.toString(),"营业时间返回与掌柜设置不一致");
	        		Assert.assertEquals(resp.get("data").getAsJsonObject().get("takeout").getAsJsonObject().get("can_takeout").getAsInt(), get_takeout_setting_isout,"是否开通外卖返回与掌柜设置不一致");
	        		}
        		else{//外卖关闭情况下进行校验
        			Assert.assertEquals(resp.get("data").getAsJsonObject().get("takeout").getAsJsonObject().get("can_takeout").getAsInt(), get_takeout_setting_isout,"是否开通外卖返回与掌柜设置不一致");
        		}
        	}
        	
        }
        
    }
    
    @BeforeTest
    public void beforeTest() {
    }

    @AfterTest
    public void afterTest() {
    }
  

}
