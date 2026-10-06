package com.example.recetario;

public class Receta {

    private int id;
    private String nombre;
    private String preparacion;
    private String foto;

    public Receta() {
    }

    public Receta(int id, String nombre, String preparacion, String foto) {
        this.id = id;
        this.nombre = nombre;
        this.preparacion = preparacion;
        this.foto = foto;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPreparacion() {
        return preparacion;
    }

    public void setPreparacion(String preparacion) {
        this.preparacion = preparacion;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }
}