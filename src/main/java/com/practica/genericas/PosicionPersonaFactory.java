package com.practica.genericas;

public class PosicionPersonaFactory {
    public static PosicionPersona createPosicionPersona(String[] data) {
        String documento = data[1];
        String fecha = data[2];
        String hora = data[3];
        float latitud = Float.parseFloat(data[4]);
        float longitud = Float.parseFloat(data[5]);
        return new PosicionPersona(
            documento, new FechaHora(fecha,hora), new Coordenada(latitud, longitud)
        );
    }
}
