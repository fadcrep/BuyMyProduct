package com.fadcode.buymyproduct;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {

    private List<Product> productList;
    private Context context;
    private ClickedItem clickedItem;

    public ProductAdapter(ClickedItem clickedItem){
        this.clickedItem = clickedItem;
    }

    public void setData (List<Product> productList) {
        this.productList = productList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ProductAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new ProductAdapter.ViewHolder(LayoutInflater.from(context).inflate(R.layout.list_product, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ProductAdapter.ViewHolder holder, int position) {
        Product product = productList.get(position);

        String productName = product.getTitle();
        holder.productName.setText(productName);
        Picasso.get()
                .load(product.getFilename())
                .resize(50, 50)
                .centerCrop()
                .into(holder.imageProduct);

        holder.imageProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickedItem.clickedProduct(product);

            }
        });
    }

    public interface ClickedItem{
        public void clickedProduct(Product product);
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView productName;
        ImageView imageProduct;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.productName);
            imageProduct = itemView.findViewById(R.id.imageProduct);
        }
    }
}
