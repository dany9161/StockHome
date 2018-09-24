package com.example.dany.stockhome.Activities;

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

import com.example.dany.stockhome.Database.DBHelper;
import com.example.dany.stockhome.Fragments.ver_ingredientesFragment;
import com.example.dany.stockhome.R;

public class AddReceitasActivity extends AppCompatActivity {

    Button gravar;
    ImageButton addIng;
    EditText nome,preparaçao;
    int id;
    ver_ingredientesFragment verIngredientes;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    DBHelper DBHelper;
    SQLiteDatabase sqLiteDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_receitas);

        nome = (EditText) findViewById(R.id.editTextnomeReceita);
        preparaçao = (EditText) findViewById(R.id.editText2preparacaoReceita);
        gravar = (Button) findViewById(R.id.gravarReceitas);
        addIng = (ImageButton) findViewById(R.id.button);


        nome.setText(getIntent().getStringExtra("EtNome"));
        preparaçao.setText(getIntent().getStringExtra("prep"));

        verIngredientes = new ver_ingredientesFragment();
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.ing,verIngredientes);
        fragmentTransaction.commit();

        DBHelper = new DBHelper(getApplicationContext());
        sqLiteDatabase = DBHelper.getReadableDatabase();

        DBHelper.onOpen(sqLiteDatabase);
        id = DBHelper.getIdReceita()+1;

        addIng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),AddIngActivity.class);
                intent.putExtra("id",id);
                if (nome.getText().toString() != null){
                    intent.putExtra("EtNome",nome.getText().toString());
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
                    AlertDialog.Builder builder =new AlertDialog.Builder(AddReceitasActivity.this);
                    builder.setTitle("Aviso");
                    builder.setMessage("Por favor insira um EtNome para a receita.");
                   builder.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                       @Override
                       public void onClick(DialogInterface dialog, int which) {
                           dialog.dismiss();
                       }
                   });
                    builder.show();
                }
                if (DBHelper.verIng(id)>0) {
                    DBHelper.insereReceita(nome.getText().toString(), preparaçao.getText().toString());
                    Toast.makeText(getBaseContext(), "Receita adcionada com sucesso!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(),ConsultaReceitasActivity.class));
                }else{
                    AlertDialog.Builder builder =new AlertDialog.Builder(AddReceitasActivity.this);
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
                            DBHelper.insereReceita(nome.getText().toString(), preparaçao.getText().toString());
                            Toast.makeText(getBaseContext(), "Receita adicionada com sucesso!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(),ConsultaReceitasActivity.class));
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
            DBHelper DBHelper = new DBHelper(getApplicationContext());
            DBHelper.DelIng(id);
            startActivity(new Intent(AddReceitasActivity.this,ConsultaReceitasActivity.class));
        }
        return super.onKeyDown(keyCode, event);
    }
}
