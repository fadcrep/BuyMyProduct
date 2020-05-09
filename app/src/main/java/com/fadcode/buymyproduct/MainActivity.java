 package com.fadcode.buymyproduct;

 import androidx.appcompat.app.AppCompatActivity;
 import androidx.appcompat.widget.SearchView;
 import androidx.appcompat.widget.Toolbar;
 import androidx.recyclerview.widget.RecyclerView;
 import androidx.recyclerview.widget.StaggeredGridLayoutManager;

 import android.app.SearchManager;
 import android.content.Context;
 import android.content.Intent;
 import android.content.SharedPreferences;
 import android.graphics.Color;
 import android.os.Build;
 import android.os.Bundle;
 import android.os.Parcelable;
 import android.util.Log;
 import android.view.Menu;
 import android.view.MenuItem;
 import android.view.View;
 import android.widget.Toast;

 import com.fadcode.buymyproduct.Api.ApiUtils;
 import com.fadcode.buymyproduct.Model.Product;
 import com.fadcode.buymyproduct.Views.Adapter.ProductAdapter;
 import com.fadcode.buymyproduct.Views.ProductDetailsActivity;
 import com.fadcode.buymyproduct.Views.ProfileActivity;

 import org.jetbrains.annotations.NotNull;

 import java.util.ArrayList;
 import java.util.List;
 import java.util.Objects;

 import retrofit2.Call;
 import retrofit2.Callback;
 import retrofit2.Response;
 import retrofit2.Retrofit;
 import retrofit2.converter.gson.GsonConverterFactory;

 public class MainActivity extends AppCompatActivity implements ProductAdapter.ProductsAdapterListener{

     private RecyclerView recyclerView;
     Menu menu;
     SharedPreferences preferences;
     private StaggeredGridLayoutManager staggeredGridLayoutManager;
     private ProductAdapter productAdapter;
     private List<Product> productList;
     private SearchView searchView;
     private MenuItem profileMenuItem, loginMenuItem, registerMenuItem, deconectMenuItem;
     String textUser = "user_email";

     @Override
     protected void onCreate(Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);
         setContentView(R.layout.activity_main);
         Toolbar toolbar = findViewById(R.id.toolbar);
         setSupportActionBar(toolbar);

         // toolbar fancy stuff
       //  getSupportActionBar().setDisplayHomeAsUpEnabled(true);
         getSupportActionBar().setTitle(R.string.app_name);
         invalidateOptionsMenu();

         preferences = getApplicationContext().getSharedPreferences("MY_PREF",0);
         recyclerView = findViewById(R.id.recycleProduct);
         staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
         recyclerView.setLayoutManager(staggeredGridLayoutManager);

         whiteNotificationBar(recyclerView);
         productList = new ArrayList<>();
         productAdapter = new ProductAdapter(productList,this,this);
         recyclerView.setAdapter(productAdapter);
         getAllProduct();
     }

     public void getAllProduct(){
         Call<List<Product>> products = ApiUtils.getApiService().getProduct();

         products.enqueue(new Callback<List<Product>>() {
             @Override
             public void onResponse(@NotNull Call<List<Product>> call, @NotNull Response<List<Product>> response) {
                 if(response.isSuccessful()){
                     List<Product> items = response.body();
                     productList.clear();
                     productList.addAll(items);

                     // refreshing recycler view
                     productAdapter.notifyDataSetChanged();
                 }
             }

             @Override
             public void onFailure(@NotNull Call<List<Product>> call, @NotNull Throwable t) {
                 Log.e("Failure", Objects.requireNonNull(t.getLocalizedMessage()));
             }
         });
     }


     @Override
     public boolean onCreateOptionsMenu(Menu menu) {
         getMenuInflater().inflate(R.menu.menu, menu);

         profileMenuItem =   menu.findItem(R.id.profilMenu);
         loginMenuItem = menu.findItem(R.id.loginMenu);
         registerMenuItem = menu.findItem(R.id.registerMenu);
         deconectMenuItem = menu.findItem(R.id.deconnectMenu);

         String mypref = preferences.getString("user_email", null);
         if(mypref != null){
             Log.i("shareprefs","hello" );
             profileMenuItem.setVisible(true);
             deconectMenuItem.setVisible(true);
             loginMenuItem.setVisible(false);
             registerMenuItem.setVisible(false);
         }else {
             Log.i("shareprefs","hello2" );
             profileMenuItem.setVisible(false);
             deconectMenuItem.setVisible(false);
             loginMenuItem.setVisible(true);
             registerMenuItem.setVisible(true);
         }

         // Associate searchable configuration with the SearchView
         SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
         searchView = (SearchView) menu.findItem(R.id.action_search)
                 .getActionView();
         searchView.setSearchableInfo(searchManager
                 .getSearchableInfo(getComponentName()));
         searchView.setMaxWidth(Integer.MAX_VALUE);

         // listening to search query text change
         searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
             @Override
             public boolean onQueryTextSubmit(String query) {
                 // filter recycler view when query submitted
                 productAdapter.getFilter().filter(query);
                 return false;
             }

             @Override
             public boolean onQueryTextChange(String query) {
                 // filter recycler view when text is changed
                 productAdapter.getFilter().filter(query);
                 return false;
             }
         });
         return true;
     }



     @Override
     public boolean onOptionsItemSelected(MenuItem item) {
         // Handle action bar item clicks here. The action bar will
         // automatically handle clicks on the Home/Up button, so long
         // as you specify a parent activity in AndroidManifest.xml.
         int id = item.getItemId();

         switch (id){
             case R.id.action_search:
                 return true;

             case R.id.loginMenu:
                 startActivity(new Intent(MainActivity.this, LoginActivity.class));
                 break;

             case R.id.registerMenu:
                 startActivity(new Intent(MainActivity.this, RegisterActivity.class));
                 break;

             case R.id.profilMenu:
                 startActivity(new Intent(MainActivity.this, ProfileActivity.class));
                 break;

             case R.id.deconnectMenu:
                 deconnectUser();
                 break;


         }

         //noinspection SimplifiableIfStatement
        return super.onOptionsItemSelected(item);
     }


     private void whiteNotificationBar(View view) {
         if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
             int flags = view.getSystemUiVisibility();
             flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
             view.setSystemUiVisibility(flags);
             getWindow().setStatusBarColor(Color.WHITE);
         }
     }

     @Override
     public void onProductSelected(Product product) {
         startActivity(new Intent(this, ProductDetailsActivity.class).putExtra("data", product));
     }


    private void deconnectUser(){

         SharedPreferences.Editor editor = preferences.edit();
         editor.remove(textUser);
         editor.commit();
     }
 }

