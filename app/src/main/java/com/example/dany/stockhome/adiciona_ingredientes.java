package com.example.dany.stockhome;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class adiciona_ingredientes extends AppCompatActivity {


    Button grava;
    EditText nome, quantidade;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adiciona_ingredientes);


        grava = (Button) findViewById(R.id.grav_ing);
        nome = (EditText) findViewById(R.id.editText4);
        quantidade = (EditText) findViewById(R.id.editText3);

        final DbHelper dbHelper = new DbHelper(getBaseContext());

        final boolean or = getIntent().getBooleanExtra("origem", false);


        final int receita = getIntent().getIntExtra("id", 0);

        final String nomerec = getIntent().getStringExtra("nome");
        final String preprec = getIntent().getStringExtra("prep");

        grava.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String l = nome.getText().toString();
                int i = dbHelper.selectIng(l);

                if (quantidade.getText().toString().trim().length()>0 && nome.getText().toString().trim().length()>0) {
                    if (i != 0) {
                        final Float stock = new Float(quantidade.getText().toString());
                        if (stock != 0) {
                            dbHelper.insereIng(i, receita, dbHelper.getUni(i), stock);
                            Toast.makeText(getBaseContext(), "Ingrediente adicionada com sucesso!", Toast.LENGTH_SHORT).show();
                        }
                        if (or) {
                            Intent intent1 = new Intent(getApplicationContext(), editar_receitas.class);
                            intent1.putExtra("id", receita);
                            startActivity(intent1);
                        } else {
                            Intent intent = new Intent(v.getContext(), inserir_receitas.class);
                            if (nomerec != null) {
                                intent.putExtra("nome", nomerec);
                            }
                            if (preprec != null) {
                                intent.putExtra("prep", preprec);
                            }


                            v.getContext().startActivity(intent);
                        }
                    } else {
                        Toast.makeText(getBaseContext(), "Ingrediente nao encontrado", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    AlertDialog.Builder builderSingle = new AlertDialog.Builder(adiciona_ingredientes.this);
                    builderSingle.setTitle("Aviso");
                    builderSingle.setMessage("Por favor insira todos os campos");
                    builderSingle.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builderSingle.show();
                }
            }
        });
    }
}
