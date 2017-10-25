package com.dfire.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import com.dfire.testBase.TestBase;
import com.dfire.utils.Response;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.twodfire.util.MD5Util;


public class TokenUtil{
	
	public static Logger logger = LoggerFactory.getLogger("biz");
    private static final String SIGN_KEY = ",.xcvlasdiqpoikm,. xvz";
    public static String xtoken;
	/**
	 * 获取用户 token
	 * 该 token用于验证用户的有效性，在后续的请求中都需要带上
	 * @param httpRequest
	 * @param UUID
	 * @return
	 */
	public static String getToken() {
		
		String http_url = com.dfire.testBase.TestBase.getConfig();
	    String entityId = com.dfire.testBase.TestBase.getEntityId();
	    String unionId = com.dfire.testBase.TestBase.getUnionID();
		HttpRequestEx httpRequest = new HttpRequestEx();
		try {
			List<String> pathUrl = new ArrayList<String>();
        	pathUrl.add(http_url);
        	pathUrl.add("/dev/get_token");
			
			String signKey = generatorKey(entityId + unionId);
			
			Map<String, String> query = new HashMap<String, String>();
			query.put("entity_id", entityId);
			query.put("unionid", unionId);
			query.put("sign", signKey);
			Response response = httpRequest.get(pathUrl, query);
			JsonObject resp = new JsonParser().parse(response.getResponseStr()).getAsJsonObject();	
			Assert.assertEquals(resp.get("success").getAsString(), "true");
			
			Assert.assertEquals(resp.get("model").getAsJsonObject().get("success").getAsString(), "true");
			xtoken = resp.get("model").getAsJsonObject().get("model").getAsString();
					
		} catch (Exception e) {
			logger.error("token获取失败", e);
		}
		
		return xtoken;
	}
	
    public static String generatorKey(String source) {
        StringBuilder sb = new StringBuilder();
        sb.append(source).append(SIGN_KEY);
        return MD5Util.MD5(sb.toString());
    }
    
    
}
