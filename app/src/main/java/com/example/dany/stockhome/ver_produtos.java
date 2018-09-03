package com.example.dany.stockhome;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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

public class ver_produtos extends AppCompatActivity implements  SearchView.OnQueryTextListener {

    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<Produto> arrayList = new ArrayList<>();
    DbHelper dbHelper;
    private static final int VERTICAL_ITEM_SPACE =48;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ver_produtos_activity);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabVer);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ver_produtos.this,inserir_produtos.class));
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewVer);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        dbHelper = new DbHelper(this);
        SQLiteDatabase sqLiteDatabase = dbHelper.getReadableDatabase();

        Cursor cursor = dbHelper.getProdutosN();

        if(cursor.getCount() == 0){
            AlertDialog.Builder caixaAlerta = new AlertDialog.Builder(ver_produtos.this);
            caixaAlerta.setMessage("Tem todos os produtos disponiveis");
            caixaAlerta.setPositiveButton("Ver", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    startActivity(new Intent(ver_produtos.this,consultar_produtos.class));
                }
            });
            caixaAlerta.show();

        }else{
            cursor.moveToFirst();
            do{

                Produto fruit = new Produto(cursor.getInt(0),cursor.getString(1),cursor.getFloat(2),cursor.getString(3),cursor.getInt(4));
                arrayList.add(fruit);
            }while (cursor.moveToNext());
        }

        dbHelper.close();

        recyclerView.addItemDecoration(new VerticalSpaceItemDecoration(VERTICAL_ITEM_SPACE));

        adapter = new RecyclerAdapterProdutos(arrayList);
        recyclerView.setAdapter(adapter);


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
        Cursor produto = dbHelper.verProduto(query);
        ArrayList<Produto> produtos = new ArrayList<>();
        if (produto.getCount() == 0) {
            AlertDialog.Builder caixaAlerta = new AlertDialog.Builder(ver_produtos.this);
            caixaAlerta.setMessage("O nome inserido nao consta nos produtos que estao disponiveis.");
            caixaAlerta.setTitle("Aviso!");
            caixaAlerta.setPositiveButton("Adicionar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    startActivity(new Intent(ver_produtos.this, inserir_produtos.class));
                }
            });
            caixaAlerta.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            caixaAlerta.setNegativeButton("Produtos indisponiveis", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    startActivity(new Intent(ver_produtos.this,ver_produtos.class));
                }
            });
            caixaAlerta.show();
        } else {
            produto.moveToFirst();
            do {
                Produto fruit = new Produto(produto.getInt(0),produto.getString(1), produto.getFloat(2),produto.getString(3), produto.getInt(4));
                produtos.add(fruit);
            } while (produto.moveToNext());
        }

        dbHelper.close();

        adapter = new RecyclerAdapterProdutos(produtos);
        recyclerView.setAdapter(adapter);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }
}
