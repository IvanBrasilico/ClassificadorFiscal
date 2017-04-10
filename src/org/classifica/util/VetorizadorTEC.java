package org.classifica.util;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;


public class VetorizadorTEC {


	private String[] listaTECCompleta; //Texto da TEC
	private ArrayList<String[]> listaNCM; // TEC em formato lista
	private ArrayList<String> listaTECResumo; //Lista de códigos e descrição completa
	private Integer numeroPalavrasTECResumo;
	private ArrayList<String> vocab; // Lista de palavras vocabulário
	private short[] vetorVocab; // Vetor do vocabulário
	private ArrayList<ArrayList<Integer>> vetoresTECindex;  // Vetores dos itens da listaTECResumo
	private ArrayList<ArrayList<Integer>> vetoresTECcount;
	private ArrayList<String> vocabstem; // Lista de palavras vocabulário
	private short[] vetorVocabstem; // Vetor do vocabulário
	private ArrayList<String> vocabBigrama; // Lista de palavras vocabulário
	private short[] vetorVocabBigrama; // Vetor do vocabulário
	private ArrayList<ArrayList<Integer>> vetoresTECBigramaindex; // Vetores dos itens da listaTECResumo
	private ArrayList<ArrayList<Integer>> vetoresTECBigramacount; // Vetores dos itens da listaTECResumo
	private ArrayList<Float[]> TECsPontos;
	private ArrayList<String[]> TECsPontosDescricao;
	public ArrayList<String[]> getTECsPontosDescricao() {
		return TECsPontosDescricao;
	}
	public void setTECsPontosDescricao(ArrayList<String[]> tECsPontosDescricao) {
		TECsPontosDescricao = tECsPontosDescricao;
	}
	public enum Tipo {TEXTO, STEM, BIGRAMA};
	private Set<Tipo> tiposAtivos;
	private ArrayList<ArrayList<Integer>> vetoresTECstemindex;
	private ArrayList<ArrayList<Integer>> vetoresTECstemcount;

    //Parâmetros BM25+
	public double _delta = 1.0;
	public double _k = 1.2;
	public double _b = 0.6;

	
	public ArrayList<Float[]> getTECsPontos() {
		return TECsPontos;
	}
	public ArrayList<String> getListaTECResumo() {
		return listaTECResumo;
	}


	public VetorizadorTEC(String textotec){
		listaTECCompleta = textotec.split("\n");
		Tipo[] tipos = {Tipo.TEXTO};
		setTiposAtivos(tipos);
	}

	public String getEstatisticas(){
		String estatisticas = "";
		estatisticas = estatisticas + "TEC tem "+Integer.toString(listaTECResumo.size())+" linhas e "+numeroPalavrasTECResumo +
				" palavras. "+ "(Média de palavras por linha: "+ (numeroPalavrasTECResumo/listaTECResumo.size())+")\n";
		estatisticas = estatisticas + "Vocabulário básico tem "+Integer.toString(vocab.size())+" palavras \n";
		try{
			estatisticas = estatisticas + "Vocabulário stemizado tem "+Integer.toString(vocabstem.size())+" palavras \n";
			estatisticas = estatisticas + "Vocabulário de bigramas tem "+Integer.toString(vocabBigrama.size())+" palavras \n";
		} catch (Exception e) {

		}
		return estatisticas;
	}

	public void inicializa(){
		carregaNCM();
		carregaTECResumo();
		montaVocab();
		montaVetores();
	}


	public static String removerAcentos(String str) {
		str = Normalizer.normalize(str, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
		str = str.replaceAll("[^a-zA-Z0-9]"," ");
		return str;
	}


	protected void carregaNCM() {
		listaNCM = new ArrayList<String[]>();
		for (int r=0;r<listaTECCompleta.length;r++){
			String linha = listaTECCompleta[r];
			if(linha.length()<11){ ///Testa se tem até 10 dígitos, se é numero e se tem ponto. Se reunir estas condições, é provável ser uma entrada válida.  
				int i = linha.indexOf(".");
				if ((i>=2) && (Character.isDigit(linha.charAt(0)))){
					r=r+1;
					String linha2 = listaTECCompleta[r];
					if ((!linha2.isEmpty()) && (!Character.isDigit(linha2.charAt(0)))){ // Elimina as sequências de números
						String[] linhacompleta = {"","",""};
						/*						if (i==2){
							linha = linha.substring(0, 2) + linha.substring(3);
						}
						 */					linhacompleta[0] = linha;
						 linhacompleta[1] = linha2;
						 String linha3 = listaTECCompleta[r+1];
						 if ((linha3.isEmpty()) || (Character.isDigit(linha3.charAt(0)))){ // É uma posição com IPI (3 colunas)
							 r=r+1;
							 linhacompleta[2] = linha3;
						 }
						 listaNCM.add(linhacompleta);
					}
				}
			}
		}
	}


	protected void carregaTECResumo() {
		listaTECResumo = new ArrayList<String>();
		for (int r=0;r<listaNCM.size();r++){
			String codigo = listaNCM.get(r)[0];
			String descricao = listaNCM.get(r)[1];
			String II = listaNCM.get(r)[2];
			if (!II.isEmpty()){ // É uma Classificação válida, buscar os "pais"
				int s = r-1; 
				while (true){
					String codigo2 = listaNCM.get(s)[0];
					String descricao2 = listaNCM.get(s)[1];
					String lcodigo = "";
					lcodigo=codigo.substring(0, 2)+"."+codigo.substring(2, 4);
					//					lcodigo=codigo.substring(0, 4);
					if(lcodigo.compareTo(codigo2)==0){
						descricao=descricao+" "+descricao2;
						listaTECResumo.add(codigo+" "+descricao);
						break;
					}
					try{
						lcodigo=codigo.substring(0, 6);
						if(lcodigo.compareTo(codigo2)==0){
							descricao=descricao+" "+descricao2;
						}
						lcodigo=codigo.substring(0, 7);
						if(lcodigo.compareTo(codigo2)==0){
							descricao=descricao+" "+descricao2;
						}
						lcodigo=codigo.substring(0, 9);
						if(lcodigo.compareTo(codigo2)==0){
							descricao=descricao+" "+descricao2;
						}
					} catch (StringIndexOutOfBoundsException e){

					}
					s--;
					if ((r-s) > 30){ // Exceção encontrada, abortar
						listaTECResumo.add(codigo+" "+descricao);
						break;
					}
				}
			}
		}
	}


	protected void montaVocab() {
		vocab = new ArrayList<String>();
		vocabstem = new ArrayList<String>();
		vocabBigrama = new ArrayList<String>();
		portugueseStemmer stemmer = new portugueseStemmer(); 
		String descricao = "";
		String [] listadepalavras;
		numeroPalavrasTECResumo = 0;
		for (int r=0;r<listaTECCompleta.length;r++){
			String linha = listaTECCompleta[r];
			descricao = removerAcentos(linha);
			listadepalavras = descricao.split(" ");
			for (int s=0;s<listadepalavras.length;s++){
				String palavra = listadepalavras[s];
				numeroPalavrasTECResumo +=1;
				if(s>0){
					String bigrama = listadepalavras[s-1] + " " + palavra; 
					bigrama = bigrama.toUpperCase();
					if (vocabBigrama.indexOf(bigrama)==-1){
						vocabBigrama.add(bigrama);
					}
				}
				if (palavra.length()>2) { //and (stopwords.count(palavra)==0)):
					numeroPalavrasTECResumo +=1;
					stemmer.setCurrent(palavra);
					palavra = palavra.toUpperCase();
					if (vocab.indexOf(palavra)==-1){
						vocab.add(palavra);
					}
					stemmer.stem();
					palavra = stemmer.getCurrent();
					palavra = palavra.toUpperCase();
					if (vocabstem.indexOf(palavra)==-1){
						vocabstem.add(palavra);
					}
				}
			}

		}
	}

	protected void montaVetores() {
		portugueseStemmer stemmer = new portugueseStemmer(); 
		String descricao = "";
		String [] listadepalavras;
		Integer index = -1;
		vetorVocab = new short[vocab.size()];
		vetoresTECindex = new ArrayList<ArrayList<Integer>>();
		vetoresTECcount = new ArrayList<ArrayList<Integer>>();
		vetorVocabstem = new short[vocabstem.size()];
		vetoresTECstemindex = new ArrayList<ArrayList<Integer>>();
		vetoresTECstemcount = new ArrayList<ArrayList<Integer>>();
		vetorVocabBigrama = new short[vocabBigrama.size()];
		vetoresTECBigramaindex = new ArrayList<ArrayList<Integer>>();
		vetoresTECBigramacount = new ArrayList<ArrayList<Integer>>();
		for (int r=0;r<listaTECCompleta.length;r++){
			String linha = listaTECCompleta[r];
			linha = removerAcentos(linha);
			listadepalavras = linha.split(" ");
			for (int s=0;s<listadepalavras.length;s++){
				String palavra = listadepalavras[s];
				if(s>0){
					String bigrama = listadepalavras[s-1] + " " + palavra; 
					bigrama = bigrama.toUpperCase();
					index = vocabBigrama.indexOf(bigrama); 
					if (index >=0){
						vetorVocabBigrama[index] = (short) (vetorVocabBigrama[index] + 1);
					}
				}
				stemmer.setCurrent(palavra);
				palavra = palavra.toUpperCase();
				index = vocab.indexOf(palavra); 
				if (index >=0){
					vetorVocab[index] = (short) (vetorVocab[index] + 1);
				}
				stemmer.stem();
				palavra = stemmer.getCurrent();
				palavra = palavra.toUpperCase();
				index = vocabstem.indexOf(palavra); 
				if (index >=0){
					vetorVocabstem[index] = (short) (vetorVocabstem[index] + 1);
				}
			}
		}
		for (String linha:listaTECResumo){
			ArrayList<Integer> tecvectorindex = new ArrayList<Integer>(); 
			ArrayList<Integer> tecvectorcount = new ArrayList<Integer>(); 
			ArrayList<Integer> tecvectorstemindex = new ArrayList<Integer>(); 
			ArrayList<Integer> tecvectorstemcount = new ArrayList<Integer>(); 
			ArrayList<Integer> tecvectorbigramaindex = new ArrayList<Integer>(); 
			ArrayList<Integer> tecvectorbigramacount = new ArrayList<Integer>(); 
			descricao = linha.substring(11);
			descricao = removerAcentos(descricao);
			listadepalavras = descricao.split(" ");
			for (int s=0;s<listadepalavras.length;s++){
				String palavra = listadepalavras[s];
				if(s>0){
					String bigrama = listadepalavras[s-1] + " " + palavra; 
					bigrama = bigrama.toUpperCase();
					index = vocabBigrama.indexOf(bigrama); 
					if (index >=0){
						Integer indexLinha = tecvectorbigramaindex.indexOf(index);  
						if (indexLinha == -1){
							tecvectorbigramaindex.add(index);
							tecvectorbigramacount.add(1);
						} else {
							Integer count = tecvectorbigramacount.get(indexLinha);
							tecvectorbigramacount.set(indexLinha, count + 1);
						}
					}
				}
				stemmer.setCurrent(palavra);
				palavra = palavra.toUpperCase();
				index = vocab.indexOf(palavra); 
				if (index >=0){
					Integer indexLinha = tecvectorindex.indexOf(index);  
					if (indexLinha == -1){
						tecvectorindex.add(index);
						tecvectorcount.add(1);
					} else {
						Integer count = tecvectorcount.get(indexLinha);
						tecvectorcount.set(indexLinha, count + 1);
					}
				}
				stemmer.stem();
				palavra = stemmer.getCurrent();
				palavra = palavra.toUpperCase();
				index = vocabstem.indexOf(palavra); 
				if (index >=0){
					Integer indexLinha = tecvectorstemindex.indexOf(index);  
					if (indexLinha == -1){
						tecvectorstemindex.add(index);
						tecvectorstemcount.add(1);
					} else {
						Integer count = tecvectorstemcount.get(indexLinha);
						tecvectorstemcount.set(indexLinha, count + 1);
					}
				}
			}
			vetoresTECindex.add(tecvectorindex);
			vetoresTECcount.add(tecvectorcount);
			vetoresTECstemindex.add(tecvectorstemindex);
			vetoresTECstemcount.add(tecvectorstemcount);
			vetoresTECBigramaindex.add(tecvectorbigramaindex);
			vetoresTECBigramacount.add(tecvectorbigramacount);
		}
	}

	public double TF_IDF(Double countPalavraItemTEC, short psum, Integer pquantidadeTECs, short countPalavraVocab,
			Integer pnumeroPalavrasLinhaTEC, double pavgLengthlistaTECResumo) {
		/// Pontuação padrão BM25+
	   double tf =  countPalavraItemTEC/psum;
	   tf = ( tf * ( _k + 1 ) ) / (tf + (_k * ((1 - _b) + ( _b * (pnumeroPalavrasLinhaTEC / pavgLengthlistaTECResumo)))));
	   double idf = Math.log( (pquantidadeTECs - countPalavraVocab + 0.5) / ( countPalavraVocab + 0.5));
	   return idf * (tf + _delta);
	}

	public String pontuaTexto(String ptexto) {
		return pontuaTexto(ptexto, false);
	}

	public String pontuaTexto(String ptexto, boolean ponderado) {
		portugueseStemmer stemmer = new portugueseStemmer(); 
		ptexto = removerAcentos(ptexto);
		String[] listadepalavras = ptexto.split(" ");
		TECsPontos = new ArrayList<Float[]>();
		TECsPontosDescricao = new ArrayList<String[]>();
		String textopontuado = ptexto+"\n";
		ArrayList<Integer> indicesVocab = new ArrayList<Integer>();
		ArrayList<Integer> indicesVocabstem = new ArrayList<Integer>();
		ArrayList<Integer> indicesVocabBigrama = new ArrayList<Integer>();
		Integer index = -1;
		for (int s=0;s<listadepalavras.length;s++){
			String palavra = listadepalavras[s];
			if (tiposAtivos.contains(Tipo.BIGRAMA)) {
				if(s>0){
					String bigrama = listadepalavras[s-1] + " " + palavra; 
					bigrama = bigrama.toUpperCase();
					index = vocabBigrama.indexOf(bigrama); 
					if (index >=0){
						if (indicesVocabBigrama.indexOf(index) == -1){
							indicesVocabBigrama.add(index);
							textopontuado = textopontuado + bigrama +". Total de ocorrências: "+ Integer.toString(vetorVocabBigrama[index])+"\n";
						}
					}
				}
			}
			stemmer.setCurrent(palavra);
			if (tiposAtivos.contains(Tipo.TEXTO)){
				palavra=palavra.toUpperCase();
				index = vocab.indexOf(palavra);
				if (index >=0){
					if (indicesVocab.indexOf(index) == -1){
						indicesVocab.add(index);
						textopontuado = textopontuado + palavra +". Total de ocorrências: "+ Integer.toString(vetorVocab[index])+"\n";
					}
				}
			}
			if (tiposAtivos.contains(Tipo.STEM)) {
				stemmer.stem();
				palavra = stemmer.getCurrent();
				palavra = palavra.toUpperCase();
				index = vocabstem.indexOf(palavra); 
				if (index >=0){
					if (indicesVocabstem.indexOf(index) == -1){
						indicesVocabstem.add(index);
						textopontuado = textopontuado + palavra +". Total de ocorrências: "+ Integer.toString(vetorVocabstem[index])+"\n";
					}
				}
			}
		}
		for (int r=0;r<listaTECResumo.size();r++){
			Float totaltec = (float) 0;
			ArrayList<Integer> vetorLinhaTECindex = vetoresTECindex.get(r);
			ArrayList<Integer> vetorLinhaTECcount = vetoresTECcount.get(r);
			ArrayList<Integer> vetorLinhaTECstemindex = vetoresTECstemindex.get(r);
			ArrayList<Integer> vetorLinhaTECstemcount = vetoresTECstemcount.get(r);
			ArrayList<Integer> vetorLinhaTECBigramaindex = vetoresTECBigramaindex.get(r);
			ArrayList<Integer> vetorLinhaTECBigramacount = vetoresTECBigramacount.get(r);
			Float[] linha = {(float) 0.0,(float) 0.0};
			String[] linhadescricao = {"", ""};
			double avgLengthlistaTECResumo = numeroPalavrasTECResumo / listaTECResumo.size();
			String linhaTEC = listaTECResumo.get(r);
			String[] arrayLinhaTEC = linhaTEC.split(" ");  
			Integer numeroPalavrasLinhaTEC = arrayLinhaTEC.length; 
			String linhadescricaopontos = "";
			if (tiposAtivos.contains(Tipo.TEXTO)){
				for (int s=0;s<indicesVocab.size();s++){
					short countPalavraVocab = vetorVocab[indicesVocab.get(s)];
					Integer indexLinha = vetorLinhaTECindex.indexOf(indicesVocab.get(s));
					if (indexLinha>=0){
						Double countPalavraItemTEC = (double) vetorLinhaTECcount.get(indexLinha);
						Double valorPalavraItemTEC = (double) 0.0;
						if(ponderado){
							short sum = 0;
							for (Integer i : vetorLinhaTECcount)
								sum += i;
							valorPalavraItemTEC = TF_IDF(countPalavraItemTEC, sum, listaTECResumo.size(), countPalavraVocab, numeroPalavrasLinhaTEC, avgLengthlistaTECResumo);
						} else {
							valorPalavraItemTEC = countPalavraItemTEC;
						}
						linhadescricaopontos = linhadescricaopontos + " Palavra:" + vocab.get(indicesVocab.get(s)) + " Pontos:" + String.format("%.4f" , valorPalavraItemTEC);
						// Dividir o valor pela quantidade de tipos de busca ativos para obter a média
						totaltec = totaltec + valorPalavraItemTEC.floatValue() / tiposAtivos.size();
					}
				}
			}
			if (tiposAtivos.contains(Tipo.STEM)) {
				for (int s=0;s<indicesVocabstem.size();s++){
					short countPalavraVocab = vetorVocabstem[indicesVocabstem.get(s)];
					Integer indexLinha = vetorLinhaTECstemindex.indexOf(indicesVocabstem.get(s));
					if (indexLinha>=0){
					    Double countPalavraItemTEC = (double) vetorLinhaTECstemcount.get(indexLinha);
						Double valorPalavraItemTEC = (double) 0.0;
						if(ponderado){
							short sum = 0;
							for (Integer i : vetorLinhaTECstemcount)
								sum += i;
							valorPalavraItemTEC = TF_IDF(countPalavraItemTEC, sum, listaTECResumo.size(), countPalavraVocab, numeroPalavrasLinhaTEC, avgLengthlistaTECResumo);
						} else {
							valorPalavraItemTEC = countPalavraItemTEC;
						}
						linhadescricaopontos = linhadescricaopontos + " Palavra:" + vocabstem.get(indicesVocabstem.get(s)) + " Pontos:" + String.format("%.4f" , valorPalavraItemTEC);
						// Dividir o valor pela quantidade de tipos de busca ativos para obter a média
						totaltec = totaltec + valorPalavraItemTEC.floatValue() / tiposAtivos.size();
					}
				}
			}
			if (tiposAtivos.contains(Tipo.BIGRAMA)) {
				for (int s=0;s<indicesVocabBigrama.size();s++){
					short countPalavraVocab = vetorVocabBigrama[indicesVocabBigrama.get(s)];
					Integer indexLinha = vetorLinhaTECBigramaindex.indexOf(indicesVocabBigrama.get(s));
					if (indexLinha>=0){
						Double countPalavraItemTEC = (double) vetorLinhaTECBigramacount.get(indexLinha);
						Double valorPalavraItemTEC = (double) 0.0;
						if(ponderado){
							short sum = 0;
							for (Integer i : vetorLinhaTECBigramacount)
								sum += i;
							valorPalavraItemTEC = TF_IDF(countPalavraItemTEC, sum, listaTECResumo.size(), countPalavraVocab, numeroPalavrasLinhaTEC, avgLengthlistaTECResumo);
						} else {
							valorPalavraItemTEC = countPalavraItemTEC;
						}
						linhadescricaopontos = linhadescricaopontos + " Palavra:" + vocabBigrama.get(indicesVocabBigrama.get(s)) + " Pontos:" + String.format("%.4f" , valorPalavraItemTEC);
						// Dividir o valor pela quantidade de tipos de busca ativos para obter a média
						totaltec = totaltec + valorPalavraItemTEC.floatValue() / tiposAtivos.size();
					}
				}
			}
			if (totaltec>0){
				linha[0] = totaltec; // Pontuação
				linha[1] = (float) r; // Índice da linha da TEC
				linhadescricao[0] = linhadescricaopontos;
				linhadescricao[1] = listaTECResumo.get(r).substring(0, 11);
				TECsPontos.add(linha);
				TECsPontosDescricao.add(linhadescricao);
			}
		}
		Collections.sort(TECsPontos, new Comparator<Float[]>() {
			@Override
			public int compare(Float[] arg0, Float[] arg1) {
				// TODO Auto-generated method stub
				try{
					return arg0[0].compareTo(arg1[0]);
				} catch(Exception e ){
					return 0;
				}
			}
		});
		Collections.reverse(TECsPontos);
		return getEstatisticas()+textopontuado;
	}


	public ArrayList<String[]> getListaNCM() {
		return listaNCM;
	}

	public String getPalavrasnoVocabulario(String ptexto) {
		if (vocab.isEmpty()){
			return "Aguarde. Vocabulário sendo montado...";
		}
		ptexto = removerAcentos(ptexto);
		String textopesquisa = ptexto.toUpperCase();
		String[] listadepalavras = textopesquisa.split(" ");
		String[] listadepalavrasOriginaisCapitalizadas = ptexto.split(" ");
		ptexto = "";
		Integer index = 0;
		for (String palavra:listadepalavras){
			if (vocab.indexOf(palavra)!=-1){
				ptexto = ptexto + " " + listadepalavrasOriginaisCapitalizadas[index];
			}
			index+=1;
		}
		return ptexto;
	}
	public Set<Tipo> getTiposAtivos() {
		return tiposAtivos;
	}
	public void setTiposAtivos(Tipo[] ptiposAtivos) {
		tiposAtivos = new HashSet<Tipo>();
		tiposAtivos.addAll(tiposAtivos);
	}

	public void addTipoAtivo(Tipo ptipoAtivo) {
		tiposAtivos.add(ptipoAtivo);
	}

	public void setTipoAtivo(Tipo ptipoAtivo) {
		tiposAtivos.clear();
		tiposAtivos.add(ptipoAtivo);
	}
	public void clearTiposAtivos() {
		tiposAtivos.clear();
	}


}
