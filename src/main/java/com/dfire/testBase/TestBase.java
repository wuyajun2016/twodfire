package com.dfire.testBase;

import com.alibaba.druid.pool.DruidDataSource;
import com.dfire.test.util.CsvDataProvider;
import com.dfire.utils.HttpRequestEx;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.twodfire.util.MD5Util;
import com.twodfire.util.PropertiesUtil;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.DataProvider;

import com.dfire.test.util.JsonHelper;
import com.dfire.test.util.StringHelper;
import com.dfire.testBase.TestBase;
import com.dfire.utils.Constants;
import com.dfire.utils.Response;
import com.twodfire.share.result.ResultMap;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

import javax.annotation.Resource;

@ContextConfiguration(locations = { "classpath:applicationContext.xml" })
public class TestBase extends AbstractTestNGSpringContextTests {
	
	@Resource(name = "dataSource")
	public DruidDataSource druidDataSource;
	
	public static Logger logger = LoggerFactory.getLogger("biz");
	public static String csvFile;
	public static String bossurl;
	public static String bossentityid;
    
	public HttpRequestEx httpRequest = new HttpRequestEx();
	
	@DataProvider(name = "CsvDataProvider")
	public Iterator<Object[]> data() throws Exception {
		return (Iterator<Object[]>) new CsvDataProvider(getCsvPath());
	}


	static {
		// 读取config.properties
		Map<String, String> propertiesMap = null;
		try {
			propertiesMap = PropertiesUtil.readProperties("config.properties");
		} catch (IOException e) {
			e.printStackTrace();
		}
		csvFile = propertiesMap.get("csvFile");
	}
	
	
	public static String  getEntityId() {
		Properties prop = readProperties(file);
			return prop.getProperty("entityId");
	}
		
	
	public static String  getUnionID() {
		Properties prop = readProperties(file);
			return prop.getProperty("unionID");
	}
	
	public static String concatForm(Map<String, String> form) {

		String str = "";
		for (String key : form.keySet()) {
			String val = form.get(key);
			str += "&" + key + "=" + val;
		}
		return str.replaceFirst("&", "");
	}
	
	private static File file = new File("src/test/resources/config.properties");
	private static File ForwodfireApp = new File("src/test/resources/configForwodfireApp.properties");
	
	public static String  getConfig() {
		
		Properties prop = readProperties(file);
		String env = prop.getProperty("env");
		
		if (StringUtils.equalsIgnoreCase("dev", env)) {
			return prop.getProperty("twofire_url_dev");
		}
		
		if (StringUtils.equalsIgnoreCase("daily", env)) {
			return prop.getProperty("twofire_url_daily");
		}

		if (StringUtils.equalsIgnoreCase("pre", env)) {
			return prop.getProperty("twofire_url_pre");
		}
		
		if (StringUtils.equalsIgnoreCase("publish", env)) {
			return prop.getProperty("twofire_url_publish");
		}
		
		
		return null;
	}
	
public static String  getConfigForAPP() {
		
		
		Properties prop = readProperties(ForwodfireApp);
		String env = prop.getProperty("env");
		

		if (StringUtils.equalsIgnoreCase("dev", env)) {
			return prop.getProperty("twofireapp_url_dev");
		}
		
		if (StringUtils.equalsIgnoreCase("daily", env)) {
			return prop.getProperty("twofireapp_url_daily");
		}

		if (StringUtils.equalsIgnoreCase("pre", env)) {
			return prop.getProperty("twofireapp_url_pre");
		}
		
		if (StringUtils.equalsIgnoreCase("publish", env)) {
			return prop.getProperty("twofireapp_url_publish");
		}
		
		return null;
	}

	/**
	 * 获取掌柜端的环境配置
	 * add by xianmao
	 * @return
	 */
	public static Map<String,String>  getConfigForZG() {
		
		Map<String, String> propertiesMap = null;
		Map<String,String> prop_map = new HashMap<String, String>();

		
		try {
			propertiesMap = PropertiesUtil.readProperties("configForzgApp.properties");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		String env = propertiesMap.get("env");
	
		if (StringUtils.equalsIgnoreCase("dev", env)) {
			bossurl = propertiesMap.get("zgapp_url_dev");
			bossentityid = propertiesMap.get("zgapp_whitelist_dev");
			prop_map.put("url", bossurl);
			prop_map.put("bossentityid", bossentityid);
			return prop_map;
		}
		
		if (StringUtils.equalsIgnoreCase("daily", env)) {
			bossurl = propertiesMap.get("zgapp_url_daily");
			bossentityid = propertiesMap.get("zgapp_whitelist_daily");
			prop_map.put("url", bossurl);
			prop_map.put("bossentityid", bossentityid);
			return prop_map;
		}
	
		if (StringUtils.equalsIgnoreCase("pre", env)) {
			bossurl = propertiesMap.get("zgapp_url_pre");
			bossentityid = propertiesMap.get("zgapp_whitelist_pre");
			prop_map.put("url", bossurl);
			prop_map.put("bossentityid", bossentityid);
			return prop_map;
		}
		
		if (StringUtils.equalsIgnoreCase("publish", env)) {
			bossurl = propertiesMap.get("zgapp_url_publish");
			bossentityid = propertiesMap.get("zgapp_whitelist_publish");
			prop_map.put("url", bossurl);
			prop_map.put("bossentityid", bossentityid);
			return prop_map;
		}
		
		return null;
	}

	public static Properties readProperties(File file) {
		InputStreamReader in = null;
		Properties prop = new Properties();
		try {
			in = new InputStreamReader(new FileInputStream(file));
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}  
		try {
			prop.load(in);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return prop;
	}

	/**
	 * 获取csv文件路径
	 * @return
	 */
	public String getCsvPath() {
		String folderName = this.getClass().getPackage().getName().replaceAll("com.dfire.", "");
		return csvFile + folderName + "/" + this.getClass().getSimpleName() + ".csv";
	}
	

	
	

}