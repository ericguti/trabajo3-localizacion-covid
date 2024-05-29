package com.practica.ems.covid;


import java.util.LinkedList;
import java.util.List;

import com.practica.excecption.EmsDuplicatePersonException;
import com.practica.excecption.EmsPersonNotFoundException;
import com.practica.genericas.FechaHora;
import com.practica.genericas.Persona;

public class Poblacion {
	List<Persona> lista ;

	public Poblacion() {
		super();
		this.lista = new LinkedList<>();
	}

	public List<Persona> getLista() {
		return lista;
	}

	public void setLista(LinkedList<Persona> lista) {
		this.lista = lista;
	}

	public void addPersona (Persona persona) throws EmsDuplicatePersonException {
		try {
			findPersona(persona.getDocumento());
			throw new EmsDuplicatePersonException();
		} catch (EmsPersonNotFoundException e) {
			lista.add(persona);
		} 
	}
	
	public int findPersona (String documento) throws EmsPersonNotFoundException {
		for (int i = 0; i < lista.size(); i++) {
			if(filterPersona(lista.get(i).getDocumento(), documento)) {
				return i+1;
			}
		}
		throw new EmsPersonNotFoundException();
	}

	private boolean filterPersona (String documento1, String documento2) {
		return documento1.equals(documento2);
	}

	public boolean delPersona (String documento) throws EmsPersonNotFoundException{
		boolean deleted = this.lista.removeIf(persona -> persona.getDocumento().equals(documento));
		if (!deleted) {
			throw new EmsPersonNotFoundException();
		}
		return true;
	}
	
	@Override
	public String toString() {
		StringBuilder cadena = new StringBuilder();
        for (Persona persona : lista) {
            FechaHora fecha = persona.getFechaNacimiento();
            // Documento
            cadena.append(String.format("%s;", persona.getDocumento()));
            // nombre y apellidos
            cadena.append(String.format("%s,%s;", persona.getApellidos(), persona.getNombre()));
            // correo electrónico
            cadena.append(String.format("%s;", persona.getEmail()));
            // Direccion y código postal
            cadena.append(String.format("%s,%s;", persona.getDireccion(), persona.getCp()));
            // Fecha de nacimiento
            cadena.append(String.format("%02d/%02d/%04d\n", fecha.getFecha().getDia(),
                    fecha.getFecha().getMes(),
                    fecha.getFecha().getAnio()));
        }
		return cadena.toString();
	}
	
	
	
}
