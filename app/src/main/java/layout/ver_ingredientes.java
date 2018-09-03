package layout;

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

import com.example.dany.stockhome.DbHelper;
import com.example.dany.stockhome.Ingrediente;
import com.example.dany.stockhome.R;
import com.example.dany.stockhome.RecyclerAdapter_ingredientes_adiciona;

import java.util.ArrayList;


public class ver_ingredientes extends Fragment {

   RecyclerView.Adapter adapter;

    ArrayList<Ingrediente> arrayList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ver_ingredientes, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        DbHelper dbHelper = new DbHelper(getActivity().getApplicationContext());
        SQLiteDatabase sqLiteDatabase = dbHelper.getReadableDatabase();


        dbHelper.onOpen(sqLiteDatabase);
        int id = dbHelper.getIdReceita()+1 ;




        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView_ing);
        RecyclerView.LayoutManager mLayoutManager;
        dbHelper = new DbHelper(getActivity().getApplicationContext());
        Cursor cursor = dbHelper.getIngredientes(id);
        mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        if (cursor.getCount() != 0 ) {
            cursor.moveToFirst();
            do {

                Ingrediente fruit = new Ingrediente(cursor.getInt(0), cursor.getFloat(1), cursor.getString(2), cursor.getString(3),cursor.getInt(4),cursor.getInt(5));
                arrayList.add(fruit);
            } while (cursor.moveToNext());

            dbHelper.close();

            adapter = new RecyclerAdapter_ingredientes_adiciona(arrayList);
            recyclerView.setAdapter(adapter);
    }
    }
}

