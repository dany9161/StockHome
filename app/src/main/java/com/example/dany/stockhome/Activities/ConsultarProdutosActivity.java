package com.example.dany.stockhome.Activities;

import android.app.SearchManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.dany.stockhome.Database.DBHelper;
import com.example.dany.stockhome.Database.Produto;
import com.example.dany.stockhome.RecyclerAdapter.ProdutosRecyclerAdapter;
import com.example.dany.stockhome.R;
import com.example.dany.stockhome.VerticalSpaceItemDecoration;

import java.util.ArrayList;

public class ConsultarProdutosActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<Produto> arrayList = new ArrayList<>();
    DBHelper DBHelper;
    private int VERTICAL_ITEM_SPACE = 48;
    FloatingActionButton fab;
    Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultar_produtos);


        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ConsultarProdutosActivity.this, AddProdutosActivity.class));
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        DBHelper = new DBHelper(this);
        cursor = DBHelper.getProdutos();

        if (cursor.getCount() == 0) {
            AlertDialog.Builder caixaAlerta = new AlertDialog.Builder(ConsultarProdutosActivity.this);
            caixaAlerta.setMessage("Nao há produtos na base de dados");
            caixaAlerta.setTitle("Alerta!");
            caixaAlerta.setPositiveButton("Adicionar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    startActivity(new Intent(ConsultarProdutosActivity.this, AddProdutosActivity.class));
                }
            });
            caixaAlerta.show();
        } else {
            cursor.moveToFirst();
            do {

                Produto fruit = new Produto(cursor.getInt(0), cursor.getString(1), cursor.getFloat(2), cursor.getString(3), cursor.getInt(4));
                arrayList.add(fruit);
            } while (cursor.moveToNext());
        }

        DBHelper.close();
        recyclerView.addItemDecoration(new VerticalSpaceItemDecoration(VERTICAL_ITEM_SPACE));
        adapter = new ProdutosRecyclerAdapter(arrayList);
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
        ArrayList<Produto> produtos = new ArrayList<>();
        Cursor produto = DBHelper.showProduto(query);

        Toast.makeText(ConsultarProdutosActivity.this, new Integer(produto.getCount()).toString(), Toast.LENGTH_SHORT).show();
        if (produto.getCount() == 0) {
            AlertDialog.Builder caixaAlerta = new AlertDialog.Builder(ConsultarProdutosActivity.this);
            caixaAlerta.setMessage("O EtNome inserido nao consta nos produtos que estao disponiveis.");
            caixaAlerta.setTitle("Aviso!");
            caixaAlerta.setPositiveButton("Adicionar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    startActivity(new Intent(ConsultarProdutosActivity.this, AddProdutosActivity.class));
                }
            });
            caixaAlerta.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    startActivity(new Intent(ConsultarProdutosActivity.this, ConsultarProdutosActivity.class));
                }
            });
            caixaAlerta.setNegativeButton("Produtos indisponiveis", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    startActivity(new Intent(ConsultarProdutosActivity.this, VerProdutosActivity.class));
                }
            });
            caixaAlerta.show();
        } else {
            produto.moveToFirst();
            do {
                Produto fruit = new Produto(produto.getInt(0), produto.getString(1), produto.getFloat(2), produto.getString(3), produto.getInt(4));
                produtos.add(fruit);
            } while (produto.moveToNext());
        }
        DBHelper.close();
        adapter = new ProdutosRecyclerAdapter(produtos);
        recyclerView.setAdapter(adapter);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        hendleSearch(intent);
    }

    public void hendleSearch(Intent intent) {
        if (Intent.ACTION_SEARCH.equalsIgnoreCase(intent.getAction())) {
            String q = intent.getStringExtra(SearchManager.QUERY);
        }
    }
}
