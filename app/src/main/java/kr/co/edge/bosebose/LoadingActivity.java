package kr.co.edge.bosebose;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoadingActivity extends Activity {

    public static String uuid;

    private NetworkService networkService;
    Context context;
    Call<List<Item>> clothesCall;
    Call<List<Store>> storeCall;
    ArrayList<Item> itemList;
    ArrayList<Store> storeList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        context = this;
        uuid  = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);

        networkService = ServiceGenerator.createService(NetworkService.class,null);

        storeCall = networkService.getStores("전체","최신순",uuid);
        storeCall.enqueue(new Callback<List<Store>>() {
            @Override
            public void onResponse(Call<List<Store>> call, Response<List<Store>> response) {

                if(response.isSuccessful()){
                    Log.i("lsw","!!!!!!11");
                    storeList = new ArrayList<>();
                    storeList.addAll(response.body());
                }

               clothesCall = networkService.getClothes("전체","최신순",uuid);
                clothesCall.enqueue(new Callback<List<Item>>() {
                    @Override
                    public void onResponse(Call<List<Item>> call, retrofit2.Response<List<Item>> response) {
                        if(response.isSuccessful()){
                            itemList = new ArrayList<>();
                            itemList.addAll(response.body());

                            Intent intent = new Intent(context, MainActivity.class);
                            intent.putExtra("itemList",itemList);
                            intent.putExtra("storeList",storeList);
                            startActivity(intent);
                            finish();
                        }else{
                            Toast.makeText(getApplicationContext(),"통신 성공/ 아이템 가져오기 실패", Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onFailure(Call<List<Item>> call, Throwable t) {
                        Toast.makeText(getApplicationContext(),"통신 실패 / 아이템 가져오기 실패", Toast.LENGTH_SHORT).show();
                        Log.i("lsw","call error:"+t.getMessage());
                    }
                });
            }

            @Override
            public void onFailure(Call<List<Store>> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"통신 실패/ 가게정보 가져오기 실패", Toast.LENGTH_SHORT).show();
                Log.i("lsw","call error:"+t.getMessage());
            }
        });
    }
}
