package id.idn.datasiswa.ApiRetrofit;

import id.idn.datasiswa.ResponseServer.ResponseCreateData;
import id.idn.datasiswa.ResponseServer.ResponseReadData;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by hp on 3/27/2018.
 */
// TODO This is EndPoint

public interface ApiService {

    @GET("read_data.php")
    Call<ResponseReadData> response_read_data();

    @FormUrlEncoded
    @POST("create_data.php/")
    Call<ResponseCreateData> response_create_data(
            @Field("name") String nama,
            @Field("address") String address,
            @Field("sex") String sex,
            @Field("hometown") String hometown,
            @Field("class") String classs
    );
}
