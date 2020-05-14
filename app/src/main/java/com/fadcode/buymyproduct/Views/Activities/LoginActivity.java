package com.fadcode.buymyproduct.Views.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.fadcode.buymyproduct.Api.ApiUtils;
import com.fadcode.buymyproduct.Model.User;
import com.fadcode.buymyproduct.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.textview.MaterialTextView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    Context context;
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
        btn_connexion.setEnabled(false);
        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onLoginSuccess or onLoginFailed

                        Log.i("email", " " + email);
                        Log.i("password", " " + password);
                        authenticateUser(email, password);
                        ll_progressBar.setVisibility(View.GONE);


                    }
                }, 3000);

    }

    public void onLoginFailed() {
       // Toast.makeText(getBaseContext(), "Connexion échouée", Toast.LENGTH_LONG).show();
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

                    Log.i("TAG_APP 1", "INFO ! " + response.code() + " 229");
                    return;
                }
                Log.i("TAG_APP 2", "INFO ! " + response.code() + " 228");
                List<User> users = response.body();
                User currentUser = users.get(0);

                Log.i("TAG_APP 3", "USER_EMAIL: " + currentUser.getEmail());
                Log.i("TAG_APP 4", "ID : " + currentUser.getId());
                SharedPreferences preferences =  getApplicationContext().getSharedPreferences("MY_PREF", 0);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString( "user_email",currentUser.getEmail());
                editor.putString("user_id", currentUser.getId());
                if(currentUser.getName() != null){
                    editor.putString("name", currentUser.getName());
                }
                editor.commit();
                btn_connexion.setEnabled(true);
                email_editext.setText("");
                password_editext.setText("");
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Log.i("TAG_APP 5", "FAILURE " + t.toString());
                Snackbar.make(btn_connexion, R.string.connexion_failed_text, Snackbar.LENGTH_SHORT)
                        .show();

                btn_connexion.setEnabled(true);

            }
        });


    }

}