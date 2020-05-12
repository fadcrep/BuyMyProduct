package com.fadcode.buymyproduct;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.os.Bundle;
import android.util.Log;

import com.fadcode.buymyproduct.Model.Product;
import com.fadcode.buymyproduct.Views.Adapter.FavoriteAdapter;

import java.util.ArrayList;
import java.util.List;

public class FavoriteActivity extends AppCompatActivity {
    private RecyclerView recyclerViewFavorite;
    private RecyclerView.Adapter adapterFavorite;
    private  DatabaseHelper databaseHelper;
    List<Product> listProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);

        databaseHelper = new DatabaseHelper(getApplicationContext());
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.favorite_Product_text);


        recyclerViewFavorite = findViewById(R.id.recycleProductFavorite);
        recyclerViewFavorite.setLayoutManager( new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));

        if( databaseHelper.favoriteProductList().size()>0){
           List<Product> listProductFromQuery = databaseHelper.favoriteProductList();
            adapterFavorite = new FavoriteAdapter( this,listProductFromQuery);

            recyclerViewFavorite.setAdapter(adapterFavorite);
        }


    }
}
