package com.practica.ems.covid;


import java.util.LinkedList;
import java.util.List;

import com.practica.excecption.EmsDuplicateLocationException;
import com.practica.excecption.EmsLocalizationNotFoundException;
import com.practica.excecption.EmsPersonNotFoundException;
import com.practica.genericas.FechaHora;
import com.practica.genericas.PosicionPersona;

public class Localizacion {
	LinkedList<PosicionPersona> lista;

	public Localizacion() {
		super();
		this.lista = new LinkedList<>();
	}
	
	public LinkedList<PosicionPersona> getLista() {
		return lista;
	}

	public void setLista(LinkedList<PosicionPersona> lista) {
		this.lista = lista;
	}

	public void addLocalizacion (PosicionPersona p) throws EmsDuplicateLocationException {
		try {
			findLocalizacion(p.getDocumento(), p.getFechaPosicion());
			throw new EmsDuplicateLocationException();
		}catch(EmsLocalizationNotFoundException e) {
			lista.add(p);
		}
	}
	
	public int findLocalizacion (String documento, FechaHora fechaHora) throws EmsLocalizationNotFoundException {

		for (int i = 0; i < lista.size(); i++) {
			if (filterLocalizacion(lista.get(i).getDocumento(), documento,
					lista.get(i).getFechaPosicion(), fechaHora)) {
				return i + 1;
			}
		}
		throw new EmsLocalizationNotFoundException();
	}

	// create a findLocation method that receives a document and returns all the locations of that person
	public List<PosicionPersona> findLocation (String documento) throws EmsPersonNotFoundException{
		List<PosicionPersona> personLocations = new LinkedList<>();
		for (PosicionPersona posicionPersona : this.lista) {
			if (posicionPersona.getDocumento().equals(documento)) {
				personLocations.add(posicionPersona);
			}
		}
		if (personLocations.isEmpty()) {
			throw new EmsPersonNotFoundException();
		}
		return personLocations;
	}

	private boolean filterLocalizacion (String documento1, String documento2, FechaHora fechaHora1, FechaHora fechaHora2) {
		return documento1.equals(documento2) && fechaHora1.equals(fechaHora2);
	}

	@Override
	public String toString() {
		StringBuilder cadena = new StringBuilder();
        for (PosicionPersona pp : this.lista) {
            cadena.append(String.format("%s;", pp.getDocumento()));
            FechaHora fecha = pp.getFechaPosicion();
            cadena.append(String.format("%02d/%02d/%04d;%02d:%02d;",
                    fecha.getFecha().getDia(),
                    fecha.getFecha().getMes(),
                    fecha.getFecha().getAnio(),
                    fecha.getHora().getHora(),
                    fecha.getHora().getMinuto()));
            cadena.append(String.format("%.4f;%.4f\n", pp.getCoordenada().getLatitud(),
                    pp.getCoordenada().getLongitud()));
        }
		
		return cadena.toString();
	}
	
}
