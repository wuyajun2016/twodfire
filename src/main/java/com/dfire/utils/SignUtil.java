//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.dfire.utils;

import com.dfire.sdk.util.MD5Util;
import com.dfire.sdk.util.StringUtil;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SignUtil {
    private static Logger logger = LoggerFactory.getLogger(SignUtil.class);
    private static Map<String, String> keySecretMap = new HashMap();
    private static final List<String> UN_PARTICIPATE_PARAMS;

    public SignUtil() {
    }

    public static String getSign(Map<String, String[]> paramsMap, String token) {
        Map<String, String> params = parseParams(paramsMap);
        String appKey = (String)params.get("appKey");
        if (keySecretMap.containsKey(params.get("appKey"))) {
            params.put("secretKey", keySecretMap.get(appKey));
        }

        logger.info("request params: {}", params.toString());
        params.put("token", token);
        return generateSign(params);
    }

    private static Map<String, String> parseParams(Map<String, String[]> paramIn) {
        Map<String, String> paramOut = new HashMap();
        Iterator i$ = paramIn.entrySet().iterator();

        while(true) {
            while(i$.hasNext()) {
                Entry<String, String[]> entry = (Entry)i$.next();
                String key = (String)entry.getKey();
                String[] value = (String[])entry.getValue();
                if (null != value && value.length > 0) {
                    paramOut.put(key, value[0]);
                } else {
                    paramOut.put(key, "");
                }
            }

            return paramOut;
        }
    }

    public static Map<String, String> getKeyMap() {
        return keySecretMap;
    }

    private static String generateSign(Map<String, String> params) {
        String[] keys = (String[])params.keySet().toArray(new String[0]);
        Arrays.sort(keys);
        StringBuilder query = new StringBuilder();
        String[] arr$ = keys;
        int len$ = keys.length;
        boolean first = true;

        for(int i$ = 0; i$ < len$; ++i$) {
            String key = arr$[i$];
            if (!UN_PARTICIPATE_PARAMS.contains(key)) {
                String value = ((String)params.get(key)).toString();
                if (!StringUtil.isEmpty(key)) {
                    if (first) {
                        first = false;
                    } else {
                        query.append("&");
                    }

                    query.append(key).append("=").append(value);
                }
            }
        }

        logger.info("request query is:" + query.toString());
        return MD5Util.encode(query.toString());
    }

    public static void main(String[] args) {
        String[] appKey = new String[]{"100011"};
        String[] uid = new String[]{"13675832423"};
        String[] equipmentId = new String[]{"AF91C392F03E4E69A5D897A27B0B8EE9"};
        String[] timestamp = new String[]{"1504056097745"};
        String[] sign = new String[]{"d6bc228b86e9f5f0630346f6ee4efc7f"};
        Map<String, String[]> paramsMap = new HashMap();
        paramsMap.put("appKey", appKey);
        paramsMap.put("uid", uid);
        paramsMap.put("equipmentId", equipmentId);
        paramsMap.put("timestamp", timestamp);
        paramsMap.put("sign", sign);
        System.out.println(getSign(paramsMap, "YTAxYTE4ZGY1YTNmMTZlMTI2MTFkZjkwNmJhNTAyNWI="));
    }

    static {
        keySecretMap.put("100010", "06fd3e1fa8a34f94ac68c0062f5ec3e0");
        keySecretMap.put("100011", "8a56de338a8049d98ed2007924996c00");
        UN_PARTICIPATE_PARAMS = new ArrayList<String>() {
            {
                this.add("sign");
            }
        };
    }
}
