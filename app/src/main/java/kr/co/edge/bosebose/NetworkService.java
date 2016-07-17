package kr.co.edge.bosebose;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.Query;
/**
 * Created by snangwon on 2016-07-16.
 */
public interface NetworkService {


    public static final String SERVICE_URL="http://edge.dothome.co.kr/bosebose/";


    @GET("GetItem.php")
    Call<List<Item>> getClothes(@Query("filter") String filter, @Query("order") String order);

    @GET("GetStore.php")
    Call<List<Store>> getStore(@Query("filter") String filter, @Query("order") String order);

}
