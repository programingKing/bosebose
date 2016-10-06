package kr.co.edge.bosebose;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by snangwon on 2016-07-16.
 */
public interface NetworkService {

    //아이템 관련 API

    @GET("App/Item/AddHit")
    Call<ResponseBody> addHit(@Query("id") String id);

    @GET("App/Item/AddLike")
    Call<ResponseBody> addLike(@Query("id") String id, @Query("device_id") String deviceID);

    @GET("App/Item/SubLike")
    Call<ResponseBody> subLike(@Query("id") String id, @Query("device_id") String deviceID);

    @GET("App/Item/GetItem")
    Call<Item> getClothe(@Query("id") String id, @Query("device_id") String deviceID);

    @GET("App/Item/GetItems")
    Call<List<Item>> getClothes(@Query("category") String filter, @Query("order") String order, @Query("device_id") String deviceID);

    @GET("App/Item/GetLikeItems")
    Call<List<Item>> getLikeItems( @Query("device_id") String deviceID);

    @GET("App/Item/SearchItems")
    Call<List<Item>> searchItem(@Query("word") String word);

    //가게 관련 API

    @GET("App/Store/AddHit")
    Call<ResponseBody> addStoreHit(@Query("id") String id);

    @GET("App/Store/AddLike")
    Call<ResponseBody> addStoreLike(@Query("id") String id, @Query("device_id") String deviceID);

    @GET("App/Store/SubHit")
    Call<ResponseBody> subStoreLike(@Query("id") String id, @Query("device_id") String deviceID);

    @GET("App/Store/GetStore")
    Call<Store> getStore(@Query("id") String id, @Query("device_id") String deviceID );

    @GET("App/Store/GetStores")
    Call<List<Store>> getStores(@Query("category") String filter, @Query("order") String order, @Query("device_id") String deviceID);

    @GET("App/Store/GetFavoriteStores")
    Call<List<Store>> getFavoriteStores(@Query("category") String filter, @Query("order") String order, @Query("device_id") String deviceID);

    @GET("App/Store/GetMap")
    Call<ResponseBody> getStoreMap(@Query("longitude") String lon, @Query("latitude") String lat, @Query("storeName") String storeName);

}
