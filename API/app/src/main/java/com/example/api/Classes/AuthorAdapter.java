package com.example.api.Classes;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.api.R;

import java.util.List;
public class AuthorAdapter extends RecyclerView.Adapter<AuthorAdapter.ViewHolder> {

    public interface OnStateClickListener{
        void onStateClick(int position);
    }

    private final OnStateClickListener onClickListener;

    private final LayoutInflater inflater;
    private final List<String> authors;
    private int selectedPosition = RecyclerView.NO_POSITION;

    public AuthorAdapter(Context context, List<String> authors, OnStateClickListener onClickListener) {
        this.onClickListener = onClickListener;
        this.authors = authors;
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public AuthorAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.list_author, parent, false);
        return new AuthorAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.itemView.setSelected(false);
        String author = authors.get(position);
        holder.author.setText(author);
        holder.itemView.setOnClickListener(v -> {
            onClickListener.onStateClick(position);
        });
    }

    @Override
    public int getItemCount() {
        return  authors.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView author;

        ViewHolder(View view){
            super(view);
            author = view.findViewById(R.id.itemAuthor);
        }
    }
}
