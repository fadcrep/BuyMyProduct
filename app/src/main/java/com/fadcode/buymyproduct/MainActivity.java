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
 import android.net.ConnectivityManager;
 import android.net.Network;
 import android.net.NetworkCapabilities;
 import android.net.NetworkInfo;
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
 import java.util.Arrays;
 import java.util.List;
 import java.util.Objects;

 import retrofit2.Call;
 import retrofit2.Callback;
 import retrofit2.Response;
 import retrofit2.Retrofit;
 import retrofit2.converter.gson.GsonConverterFactory;

 public class MainActivity extends AppCompatActivity
         implements ProductAdapter.ProductsAdapterListener{

     private RecyclerView recyclerView;
     Menu menu;
     SharedPreferences preferences;
     private StaggeredGridLayoutManager staggeredGridLayoutManager;
     private ProductAdapter productAdapter;
     private List<Product> productList;
     private SearchView searchView;
     private MenuItem profileMenuItem, loginMenuItem, registerMenuItem,
             deconectMenuItem, productListMenuItem;
     String textUser = "user_email";
     String currentClass = "com.fadcode.buymyproduct.MainActivity";
     DatabaseHelper databaseHelper;
     private List<Product> productDatabaseList;

     @Override
     protected void onCreate(Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);
         setContentView(R.layout.activity_main);
         Toolbar toolbar = findViewById(R.id.toolbar);
         setSupportActionBar(toolbar);
         databaseHelper = new DatabaseHelper(getApplicationContext());

         // toolbar fancy stuff
       //  getSupportActionBar().setDisplayHomeAsUpEnabled(true);
         getSupportActionBar().setTitle(R.string.app_name);
         invalidateOptionsMenu();

         preferences = getApplicationContext().getSharedPreferences("MY_PREF",0);
         recyclerView = findViewById(R.id.recycleProduct);
         staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
         recyclerView.setLayoutManager(staggeredGridLayoutManager);

         whiteNotificationBar(recyclerView);
         productDatabaseList = new ArrayList<>();
         productList = new ArrayList<>();
         List<Product> listProductFromQuery = databaseHelper.productListFromSQLite();
         productAdapter = new ProductAdapter(productList,this,this);

         recyclerView.setAdapter(productAdapter);
         if(checkConnectivity()){

             getAllProduct();
          //   productDatabaseList = new ArrayList<>();
          //   productDatabaseList = (List<Product>) productList.get(14);
         //    Log.i("TAG_APP_DATABASE", productDatabaseList.get(0).getId());

         } else {
             productList = listProductFromQuery;
             productAdapter = new ProductAdapter(productList,this,this);

             recyclerView.setAdapter(productAdapter);



             Toast.makeText(this, "Veuillez vous connectez a internet", Toast.LENGTH_SHORT).show();
         }

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
                     productDatabaseList =  new ArrayList<>(productList.subList(0, 13)) ;
                     Log.i("TAG_APP_DATABASE", productDatabaseList.get(12).getId());
                     productAdapter.notifyDataSetChanged();
                     Log.i("database size", String.valueOf(databaseHelper.productListFromSQLite().size()));
                     if(databaseHelper.productListFromSQLite().size() == 0){
                         databaseHelper.addProductFromSQLite((ArrayList<Product>) productDatabaseList);
                         productAdapter.notifyDataSetChanged();
                     }else {
                         Log.i("lala", "on est là");
                     }



                     // refreshing recycler view

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
         productListMenuItem = menu.findItem(R.id.listProductMenu);

         String mypref = preferences.getString("user_email", null);
         if(mypref != null){
             Log.i("shareprefs","hello" );
             profileMenuItem.setVisible(true);
             deconectMenuItem.setVisible(true);
             productListMenuItem.setVisible(false);
             loginMenuItem.setVisible(false);
             registerMenuItem.setVisible(false);
         }else {
             Log.i("shareprefs","hello2" );
             profileMenuItem.setVisible(false);
             deconectMenuItem.setVisible(false);
             productListMenuItem.setVisible(true);
             loginMenuItem.setVisible(true);
             registerMenuItem.setVisible(true);

             Log.i("app_1" , getClass().getName());
             Log.i("app_2" , currentClass);

         }

         if(getClass().getName().equals(currentClass)){
             productListMenuItem.setVisible(false);
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
         //finish();
        // recreate();
        restartFirstActivity();

     }

     private void restartFirstActivity()
     {
         Intent i = getApplicationContext().getPackageManager()
                 .getLaunchIntentForPackage(getApplicationContext().getPackageName() );

         i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK );
         startActivity(i);
     }

     public boolean checkConnectivity(){
         final ConnectivityManager cm =
                 (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
         if (cm != null) {
             if (Build.VERSION.SDK_INT < 23) {
                 final NetworkInfo ni = cm.getActiveNetworkInfo();
                 if (ni != null) {
                     return (ni.isConnected() && (ni.getType() ==
                             ConnectivityManager.TYPE_WIFI ||
                             ni.getType() == ConnectivityManager.TYPE_MOBILE));
                 }
             }
             else {
                 final Network n = cm.getActiveNetwork();
                 if (n != null) {
                     final NetworkCapabilities nc = cm.getNetworkCapabilities(n);
                     return
                             (nc.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                                     nc.hasTransport(NetworkCapabilities.TRANSPORT_WIFI));
                 }
             }
         }
         return false;
     }

 }

