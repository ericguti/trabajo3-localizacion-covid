package com.practica.genericas;

public class PersonaFactory {
    public static Persona crearPersona(String[] datos) {
        String documento = datos[1];
        String nombre = datos[2];
        String apellidos = datos[3];
        String email = datos[4];
        String direccion = datos[5];
        String cod_postal = datos[6];
        FechaHora fechaNacimiento = parsearFecha(datos[7]);
        return new Persona(documento, nombre, apellidos, email, direccion, cod_postal, fechaNacimiento);
    }


    private static FechaHora parsearFecha (String fecha) {
        int dia, mes, anio;
        String[] valores = fecha.split("\\/");
        dia = Integer.parseInt(valores[0]);
        mes = Integer.parseInt(valores[1]);
        anio = Integer.parseInt(valores[2]);
        return new FechaHora(dia, mes, anio, 0, 0);
    }
}
