package com.example.dany.stockhome.Fragments;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.dany.stockhome.Database.DBHelper;
import com.example.dany.stockhome.Database.Ingrediente;
import com.example.dany.stockhome.R;
import com.example.dany.stockhome.RecyclerAdapter.IngFazerRecyclerAdapter;

import java.util.ArrayList;

/**
 * Fragmento para mostrar os ingredientes necessários para fazer a receita na activity FazerReceitaActivity
 */
public class ver_ing_fazerFragment extends Fragment {

    RecyclerView.Adapter adapter;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    ArrayList<Ingrediente> arrayList = new ArrayList<>();
    int id;
    DBHelper DBHelper;
    SQLiteDatabase sqLiteDatabase;
    Cursor cursor;
    Ingrediente fruit;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        try {
            Bundle bundle = this.getArguments();
            id = bundle.getInt("ID");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return inflater.inflate(R.layout.fragment_ver_ing_fazer, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        DBHelper = new DBHelper(getActivity().getApplicationContext());
        sqLiteDatabase = DBHelper.getReadableDatabase();
        DBHelper.onOpen(sqLiteDatabase);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView_ingFazer);
        DBHelper = new DBHelper(getActivity().getApplicationContext());
        cursor = DBHelper.getIngredientes(id);
        mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        if (cursor.getCount() != 0) {
            cursor.moveToFirst();
            do {
                fruit = new Ingrediente(cursor.getInt(0), cursor.getFloat(1), cursor.getString(2), cursor.getString(3), cursor.getInt(4), cursor.getInt(5));
                arrayList.add(fruit);
            } while (cursor.moveToNext());

            DBHelper.close();
            adapter = new IngFazerRecyclerAdapter(arrayList);
            recyclerView.setAdapter(adapter);
        }
    }
}