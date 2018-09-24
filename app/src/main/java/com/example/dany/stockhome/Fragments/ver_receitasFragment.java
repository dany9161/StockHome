package com.example.dany.stockhome.Fragments;


import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.dany.stockhome.Database.DBHelper;
import com.example.dany.stockhome.R;
import com.example.dany.stockhome.Database.Receita;
import com.example.dany.stockhome.RecyclerAdapter.ReceitasRecyclerAdapter;
import com.example.dany.stockhome.VerticalSpaceItemDecoration;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class ver_receitasFragment extends Fragment {

    RecyclerView.Adapter adapter;
    ArrayList<Receita> arrayList = new ArrayList<>();
    String x;
    ArrayList<Receita> procura = new ArrayList<>();
    private int VERTICAL_ITEM_SPACE = 48;
    DBHelper DBHelper;
    RecyclerView recyclerView;
    Cursor cursor;
    TextView textView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        try {
            Bundle bundle = this.getArguments();
            x = bundle.getString("Nome");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return inflater.inflate(R.layout.fragment_ver_receitas, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerViewMain);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        DBHelper = new DBHelper(getActivity().getApplicationContext());

        //da me as receitas que posso fazer
        cursor = DBHelper.getReceitas();
        textView = (TextView) view.findViewById(R.id.textView7);

        //ve se nao é uma pesquisa
        if (x == null) {
            //verifica se ha receitas na base de daos
            if (cursor.getCount() > 0) {
                arrayList = DBHelper.getReceita();
                //verifica se ha receitas possiveis de fazer
                if (arrayList.size() != 0) {
                    recyclerView.addItemDecoration(new VerticalSpaceItemDecoration(VERTICAL_ITEM_SPACE));
                    adapter = new ReceitasRecyclerAdapter(arrayList);
                    recyclerView.setAdapter(adapter);
                } else {
                    //se nao houver receitas possiveis de se fazer
                    textView.setVisibility(View.VISIBLE);
                }
            } else {
                //se nao hover receitas na base de dados
                textView.setVisibility(View.VISIBLE);
            }
            //se nao for uma pesquisa
        } else {
            //ve se ha receitas na base de dados
            if (cursor.getCount() > 0) {
                arrayList = DBHelper.getReceita();
                if (arrayList.contains(x)) {

                    for (int i = 0; i < arrayList.size(); i++) {
                        if (arrayList.get(i).toString() == x) {
                            procura.add(arrayList.get(i));
                            arrayList = procura;
                        }
                    }
                } else {
                    textView.setVisibility(View.VISIBLE);
                }
            }
            recyclerView.addItemDecoration(new VerticalSpaceItemDecoration(VERTICAL_ITEM_SPACE));
            adapter = new ReceitasRecyclerAdapter(arrayList);
            recyclerView.setAdapter(adapter);
        }
    }
}