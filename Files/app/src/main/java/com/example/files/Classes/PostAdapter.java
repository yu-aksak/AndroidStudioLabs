package com.example.files.Classes;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.files.R;
import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {


    public interface OnStateClickListener{
        void onStateClick(Post state, int position);
    }

    private final OnStateClickListener onClickListener;

    private LayoutInflater inflater;
    private List<Post> posts;

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
    public void onBindViewHolder(PostAdapter.ViewHolder holder, int position) {
        Post post = posts.get(position);
        holder.imageView.setImageURI(Uri.parse(post.getImgString()));
        holder.title.setText(post.getTitle());
        holder.description.setText(post.getDescription());

        holder.itemView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                onClickListener.onStateClick(post, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return  posts.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final ImageView imageView;
        final TextView title;
        final TextView description;

        ViewHolder(View view){
            super(view);
            imageView = (ImageView)view.findViewById(R.id.image);
            title = (TextView) view.findViewById(R.id.titleItem);
            description = (TextView) view.findViewById(R.id.descriptionList);
        }
    }
}
