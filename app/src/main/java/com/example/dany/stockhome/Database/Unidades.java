package com.example.dany.stockhome.Database;

/**
 * Created by dany on 15/04/2016.
 */
public class Unidades {
    int id;
    String unidades;

    public Unidades(int id, String unidades) {
        this.id = id;
        this.unidades = unidades;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUnidades() {
        return unidades;
    }

    public void setUnidades(String unidades) {
        this.unidades = unidades;
    }
}
