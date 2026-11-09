package com.example.pe02_registreusuari;

import java.io.Serializable;

/**
 * Clase que guarda los datos de un usuario.
 *
 * "implements Serializable" permite meter el objeto dentro de un Intent
 * (con putExtra) para enviarlo a otra Activity.
 */
public class Persona implements Serializable {

    // Atributos (los datos de la ficha)
    private String nombre;
    private String apellidos;
    private int edad;
    private boolean matriculado;

    // Constructor: crea una Persona con todos sus datos
    public Persona(String nombre, String apellidos, int edad, boolean matriculado) {
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.edad = edad;
        this.matriculado = matriculado;
    }

    // Getters: métodos para leer los datos desde otras clases
    public String getNombre() {
        return nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public int getEdad() {
        return edad;
    }

    public boolean isMatriculado() {
        return matriculado;
    }
}
