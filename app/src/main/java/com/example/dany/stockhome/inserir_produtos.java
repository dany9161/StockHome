package com.example.dany.stockhome;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

public class inserir_produtos extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    DbHelper dfHelper;
    Spinner spinner;

    ArrayList<String> units = new ArrayList<String>();
    ArrayAdapter<String> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inserir_produtos_activity);



        dfHelper = new DbHelper(this,null,null,1);
        final EditText nome = (EditText) findViewById(R.id.et1);
        final EditText stock = (EditText) findViewById(R.id.et2);
        Button inserir = (Button) findViewById(R.id.bt1);
        spinner = (Spinner) findViewById(R.id.spinner);



        DbHelper dbHelper = new DbHelper(getBaseContext());
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        units.clear();
        Cursor cursor = dbHelper.getUnidades();
        while (cursor.moveToNext())
        {
            String name = cursor.getString(1);
            units.add(name);
        }

        adapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item,units);

        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);









                inserir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                if (stock.getText().toString().trim().length()>0 && nome .getText().toString().trim().length()>0) {
                if(dfHelper.verificaProduto(nome.getText().toString())){
                    AlertDialog.Builder caixaAlerta = new AlertDialog.Builder(inserir_produtos.this);
                    caixaAlerta.setMessage("O produto que tentou inserir ja existe");
                    caixaAlerta.setTitle("Alerta!");
                    caixaAlerta.setNeutralButton("OK",null);
                    caixaAlerta.show();
                    nome.setText("");
                }else{
                dfHelper.adicionaProduto(nome.getText().toString(), new Float(stock.getText().toString()), (spinner.getSelectedItemPosition())+1);
                Toast.makeText(getBaseContext(),"Produto adcionada com sucesso!",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(inserir_produtos.this, consultar_produtos.class));
                }
            }else{
                    AlertDialog.Builder builderSingle = new AlertDialog.Builder(inserir_produtos.this);
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



    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
    }
    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }
}




