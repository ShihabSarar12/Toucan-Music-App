package com.example.toucan;

import static com.example.toucan.Music.musicFiles;

import android.os.Bundle;

import androidx.appcompat.view.menu.MenuBuilder;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;


public class SongsFragment extends Fragment {
    RecyclerView recyclerView;
    MusicAdapter musicAdapter;

    public SongsFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_songs,container,false);
        recyclerView =view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);

        if(!(musicFiles.size()<1)) {
            musicAdapter=new MusicAdapter(getContext(),musicFiles);
            recyclerView.setAdapter(musicAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false));

        }
        return view;
    }
}