package com.example.pe02_registreusuari;

import java.io.Serializable;

/**
 * Clase que guarda los datos de un usuario.
 *
 * "implements Serializable" permite convertir el objeto en bytes para poder
 * meterlo dentro de un Intent (en los Extras / Bundle) y enviarlo a otra Activity.
 */
public class Persona implements Serializable {

    // Número de versión de la clase. Java lo usa para comprobar que el objeto
    // que se envía y el que se recibe son de la misma versión de la clase.
    private static final long serialVersionUID = 1L;

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
