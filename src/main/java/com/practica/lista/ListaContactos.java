package com.practica.lista;

import com.practica.genericas.FechaHora;
import com.practica.genericas.PosicionPersona;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListaContactos {
	private List<NodoTemporal> lista;
	private int size;

	public ListaContactos() {
		this.lista = new ArrayList<>();
		this.size = 0;
	}

	public void insertarNodoTemporal(PosicionPersona p) {
		NodoTemporal encontrado = null;
		for (NodoTemporal nodoTemporal : lista) {
			if (nodoTemporal.getFecha().equals(p.getFechaPosicion())) {
				encontrado = nodoTemporal;
				break;
			}
		}

		if (encontrado != null) {
			insertarCoordenada(encontrado, p);
		} else {
			NodoTemporal nuevo = new NodoTemporal();
			nuevo.setFecha(p.getFechaPosicion());
			insertarCoordenada(nuevo, p);
			lista.add(nuevo);
			size++;
			lista.sort((a, b) -> a.getFecha().compareTo(b.getFecha()));
		}
	}

	private void insertarCoordenada(NodoTemporal nodoTemporal, PosicionPersona p) {
		NodoPosicion encontrado = null;
		NodoPosicion current = nodoTemporal.getListaCoordenadas();
		while (current != null) {
			if (current.getCoordenada().equals(p.getCoordenada())) {
				encontrado = current;
				break;
			}
			current = current.getSiguiente();
		}

		if (encontrado != null) {
			encontrado.setNumPersonas(encontrado.getNumPersonas() + 1);
		} else {
			NodoPosicion nuevo = new NodoPosicion(p.getCoordenada(), 1, nodoTemporal.getListaCoordenadas());
			nodoTemporal.setListaCoordenadas(nuevo);
		}
	}

	public int personasEnCoordenadas() {
		int totalPersonas = 0;
		for (NodoTemporal nodoTemporal : lista) {
			NodoPosicion current = nodoTemporal.getListaCoordenadas();
			while (current != null) {
				totalPersonas += current.getNumPersonas();
				current = current.getSiguiente();
			}
		}
		return totalPersonas;
	}

	public int tamanioLista() {
		return this.size;
	}

	public String getPrimerNodo() {
		if (lista.isEmpty()) {
			return "";
		}
		NodoTemporal aux = lista.get(0);
		return aux.getFecha().getFecha().toString() + ";" + aux.getFecha().getHora().toString();
	}

	public int numPersonasEntreDosInstantes(FechaHora inicio, FechaHora fin) {
		int totalPersonas = 0;
		for (NodoTemporal nodoTemporal : lista) {
			if (nodoTemporal.getFecha().compareTo(inicio) >= 0 && nodoTemporal.getFecha().compareTo(fin) <= 0) {
				NodoPosicion current = nodoTemporal.getListaCoordenadas();
				while (current != null) {
					totalPersonas += current.getNumPersonas();
					current = current.getSiguiente();
				}
			}
		}
		return totalPersonas;
	}

	public int numNodosCoordenadaEntreDosInstantes(FechaHora inicio, FechaHora fin) {
		int totalNodos = 0;
		for (NodoTemporal nodoTemporal : lista) {
			if (nodoTemporal.getFecha().compareTo(inicio) >= 0 && nodoTemporal.getFecha().compareTo(fin) <= 0) {
				NodoPosicion current = nodoTemporal.getListaCoordenadas();
				while (current != null) {
					totalNodos++;
					current = current.getSiguiente();
				}
			}
		}
		return totalNodos;
	}

	@Override
	public String toString() {
		StringBuilder cadena = new StringBuilder();
		for (NodoTemporal nodoTemporal : lista) {
			cadena.append(nodoTemporal.getFecha().getFecha().toString())
					.append(";")
					.append(nodoTemporal.getFecha().getHora().toString())
					.append(" ");
		}
		return cadena.toString().trim();
	}
}
