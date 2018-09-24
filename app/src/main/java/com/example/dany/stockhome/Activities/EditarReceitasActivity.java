package com.example.dany.stockhome.Activities;

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

import com.example.dany.stockhome.Database.DBHelper;
import com.example.dany.stockhome.Fragments.ver_ingredientes_editaFragment;
import com.example.dany.stockhome.R;

public class EditarReceitasActivity extends AppCompatActivity {

    EditText nome, preparacao;
    Button gravar, elemina;
    int id;
    ver_ingredientes_editaFragment ver_ingredientes_editaFragment;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    Bundle bundle;
    DBHelper DBHelper;
    SQLiteDatabase sqLiteDatabase;
    ImageButton mais;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_receitas);

        nome = (EditText) findViewById(R.id.editTextnomeReceitaedita);
        preparacao = (EditText) findViewById(R.id.editText2preparacaoReceitaedita);

        id = getIntent().getIntExtra("id", 0);

        bundle = new Bundle();
        bundle.putInt("ID", id);
        ver_ingredientes_editaFragment = new ver_ingredientes_editaFragment();
        ver_ingredientes_editaFragment.setArguments(bundle);
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.ingedita, ver_ingredientes_editaFragment);
        fragmentTransaction.commit();

        DBHelper = new DBHelper(getApplicationContext());
        sqLiteDatabase = DBHelper.getReadableDatabase();
        DBHelper.onOpen(sqLiteDatabase);

        if (getIntent().getStringExtra("EtNome") != null && getIntent().getStringExtra("prep") != null) {
            nome.setText(getIntent().getStringExtra("EtNome"));
            preparacao.setText(getIntent().getStringExtra("prep"));
        } else {
            nome.setText(DBHelper.getNomeReceita(id));
            preparacao.setText(DBHelper.getPrep(id));
        }

        gravar = (Button) findViewById(R.id.gravarReceitasedita);
        gravar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (DBHelper.verIng(id) > 0) {
                    DBHelper.upgradeReceita((getIntent().getIntExtra("id", 0)), (nome.getText().toString()), (preparacao.getText().toString()));
                    Toast.makeText(getBaseContext(), "Receita editada com sucesso!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(EditarReceitasActivity.this, ConsultaReceitasActivity.class));
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(EditarReceitasActivity.this);
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
                            DBHelper.upgradeReceita((getIntent().getIntExtra("id", 0)), (nome.getText().toString()), (preparacao.getText().toString()));
                            startActivity(new Intent(getApplicationContext(), ConsultaReceitasActivity.class));
                        }
                    });
                    builder.show();
                }
            }
        });

        elemina = (Button) findViewById(R.id.delreceita);
        elemina.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DBHelper.DelIng(id);
                DBHelper.delRec(id);
                startActivity(new Intent(getApplicationContext(), ConsultaReceitasActivity.class));
            }
        });

        mais = (ImageButton) findViewById(R.id.button);
        mais.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AddIngActivity.class);
                intent.putExtra("id", id);
                intent.putExtra("fromEditar", true);
                startActivity(intent);
            }
        });
    }
}
