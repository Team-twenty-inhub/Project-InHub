package com.twenty.inhub.ut.ut;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

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
}