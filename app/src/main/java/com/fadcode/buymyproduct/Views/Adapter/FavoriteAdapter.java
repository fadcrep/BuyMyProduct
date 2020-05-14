package com.fadcode.buymyproduct.Views.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fadcode.buymyproduct.Database.DatabaseHelper;
import com.fadcode.buymyproduct.Model.Product;
import com.fadcode.buymyproduct.R;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.squareup.picasso.Picasso;

import java.util.List;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.ViewHolderFavorite> {

    private List<Product> productListFavorite;
    private Context context;


    public FavoriteAdapter(Context context, List<Product> productListFavorite){
        this.context = context;
        this.productListFavorite = productListFavorite;
    }

    @NonNull
    @Override
    public FavoriteAdapter.ViewHolderFavorite onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_favorite, parent, false);
        return new ViewHolderFavorite(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteAdapter.ViewHolderFavorite holder, int position) {
        Product productFavorite = productListFavorite.get(position);
        holder.productNameFavorite.setText(productFavorite.getTitle());
        Picasso.get()
                .load(productFavorite.getFilename())
                .resize(50, 50)
                .centerCrop()
                .into(holder.imageProductFavorite);
    }

    @Override
    public int getItemCount() {
        return productListFavorite.size();
    }

    public class ViewHolderFavorite extends RecyclerView.ViewHolder {
        public ImageView imageProductFavorite;
        public TextView productNameFavorite;
        public ImageView buttonDeletefavori;

        public ViewHolderFavorite(@NonNull View itemView) {
            super(itemView);

            imageProductFavorite = itemView.findViewById(R.id.imageProductFavorite);
            productNameFavorite = itemView.findViewById(R.id.productNameFavorite);
            buttonDeletefavori = itemView.findViewById(R.id.buttonDeletefavori);
            final DatabaseHelper databaseHelper;
            databaseHelper = new DatabaseHelper(context);

            buttonDeletefavori.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MaterialAlertDialogBuilder materialAlertDialogBuilder = new MaterialAlertDialogBuilder(context);
                    materialAlertDialogBuilder
                            .setTitle(R.string.delete_product_favorite)
                            .setPositiveButton(R.string.delete_product_favorite, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
//                                    Toast.makeText(context, "Positive button", Toast.LENGTH_SHORT).show();
                                    Product product = productListFavorite.get(getAdapterPosition());
                                    if (product.getId() != null) {
                                        databaseHelper.deleteProductToFavorite(product.getId());
                                        productListFavorite.remove(getAdapterPosition());
                                        dialog.dismiss();
                                        notifyItemRemoved(getAdapterPosition());
                                       // notifyItemRangeChanged(getAdapterPosition(), );
                                       // databaseHelper.favoriteProductList();
                                    }

                                }
                            })
                            .setNegativeButton(R.string.cancel_favorite, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
//                                    Toast.makeText(context, "Negative button", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .show();

                }
            });
        }

    }
}
