package id.idn.datasiswa.ApiRetrofit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by hp on 3/27/2018.
 */

public class InstanceRetrofit {

    public static final String WebUrl = "http://192.168.43.192/phpcrud/";

    public static Retrofit setInit() {
//        Gson gson = new GsonBuilder()
//                .setLenient()
//                .create();

        return new Retrofit.Builder()
                .baseUrl(WebUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static ApiService getInstance() {
        return setInit().create(ApiService.class);
    }
}
