package com.fadcode.buymyproduct;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolderComment> {
    private List<Comment> comments;
    private Context context;

    public CommentAdapter(List<Comment> comments, Context context) {
        this.comments = comments;
        this.context = context;
    }

    @NonNull
    @Override
    public CommentAdapter.ViewHolderComment onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_comment, parent, false);
        return new ViewHolderComment(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentAdapter.ViewHolderComment holder, int position) {
        final Comment comment = comments.get(position);
        String productId = comment.getProductId();
        String email = comment.getEmail();
        String content = comment.getContent();

        Log.i("OnBindView", productId);

        holder.productId.setText(productId);
        holder.email.setText(email);
        holder.content.setText(content);

        Log.i("Holder", holder.productId.toString());
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    public class ViewHolderComment extends RecyclerView.ViewHolder {
        TextView productId;
        TextView email;
        TextView content;
        public ViewHolderComment(@NonNull View itemView) {
            super(itemView);

            productId = itemView.findViewById(R.id.productId);
            email = itemView.findViewById(R.id.email);
            content = itemView.findViewById(R.id.content);

        }
    }
}
