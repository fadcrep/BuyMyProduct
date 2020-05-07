 package com.fadcode.buymyproduct;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

 public class MainActivity extends AppCompatActivity implements ProductAdapter.ClickedItem {

    private RecyclerView recyclerView;
    private StaggeredGridLayoutManager staggeredGridLayoutManager;
    private ProductAdapter productAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recycleProduct);
        staggeredGridLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);

        productAdapter = new ProductAdapter(this::clickedProduct);

        getAllProduct();
    }

    public void getAllProduct(){
        Call<List<Product>> products = ApiClient.getProductService().getProduct();

        products.enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(@NotNull Call<List<Product>> call, @NotNull Response<List<Product>> response) {
                if(response.isSuccessful()){
                    List<Product> productList = response.body();
                    productAdapter.setData(productList);
                    recyclerView.setAdapter(productAdapter);
                }
            }

            @Override
            public void onFailure(@NotNull Call<List<Product>> call, @NotNull Throwable t) {
                Log.e("Failure", Objects.requireNonNull(t.getLocalizedMessage()));
            }
        });
    }

     @Override
     public void clickedProduct(Product product) {
//        Log.e("Clicked User", product.toString());
         startActivity(new Intent(this, ProductDetailsActivity.class).putExtra("data", product));
     }
 }
