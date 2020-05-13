package com.fadcode.buymyproduct.Views;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import com.fadcode.buymyproduct.Api.ApiUtils;
import com.fadcode.buymyproduct.Model.User;
import com.fadcode.buymyproduct.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ProfileActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView textView_email, textView_username;
    private String username, email;
    private Button updateProfile;
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
        updateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myCustomDialog();
            }
        });

        if(username != null){
            textView_username.setText(username);
        }


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
        updateProfile = findViewById(R.id.buttonModifier);
    }

    public void getFromSharedPreferences (){
        SharedPreferences preferences = getApplicationContext().getSharedPreferences("MY_PREF", 0);
        email= preferences.getString("user_email", null);

        if(preferences.getString("name", null) != null){
             username = preferences.getString("name", null);
        }
    }

    public void myCustomDialog(){
        final AlertDialog dialogBuilder = new AlertDialog.Builder(this).create();
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.custom_dialog_profil, null);

        final TextInputEditText editText_email =  dialogView.findViewById(R.id.email_editext_profile);
        final TextInputEditText editText_name =  dialogView.findViewById(R.id.name_editext_profile);
        final TextInputEditText  editext_password =  dialogView.findViewById(R.id.password_editext_profile);
        Button btn_submit =  dialogView.findViewById(R.id.buttonSubmit);
        Button btn_cancel = dialogView.findViewById(R.id.buttonCancel);

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = editText_email.getText().toString().trim();
                String name = editText_name.getText().toString().trim();
                String password = editext_password.getText().toString().trim();
                Log.i("tag content", email);
                User user = new User();
                SharedPreferences preferences = getApplicationContext().getSharedPreferences("MY_PREF", 0);
                if(preferences!= null){
                    Log.i("tag prefenrences", preferences.getString("user_id", null));
                    user.setId(preferences.getString("user_id", null));
                }

                if(email.isEmpty() && name.isEmpty() &&  password.isEmpty()){
                    editext_password.setError(getString(R.string.Add_one_value_please));
                } else {
                   if(!email.isEmpty()){
                       user.setEmail(email);
                   }
                   if(!name.isEmpty()){
                       user.setName(name);
                   }
                   if(!password.isEmpty()){
                       user.setPassword(password);
                   }
                   // Log.i("tag content", user.getEmail());
                   // Log.i("tag content", user.getName());
                  //  Log.i("tag content", user.getPassword());

                    updateUser(user);

                }



            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // DO SOMETHINGS
                dialogBuilder.dismiss();
            }
        });

        dialogBuilder.setView(dialogView);
        dialogBuilder.show();
    }

}
