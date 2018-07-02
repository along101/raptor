package com.ppdai.framework.raptor.utils;

/**
 * @author yinzuolong
 */
public class UrlUtils {

    public static String getUri(String url) {
        String uri = url;
        int i = url.indexOf("?");
        if (i >= 0) {
            uri = url.substring(0, i);
        }
        return uri;
    }
}
