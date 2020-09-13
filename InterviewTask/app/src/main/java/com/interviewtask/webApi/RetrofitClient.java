package com.interviewtask.webApi;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.HeaderMap;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Url;

public class RetrofitClient {

    private static final String BASE_URL = "https://api.github.com/";
    private static Retrofit retrofit;

    private static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            //TODO 60 to 30 second at everywhere
            OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .writeTimeout(30, TimeUnit.SECONDS)
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    static ApiServiceInterface getInstanceNew() {
        return getRetrofitInstance().create(ApiServiceInterface.class);
    }

    public interface methodType {
        String GET = "GET";
        String POST = "POST";
        String PUT = "PUT";
        String DELETE = "DELETE";
    }

    public interface apiPostUrl {
        String getUsers = "users?";
    }

    public interface ApiServiceInterface {

        @GET
        Call<ResponseBody> getServiceCall(@Url String url, @HeaderMap() Map<String, String> header);

        @POST
        Call<ResponseBody> postServiceCall(@Url String url, @HeaderMap() Map<String, String> header, @Body Object requestBody);

        @PUT
        Call<ResponseBody> putServiceCall(@Url String url, @HeaderMap() Map<String, String> header, @Body Object requestBody);

        @HTTP(method = "DELETE", path = "", hasBody = true)
        Call<ResponseBody> deleteServiceCall(@Url String url, @HeaderMap() Map<String, String> header, @Body Object requestBody);
    }
}