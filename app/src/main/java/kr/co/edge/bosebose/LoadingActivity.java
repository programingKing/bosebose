package kr.co.edge.bosebose;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoadingActivity extends Activity {

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

        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(networkService.SERVICE_URL)
                .build();

        networkService = retrofit.create(NetworkService.class);
        storeCall = networkService.getStore("전체","최신순");
        storeCall.enqueue(new Callback<List<Store>>() {
            @Override
            public void onResponse(Call<List<Store>> call, Response<List<Store>> response) {
                storeList = new ArrayList<>();
                storeList.addAll(response.body());

               clothesCall = networkService.getClothes("aaa","bbb");
                clothesCall.enqueue(new Callback<List<Item>>() {
                    @Override
                    public void onResponse(Call<List<Item>> call, retrofit2.Response<List<Item>> response) {
                        itemList = new ArrayList<>();
                        itemList.addAll(response.body());

                        Intent intent = new Intent(context, MainActivity.class);
                        intent.putExtra("itemList",itemList);
                        intent.putExtra("storeList",storeList);
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void onFailure(Call<List<Item>> call, Throwable t) {
                        Log.i("lsw","call error:"+t.getMessage());
                    }
                });
            }

            @Override
            public void onFailure(Call<List<Store>> call, Throwable t) {
                Log.i("lsw","call error:"+t.getMessage());
            }
        });
    }
}
