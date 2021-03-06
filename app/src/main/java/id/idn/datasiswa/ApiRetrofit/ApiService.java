package id.idn.datasiswa.ApiRetrofit;

import id.idn.datasiswa.ResponseServer.ResponseCreateData;
import id.idn.datasiswa.ResponseServer.ResponseDeleteData;
import id.idn.datasiswa.ResponseServer.ResponseReadData;
import id.idn.datasiswa.ResponseServer.ResponseUpdateData;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by hp on 3/27/2018.
 */
// TODO This is EndPoint

public interface ApiService {
    //Api
    @GET("read_data.php")
    Call<ResponseReadData> response_read_data();

    @FormUrlEncoded
    @POST("create_data.php")
    Call<ResponseCreateData> response_create_data(
            @Field("vsname") String nama,
            @Field("vsaddress") String address,
            @Field("vssex") String sex,
            @Field("vshometown") String hometown,
            @Field("vsclass") String classs
    );

    @FormUrlEncoded
    @POST("delete_data.php")
    Call<ResponseDeleteData> response_delete_data(
            @Field("vsid") String id
    );

    @FormUrlEncoded
    @POST("update_data.php")
    Call<ResponseUpdateData> response_update_Data(
            @Field("vsname") String nama,
            @Field("vsaddress") String address,
            @Field("vssex") String sex,
            @Field("vshometown") String hometown,
            @Field("vsclass") String classs,
            @Field("vsid") String id);
}