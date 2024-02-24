package com.irpinnovative.qrscanner.Network;


import android.content.Context;

import com.irpinnovative.qrscanner.BuildConfig;
import com.irpinnovative.qrscanner.Interface.ApiService;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class RetrofitClient {
    private static Retrofit retrofit = null;
    private static HttpLoggingInterceptor interceptor;
    private static OkHttpClient client;
    private static Context context;


    private static Interceptor REQUEST_INTERCEPTOR = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();

//            UserSharedpreference userSharedpreference = UserSharedpreference.getInstance(context);
//            Log.d("dededede", "intercept: "+ userSharedpreference.getStringData(SharedPreferenceConfig.DEVICE_ID));
//
            request = request.newBuilder()
                    .addHeader("Accept", "application/json")
//                    .addHeader("Deviceid", userSharedpreference.getStringData(SharedPreferenceConfig.DEVICE_ID) == null ?
//                            "" : userSharedpreference.getStringData(SharedPreferenceConfig.DEVICE_ID))
//                    .addHeader("Userid", userSharedpreference.getStringData(SharedPreferenceConfig.USER_ID) == null ?
//                            "" : userSharedpreference.getStringData(SharedPreferenceConfig.USER_ID))
//                    .addHeader("Type", userDetailsPrefrennce.getStringData(SharedPreferenceConfig.USER_TYPE) == null ?
//                            "" : userDetailsPrefrennce.getStringData(SharedPreferenceConfig.USER_TYPE))
                    .addHeader("Content-Type", "application/json")
                    .build();
            Response response = chain.proceed(request);
            return response;
        }
    };

    static Retrofit getClient() {
        interceptor = new HttpLoggingInterceptor();
        if (retrofit == null) {
            if (BuildConfig.DEBUG) {
                interceptor = new HttpLoggingInterceptor();
                interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
                client = new OkHttpClient.Builder()
                        .addInterceptor(REQUEST_INTERCEPTOR)
                        .addInterceptor(interceptor)
//                        .connectTimeout(10, TimeUnit.MINUTES)
//                        .readTimeout(5, TimeUnit.MINUTES)
//                        .writeTimeout(5, TimeUnit.MINUTES)
                        .build();

            }
            else{
                client = new OkHttpClient.Builder()
                        .addInterceptor(REQUEST_INTERCEPTOR)
                        .addInterceptor(interceptor)
//                        .connectTimeout(10, TimeUnit.MINUTES)
//                        .readTimeout(5, TimeUnit.MINUTES)
//                        .writeTimeout(5, TimeUnit.MINUTES)
                        .build();
            }
            retrofit = new Retrofit.Builder()
                    .baseUrl(NetworkConfig.GET_BASE_URL())
                    .addConverterFactory(GsonConverterFactory.create())
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .client(client)
                    .build();
        }
        return retrofit;
    }

    public static ApiService getInterface() {
        return getClient().create(ApiService.class);
    }
}
