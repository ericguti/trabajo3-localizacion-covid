package com.practica.ems.covid;


import java.util.Iterator;
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
		this.lista = new LinkedList<Persona>();
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

	public boolean delPersona (String documento){
		return this.lista.removeIf(persona -> persona.getDocumento().equals(documento));
	}
	
	@Override
	public String toString() {
		String cadena = "";
		for(int i = 0; i < lista.size(); i++) {
			FechaHora fecha = lista.get(i).getFechaNacimiento();
	        // Documento	    	    	
	        cadena+=String.format("%s;", lista.get(i).getDocumento());
	        // nombre y apellidos	              
	        cadena+=String.format("%s,%s;",lista.get(i).getApellidos(), lista.get(i).getNombre());	        
	        // correo electrónico
	        cadena+=String.format("%s;", lista.get(i).getEmail());
	        // Direccion y código postal
	        cadena+=String.format("%s,%s;", lista.get(i).getDireccion(), lista.get(i).getCp());	        
	        // Fecha de nacimiento
	        cadena+=String.format("%02d/%02d/%04d\n", fecha.getFecha().getDia(), 
	        		fecha.getFecha().getMes(), 
	        		fecha.getFecha().getAnio());
		}
		return cadena;
	}
	
	
	
}
