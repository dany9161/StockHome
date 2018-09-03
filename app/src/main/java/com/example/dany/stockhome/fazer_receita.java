package com.example.dany.stockhome;

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

import java.util.ArrayList;

import layout.ver_ing_fazer;

public class fazer_receita extends AppCompatActivity {

    TextView nome, prep;
    Button fazer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fazer_receita_activity);

        nome = (TextView) findViewById(R.id.editTextnomeReceitaFazer);
        prep = (TextView) findViewById(R.id.editText2preparacaoReceitaFazer);


        final int id = getIntent().getIntExtra("id", 0);

        ver_ing_fazer ver_ingredientes_edita = new ver_ing_fazer();
        Bundle bundle = new Bundle();
        bundle.putInt("ID", id);
        ver_ingredientes_edita.setArguments(bundle);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.ingFazer, ver_ingredientes_edita);

        fragmentTransaction.commit();

        final DbHelper dbHelper = new DbHelper(getApplicationContext());
        SQLiteDatabase sqLiteDatabase = dbHelper.getReadableDatabase();

        dbHelper.onOpen(sqLiteDatabase);

        if (getIntent().getStringExtra("nome") != null && getIntent().getStringExtra("prep") != null) {
            nome.setText(getIntent().getStringExtra("nome"));
            prep.setText(getIntent().getStringExtra("prep"));
        } else {
            nome.setText(dbHelper.getNomeReceita(id));
            prep.setText(dbHelper.getPrep(id));
        }

        if (dbHelper.getPrep(id).isEmpty() && getIntent().getStringExtra("prep").isEmpty()) {
            TextView tx = (TextView) findViewById(R.id.textView3Fazer);
            tx.setText("Esta receita nao tem preparação.");
        }

        fazer = (Button) findViewById(R.id.gravarReceitasFazer);
        fazer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> p = new ArrayList<>();
                p = dbHelper.CHECKatualizaINGs(id, getBaseContext());
                if(p.size() == 0){
                    dbHelper.atualizaINGs(id, getBaseContext());
                    Toast.makeText(fazer_receita.this, "Bom Apetite!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                }else{
                    final AlertDialog.Builder builderSingle = new AlertDialog.Builder(fazer_receita.this);

                    builderSingle.setTitle("Nao tem os seguintes ingredientes, deseja comprar?");
                   String [] ing = new String[p.size()];
                    for (int i=0; i<p.size();i++) {
                        ing[i]=p.get(i);
                    }
                    builderSingle.setNegativeButton("Não",new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    builderSingle.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startActivity(new Intent(getApplicationContext(),comprarProdutos.class));
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

        Cursor cursor = dbHelper.getIngredientes(id);
        if (cursor.getCount()==0){
            TextView tx = (TextView) findViewById(R.id.textView5fazer);
            tx.setText("Esta receita nao tem ingredientes");
            fazer.setVisibility(View.INVISIBLE);
        }
    }
}
