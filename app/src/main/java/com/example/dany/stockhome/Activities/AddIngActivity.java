package com.example.dany.stockhome.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.dany.stockhome.Database.DBHelper;
import com.example.dany.stockhome.R;

/**
 * Activity para inserir ingredientes numa receita
 * Vai trabalhar de maneira diferente se vier do AddREceitasActivity ou do EditarReceitasActivity
 */
public class AddIngActivity extends AppCompatActivity {

    Button BtnGrava;
    EditText EtNome, EtQuant;
    DBHelper DBHelper;
    boolean origem;
    String nome, nomerec, preprec;
    int id, receita;
    Float stock;
    Intent intent1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_ing);

        BtnGrava = (Button) findViewById(R.id.grav_ing);
        EtNome = (EditText) findViewById(R.id.editText4);
        EtQuant = (EditText) findViewById(R.id.editText3);

        DBHelper = new DBHelper(getBaseContext());

        origem = getIntent().getBooleanExtra("fromEditar", false);

        receita = getIntent().getIntExtra("id", 0);

        nomerec = getIntent().getStringExtra("EtNome");
        preprec = getIntent().getStringExtra("prep");

        BtnGrava.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nome = EtNome.getText().toString();
                id = DBHelper.selectIng(nome);

                if (EtQuant.getText().toString().trim().length() > 0 && EtNome.getText().toString().trim().length() > 0) {
                    if (id != 0) {
                        stock = new Float(EtQuant.getText().toString());
                        if (stock != 0) {
                            DBHelper.insereIng(id, receita, DBHelper.getUni(id), stock);
                            Toast.makeText(getBaseContext(), "Ingrediente adicionada com sucesso!", Toast.LENGTH_SHORT).show();
                        }
                        if (origem) {
                            intent1 = new Intent(getApplicationContext(), EditarReceitasActivity.class);
                            intent1.putExtra("id", receita);
                            startActivity(intent1);
                        } else {
                            Intent intent = new Intent(v.getContext(), AddReceitasActivity.class);
                            if (nomerec != null) {
                                intent.putExtra("EtNome", nomerec);
                            }
                            if (preprec != null) {
                                intent.putExtra("prep", preprec);
                            }
                            v.getContext().startActivity(intent);
                        }
                    } else {
                        Toast.makeText(getBaseContext(), "Ingrediente nao encontrado", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    AlertDialog.Builder builderSingle = new AlertDialog.Builder(AddIngActivity.this);
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
