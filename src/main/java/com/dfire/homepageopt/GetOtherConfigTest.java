package com.dfire.homepageopt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.dfire.test.util.JsonHelper;
import com.dfire.test.util.StringHelper;
import com.dfire.testBase.TestBase;
import com.dfire.utils.Constants;
import com.dfire.utils.Response;
import com.twodfire.share.result.ResultMap;

/**
 * 扫码首页提速优化-H5扫码-店铺其他字段  2017/8/14
 * @author sangye 
 * http://api.l.whereask.com/homepage_opt_server/initial/v1/get_other_config?entity_id=99932333&xtoken=6B2F30C153881D868B7C9B7A6DB4CDF0CCB060FE843A0797E71C815E09E30CBA
 */
public class GetOtherConfigTest extends TestBase{
	
	private static final String String = null;
	public static Logger logger = LoggerFactory.getLogger("biz");
    public static String caseid;
    public static String description;
    public static String entity_id;
    public static String qr_code;
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
        exp_code = StringHelper.convert2int(data.get("exp_code"));
        msg = StringHelper.convert2String(data.get("msg"));
        
        // 调用http服务
        Response response = null;
        try {
        	List<String> pathUrl = new ArrayList<String>();
        	pathUrl.add(http_url);
        	pathUrl.add("/initial/v1/");
        	pathUrl.add("get_other_config");
        	
            Map<String, String> params = new HashMap<String, String>();
            // params
            params.put("xtoken", xtoken);
            if(!"6".equals(caseid)){
            	 params.put("entity_id", entity_id);
            }
            response = httpRequest.get(pathUrl, params);
            System.out.println("response: " + JsonHelper.objectToString(response.getResponseStr()));
        } catch (Exception e) {
            logger.error("caseid: " + caseid + ",case描述: " + description + ",调用服务失败", e);
        }
        // 校验结果
        ResultMap<String> rm = JsonHelper.stringToObject(response.getResponseStr(), ResultMap.class);
        Assert.assertEquals(rm.get("code"),exp_code);
        int code=(int) rm.get("code");
    	
        if(response.getStatus() == 200 || code == Constants.EXP_CODE){
        	Map data1 = (Map)rm.get("data");
        	//int is_open_menucode =(int) data1.get("is_open_menucode");
        	if("1".equals(caseid)){
        		Assert.assertEquals((int) data1.get("is_open_menucode"),Constants.TREU_FLAG,"is_open_menucode实际返回与期望不一致");
        		//Assert.assertEquals((int) data1.get("is_open_intelligent"),Constants.TREU_FLAG,"is_open_intelligent实际返回与期望不一致");
        	//	Assert.assertEquals((int) data1.get("is_set_userinfo"),Constants.TREU_FLAG,"is_set_userinfo实际返回与期望不一致");
        		Assert.assertEquals((int)data1.get("is_display_note"),Constants.TREU_FLAG,"is_display_note实际返回与期望不一致");
        		Assert.assertEquals((int)data1.get("is_display_price"),Constants.TREU_FLAG,"is_display_price实际返回与期望不一致");
        		Assert.assertEquals((int)data1.get("is_pre_pay"),Constants.TREU_FLAG,"is_pre_pay实际返回与期望不一致");
        		Assert.assertEquals((int)data1.get("is_pre_pay_shop"),Constants.TREU_FLAG,"is_pre_pay_shop实际返回与期望不一致");
        		Assert.assertEquals((int)data1.get("is_menu_show_blank"),Constants.TREU_FLAG,"is_menu_show_blank实际返回与期望不一致");
        	}
        	/*if("2".equals(caseid)){
        		Assert.assertEquals((int) data1.get("is_open_menucode"),Constants.FLASE_FLAG,"is_open_menucode实际返回与期望不一致");
        		//Assert.assertEquals((int) data1.get("is_open_intelligent"),Constants.FLASE_FLAG,"is_open_intelligent实际返回与期望不一致");
        		Assert.assertEquals((int) data1.get("is_set_userinfo"),Constants.FLASE_FLAG,"is_set_userinfo实际返回与期望不一致");
        		Assert.assertEquals((int)data1.get("is_display_note"),Constants.FLASE_FLAG,"is_display_note实际返回与期望不一致");
        		Assert.assertEquals((int)data1.get("is_display_price"),Constants.FLASE_FLAG,"is_display_price实际返回与期望不一致");
        		Assert.assertEquals((int)data1.get("is_pre_pay"),Constants.FLASE_FLAG,"is_pre_pay实际返回与期望不一致");
        		Assert.assertEquals((int)data1.get("is_pre_pay_shop"),Constants.FLASE_FLAG,"is_pre_pay_shop实际返回与期望不一致");
        		Assert.assertEquals((int)data1.get("is_menu_show_blank"),Constants.FLASE_FLAG,"is_menu_show_blank实际返回与期望不一致");
        	}*/
        }	
        
    }
}
