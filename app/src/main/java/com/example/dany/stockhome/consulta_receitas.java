package com.example.dany.stockhome;

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

import java.util.ArrayList;

public class consulta_receitas extends AppCompatActivity implements SearchView.OnQueryTextListener {

    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<Receita> arrayList = new ArrayList<>();
    DbHelper dbHelper = new DbHelper(this);
    private static final int VERTICAL_ITEM_SPACE =48;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.consultar__receitas_activity);



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab1);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(v.getContext(),inserir_receitas.class));
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewmostrareceitas);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

    dbHelper = new DbHelper(this);
        Cursor cursor = dbHelper.getReceitas();


        if(cursor.getCount() == 0){
            AlertDialog.Builder caixaAlerta = new AlertDialog.Builder(consulta_receitas.this);
            caixaAlerta.setMessage("Nao há receitas na base de dados");
            caixaAlerta.setTitle("Alerta!");
            caixaAlerta.setPositiveButton("Adicionar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    startActivity(new Intent(getApplicationContext(),inserir_receitas.class));
                }
            });
            caixaAlerta.show();

        }else{
            cursor.moveToFirst();
            do{

                Receita fruit = new Receita(cursor.getInt(0),cursor.getString(1),cursor.getString(2));
                arrayList.add(fruit);
            }while (cursor.moveToNext());

            dbHelper.close();
            recyclerView.addItemDecoration(new VerticalSpaceItemDecoration(VERTICAL_ITEM_SPACE));

            adapter = new RecyclerAdapterReceitas(arrayList);
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
        Cursor produto = dbHelper.showReceita(query);
        ArrayList<Receita> produtos = new ArrayList<>();
        if (produto.getCount() == 0) {
            AlertDialog.Builder caixaAlerta = new AlertDialog.Builder(consulta_receitas.this);
            caixaAlerta.setMessage("A receita nao consta na base de dados, o que deseja fazer?");
            caixaAlerta.setTitle("Aviso!");
            caixaAlerta.setPositiveButton("Adicionar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    startActivity(new Intent(consulta_receitas.this, inserir_receitas.class));
                }
            });
            caixaAlerta.setNeutralButton("Nada", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    startActivity(new Intent(getApplicationContext(),consulta_receitas.class));
                }
            });
            caixaAlerta.show();
        } else {
            produto.moveToFirst();
            do {
                Receita fruit = new Receita(produto.getInt(0),produto.getString(1),produto.getString(2));
                produtos.add(fruit);
            } while (produto.moveToNext());
        }

        dbHelper.close();

        adapter = new RecyclerAdapterReceitas(produtos);
        recyclerView.setAdapter(adapter);
        return false;
}

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }
}
