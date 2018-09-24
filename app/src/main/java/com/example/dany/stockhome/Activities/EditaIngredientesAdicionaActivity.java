package com.example.dany.stockhome.Activities;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.dany.stockhome.Database.DBHelper;
import com.example.dany.stockhome.R;

public class EditaIngredientesAdicionaActivity extends AppCompatActivity {


    Button grava;
    EditText nome, quantidade;
    int unit, receita, nme,i;
    String produto,l;
    Float quant;
    DBHelper DBHelper;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edita_ingredientes_adiciona);

        unit = getIntent().getIntExtra("Unidade", 0);
        produto = getIntent().getStringExtra("Produto");
        receita = getIntent().getIntExtra("idReceita", 0);
        quant = getIntent().getFloatExtra("stock", 0);
        nme = getIntent().getIntExtra("oldname", 0);

        grava = (Button) findViewById(R.id.grav_ingedita);
        nome = (EditText) findViewById(R.id.editText4edita);
        quantidade = (EditText) findViewById(R.id.editText3edita);

        nome.setText(produto.toString());
        quantidade.setText(String.format("%.2f", (quant)));

        DBHelper = new DBHelper(getBaseContext());
        db = DBHelper.getReadableDatabase();

        grava.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                l = nome.getText().toString();
                i = DBHelper.selectIng(l);
                if ((new Float(quantidade.getText().toString())) != 0) {
                    DBHelper.upgradeING((getIntent().getIntExtra("oldname", 0)), (getIntent().getIntExtra("idReceita", 0)), i, (new Float(quantidade.getText().toString())));
                    Toast.makeText(getBaseContext(), "Ingrediente editado com sucesso!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(v.getContext(), AddReceitasActivity.class);
                    intent.putExtra("id", getIntent().getIntExtra("idReceita", 0));
                    startActivity(intent);
                } else {
                    DBHelper.delIng((getIntent().getIntExtra("oldname", 0)), (getIntent().getIntExtra("idReceita", 0)));
                    Intent intent = new Intent(v.getContext(), AddReceitasActivity.class);
                    intent.putExtra("id", getIntent().getIntExtra("idReceita", 0));
                    startActivity(intent);
                }
            }
        });
    }
}
