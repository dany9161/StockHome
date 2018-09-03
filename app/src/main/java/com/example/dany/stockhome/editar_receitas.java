package com.example.dany.stockhome;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import layout.ver_ingredientes_edita;

public class editar_receitas extends AppCompatActivity {

    EditText nome,preparacao;
    Button gravar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editar_receitas_activity);

        nome = (EditText) findViewById(R.id.editTextnomeReceitaedita);
        preparacao = (EditText) findViewById(R.id.editText2preparacaoReceitaedita);


        final int id = getIntent().getIntExtra("id", 0);

        ver_ingredientes_edita ver_ingredientes_edita = new ver_ingredientes_edita();
        Bundle bundle = new Bundle();
        bundle.putInt("ID", id);
        ver_ingredientes_edita.setArguments(bundle);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.ingedita, ver_ingredientes_edita);

        fragmentTransaction.commit();

        final DbHelper dbHelper = new DbHelper(getApplicationContext());
        SQLiteDatabase sqLiteDatabase = dbHelper.getReadableDatabase();

        dbHelper.onOpen(sqLiteDatabase);

        if (getIntent().getStringExtra("nome") != null && getIntent().getStringExtra("prep") != null) {
            nome.setText(getIntent().getStringExtra("nome"));
            preparacao.setText(getIntent().getStringExtra("prep"));
        } else {
            nome.setText(dbHelper.getNomeReceita(id));
            preparacao.setText(dbHelper.getPrep(id));
        }

        gravar = (Button) findViewById(R.id.gravarReceitasedita);
        gravar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dbHelper.verIng(id) > 0) {
                    dbHelper.upgradeReceita((getIntent().getIntExtra("id", 0)), (nome.getText().toString()), (preparacao.getText().toString()));
                    Toast.makeText(getBaseContext(), "Receita editada com sucesso!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(editar_receitas.this, consulta_receitas.class));
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(editar_receitas.this);
                    builder.setTitle("Aviso");
                    builder.setMessage("A receita nao tem ingredientes.Deseja inserir?");
                    builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.setNegativeButton("Nao", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dbHelper.upgradeReceita((getIntent().getIntExtra("id", 0)), (nome.getText().toString()), (preparacao.getText().toString()));
                            startActivity(new Intent(getApplicationContext(), consulta_receitas.class));
                        }
                    });
                    builder.show();
                }
            }
        });

        Button elemina = (Button) findViewById(R.id.delreceita);
        elemina.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbHelper.DelIng(id);
                dbHelper.delRec(id);
                startActivity(new Intent(getApplicationContext(), consulta_receitas.class));
            }
        });

        ImageButton mais = (ImageButton) findViewById(R.id.button);
        mais.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),adiciona_ingredientes.class);
                intent.putExtra("id",id);
                intent.putExtra("origem",true);
                startActivity(intent);
            }
        });
    }
}
