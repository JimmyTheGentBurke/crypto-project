package com.drop.seller.component;

import com.drop.seller.util.OkHttpUtil;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OkHttpRequestSender {
    private final OkHttpClient okHttpClient;

    @SneakyThrows
    public String sendRequest(String url) {

        ResponseBody body = null;

        String json = null;
        try {
            body = okHttpClient
                    .newCall(OkHttpUtil.requestBuilderWithCryptoCode(url))
                    .execute()
                    .body();
            json = body.string();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            body.close();
        }
        return json;
    }

}
