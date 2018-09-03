package com.example.dany.stockhome;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class editar_produtos extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    DbHelper dfHelper;
    Spinner spinner;
    ImageButton mais,menos;

    ArrayList<String> units = new ArrayList<String>();
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editar_produtos_activity);


        dfHelper = new DbHelper(this, null, null, 1);

        final EditText nome = (EditText) findViewById(R.id.et1);
        Button mostrar = (Button) findViewById(R.id.bt1);
        final EditText quantidade = (EditText) findViewById(R.id.et2);

        spinner = (Spinner) findViewById(R.id.spinner);
        menos = (ImageButton) findViewById(R.id.button2);
        mais = (ImageButton) findViewById(R.id.button);

        menos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (new Float(quantidade.getText().toString()) > 0) {
                    float s= Float.parseFloat( quantidade.getText().toString());
                    s--;
                    String e;
                    quantidade.setText( Float.toString(s));
                }
            }
        });

        mais.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                float s = Float.parseFloat(quantidade.getText().toString());
                s++;
                String e;
                quantidade.setText( Float.toString(s));
            }
        });

        final int id = getIntent().getIntExtra("id", 0);
        final String fruta = getIntent().getStringExtra("nome");
        final Float fgh = getIntent().getFloatExtra("stock", 0);
        final int unit = getIntent().getIntExtra("unit", 0);


        nome.setText(fruta);
        quantidade.setText(String.format("%.2f",(fgh)));

        DbHelper dfHelper = new DbHelper(getBaseContext());


        units.clear();
        Cursor cursor = dfHelper.getUnidades();
        while (cursor.moveToNext()) {
            String name = cursor.getString(1);
            units.add(name);
        }

        adapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, units);

        spinner.setAdapter(adapter);
        spinner.setSelection(unit - 1);
        spinner.setOnItemSelectedListener(this);
        final DbHelper dbHelper = new DbHelper(this);


        mostrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String name = nome.getText().toString();
                final float s = Float.parseFloat(quantidade.getText().toString());
                boolean p = fruta.equals(name);
                //muda a unidade na receita
                if (unit != (spinner.getSelectedItemPosition()+1)){
                    dbHelper.MudaUni((spinner.getSelectedItemPosition()+1),id);
                }
                if (p) {
                    dbHelper.upgradeProduto(id, name, s, (spinner.getSelectedItemPosition()) + 1);
                    Toast.makeText(getBaseContext(), "Produto atualizada com sucesso!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(editar_produtos.this, consultar_produtos.class));
                }
                if (!p) {
                    if (dbHelper.verificaProduto(name)) {
                        Toast.makeText(editar_produtos.this, "verifica produto", Toast.LENGTH_SHORT).show();
                        AlertDialog.Builder caixa = new AlertDialog.Builder(editar_produtos.this);
                        caixa.setTitle("Aviso");
                        caixa.setMessage("Foi detetado que tem um produto com o mesmo nome. Deseja juntar as quantidades ou alterar o nome?");
                        caixa.setNegativeButton("Alterar o nome", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        caixa.setPositiveButton("Juntar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dbHelper.juntaStock(name, s);
                                dbHelper.SubsPro(fruta, name);
                                dbHelper.DelProd(id);
                                Toast.makeText(editar_produtos.this, "Juntamento com sucesso", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(editar_produtos.this, consultar_produtos.class));
                            }
                        });
                        caixa.show();
                    }
                    dbHelper.upgradeProduto(id, name, s, (spinner.getSelectedItemPosition()) + 1);
                    Toast.makeText(getBaseContext(), "Produto atualizada com sucesso!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(editar_produtos.this, consultar_produtos.class));
                }
            }
        });


         Button elemina = (Button) findViewById(R.id.btDelPro);
        elemina.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbHelper.DelProd(id);
                Toast.makeText(editar_produtos.this, "Produto apagado com sucesso", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(editar_produtos.this,consultar_produtos.class));
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public String stock (float i){
        DecimalFormat df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.CEILING);
        Float d = i;
        String r = df.format(d);
        return r;
    }
}