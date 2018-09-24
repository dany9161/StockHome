package com.example.dany.stockhome.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by dany on 12/04/2016.
 */
public class DBHelper extends SQLiteOpenHelper {
    //db
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "stockHomeDB.db";
    //TABELA RECEITAS
    public String TABLE_RECEITAS = "receitas";
    public String RECEITAS_COLUMN_ID = "_idReceitas";
    public String RECEITAS_COLUMN_NAME = "Nome";
    public String RECEITAS_COLUMN_PREPARACAO = "Preparacao";
    //TABELA INGREDIENTES
    public String TABLE_INGREDIENTES = "ingredientes";
    public String COLUMN_ID_RECEITAS = "_idReceitas";
    public String COLUMN_ID_PRODUTOS = "_idProduto";
    public String INGREDIENTES_COLUMN_QUANTIDADE = "Quantidade";
    public String COLIMN_ID_UNIDADE = "_id";
    //TABELA PRODUTOS
    public String TABLE_PRODUTOS = "produtos";
    public String PRODUTOS_COLUMN_ID = "_idProdutos";
    public String PRODUTOS_COLUMN_NAME = "Nome";
    public String PRODUTOS_COLUMN_QUANTIDADE = "Stock";
    public String PRODUTOS_COLUMN_UNIDADE = "_idUnidade";
    //TABELA UNIDADES
    public String TABLE_UNIDADES = "unidades";
    public String UNIDADES_COLUMN_ID = "_id";
    public String UNIDADES_COLUMN_UNIDADE = "unidade";
    //UNIDADES
    public String UNIDADES_LITROS = "Lt";
    public String UNIDADES_GRAMAS = "Gr";
    public String UNIDADES_PEÇAS = "Uni.";

    //construtor
    //
    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String criaReceitas = "CREATE TABLE IF NOT EXISTS '" + TABLE_RECEITAS + "' ( '" + RECEITAS_COLUMN_ID + "' INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "'" + RECEITAS_COLUMN_NAME + "' TEXT," +
                " '" + RECEITAS_COLUMN_PREPARACAO + "' TEXT );";

        String criaIngredientes = "CREATE TABLE IF NOT EXISTS '" + TABLE_INGREDIENTES + "' ( '" + COLUMN_ID_RECEITAS + "' INTEGER, " +
                "'" + COLUMN_ID_PRODUTOS + "' INTEGER, " +
                "'" + COLIMN_ID_UNIDADE + "' INTEGER, " +
                " '" + INGREDIENTES_COLUMN_QUANTIDADE + "' FLOAT);";


        String criaProdutos = "CREATE TABLE IF NOT EXISTS '" + TABLE_PRODUTOS + "' ( '" + PRODUTOS_COLUMN_ID + "' INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "'" + PRODUTOS_COLUMN_NAME + "' TEXT, " +
                "'" + PRODUTOS_COLUMN_QUANTIDADE + "' FOAT, " +
                "'" + PRODUTOS_COLUMN_UNIDADE + "' INTEGER);";

        String criaUnidades = "CREATE TABLE IF NOT EXISTS '" + TABLE_UNIDADES + "' ( '" + UNIDADES_COLUMN_ID + "' INTEGER PRIMARY KEY AUTOINCREMENT , " +
                "'" + UNIDADES_COLUMN_UNIDADE + "' TEXT );";

        db.execSQL(criaReceitas);
        db.execSQL(criaIngredientes);
        db.execSQL(criaProdutos);
        db.execSQL(criaUnidades);

        insereUnidades(db);
    }

    public void insereUnidades(SQLiteDatabase db) {
        String verifica = "SELECT * FROM " + TABLE_UNIDADES + "";
        Cursor cursor = db.rawQuery(verifica, null);
        if (cursor.getCount() == 0) {
            db.execSQL("INSERT INTO " + TABLE_UNIDADES + " ( " + UNIDADES_COLUMN_UNIDADE + " ) VALUES ( '" + UNIDADES_LITROS + "' );");
            db.execSQL("INSERT INTO " + TABLE_UNIDADES + " ( " + UNIDADES_COLUMN_UNIDADE + " ) VALUES ( '" + UNIDADES_PEÇAS + "' );");
            db.execSQL("INSERT INTO " + TABLE_UNIDADES + " ( " + UNIDADES_COLUMN_UNIDADE + " ) VALUES ( '" + UNIDADES_GRAMAS + "' );");
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECEITAS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_INGREDIENTES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUTOS);
        onCreate(db);
    }


    //-----------------------------------------
    //-----------------------------------------
    //-----------------------------------------
    //-----------------------------------------

    //adicionar PRODUTO
    public void adicionaProduto(String nome, Float stock, int unit) {
        SQLiteDatabase db = getWritableDatabase();
        String query = "INSERT INTO " + TABLE_PRODUTOS + " ( " + PRODUTOS_COLUMN_NAME + " , " + PRODUTOS_COLUMN_QUANTIDADE + " , " + PRODUTOS_COLUMN_UNIDADE + "  ) VALUES ( '" + nome + "' , " + stock + " , " + unit + " );";
        db.execSQL(query);
    }

    //verifica produto
    public boolean verificaProduto(String nome) {
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT " + PRODUTOS_COLUMN_NAME + " FROM " + TABLE_PRODUTOS + " WHERE " + PRODUTOS_COLUMN_NAME + " = '" + nome + "' ;";
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.getCount() == 0) {
            return false;
        } else {
            return true;
        }
    }

    public Cursor getProdutos() {
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT " + PRODUTOS_COLUMN_ID + " , " + PRODUTOS_COLUMN_NAME + " , " + PRODUTOS_COLUMN_QUANTIDADE + " , " + UNIDADES_COLUMN_UNIDADE + " , " + PRODUTOS_COLUMN_UNIDADE + " FROM " + TABLE_PRODUTOS + " , " + TABLE_UNIDADES + " WHERE " + PRODUTOS_COLUMN_UNIDADE + " = " + UNIDADES_COLUMN_ID + " AND " + PRODUTOS_COLUMN_QUANTIDADE + " > 0 ORDER BY " + PRODUTOS_COLUMN_NAME + ";";
        Cursor cursor = db.rawQuery(query, null);
        return cursor;
    }

    public Cursor getProdutosN() {
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT " + PRODUTOS_COLUMN_ID + " , " + PRODUTOS_COLUMN_NAME + " , " + PRODUTOS_COLUMN_QUANTIDADE + " , " + UNIDADES_COLUMN_UNIDADE + " , " + PRODUTOS_COLUMN_UNIDADE + " FROM " + TABLE_PRODUTOS + " , " + TABLE_UNIDADES + " WHERE " + PRODUTOS_COLUMN_UNIDADE + " = " + UNIDADES_COLUMN_ID + " AND " + PRODUTOS_COLUMN_QUANTIDADE + " <= 0 ORDER BY " + PRODUTOS_COLUMN_NAME + ";";
        Cursor cursor = db.rawQuery(query, null);
        return cursor;
    }

    public void upgradeProduto(int id, String name, Float quantidade, int unit) {
        SQLiteDatabase db = getWritableDatabase();

        String query = "UPDATE " + TABLE_PRODUTOS + " SET " + PRODUTOS_COLUMN_NAME + " = '" + name + "' , " + PRODUTOS_COLUMN_QUANTIDADE + " = " + quantidade + " , " + PRODUTOS_COLUMN_UNIDADE + " = " + unit + " WHERE " + PRODUTOS_COLUMN_ID + " = " + id + ";";
        db.execSQL(query);
    }

    public Cursor getUnidades() {
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_UNIDADES;
        Cursor cursor = db.rawQuery(query, null);
        return cursor;
    }

    public Cursor getIngredientes(int id) {
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT " + COLUMN_ID_RECEITAS + "," + INGREDIENTES_COLUMN_QUANTIDADE + " , " + PRODUTOS_COLUMN_NAME + " , " + TABLE_UNIDADES + "." + UNIDADES_COLUMN_UNIDADE + ", " + TABLE_UNIDADES + "." + COLIMN_ID_UNIDADE + " , " + COLUMN_ID_PRODUTOS + "  FROM " + TABLE_INGREDIENTES + "," + TABLE_PRODUTOS + "," + TABLE_UNIDADES + " WHERE " + COLUMN_ID_RECEITAS + " = " + id + " AND " + TABLE_INGREDIENTES + "." + COLUMN_ID_PRODUTOS + " = " + TABLE_PRODUTOS + "." + PRODUTOS_COLUMN_ID + " AND " + TABLE_INGREDIENTES + "." + COLIMN_ID_UNIDADE + " = " + TABLE_UNIDADES + "." + UNIDADES_COLUMN_ID + " ;";
        Cursor cursor = db.rawQuery(query, null);

        return cursor;
    }

    public int getIdReceita() {
        int i;
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT MAX(" + RECEITAS_COLUMN_ID + ") FROM " + TABLE_RECEITAS + " ;";
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        do {
            i = cursor.getInt(0);
        } while (cursor.moveToNext());

        return i;
    }

    /**
     * Dado o nome do produto a função faz uma pesquisa na tabela de produtos para saber qual é o ID
     *
     * @param name nome do produto para ser pesquisado o ID
     * @return ID do produto procurado
     */
    public int selectIng(String name) {
        int i = 0;
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT " + PRODUTOS_COLUMN_ID + " FROM " + TABLE_PRODUTOS + " WHERE " + PRODUTOS_COLUMN_NAME + " LIKE '" + name + "' ";
        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();
        if (c.getCount() != 0) {
            i = c.getInt(0);
            return i;
        }
        return i;
    }

    public void insereIng(int idProduto, int idReceita, int unidade, Float quantidade) {
        SQLiteDatabase db = getWritableDatabase();
        String query = "INSERT INTO " + TABLE_INGREDIENTES + " ( " + COLUMN_ID_RECEITAS + " , " + COLUMN_ID_PRODUTOS + " , " + COLIMN_ID_UNIDADE + " , " + INGREDIENTES_COLUMN_QUANTIDADE + " ) VALUES ( " + idReceita + " , " + idProduto + " , " + unidade + " , " + quantidade + " );";
        db.execSQL(query);
    }

    public void insereReceita(String nome, String prep) {
        SQLiteDatabase db = getWritableDatabase();
        String query = "INSERT INTO " + TABLE_RECEITAS + " ( " + RECEITAS_COLUMN_NAME + " , " + RECEITAS_COLUMN_PREPARACAO + " ) VALUES ( '" + nome + "' , '" + prep + "' ); ";
        db.execSQL(query);
    }

    public Cursor getReceitas() {
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_RECEITAS + " ORDER BY " + RECEITAS_COLUMN_NAME + " ;";
        Cursor cursor = db.rawQuery(query, null);
        return cursor;
    }

    public void upgradeReceita(int id, String nome, String prep) {
        SQLiteDatabase db = getWritableDatabase();
        String query = "UPDATE " + TABLE_RECEITAS + " SET " + RECEITAS_COLUMN_NAME + " = '" + nome + "' , " + RECEITAS_COLUMN_PREPARACAO + " = '" + prep + "'  WHERE " + RECEITAS_COLUMN_ID + " = " + id + ";";
        db.execSQL(query);
    }

    public void upgradeING(int oldname, int receita, int name, Float quantidade) {
        SQLiteDatabase db = getWritableDatabase();
        String query = "UPDATE " + TABLE_INGREDIENTES + " SET " + COLUMN_ID_PRODUTOS + " = " + name + " , " + INGREDIENTES_COLUMN_QUANTIDADE + " = " + quantidade + " WHERE " + COLUMN_ID_PRODUTOS + " = " + oldname + " AND " + COLUMN_ID_RECEITAS + " = " + receita + ";";
        db.execSQL(query);
    }

    public String getNomeReceita(int id) {
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT " + RECEITAS_COLUMN_NAME + " FROM " + TABLE_RECEITAS + " WHERE " + RECEITAS_COLUMN_ID + " = " + id + ";";
        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();
        String n = c.getString(0);
        return n;
    }

    public String getPrep(int id) {
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT " + RECEITAS_COLUMN_PREPARACAO + " FROM " + TABLE_RECEITAS + " WHERE " + RECEITAS_COLUMN_ID + " = " + id + ";";
        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();
        String n = c.getString(0);
        return n;
    }

    public void DelIng(int id) {
        SQLiteDatabase db = getWritableDatabase();
        String query = "DELETE FROM " + TABLE_INGREDIENTES + " WHERE " + COLUMN_ID_RECEITAS + " = " + id + ";";
        db.execSQL(query);
    }

    public ArrayList<Receita> getReceita() {
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<Integer> q = new ArrayList<>();
        ArrayList<Integer> y = new ArrayList<>();
        ArrayList<Receita> u = new ArrayList<>();
        Cursor j = null;

        String query = "SELECT DISTINCT " + COLUMN_ID_RECEITAS + " FROM " + TABLE_INGREDIENTES + "";
        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();
        if (c.getCount() > 0) {
            do {
                q.add(c.getInt(0));
            } while (c.moveToNext());

            for (int i = 0; i < q.size(); i++) {
                //acha o numero de produtos que tem mais quantidade que stock
                String query2 = "SELECT COUNT(" + TABLE_INGREDIENTES + "." + COLUMN_ID_PRODUTOS + ") FROM " + TABLE_INGREDIENTES + "," + TABLE_PRODUTOS + " WHERE " + INGREDIENTES_COLUMN_QUANTIDADE + " <= " + PRODUTOS_COLUMN_QUANTIDADE + " AND " + TABLE_INGREDIENTES + "." + COLUMN_ID_PRODUTOS + "=" + TABLE_PRODUTOS + "." + PRODUTOS_COLUMN_ID + " AND " + COLUMN_ID_RECEITAS + "=" + q.get(i) + "; ";
                Cursor e = db.rawQuery(query2, null);
                e.moveToFirst();
                int w = e.getInt(0);
                if (w > 0) {
                    String query3 = "SELECT COUNT(" + COLUMN_ID_PRODUTOS + ") FROM " + TABLE_INGREDIENTES + " WHERE " + COLUMN_ID_RECEITAS + " = " + q.get(i) + ";";
                    Cursor r = db.rawQuery(query3, null);
                    r.moveToFirst();
                    int t = r.getInt(0);
                    if (w == t) {
                        y.add(q.get(i));
                    }
                }
            }
            for (int k = 0; k < y.size(); k++) {
                String query4 = "SELECT * FROM " + TABLE_RECEITAS + " WHERE " + RECEITAS_COLUMN_ID + " = " + y.get(k) + ";";
                j = db.rawQuery(query4, null);
                j.moveToFirst();
                if (j.getCount() > 0) {
                    do {
                        Receita fruit = new Receita(j.getInt(0), j.getString(1), j.getString(2));
                        u.add(fruit);
                    } while (j.moveToNext());
                }
            }
        }
        return u;
    }

    public void atualizaINGs(int id, Context q) {
        ArrayList<Integer> t = new ArrayList<>();

        SQLiteDatabase db = getWritableDatabase();

        //seleciona todos os produtos da receita
        String query1 = "SELECT " + COLUMN_ID_PRODUTOS + " FROM " + TABLE_INGREDIENTES + " WHERE " + COLUMN_ID_RECEITAS + " = " + id + ";";
        Cursor c = db.rawQuery(query1, null);
        c.moveToFirst();
        do {
            int i = c.getInt(0);
            t.add(i);
        } while (c.moveToNext());
        for (int j = 0; j < t.size(); j++) {
            //vai buscar a quantidade que é preciso para a receita
            String query2 = "SELECT " + INGREDIENTES_COLUMN_QUANTIDADE + " FROM " + TABLE_INGREDIENTES + " WHERE " + COLUMN_ID_PRODUTOS + " = " + t.get(j) + " AND " + COLUMN_ID_RECEITAS + " = " + id + " ;";
            Cursor cursor = db.rawQuery(query2, null);
            cursor.moveToFirst();
            float i = cursor.getFloat(0);
            //vai busvar a quantidade do produto
            String query3 = "SELECT " + PRODUTOS_COLUMN_QUANTIDADE + " FROM " + TABLE_PRODUTOS + " WHERE " + PRODUTOS_COLUMN_ID + " = " + t.get(j) + " ;";
            Cursor a = db.rawQuery(query3, null);
            a.moveToFirst();
            float y = a.getFloat(0);
            float p = y - i;

            String query4 = "UPDATE " + TABLE_PRODUTOS + " SET " + PRODUTOS_COLUMN_QUANTIDADE + " = " + p + " WHERE " + PRODUTOS_COLUMN_ID + " = " + t.get(j) + " ;";
            db.execSQL(query4);
        }
    }

    public ArrayList<String> CHECKatualizaINGs(int id, Context q) {
        ArrayList<Integer> t = new ArrayList<>();
        ArrayList<Integer> w = new ArrayList<>();
        SQLiteDatabase db = getWritableDatabase();
        ArrayList<String> o = new ArrayList<>();
        //seleciona todos os produtos da receita
        String query1 = "SELECT " + COLUMN_ID_PRODUTOS + " FROM " + TABLE_INGREDIENTES + " WHERE " + COLUMN_ID_RECEITAS + " = " + id + ";";
        Cursor c = db.rawQuery(query1, null);
        c.moveToFirst();
        do {
            int i = c.getInt(0);
            t.add(i);
        } while (c.moveToNext());
        for (int j = 0; j < t.size(); j++) {
            //vai buscar a quantidade que é preciso para a receita
            String query2 = "SELECT " + INGREDIENTES_COLUMN_QUANTIDADE + " FROM " + TABLE_INGREDIENTES + " WHERE " + COLUMN_ID_PRODUTOS + " = " + t.get(j) + " AND " + COLUMN_ID_RECEITAS + " = " + id + " ;";
            Cursor cursor = db.rawQuery(query2, null);
            cursor.moveToFirst();
            float i = cursor.getFloat(0);
            //vai busvar a quantidade do produto
            String query3 = "SELECT " + PRODUTOS_COLUMN_QUANTIDADE + " FROM " + TABLE_PRODUTOS + " WHERE " + PRODUTOS_COLUMN_ID + " = " + t.get(j) + " ;";
            Cursor a = db.rawQuery(query3, null);
            a.moveToFirst();
            float y = a.getFloat(0);
            float p = y - i;
            if (p >= 0) {
            } else {
                w.add(t.get(j));
            }
        }

        for (int x = 0; x < w.size(); x++) {
            String query5 = "Select " + PRODUTOS_COLUMN_NAME + " FROM " + TABLE_PRODUTOS + " WHERE " + PRODUTOS_COLUMN_ID + " = " + w.get(x) + ";";
            Cursor cursor = db.rawQuery(query5, null);
            cursor.moveToFirst();
            do {
                String r = cursor.getString(0);
                o.add(r);
            } while (cursor.moveToNext());
        }
        return o;
    }

    public void delIng(int oldname, int receita) {
        SQLiteDatabase db = getWritableDatabase();
        String query = "DELETE FROM " + TABLE_INGREDIENTES + " WHERE " + COLUMN_ID_PRODUTOS + " = " + oldname + " AND " + COLUMN_ID_RECEITAS + " = " + receita + ";";
        db.execSQL(query);
    }

    public Cursor showProduto(String nome) {
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT " + PRODUTOS_COLUMN_ID + " , " + PRODUTOS_COLUMN_NAME + " , " + PRODUTOS_COLUMN_QUANTIDADE + " , " + UNIDADES_COLUMN_UNIDADE + " , " + PRODUTOS_COLUMN_UNIDADE + " FROM " + TABLE_PRODUTOS + " , " + TABLE_UNIDADES + " WHERE " + PRODUTOS_COLUMN_UNIDADE + " = " + UNIDADES_COLUMN_ID + " AND " + PRODUTOS_COLUMN_QUANTIDADE + " > 0 AND " + PRODUTOS_COLUMN_NAME + " Like '%" + nome + "%' ORDER BY " + PRODUTOS_COLUMN_NAME + ";";
        Cursor cursor = db.rawQuery(query, null);
        return cursor;
    }

    public int verIng(int id) {
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT COUNT(" + COLUMN_ID_PRODUTOS + ") FROM " + TABLE_INGREDIENTES + " WHERE " + COLUMN_ID_RECEITAS + " = " + id + ";";
        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();
        int i = c.getInt(0);
        return i;
    }

    public void delRec(int id) {
        SQLiteDatabase db = getWritableDatabase();
        String query = "DELETE FROM " + TABLE_RECEITAS + " WHERE " + RECEITAS_COLUMN_ID + " = " + id + " ;";
        db.execSQL(query);
    }

    public int getUni(int id) {
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT " + PRODUTOS_COLUMN_UNIDADE + " FROM " + TABLE_PRODUTOS + " WHERE " + PRODUTOS_COLUMN_ID + " = " + id + ";";
        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();
        int i = c.getInt(0);
        return i;
    }

    public Cursor showReceita(String nome) {
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_RECEITAS + " WHERE " + RECEITAS_COLUMN_NAME + " LIKE '%" + nome + "%' ;";
        Cursor cursor = db.rawQuery(query, null);
        return cursor;
    }

    public Cursor verProduto(String nome) {
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT " + PRODUTOS_COLUMN_ID + " , " + PRODUTOS_COLUMN_NAME + " , " + PRODUTOS_COLUMN_QUANTIDADE + " , " + UNIDADES_COLUMN_UNIDADE + " , " + PRODUTOS_COLUMN_UNIDADE + " FROM " + TABLE_PRODUTOS + " , " + TABLE_UNIDADES + " WHERE " + PRODUTOS_COLUMN_UNIDADE + " = " + UNIDADES_COLUMN_ID + " AND " + PRODUTOS_COLUMN_QUANTIDADE + " <= 0  AND " + PRODUTOS_COLUMN_NAME + " LIKE '%" + nome + "%' ORDER BY " + PRODUTOS_COLUMN_NAME + ";";
        Cursor cursor = db.rawQuery(query, null);
        return cursor;
    }

    public void DelProd(int id) {
        SQLiteDatabase db = getWritableDatabase();
        String query = "DELETE FROM " + TABLE_PRODUTOS + " WHERE " + PRODUTOS_COLUMN_ID + " = " + id + ";";
        db.execSQL(query);
    }

    public void juntaStock(String name, Float s) {
        SQLiteDatabase db = getWritableDatabase();
        String query = "Select " + PRODUTOS_COLUMN_QUANTIDADE + " FROM " + TABLE_PRODUTOS + " WHERE " + PRODUTOS_COLUMN_NAME + " = '" + name + "';";
        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();
        float q = s + c.getFloat(0);
        String query4 = "UPDATE " + TABLE_PRODUTOS + " SET " + PRODUTOS_COLUMN_QUANTIDADE + " = " + q + " WHERE " + PRODUTOS_COLUMN_NAME + " = '" + name + "' ;";
        db.execSQL(query4);
    }

    public void SubsPro(String fruta, String name) {
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT " + PRODUTOS_COLUMN_ID + " FROM " + TABLE_PRODUTOS + " WHERE " + PRODUTOS_COLUMN_NAME + "='" + fruta + "';";
        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();
        int idantigo = c.getInt(0);
        String query1 = "SELECT " + PRODUTOS_COLUMN_ID + " FROM " + TABLE_PRODUTOS + " WHERE " + PRODUTOS_COLUMN_NAME + "='" + name + "';";
        Cursor cursor = db.rawQuery(query1, null);
        cursor.moveToFirst();
        int idnovo = cursor.getInt(0);
        String query4 = "UPDATE " + TABLE_INGREDIENTES + " SET " + COLUMN_ID_PRODUTOS + " = " + idnovo + " WHERE " + COLUMN_ID_PRODUTOS + " = " + idantigo + " ;";
        db.execSQL(query4);
    }

    public void MudaUni(int i, int id) {
        SQLiteDatabase db = getWritableDatabase();
        String query = "UPDATE " + TABLE_INGREDIENTES + " SET " + COLIMN_ID_UNIDADE + " = " + i + " WHERE " + COLUMN_ID_PRODUTOS + " = " + id + ";";
        db.execSQL(query);
    }
}