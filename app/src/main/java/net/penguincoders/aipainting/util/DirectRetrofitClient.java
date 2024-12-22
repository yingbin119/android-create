package net.penguincoders.aipainting.util;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DirectRetrofitClient {

    private static Retrofit retrofit;

    public static Retrofit getDirectClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(DirectServerConfig.getDirectServerAddress())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
