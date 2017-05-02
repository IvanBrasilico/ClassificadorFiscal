package org.classifica.entidades;

import java.io.Serializable;

public class Dicionario implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String palavra;
	Integer indicevocab; // Índice do sinônimo no vocabulário
	
	public String getPalavra() {
		return palavra;
	}

	public void setPalavra(String palavra) {
		this.palavra = palavra;
	}

	public Integer getIndicevocab() {
		return indicevocab;
	}

	public void setIndicevocab(Integer indicevocab) {
		this.indicevocab = indicevocab;
	}

	@Override
	public String toString(){
		return palavra;
	}

}
