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

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    MaterialButton btn_connexion;
    View ll_progressBar;
    private String email;
    private String password;
    TextInputLayout email_layout, password_layout;
    MaterialTextView goto_registerActivity;
    TextInputEditText email_editext, password_editext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        findViewById();

       btn_connexion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });

        goto_registerActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });


    }

    public void findViewById() {

        btn_connexion = findViewById(R.id.btn_connexion);
        goto_registerActivity = findViewById(R.id.goto_register);
        email_editext = findViewById(R.id.email_editext);
        password_editext = findViewById(R.id.password_editext);
        ll_progressBar = findViewById(R.id.llProgressBar);
        email_layout = findViewById(R.id.email_layout);
        password_layout = findViewById(R.id.password_layout);

    }

    public void login() {
        Log.d(TAG, "Login");
        ll_progressBar.setVisibility(View.VISIBLE);
        // ll_progressBar.setIndeterminate(true);
        if (!validate()) {
            onLoginFailed();
            return;
        }
        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onLoginSuccess or onLoginFailed
                        btn_connexion.setEnabled(false);
                        Log.i("email", " " + email);
                        Log.i("password", " " + password);
                        authenticateUser(email, password);
                        ll_progressBar.setVisibility(View.GONE);
                    }
                }, 3000);

    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Connexion échouée", Toast.LENGTH_LONG).show();
        ll_progressBar.setVisibility(View.GONE);
        btn_connexion.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;
        email = email_editext.getText().toString();
        password = password_editext.getText().toString();

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

        return valid;
    }


    public void authenticateUser(String email, String password) {

        Call <List<User>> call = ApiUtils.getApiService().login(email, password);
        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (!response.isSuccessful()) {

                    Log.i("TAG_APP", "INFO ! " + response.code());
                    return;
                }

                List<User> users = response.body();
                User currentUser = users.get(0);

                Log.i("TAG_APP", "USER_EMAIL: " + currentUser.getEmail());
                Log.i("TAG_APP", "ID : " + currentUser.getId());

                startActivity(new Intent(LoginActivity.this, MainActivity.class));
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Log.i("TAG_APP", "FAILURE " + t.toString());
            }
        });
        /*call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (!response.isSuccessful()) {
                    Log.i("TAG_APP", "INFO ! " + response.code());
                    return;
                }

                User user = response.body();

                Log.i("TAG_APP", "USER_EMAIL: " + user.getEmail());
                Log.i("TAG_APP", "ID : " + user.getId());
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.i("TAG_APP", "FAILURE " + t.toString());
            }
        });*/

    }

}