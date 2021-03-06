package com.example.dany.stockhome;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class edita_ingredientes extends AppCompatActivity {


    ArrayAdapter<String> adapter;
    Button grava;
    EditText nome,quantidade;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edita_ingredientes_activity);


        final String produto = getIntent().getStringExtra("Produto");
        final Float quant  = getIntent().getFloatExtra("stock",0);





        grava = (Button) findViewById(R.id.grav_ingedita);
        nome = (EditText) findViewById(R.id.editText4edita);
        quantidade = (EditText) findViewById(R.id.editText3edita);

        nome.setText(produto.toString());
        quantidade.setText(String.format("%.2f",(quant)));

        final DbHelper dbHelper = new DbHelper(getBaseContext());
        final SQLiteDatabase db = dbHelper.getReadableDatabase();









        grava.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String l= nome.getText().toString();
                int i = dbHelper.selectIng(l);
                if ((new Float(quantidade.getText().toString()))> 0) {
                    dbHelper.upgradeING((getIntent().getIntExtra("oldname", 0)), (getIntent().getIntExtra("idReceita", 0)), i, (new Float(quantidade.getText().toString())));
                    Toast.makeText(getBaseContext(),"Ingrediente editado com sucesso!",Toast.LENGTH_SHORT).show();
                }else{
                dbHelper.delIng((getIntent().getIntExtra("oldname", 0)),(getIntent().getIntExtra("idReceita", 0)));
                    Toast.makeText(getBaseContext(),"Ingrediente apagado com sucesso!",Toast.LENGTH_SHORT).show();
                }

                Intent intent= new Intent(v.getContext(),editar_receitas.class);
                intent.putExtra("id",getIntent().getIntExtra("idReceita",0));
                startActivity(intent);
            }
        });

    }
}
