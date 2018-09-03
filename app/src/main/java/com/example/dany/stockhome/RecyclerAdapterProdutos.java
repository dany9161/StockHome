package com.example.dany.stockhome;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by dany on 15/04/2016.
 */
 public class RecyclerAdapterProdutos extends RecyclerView.Adapter<RecyclerAdapterProdutos.RecyclerViewHolder> {

    ArrayList<Produto> arrayList = new ArrayList<>();
    public RecyclerAdapterProdutos(ArrayList<Produto> arrayList){this.arrayList = arrayList;}

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_row_produtos, parent, false);
        RecyclerViewHolder recyclerViewHolder = new RecyclerViewHolder(view);
        return recyclerViewHolder;
    }

    @Override
    public void onBindViewHolder(final RecyclerViewHolder holder, int position) {

        final Produto fruit = arrayList.get(position);
        holder.Name.setText(fruit.getNome());
        holder.Quantidade.setText(stock(fruit.getQuantidade().floatValue())+" ");
        holder.Unidade.setText("  "+fruit.getUnidade());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),editar_produtos.class);
                intent.putExtra("id",fruit.getId());
                intent.putExtra("nome", fruit.getNome());
                intent.putExtra("stock",fruit.getQuantidade());
                intent.putExtra("unit",fruit.getIdUnidade());
                v.getContext().startActivity(intent);
            }
        });
    }

    public void insert(int position, Produto data) {
        arrayList.add(position, data);
        notifyItemInserted(position);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }



    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public  static class RecyclerViewHolder extends RecyclerView.ViewHolder{

        TextView Name,Quantidade,Unidade;
        public RecyclerViewHolder(View View) {
            super(View);
            Name = (TextView)itemView.findViewById(R.id.nome);
            Quantidade = (TextView) itemView.findViewById(R.id.Quantidade);
            Unidade = (TextView) itemView.findViewById(R.id.Unidade);
        }
    }

    public String stock (float i){
        DecimalFormat df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.CEILING);
        Float d = i;
        String r = df.format(d);
        return r;
    }



}
