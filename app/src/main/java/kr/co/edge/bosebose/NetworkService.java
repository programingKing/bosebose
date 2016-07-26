package kr.co.edge.bosebose;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
/**
 * Created by snangwon on 2016-07-16.
 */
public interface NetworkService {


    public static final String SERVICE_URL="http://edge.dothome.co.kr/bosebose/";


    @GET("GetItem.php")
    Call<List<Item>> getClothes(@Query("category") String filter, @Query("order") String order);

    @GET("GetStore.php")
    Call<List<Store>> getStore(@Query("category") String filter, @Query("order") String order);

    @GET("AddHit.php")
    Call<ResponseBody> addHit(@Query("id") String id);

    @GET("SearchItem.php")
    Call<List<Item>> searchItem(@Query("word") String word);

    @GET("GetStoreMap.php")
    Call<ResponseBody> getStoreMap(@Query("lon") String lon, @Query("lat") String lat, @Query("storeName") String storeName);

    @FormUrlEncoded
    @POST("GetLikeItem.php")
    Call<List<Item>> getLikeItem(@Field("id[]") List<String> ids);


}
