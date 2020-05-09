package com.fadcode.buymyproduct;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.fadcode.buymyproduct.Api.ApiService;
import com.fadcode.buymyproduct.Api.ApiUtils;
import com.fadcode.buymyproduct.Model.User;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.textview.MaterialTextView;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG= "RegisterActivity";
    MaterialButton btn_register;
    View ll_progressBar;
    private String email;
    private String password;
    private String confirm_password;
    TextInputLayout email_layout, password_layout, confirm_password_layout;


    MaterialTextView goto_loginActivity;
    TextInputEditText email_editext , password_editext, confirm_password_editext ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

       // apiService = ApiUtils.getAPIService();
        findViewById();

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register();
            }
        });
        goto_loginActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });
    }

    public void findViewById(){

        btn_register = findViewById(R.id.btn_register);
        goto_loginActivity = findViewById(R.id.goto_login);
        email_editext = findViewById(R.id.email_editext_register);
        password_editext = findViewById(R.id.password_editext_register);
        confirm_password_editext = findViewById(R.id.confirm_password_editext);
        confirm_password_layout = findViewById(R.id.confirm_password_layout);
        ll_progressBar = findViewById(R.id.llProgressBar_register);
        email_layout = findViewById(R.id.email_layout_register);
        password_layout = findViewById(R.id.password_layout_register);

    }

    public void register(){
        Log.d(TAG, "Register");
        ll_progressBar.setVisibility(View.VISIBLE);
        // ll_progressBar.setIndeterminate(true);
        if (!validate()) {
            onRegisterFailed();
            return;
        }

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onLoginSuccess or onLoginFailed
                        btn_register.setEnabled(false);
                      //  User user = new User(email, password);
                       // postUser(user);
                        createUser(email,password);
                        ll_progressBar.setVisibility(View.GONE);
                    }
                }, 3000);

    }

    public void onRegisterFailed() {
        Toast.makeText(getBaseContext(), "Création de compte  échouée", Toast.LENGTH_LONG).show();
        ll_progressBar.setVisibility(View.GONE);
        btn_register.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        email = email_editext.getText().toString();
        password = password_editext.getText().toString();
        confirm_password = confirm_password_editext.getText().toString();
        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            email_layout.setError(getString(R.string.error_email));
            valid = false;
        } else {
            email_layout.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            password_layout.setError(getString(R.string.password_error_text));
            valid = false;
        } else {
            password_layout.setError(null);
        }

        if(!password.equals(confirm_password) ){
            confirm_password_layout.setError(getString(R.string.non_identiques_password));
            valid = false;
        } else {
            confirm_password_layout.setError(null);
        }

        return valid;
    }

    /*public void postUser(User user){
        apiService.register(user).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.isSuccessful()){

                    if(response.body()!=null){
                        try {
                            JSONObject user = new JSONObject(String.valueOf(response.body()));
                            Log.i(TAG , "POST USER FOR REGISTER " ) ;

                            Log.i(TAG , "POST USER FOR REGISTER " + response.code());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }


                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e(TAG, "Unable to submit post to API." + t.toString());

            }
        });
    } */

    public void createUser(String email, String password){

        Call<User> call = ApiUtils.getApiService().register(email,password);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(!response.isSuccessful()){
                    Log.i("TAG_APP", "INFO ! "+response.code());
                    return;
                }
                User user = response.body();

                Log.i("TAG_APP", "USER_EMAIL: "+user.getEmail());
                Log.i("TAG_APP", "ID : "+user.getId());

            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.i("TAG_APP", "FAILURE");
            }
        });
    }
}
