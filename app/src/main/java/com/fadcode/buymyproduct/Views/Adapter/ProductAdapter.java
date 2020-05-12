package com.fadcode.buymyproduct.Views.Adapter;


import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fadcode.buymyproduct.DatabaseHelper;
import com.fadcode.buymyproduct.Model.Product;
import com.fadcode.buymyproduct.R;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> implements Filterable {

    private List<Product> productList;
    private List<Product> productFiltered;
    private Context context;
    private ProductsAdapterListener productsAdapterListener;
    DatabaseHelper databaseHelper;

    public ProductAdapter(List<Product> productList, Context context, ProductsAdapterListener productsAdapterListener) {
        this.productList = productList;
        this.context = context;
        this.productFiltered= productList;
        this.productsAdapterListener = productsAdapterListener;
    }

  /*  public void setData (List<Product> productList) {
        this.productList = productList;
        notifyDataSetChanged();
    } */


    @Override
    public ProductAdapter.ViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_product, parent, false);

        return new ViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(ProductAdapter.ViewHolder holder, int position) {
        final Product product = productFiltered.get(position);

        String productName = product.getTitle();
        holder.productName.setText(productName);
        Picasso.get()
                .load(product.getFilename())
                .resize(50, 50)
                .centerCrop()
                .into(holder.imageProduct);

      /*  holder.imageProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickedItem.clickedProduct(product);

            }
        }); */
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {

                String charString = charSequence.toString();
                if (charString.isEmpty()){

                    productFiltered=productList;
                } else{
                    List<Product> filteredList = new ArrayList<>();

                    for(Product row: productList){

                        if(row.getTitle().toLowerCase().contains(charString.toLowerCase())||
                                row.getId().contains(charSequence)){
                            filteredList.add(row);
                        }
                    }

                    productFiltered=filteredList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values=productFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                productFiltered= (ArrayList<Product>) filterResults.values;
                notifyDataSetChanged();

            }
        };
    }


    public interface ProductsAdapterListener {
        void onProductSelected(Product product);
    }

    @Override
    public int getItemCount() {
        return productFiltered.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView productName;
        ImageView imageProduct;
        ImageView imageFavorite;

        public ViewHolder( View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.productName);
            imageProduct = itemView.findViewById(R.id.imageProduct);
            imageFavorite = itemView.findViewById(R.id.imageFavorite);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    productsAdapterListener.onProductSelected(productFiltered.get(getAdapterPosition()));
                }
            });
            databaseHelper = new DatabaseHelper(context);
           final SharedPreferences preferences = context.getSharedPreferences("MY_PREF", 0);
            if(preferences != null){
                imageFavorite.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                       MaterialAlertDialogBuilder materialAlertDialogBuilder = new MaterialAlertDialogBuilder(context);
                        materialAlertDialogBuilder
                                 .setTitle(R.string.add_product_in_favoris)
                                 .setPositiveButton(R.string.add_product_to_favorite, new DialogInterface.OnClickListener() {
                                     @Override
                                     public void onClick(DialogInterface dialog, int which) {
                                         Toast.makeText(context, "nPositive button", Toast.LENGTH_SHORT).show();
                                         Product product = productFiltered.get(getAdapterPosition());
                                         if(product.getId() != null){
                                             databaseHelper.addProductToFavorite(productFiltered.get(getAdapterPosition()));
                                             imageFavorite.setVisibility(View.INVISIBLE);
                                             dialog.dismiss();
                                         }

                                     }
                                 })
                                 .setNegativeButton(R.string.cancel_favorite, new DialogInterface.OnClickListener() {
                                     @Override
                                     public void onClick(DialogInterface dialog, int which) {
                                         Toast.makeText(context, "Negative button", Toast.LENGTH_SHORT).show();
                                     }
                                 })
                                .show();



                        }


                            // Respond to neutral button press


                });


            } else {
                imageFavorite.setVisibility(View.INVISIBLE);
            }





        }
    }

}

