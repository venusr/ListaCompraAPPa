package br.com.fiap.listacompraappa.model;

/**
 * Created by Cecilia_2 on 12/02/2018.
 */

public class CursoItem {

    private int id;
    private String titulo;
    private String turno;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getTurno() {
        return turno;
    }

    public void setTurno(String turno) {
        this.turno = turno;
    }

    @Override
    public String toString() {
        return "CursoItem{" +
                "id=" + id +
                ", titulo='" + titulo + '\'' +
                ", turno='" + turno + '\'' +
                '}';
    }
}