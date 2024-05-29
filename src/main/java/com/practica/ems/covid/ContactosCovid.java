package com.practica.ems.covid;


import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
		PosicionPersona pp = PosicionPersonaFactory.createPosicionPersona(data.getDatos());
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
			processFileLines(br);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void processFileLines(BufferedReader br)
			throws EmsInvalidTypeException, EmsInvalidNumberOfDataException, EmsDuplicatePersonException,
			EmsDuplicateLocationException, IOException
	{
		String fileLine;
		while ((fileLine = br.readLine()) != null) {
			loadData(fileLine);
		}
	}

	public int findPersona(String documento) throws EmsPersonNotFoundException {
		return this.poblacion.findPersona(documento);
	}

	public int findLocalizacion(String documento, String fecha, String hora) throws EmsLocalizationNotFoundException {
		return this.localizacion.findLocalizacion(documento, new FechaHora(fecha, hora));
	}

	public List<PosicionPersona> localizacionPersona(String documento) throws EmsPersonNotFoundException {
		return this.localizacion.findLocation(documento);
	}

	public boolean delPersona(String documento) throws EmsPersonNotFoundException {
		return this.poblacion.delPersona(documento);
	}

	private String[] dividirEntrada(String input) {
		String[] cadenas = input.split("\\n");
		return cadenas;
	}
}
