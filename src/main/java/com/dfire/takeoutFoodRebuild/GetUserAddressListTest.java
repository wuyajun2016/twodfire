package com.dfire.takeoutFoodRebuild;

import org.testng.annotations.Test;

import com.dfire.soa.annotation.EntityId;
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
 * 小二端-获取地址列表（member/address/v1/get_user_address_list）
 * @author xianmao
 * 用例：
 * X1:店铺有经纬度，但是用户地址没有经纬度时候，查看返回hasOutRange为XXX
 * X2：店铺没有经纬度，用户地址有经纬度时，查看返回hasOutRange为XXX
 * 3:都有经纬度时候，如果超出了范围，hasOutRange为true
 * 4：都有经纬度时候，在范围内，hasOutRange为false
 * (备注:账号下手动造两条符合第三点和第四点的数据)
 */
public class GetUserAddressListTest extends TestBase{
	
	public static Logger logger = LoggerFactory.getLogger("biz");
    public static String caseid;
    public static String description;
    public static int exp_code;
    public static String msg;
    

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
//        	pathUrl.add("/takeout_optimize_server/member/address/v1/get_user_address_list");
        	pathUrl.add("/member/address/v1/get_user_address_list");
        	
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
        	try{
        		if("1".equals(caseid)){
            		Assert.assertEquals(resp.getAsJsonArray("data").get(0).getAsJsonObject().get("hasOutRange").getAsString(), msg,"hasOutRange返回值与期望不一致");
            	}
        	}catch(Exception e){
        		 logger.error("hasoutrange没有出现：", e);
        	}
        	
//        	if("2".equals(caseid)){//暂且不考虑，数据经常会变
//        		Assert.assertEquals(resp.getAsJsonArray("data").get(1).getAsJsonObject().get("hasOutRange").getAsString(), msg,"hasOutRange返回值与期望不一致");
//        	}
        }
        
    }
    
    @BeforeTest
    public void beforeTest() {
    }

    @AfterTest
    public void afterTest() {
    }
  

}
