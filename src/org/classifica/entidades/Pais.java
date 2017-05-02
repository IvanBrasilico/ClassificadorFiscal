package org.classifica.entidades;

import java.io.Serializable;
import java.util.ArrayList;

public class Pais implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer ID;
	private String nompais;
	
	private ArrayList<String[]> estatisticacapncm; //{"Código", "Descrição", "%"}

	public Integer getID() {
		return ID;
	}
	public void setID(Integer codpais) {
		this.ID = codpais;
	}
	public String getNompais() {
		return nompais;
	}
	public void setNompais(String nompais) {
		this.nompais = nompais;
	}
	@Override
	public String toString(){
		return getNompais();
	}
	public ArrayList<String[]> getEstatisticacapncm() {
		return estatisticacapncm;
	}
	public void setEstatisticacapncm(ArrayList<String[]> estatisticacapncm) {
		this.estatisticacapncm = estatisticacapncm;
	}
	

}
