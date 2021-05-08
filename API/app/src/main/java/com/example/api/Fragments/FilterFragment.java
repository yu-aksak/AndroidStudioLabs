package com.example.api.Fragments;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.api.Classes.AuthorAdapter;
import com.example.api.R;

import java.util.ArrayList;
import java.util.Objects;

public class FilterFragment extends Fragment {
    private CheckBox date;
    private RadioGroup group;
    private RadioButton up;
    private RadioButton down;
    private CheckBox author;
    private RecyclerView authors;
    private Button apply;
    private Button reset;
    private Button back;
    private TextView chosenAuthor;

    private String _author;

    private ArrayList<String> content = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root =  inflater.inflate(R.layout.fragment_filter, container, false);
        date = root.findViewById(R.id.dateChecker);
        author = root.findViewById(R.id.authorChecker);
        authors = root.findViewById(R.id.authors);
        apply = root.findViewById(R.id.apply);
        reset = root.findViewById(R.id.reset);
        back = root.findViewById(R.id.back2);
        group = root.findViewById(R.id.group);
        up = root.findViewById(R.id.up);
        down = root.findViewById(R.id.down);
        chosenAuthor = root.findViewById(R.id.chosenAuthor);

        View.OnClickListener applyButtonOnClickListener = view -> Apply();
        apply.setOnClickListener(applyButtonOnClickListener);

        View.OnClickListener resetButtonOnClickListener = view -> Reset();
        reset.setOnClickListener(resetButtonOnClickListener);

        View.OnClickListener backButtonOnClickListener = view -> Back();
        back.setOnClickListener(backButtonOnClickListener);

        date.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    group.setVisibility(View.VISIBLE);
                    up.setChecked(true);
                }
                else
                    group.setVisibility(View.INVISIBLE);
            }
        });

        author.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    authors.setVisibility(View.VISIBLE);
                else
                    authors.setVisibility(View.INVISIBLE);
            }
        });

        Bundle bundle = getArguments();
        content = bundle.getStringArrayList( "authors");

        AuthorAdapter.OnStateClickListener stateClickListener = (position) -> {
            _author = content.get(position);
            chosenAuthor.setText(_author);
        };

        AuthorAdapter adapter = new AuthorAdapter(getActivity(), content, stateClickListener);
        authors.setAdapter(adapter);
        return root;
    }

    private void Apply(){
        Bundle bundle = new Bundle();
        if(date.isChecked()) {
            if (down.isChecked())
                bundle.putString("date", "down");
            else
                bundle.putString("date", "up");
        }
        else
            bundle.putString("date", null);

        if(author.isChecked() && _author != null)
            bundle.putString("author", _author);
        else
            bundle.putString("author", null);
        ImageMusicFragment imf = (ImageMusicFragment) getActivity().getSupportFragmentManager().
                findFragmentByTag("imageMusic");
        imf.setArguments(bundle);
        getActivity().getSupportFragmentManager().beginTransaction().show(imf).commit();
        imf.Filter();
        getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();

    }

    private void Reset(){
        ImageMusicFragment imf = (ImageMusicFragment) getActivity().getSupportFragmentManager().
                findFragmentByTag("imageMusic");
        Bundle bundle = null;
        imf.setArguments(bundle);
        getActivity().getSupportFragmentManager().beginTransaction().show(imf).commit();
        imf.Filter();
        getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
    }

    private void Back(){
        ImageMusicFragment imf = (ImageMusicFragment) getActivity().getSupportFragmentManager().
                findFragmentByTag("imageMusic");
        getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
        getActivity().getSupportFragmentManager().beginTransaction().show(imf).commit();
    }
}