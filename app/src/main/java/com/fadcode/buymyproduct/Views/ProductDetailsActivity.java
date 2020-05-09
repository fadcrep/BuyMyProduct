package com.fadcode.buymyproduct.Views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.fadcode.buymyproduct.Api.ApiUtils;
import com.fadcode.buymyproduct.Model.Comment;
import com.fadcode.buymyproduct.Model.Product;
import com.fadcode.buymyproduct.R;
import com.fadcode.buymyproduct.Views.Adapter.CommentAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductDetailsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private CommentAdapter commentAdapter;
    private List<Comment> commentList;
    private Context context;

    ImageView imageProduct;
    TextView productName;

    FloatingActionButton floating_action_button;
    Product product;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // toolbar fancy stuff
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.product_detail_title);

        imageProduct = findViewById(R.id.imageProduct);
        productName = findViewById(R.id.productName);

        recyclerView = findViewById(R.id.recycleComment);

        floating_action_button = findViewById(R.id.floating_action_button);

        Intent intent = getIntent();
        if(intent.getExtras() != null){
            product = (Product) intent.getSerializableExtra("data");
            String title = product.getTitle();
            productName.setText(title);
            Picasso.get()
                    .load(product.getFilename())
                    .resize(50, 50)
                    .centerCrop()
                    .into(imageProduct);

            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            commentList = new ArrayList<>();
            commentAdapter = new CommentAdapter(commentList, this);
            recyclerView.setAdapter(commentAdapter);

            getComments(product.getId());

        }

        floating_action_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createComment(product.getId(), "lola22@mail.com", "tres belle photo-image");
                getComments(product.getId());
            }
        });

    }

    public void getComments(String productId){


        Call<List<Comment>> comments = ApiUtils.getApiService().getComments(productId);
        comments.enqueue(new Callback<List<Comment>>() {
            @Override
            public void onResponse(Call<List<Comment>> call, Response<List<Comment>> response) {

                if(response.isSuccessful()){
                    if (response.body() != null){
                        List<Comment> comments = response.body();
                        commentList.clear();
                        commentList.addAll(comments);
//                        commentList = response.body();
                        commentAdapter.notifyDataSetChanged();
                        //Comment currentComment = commentList.get(0);


//                       Log.i("TAG_APP", currentComment.getContent());
//                       Log.i("TAG_APP", currentComment.getEmail());
//                       Log.i("TAG_APP", currentComment.getProductId());
                        //Log.i("TAG_APP", currentComment.getId());
                    } else {
                        //commentList.size();
                    }

                }
            }

            @Override
            public void onFailure(Call<List<Comment>> call, Throwable t) {
                Log.i("TAG_APP", t.getMessage());
            }
        });
    }

    public void createComment(String productId, String email, String content){
        Call<Comment> addcomments = ApiUtils.getApiService().createComment(productId, email, content);

        addcomments.enqueue(new Callback<Comment>() {
            @Override
            public void onResponse(Call<Comment> call, Response<Comment> response) {
                if(response.isSuccessful()){
                    Log.i("TAG_APP", String.valueOf(response.code()));
                }
            }

            @Override
            public void onFailure(Call<Comment> call, Throwable t) {

            }
        });
    }
}

