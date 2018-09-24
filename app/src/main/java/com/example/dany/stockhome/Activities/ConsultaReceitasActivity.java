package com.example.dany.stockhome.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.dany.stockhome.Database.DBHelper;
import com.example.dany.stockhome.R;
import com.example.dany.stockhome.Database.Receita;
import com.example.dany.stockhome.RecyclerAdapter.ReceitasRecyclerAdapter;
import com.example.dany.stockhome.VerticalSpaceItemDecoration;

import java.util.ArrayList;

public class ConsultaReceitasActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<Receita> arrayList = new ArrayList<>();
    DBHelper DBHelper = new DBHelper(this);
    private int VERTICAL_ITEM_SPACE = 48;
    FloatingActionButton fab;
    Cursor cursor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consulta_receitas);

        fab = (FloatingActionButton) findViewById(R.id.fab1);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(v.getContext(), AddReceitasActivity.class));
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewmostrareceitas);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        DBHelper = new DBHelper(this);
        cursor = DBHelper.getReceitas();

        if (cursor.getCount() == 0) {
            AlertDialog.Builder caixaAlerta = new AlertDialog.Builder(ConsultaReceitasActivity.this);
            caixaAlerta.setMessage("Nao há receitas na base de dados");
            caixaAlerta.setTitle("Alerta!");
            caixaAlerta.setPositiveButton("Adicionar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    startActivity(new Intent(getApplicationContext(), AddReceitasActivity.class));
                }
            });
            caixaAlerta.show();

        } else {
            cursor.moveToFirst();
            do {

                Receita fruit = new Receita(cursor.getInt(0), cursor.getString(1), cursor.getString(2));
                arrayList.add(fruit);
            } while (cursor.moveToNext());

            DBHelper.close();
            recyclerView.addItemDecoration(new VerticalSpaceItemDecoration(VERTICAL_ITEM_SPACE));

            adapter = new ReceitasRecyclerAdapter(arrayList);
            recyclerView.setAdapter(adapter);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem searchItem = menu.findItem(R.id.action_searchable_activity);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(this);
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        Cursor produto = DBHelper.showReceita(query);
        ArrayList<Receita> produtos = new ArrayList<>();
        if (produto.getCount() == 0) {
            AlertDialog.Builder caixaAlerta = new AlertDialog.Builder(ConsultaReceitasActivity.this);
            caixaAlerta.setMessage("A receita nao consta na base de dados, o que deseja fazer?");
            caixaAlerta.setTitle("Aviso!");
            caixaAlerta.setPositiveButton("Adicionar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    startActivity(new Intent(ConsultaReceitasActivity.this, AddReceitasActivity.class));
                }
            });
            caixaAlerta.setNeutralButton("Nada", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    startActivity(new Intent(getApplicationContext(), ConsultaReceitasActivity.class));
                }
            });
            caixaAlerta.show();
        } else {
            produto.moveToFirst();
            do {
                Receita fruit = new Receita(produto.getInt(0), produto.getString(1), produto.getString(2));
                produtos.add(fruit);
            } while (produto.moveToNext());
        }

        DBHelper.close();

        adapter = new ReceitasRecyclerAdapter(produtos);
        recyclerView.setAdapter(adapter);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }
}
