package com.practica.ems.covid;

import com.practica.excecption.EmsInvalidTypeException;

public class FileLine {
    private String type;
    private String[] datos;

    public FileLine(String rawLine) throws EmsInvalidTypeException {
        this.datos = dividirLineaDatos(rawLine);
        this.type = datos[0];
        if ( ! (type.equals("PERSONA") || type.equals("LOCALIZACION") ) ) {
            throw new EmsInvalidTypeException("Tipo de dato no válido");
        }
    }

    private String[] dividirLineaDatos(String rawLine){
        return rawLine.split("\\;");
    }

    public String getType(){
        return this.type;
    }

    public String[] getDatos(){
        return this.datos;
    }
}
