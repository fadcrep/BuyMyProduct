package com.fadcode.buymyproduct.Views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;


import com.fadcode.buymyproduct.Api.ApiUtils;
import com.fadcode.buymyproduct.Model.User;
import com.fadcode.buymyproduct.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ProfileActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView textView_email, textView_username;
    private String username, email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
         findviewById();

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Profile");
        getFromSharedPreferences();


        textView_email.setText(email);

        User user = new User();
        user.setId("24");
        user.setName("LOLA");
        user.setEmail("lola22@gmail.com");
        user.setPassword("lola22");

        updateUser(user);

    }

    private void updateUser(User user){
        Call<User> call = ApiUtils.getApiService().updateUser(user.getId(), user.getName(), user.getEmail(), user.getPassword());
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (!response.isSuccessful()) {

                    Log.i("TAG_APP 1", "INFO ! " + response.code() + " 229");
                    return;
                }
                Log.i("TAG_APP 2", "INFO ! " + response.code() + " 228");
                Log.i("TAG_APP  body", "INFO ! " + response.body());
               // List<User> users = response.body();
                User currentUser = response.body();

                Log.i("TAG_APP 3", "USER_EMAIL: " + currentUser.getEmail());
                Log.i("TAG_APP 4", "ID : " + currentUser.getId());
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });
    }

    public void findviewById(){
        toolbar = findViewById(R.id.toolbar);
        textView_email = findViewById(R.id.user_email);
        textView_username = findViewById(R.id.user_name);
    }

    public void getFromSharedPreferences (){
        SharedPreferences preferences = getApplicationContext().getSharedPreferences("MY_PREF", 0);
        email= preferences.getString("user_email", null);
    }

}
