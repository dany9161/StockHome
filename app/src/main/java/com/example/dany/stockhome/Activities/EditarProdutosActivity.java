package com.example.dany.stockhome.Activities;

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

import com.example.dany.stockhome.Database.DBHelper;
import com.example.dany.stockhome.R;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class EditarProdutosActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    DBHelper dfHelper;
    Spinner spinner;
    ImageButton mais, menos;
    ArrayList<String> units = new ArrayList<String>();
    ArrayAdapter<String> adapter;
    EditText nome, quantidade;
    Button mostrar, elemina;
    int id, unit;
    String fruta;
    Float fgh;
    Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_produtos);

        dfHelper = new DBHelper(this, null, null, 1);

        nome = (EditText) findViewById(R.id.et1);
        quantidade = (EditText) findViewById(R.id.et2);
        mostrar = (Button) findViewById(R.id.bt1);

        spinner = (Spinner) findViewById(R.id.spinner);
        menos = (ImageButton) findViewById(R.id.button2);
        mais = (ImageButton) findViewById(R.id.button);

        menos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (new Float(quantidade.getText().toString()) > 0) {
                    float s = Float.parseFloat(quantidade.getText().toString());
                    s--;
                    quantidade.setText(Float.toString(s));
                }
            }
        });

        mais.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float s = Float.parseFloat(quantidade.getText().toString());
                s++;
                quantidade.setText(Float.toString(s));
            }
        });

        id = getIntent().getIntExtra("id", 0);
        fruta = getIntent().getStringExtra("EtNome");
        fgh = getIntent().getFloatExtra("stock", 0);
        unit = getIntent().getIntExtra("unit", 0);

        nome.setText(fruta);
        quantidade.setText(String.format("%.2f", (fgh)));

        dfHelper = new DBHelper(getBaseContext());

        units.clear();
        cursor = dfHelper.getUnidades();
        while (cursor.moveToNext()) {
            String name = cursor.getString(1);
            units.add(name);
        }

        adapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, units);

        spinner.setAdapter(adapter);
        spinner.setSelection(unit - 1);
        spinner.setOnItemSelectedListener(this);
        final DBHelper DBHelper = new DBHelper(this);

        mostrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String name = nome.getText().toString();
                final float s = Float.parseFloat(quantidade.getText().toString());
                boolean p = fruta.equals(name);
                //muda a unidade na receita
                if (unit != (spinner.getSelectedItemPosition() + 1)) {
                    DBHelper.MudaUni((spinner.getSelectedItemPosition() + 1), id);
                }
                if (p) {
                    DBHelper.upgradeProduto(id, name, s, (spinner.getSelectedItemPosition()) + 1);
                    Toast.makeText(getBaseContext(), "Produto atualizada com sucesso!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(EditarProdutosActivity.this, ConsultarProdutosActivity.class));
                }
                if (!p) {
                    if (DBHelper.verificaProduto(name)) {
                        Toast.makeText(EditarProdutosActivity.this, "verifica produto", Toast.LENGTH_SHORT).show();
                        AlertDialog.Builder caixa = new AlertDialog.Builder(EditarProdutosActivity.this);
                        caixa.setTitle("Aviso");
                        caixa.setMessage("Foi detetado que tem um produto com o mesmo EtNome. Deseja juntar as quantidades ou alterar o EtNome?");
                        caixa.setNegativeButton("Alterar o EtNome", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        caixa.setPositiveButton("Juntar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                DBHelper.juntaStock(name, s);
                                DBHelper.SubsPro(fruta, name);
                                DBHelper.DelProd(id);
                                Toast.makeText(EditarProdutosActivity.this, "Juntamento com sucesso", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(EditarProdutosActivity.this, ConsultarProdutosActivity.class));
                            }
                        });
                        caixa.show();
                    }
                    DBHelper.upgradeProduto(id, name, s, (spinner.getSelectedItemPosition()) + 1);
                    Toast.makeText(getBaseContext(), "Produto atualizada com sucesso!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(EditarProdutosActivity.this, ConsultarProdutosActivity.class));
                }
            }
        });

        elemina = (Button) findViewById(R.id.btDelPro);
        elemina.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DBHelper.DelProd(id);
                Toast.makeText(EditarProdutosActivity.this, "Produto apagado com sucesso", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(EditarProdutosActivity.this, ConsultarProdutosActivity.class));
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    public String stock(float i) {
        DecimalFormat df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.CEILING);
        Float d = i;
        String r = df.format(d);
        return r;
    }
}