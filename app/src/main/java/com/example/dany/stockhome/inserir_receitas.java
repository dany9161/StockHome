package com.example.dany.stockhome;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import layout.ver_ingredientes;

public class inserir_receitas extends AppCompatActivity {

    Button gravar;
    ImageButton addIng;
    EditText nome,preparaçao;
    int id;

DbHelper dbHelper = new DbHelper(getBaseContext());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inserir_receitas);

        nome = (EditText) findViewById(R.id.editTextnomeReceita);
        preparaçao = (EditText) findViewById(R.id.editText2preparacaoReceita);


        nome.setText(getIntent().getStringExtra("nome"));
        preparaçao.setText(getIntent().getStringExtra("prep"));


        ver_ingredientes verIngredientes = new ver_ingredientes();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.ing,verIngredientes);
        fragmentTransaction.commit();


        final DbHelper dbHelper = new DbHelper(getApplicationContext());
        SQLiteDatabase sqLiteDatabase = dbHelper.getReadableDatabase();


        dbHelper.onOpen(sqLiteDatabase);
       id = dbHelper.getIdReceita()+1;



        gravar = (Button) findViewById(R.id.gravarReceitas);

        addIng = (ImageButton) findViewById(R.id.button);



        addIng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(v.getContext(),adiciona_ingredientes.class);
                intent.putExtra("id",id);
                if (nome.getText().toString() != null){
                    intent.putExtra("nome",nome.getText().toString());
                }
                if (preparaçao.getText().toString() != null){
                    intent.putExtra("prep",preparaçao.getText().toString());
                }
                v.getContext().startActivity(intent);
            }
        });

        gravar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nome.getText().toString().isEmpty()){
                    AlertDialog.Builder builder =new AlertDialog.Builder(inserir_receitas.this);
                    builder.setTitle("Aviso");
                    builder.setMessage("Por favor insira um nome para a receita.");
                   builder.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                       @Override
                       public void onClick(DialogInterface dialog, int which) {
                           dialog.dismiss();
                       }
                   });
                    builder.show();
                }
                if (dbHelper.verIng(id)>0) {
                    dbHelper.insereReceita(nome.getText().toString(), preparaçao.getText().toString());
                    Toast.makeText(getBaseContext(), "Receita adcionada com sucesso!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(),consulta_receitas.class));
                }else{
                    AlertDialog.Builder builder =new AlertDialog.Builder(inserir_receitas.this);
                    builder.setTitle("Aviso");
                    builder.setMessage("A receita nao tem ingredientes.Deseja inserir ingredientes?");
                    builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.setNegativeButton("Nao", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dbHelper.insereReceita(nome.getText().toString(), preparaçao.getText().toString());
                            Toast.makeText(getBaseContext(), "Receita adicionada com sucesso!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(),consulta_receitas.class));
                        }
                    });
                    builder.show();
                }

            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            DbHelper dbHelper = new DbHelper(getApplicationContext());
            dbHelper.DelIng(id);
            startActivity(new Intent(inserir_receitas.this,consulta_receitas.class));
        }
        return super.onKeyDown(keyCode, event);
    }
}
