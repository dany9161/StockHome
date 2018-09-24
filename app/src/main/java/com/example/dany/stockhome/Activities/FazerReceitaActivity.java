package com.example.dany.stockhome.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dany.stockhome.Database.DBHelper;
import com.example.dany.stockhome.Fragments.ver_ing_fazerFragment;
import com.example.dany.stockhome.R;

import java.util.ArrayList;

public class FazerReceitaActivity extends AppCompatActivity {

    TextView nome, prep;
    Button fazer;
    Bundle bundle;
    ver_ing_fazerFragment ver_ingredientes_edita;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    int id;
    DBHelper DBHelper;
    SQLiteDatabase sqLiteDatabase;
    ArrayList<String> p;
    String[] ing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fazer_receita);

        nome = (TextView) findViewById(R.id.editTextnomeReceitaFazer);
        prep = (TextView) findViewById(R.id.editText2preparacaoReceitaFazer);

        id = getIntent().getIntExtra("id", 0);

        ver_ingredientes_edita = new ver_ing_fazerFragment();
        bundle = new Bundle();
        bundle.putInt("ID", id);
        ver_ingredientes_edita.setArguments(bundle);
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.ingFazer, ver_ingredientes_edita);
        fragmentTransaction.commit();

        DBHelper = new DBHelper(getApplicationContext());
        sqLiteDatabase = DBHelper.getReadableDatabase();
        DBHelper.onOpen(sqLiteDatabase);

        if (getIntent().getStringExtra("EtNome") != null && getIntent().getStringExtra("prep") != null) {
            nome.setText(getIntent().getStringExtra("EtNome"));
            prep.setText(getIntent().getStringExtra("prep"));
        } else {
            nome.setText(DBHelper.getNomeReceita(id));
            prep.setText(DBHelper.getPrep(id));
        }

        if (DBHelper.getPrep(id).isEmpty() && getIntent().getStringExtra("prep").isEmpty()) {
            TextView tx = (TextView) findViewById(R.id.textView3Fazer);
            tx.setText("Esta receita nao tem preparação.");
        }

        fazer = (Button) findViewById(R.id.gravarReceitasFazer);
        fazer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                p = new ArrayList<>();
                p = DBHelper.CHECKatualizaINGs(id, getBaseContext());
                if (p.size() == 0) {
                    DBHelper.atualizaINGs(id, getBaseContext());
                    Toast.makeText(FazerReceitaActivity.this, "Bom Apetite!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                } else {
                    final AlertDialog.Builder builderSingle = new AlertDialog.Builder(FazerReceitaActivity.this);
                    builderSingle.setTitle("Nao tem os seguintes ingredientes, deseja comprar?");
                    ing = new String[p.size()];
                    for (int i = 0; i < p.size(); i++) {
                        ing[i] = p.get(i);
                    }
                    builderSingle.setNegativeButton("Não", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builderSingle.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startActivity(new Intent(getApplicationContext(), ComprarProdutosActivity.class));
                        }
                    });
                    builderSingle.setItems(ing, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    builderSingle.show();
                }
            }
        });

        Cursor cursor = DBHelper.getIngredientes(id);
        if (cursor.getCount() == 0) {
            TextView tx = (TextView) findViewById(R.id.textView5fazer);
            tx.setText("Esta receita nao tem ingredientes");
            fazer.setVisibility(View.INVISIBLE);
        }
    }
}
