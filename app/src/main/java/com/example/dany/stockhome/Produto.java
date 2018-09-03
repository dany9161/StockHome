package com.example.dany.stockhome;

/**
 * Created by dany on 13/04/2016.
 */
public class Produto {
    int id,idUnidade;
    Float quantidade;
    String nome,unidade;

    public Produto(int _id, String nome, Float quantidade, String unit, int nUnit) {
        this.id = _id;
        this.quantidade = quantidade;
        this.nome = nome;
        this.unidade = unit;
        this.idUnidade = nUnit;
    }



    public int getIdUnidade() {
        return idUnidade;
    }

    public void setIdUnidade(int idUnidade) {
        this.idUnidade = idUnidade;}

    public String getUnidade() {
        return unidade;
    }

    public void setUnidade(String unidade) {
        this.unidade = unidade;
    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public Float getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Float unidade) {
        this.quantidade = unidade;
    }


    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }


}
