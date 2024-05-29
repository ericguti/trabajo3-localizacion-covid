package com.practica.ems.covid;


import java.io.BufferedReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.practica.excecption.EmsDuplicateLocationException;
import com.practica.excecption.EmsDuplicatePersonException;
import com.practica.excecption.EmsInvalidNumberOfDataException;
import com.practica.excecption.EmsInvalidTypeException;
import com.practica.excecption.EmsLocalizationNotFoundException;
import com.practica.excecption.EmsPersonNotFoundException;
import com.practica.genericas.*;
import com.practica.lista.ListaContactos;

public class ContactosCovid {
	private Poblacion poblacion;
	private Localizacion localizacion;
	private ListaContactos listaContactos;

	public ContactosCovid() {
		this.poblacion = new Poblacion();
		this.localizacion = new Localizacion();
		this.listaContactos = new ListaContactos();
	}

	public Poblacion getPoblacion() {
		return poblacion;
	}

	public Localizacion getLocalizacion() {
		return localizacion;
	}

	public void setLocalizacion(Localizacion localizacion) {
		this.localizacion = localizacion;
	}
	
	

	public ListaContactos getListaContactos() {
		return listaContactos;
	}


	public void addLocalizacionFromDataLine(FileLine data)
			throws EmsInvalidNumberOfDataException, EmsDuplicateLocationException {
		if (data.getDatos().length != Constantes.MAX_DATOS_LOCALIZACION) {
			throw new EmsInvalidNumberOfDataException("El número de datos para LOCALIZACION es menor de 6");
		}
		PosicionPersona pp = this.crearPosicionPersona(data.getDatos());
		this.localizacion.addLocalizacion(pp);
		this.listaContactos.insertarNodoTemporal(pp);
	}

	public void addPersonaFromDataLine(FileLine data)
			throws EmsInvalidNumberOfDataException, EmsDuplicatePersonException {
		if (data.getDatos().length != Constantes.MAX_DATOS_PERSONA) {
			throw new EmsInvalidNumberOfDataException("El número de datos para PERSONA es menor de 8");
		}
		this.poblacion.addPersona(PersonaFactory.crearPersona(data.getDatos()));
	}

	public void resetData() {
		this.poblacion = new Poblacion();
		this.localizacion = new Localizacion();
		this.listaContactos = new ListaContactos();
	}

	public void loadDataFromLine(String line)
			throws EmsInvalidTypeException, EmsInvalidNumberOfDataException, EmsDuplicatePersonException,
			EmsDuplicateLocationException
	{
		FileLine data = new FileLine(line);
		if (data.getType().equals("PERSONA")) {
			this.addPersonaFromDataLine(data);
		}
		if (data.getType().equals("LOCALIZACION")) {
			this.addLocalizacionFromDataLine(data);
		}
	}

	public void loadData(String data, boolean reset)
			throws EmsInvalidTypeException, EmsInvalidNumberOfDataException, EmsDuplicatePersonException,
			EmsDuplicateLocationException
	{
		if (reset) {
			resetData();
		}
		String[] lineas = dividirEntrada(data);
		for (String linea : lineas) {
			loadDataFromLine(linea);
		}
	}

	public void loadData(String data)
			throws EmsInvalidTypeException, EmsInvalidNumberOfDataException, EmsDuplicatePersonException,
			EmsDuplicateLocationException
	{
		loadData(data, false);
	}

	public void loadDataFile(String pathString, boolean reset) {
		if (reset) {
			resetData();
		}
		Path path = Paths.get(pathString);
		try (BufferedReader br = Files.newBufferedReader(path)) {
			String fileLine;
			while ((fileLine = br.readLine()) != null) {
				loadData(fileLine);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public int findPersona(String documento) throws EmsPersonNotFoundException {
		return this.poblacion.findPersona(documento);
	}

	public int findLocalizacion(String documento, String fecha, String hora) throws EmsLocalizationNotFoundException {
		return this.localizacion.findLocalizacion(documento, new FechaHora(fecha, hora));
	}

	public List<PosicionPersona> localizacionPersona(String documento) throws EmsPersonNotFoundException {




		int cont = 0;
		List<PosicionPersona> lista = new ArrayList<PosicionPersona>();
		Iterator<PosicionPersona> it = this.localizacion.getLista().iterator();
		while (it.hasNext()) {
			PosicionPersona pp = it.next();
			if (pp.getDocumento().equals(documento)) {
				cont++;
				lista.add(pp);
			}
		}
		if (cont == 0)
			throw new EmsPersonNotFoundException();
		else
			return lista;
	}

	public boolean delPersona(String documento) throws EmsPersonNotFoundException {

		boolean deleted = this.poblacion.delPersona(documento);
		if (!deleted) {
			throw new EmsPersonNotFoundException();
		}
		return deleted;
	}

	private String[] dividirEntrada(String input) {
		String cadenas[] = input.split("\\n");
		return cadenas;
	}

	private PosicionPersona crearPosicionPersona(String[] data) {
		PosicionPersona posicionPersona = new PosicionPersona();
		String fecha = null, hora;
		float latitud = 0, longitud;
		for (int i = 1; i < Constantes.MAX_DATOS_LOCALIZACION; i++) {
			String s = data[i];
			switch (i) {
			case 1:
				posicionPersona.setDocumento(s);
				break;
			case 2:
				fecha = data[i];
				break;
			case 3:
				hora = data[i];
				posicionPersona.setFechaPosicion(parsearFecha(fecha, hora));
				break;
			case 4:
				latitud = Float.parseFloat(s);
				break;
			case 5:
				longitud = Float.parseFloat(s);
				posicionPersona.setCoordenada(new Coordenada(latitud, longitud));
				break;
			}
		}
		return posicionPersona;
	}
	
	private FechaHora parsearFecha (String fecha, String hora) {
		int dia, mes, anio;
		String[] valores = fecha.split("\\/");
		dia = Integer.parseInt(valores[0]);
		mes = Integer.parseInt(valores[1]);
		anio = Integer.parseInt(valores[2]);
		int minuto, segundo;
		valores = hora.split("\\:");
		minuto = Integer.parseInt(valores[0]);
		segundo = Integer.parseInt(valores[1]);
		FechaHora fechaHora = new FechaHora(dia, mes, anio, minuto, segundo);
		return fechaHora;
	}
}
