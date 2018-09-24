package com.example.dany.stockhome.Database;

/**
 * Created by dany on 26/04/2016.
 */
public class Ingrediente {
    int idReceita, idProduto, idUnidade;
    Float Quantidade;
    String Produto, Unidade;

    public Ingrediente(int idReceita, Float quantidade, String Produto, String Unidade, int idUnidade, int idProduto) {
        this.idReceita = idReceita;
        this.Produto = Produto;
        Quantidade = quantidade;
        this.Unidade = Unidade;
        this.idUnidade = idUnidade;
        this.idProduto = idProduto;
    }

    public String getProduto() {
        return Produto;
    }

    public void setProduto(String produto) {
        Produto = produto;
    }

    public String getUnidade() {
        return Unidade;
    }

    public void setUnidade(String unidade) {
        Unidade = unidade;
    }

    public int getIdUnidade() {
        return idUnidade;
    }

    public void setIdUnidade(int idUnidade) {
        this.idUnidade = idUnidade;
    }

    public int getIdReceita() {
        return idReceita;
    }

    public void setIdReceita(int idReceita) {
        this.idReceita = idReceita;
    }

    public int getIdProduto() {
        return idProduto;
    }

    public void setIdProduto(int idProduto) {
        this.idProduto = idProduto;
    }

    public Float getQuantidade() {
        return Quantidade;
    }

    public void setQuantidade(Float quantidade) {
        Quantidade = quantidade;
    }
}
