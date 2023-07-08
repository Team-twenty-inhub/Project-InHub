package com.twenty.inhub.ut.ut;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public class Ut {

    public static class url{

        //-- String -> URL 인코딩 --//
        public static String encode(String str) {

            try {
                return URLEncoder.encode(str, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                return str;
            }
        }

        //-- 파라미터 추가 로직 --//
        public static String modifyQueryParam(String url, String paramName, String paramValue) {

            url = deleteQueryParam(url, paramName);
            url = addQueryParam(url, paramName, paramValue);
            return url;
        }


        private static String deleteQueryParam(String url, String paramName) {
            int startPoint = url.indexOf(paramName + "=");
            if (startPoint == -1) return url;

            int endPoint = url.substring(startPoint).indexOf("&");

            if (endPoint == -1) {
                return url.substring(0, startPoint - 1);
            }

            String urlAfter = url.substring(startPoint + endPoint + 1);

            return url.substring(0, startPoint) + urlAfter;
        }

        public static String addQueryParam(String url, String paramName, String paramValue) {
            if (url.contains("?") == false)
                url += "?";

            if (url.endsWith("?") == false && url.endsWith("&") == false)
                url += "&";

            return url + paramName + "=" + paramValue;
        }
    }

    public static class json {

        public static String toStr(Map map) {
            try {
                return new ObjectMapper().writeValueAsString(map);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }

        public static Object toString(Map<String, Object> map) {
            try {
                return new ObjectMapper().writeValueAsString(map);
            } catch (JsonProcessingException e) {
                return null;
            }
        }

        public static Map<String, Object> toMap(String jsonStr) {
            try {
                return new ObjectMapper().readValue(jsonStr, LinkedHashMap.class);
            } catch (JsonProcessingException e) {
                return null;
            }
        }
    }
}