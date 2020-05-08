package com.fadcode.buymyproduct;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class ProductDetailsActivity extends AppCompatActivity {

    ImageView imageProduct;
    TextView productName;
    Product product;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        imageProduct = findViewById(R.id.imageProduct);
        productName = findViewById(R.id.productName);

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
        }
    }
}
