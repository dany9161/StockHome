package com.example.dany.stockhome.RecyclerAdapter;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.dany.stockhome.Activities.EditaIngredientesAdicionaActivity;
import com.example.dany.stockhome.Database.Ingrediente;
import com.example.dany.stockhome.R;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by dany on 07/05/2016.
 */
public class AddIngredientesRecyclerAdapter extends RecyclerView.Adapter<AddIngredientesRecyclerAdapter.RecyclerViewHolder> {

    ArrayList<Ingrediente> arrayList = new ArrayList<>();
    private LayoutInflater inflater;
    Cursor data;
    Ingrediente fruit;

    public AddIngredientesRecyclerAdapter(ArrayList<Ingrediente> arrayList) {
        this.arrayList = arrayList;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_ing, parent, false);
        RecyclerViewHolder recyclerViewHolder = new RecyclerViewHolder(view);
        return recyclerViewHolder;
    }

    @Override
    public void onBindViewHolder(final RecyclerViewHolder holder, int position) {
        fruit = arrayList.get(position);
        holder.Name.setText(fruit.getProduto());
        holder.Quantidade.setText(stock(fruit.getQuantidade().floatValue()) + " ");
        holder.Unidade.setText(fruit.getUnidade() + " ");
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), EditaIngredientesAdicionaActivity.class);
                intent.putExtra("idReceita", fruit.getIdReceita());
                intent.putExtra("Produto", fruit.getProduto());
                intent.putExtra("oldname", fruit.getIdProduto());
                intent.putExtra("Unidade", fruit.getIdUnidade());
                intent.putExtra("stock", fruit.getQuantidade());
                v.getContext().startActivity(intent);
            }
        });
    }

//    public void insert(int position, Produto data) {
//        arrayList.add(position, data);
//        notifyItemInserted(position);
//    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class RecyclerViewHolder extends RecyclerView.ViewHolder {
        TextView Name, Quantidade, Unidade;

        public RecyclerViewHolder(View View) {
            super(View);
            Quantidade = (TextView) itemView.findViewById(R.id.quantidade);
            Unidade = (TextView) itemView.findViewById(R.id.unidade);
            Name = (TextView) itemView.findViewById(R.id.nome);
        }
    }

    public String stock(float i) {
        DecimalFormat df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.CEILING);
        Float d = i;
        String r = df.format(d);
        return r;
    }
}
