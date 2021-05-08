package com.example.database.Adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.database.Classes.Post;
import com.example.database.R;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class PostAdapter  extends RecyclerView.Adapter<PostAdapter.ViewHolder> {


    public interface OnStateClickListener{
        void onStateClick(Post state, int position);
    }

    private final OnStateClickListener onClickListener;

    private final LayoutInflater inflater;
    private final List<Post> posts;

    public PostAdapter(Context context, List<Post> posts, OnStateClickListener onClickListener) {
        this.onClickListener = onClickListener;
        this.posts = posts;
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public PostAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NotNull PostAdapter.ViewHolder holder, int position) {
        Post post = posts.get(position);
        if(post.getImgString()!= null)
            if(post.getAuthor().equals("Пост из вк"))
                Picasso.with(inflater.getContext()).load(post.getImgString()).into(holder.imageView);
            else
                holder.imageView.setImageURI(Uri.parse(post.getImgString()));
        holder.author.setText(post.getAuthor());
        holder.title.setText(post.getTitle());
        holder.description.setText(post.getDescription());
        holder.date.setText(post.getDate());

        holder.itemView.setOnClickListener(v -> onClickListener.onStateClick(post, position));
    }

    @Override
    public int getItemCount() {
        return  posts.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final ImageView imageView;
        final TextView author;
        final TextView title;
        final TextView description;
        final TextView date;

        ViewHolder(View view){
            super(view);
            imageView = view.findViewById(R.id.image);
            author = view.findViewById(R.id.authorItem);
            title = view.findViewById(R.id.titleItem);
            description = view.findViewById(R.id.descriptionItem);
            date = view.findViewById(R.id.dateItem);
        }
    }
}
