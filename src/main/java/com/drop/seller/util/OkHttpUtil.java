package com.drop.seller.util;

import okhttp3.Request;
import org.springframework.http.HttpHeaders;

public class OkHttpUtil {
    private static final String API_KEY = "eb1bdee2-b00d-42d8-8360-3a4b63a78b19";
    private static final String TOKEN_HEADER = "X-CMC_PRO_API_KEY";
    private static final String ACCEPT_HEADER = "application/json";

    public static String addQueryParameter(String url, String parameter, String value) {
        return url + "?" + parameter + "=" + value;
    }

    public static Request requestBuilderWithCryptoCode(String url) {
        return new Request.Builder()
                .url(url)
                .header(HttpHeaders.ACCEPT, ACCEPT_HEADER)
                .header(TOKEN_HEADER, API_KEY)
                .build();
    }

}

