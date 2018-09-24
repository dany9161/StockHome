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
import android.widget.TextView;

import com.example.dany.stockhome.Database.DBHelper;
import com.example.dany.stockhome.Database.Ingrediente;
import com.example.dany.stockhome.R;
import com.example.dany.stockhome.RecyclerAdapter.IngredientesRecyclerAdapter;

import java.util.ArrayList;

public class ver_ingredientes_editaFragment extends Fragment {

    TextView tx;
    RecyclerView.Adapter adapter;
    int i;
    DBHelper DBHelper;
    ArrayList<Ingrediente> arrayList = new ArrayList<>();
    SQLiteDatabase sqLiteDatabase;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    Cursor cursor;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        try {
            Bundle bundle = this.getArguments();
            i = bundle.getInt("ID");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return inflater.inflate(R.layout.fragment_ver_ingredientes_edita, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        DBHelper = new DBHelper(getActivity().getApplicationContext());
        sqLiteDatabase = DBHelper.getReadableDatabase();
        DBHelper.onOpen(sqLiteDatabase);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView_ingedita);
        DBHelper = new DBHelper(getActivity().getApplicationContext());
        mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);

        cursor = DBHelper.getIngredientes(i);
        if (cursor.getCount() != 0) {
            cursor.moveToFirst();
            do {

                Ingrediente fruit = new Ingrediente(cursor.getInt(0), cursor.getFloat(1), cursor.getString(2), cursor.getString(3), cursor.getInt(4), cursor.getInt(5));
                arrayList.add(fruit);
            } while (cursor.moveToNext());
            DBHelper.close();
            adapter = new IngredientesRecyclerAdapter(arrayList);
            recyclerView.setAdapter(adapter);
        }
    }
}