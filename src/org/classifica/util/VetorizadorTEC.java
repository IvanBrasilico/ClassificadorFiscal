package org.classifica.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import org.classifica.entidades.Capitulo;





public class VetorizadorTEC {

	//Neste diret�rio s�o salvos os par�metros e as listas montadas e outras vari�veis
	// Para fazer o sistema gerar novamente as listas � preciso apagar os arquivos
	public final String caminho = System.getProperty("user.home")+"/vetorizador/";

	private String[] listaTECCompleta; //Texto da TEC - TODAS as linhas
	private ArrayList<String[]> listaNCM; // TEC em formato lista hier�rquica - apenas tabela NCM
	private ArrayList<String> listaTECResumo; //Lista de subitens - c�digo e descri��o completa do subitem - apenas tabela NCM
	private Integer numeroPalavrasTECResumo;
	private ArrayList<String> vocab; // Lista de palavras vocabul�rio da TEC Resumo
	private short[] vetorVocab; // Vetor do vocabul�rio
	private ArrayList<ArrayList<Integer>> vetoresTECindex;  // Vetores dos itens da listaTECResumo
	private ArrayList<ArrayList<Integer>> vetoresTECcount;
	private ArrayList<String> vocabstem; // Lista de palavras vocabul�rio
	private short[] vetorVocabstem; // Vetor do vocabul�rio
	private ArrayList<String> vocabBigrama; // Lista de palavras vocabul�rio
	private short[] vetorVocabBigrama; // Vetor do vocabul�rio
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

	//Par�metros BM25+
	private double _delta = 1.0;
	private double _k = 1.2;
	private double _b = 0.6;
	private boolean _normalizado = true;
	private boolean _dicionarizado = false;
	public double get_delta() {
		return _delta;
	}
	public double get_k() {
		return _k;
	}
	public double get_b() {
		return _b;
	}
	public boolean is_normalizado() {
		return _normalizado;
	}
	private ArrayList<Capitulo> listaCapitulos; // Cap�tulos da TEC, descri��o e notas
	private ArrayList<String> listaTECResumoPosicoes;//Lista de posi��es - c�digo e descri��o concatenada de todos os itens da posi��o  - apenas tabela NCM
	private Integer numeroPalavrasTECResumoPosicoes;

	private short[] vetorVocabPosicoes;

	private short[] vetorVocabstemPosicoes;

	private short[] vetorVocabBigramaPosicoes;

	private ArrayList<ArrayList<Integer>> vetoresTECindexPosicoes;

	private ArrayList<ArrayList<Integer>> vetoresTECcountPosicoes;

	private ArrayList<ArrayList<Integer>> vetoresTECstemindexPosicoes;

	private ArrayList<ArrayList<Integer>> vetoresTECstemcountPosicoes;

	private ArrayList<ArrayList<Integer>> vetoresTECBigramaindexPosicoes;

	private ArrayList<ArrayList<Integer>> vetoresTECBigramacountPosicoes;

	private ArrayList<Float[]> TECsPontosPosicao;

	private ArrayList<String[]> TECsPontosDescricaoPosicao;

	private short[] vetorVocabPosicoescountdocs;

	private short[] vetorVocabstemPosicoescountdocs;

	private short[] vetorVocabBigramaPosicoescountdocs;

	private short[] vetorVocabcountdocs;

	private short[] vetorVocabstemcountdocs;

	private short[] vetorVocabBigramacountdocs;

	private TreeMap<String, Integer> dicionario;

	public ArrayList<Capitulo> getListaCapitulos() {
		return listaCapitulos;
	}
	public ArrayList<Float[]> getTECsPontos() {
		return TECsPontos;
	}
	public ArrayList<Float[]> getTECsPontosPosicao() {
		return TECsPontosPosicao;
	}
	public ArrayList<String> getListaTECResumo() {
		return listaTECResumo;
	}
	public ArrayList<String> getListaTECResumoPosicoes() {
		return listaTECResumoPosicoes;
	}


	public VetorizadorTEC(String textotec){
		listaTECCompleta = textotec.split("\n");
		Tipo[] tipos = {Tipo.TEXTO};
		setTiposAtivos(tipos);
		File dir = new File(caminho);
		File fdelta = new File(caminho+"_delta");
		if (!fdelta.exists()){ // Salva valores padr�o dos par�metros
			if(!dir.exists()) dir.mkdir();
			setK(_k);
			setB(_b);
			setDelta(_delta);
			setNormalizado(_normalizado);
			set_dicionarizado(_dicionarizado);
		} else { // L� valores dos par�metros
			_delta = (double) deSerialize(caminho+"_delta");
			_k = (double) deSerialize(caminho+"_k");
			_b = (double) deSerialize(caminho+"_b");
			_normalizado = (boolean) deSerialize(caminho+"_normalizado");
			_dicionarizado = (boolean) deSerialize(caminho+"_dicionarizado");
		}
	}


	public String getEstatisticas(){
		String estatisticas = "";
		estatisticas = estatisticas + "TEC tem "+Integer.toString(listaTECResumo.size())+" subitens e "+numeroPalavrasTECResumo +
				" palavras. "+ "(M�dia de palavras por linha: "+ (numeroPalavrasTECResumo/listaTECResumo.size())+")\n";
		estatisticas = estatisticas + "Posicoes tem "+Integer.toString(listaTECResumoPosicoes.size())+" linhas e "+numeroPalavrasTECResumoPosicoes +
				" palavras. "+ "(M�dia de palavras por linha: "+ (numeroPalavrasTECResumoPosicoes/listaTECResumoPosicoes.size())+")\n";
		estatisticas = estatisticas + "Vocabul�rio b�sico tem "+Integer.toString(vocab.size())+" palavras \n";
		try{
			estatisticas = estatisticas + "Vocabul�rio stemizado tem "+Integer.toString(vocabstem.size())+" palavras \n";
			estatisticas = estatisticas + "Vocabul�rio de bigramas tem "+Integer.toString(vocabBigrama.size())+" palavras \n";
		} catch (Exception e) {

		}
		return estatisticas;
	}

	public void inicializa(){
		int contador = 0;
		System.out.println(new Date());
		System.out.println(contador);//0
		contador++;
		carregaNCM();
		System.out.println(new Date());
		System.out.println(contador);//1
		contador++;
		carregaTECResumo();
		carregaTECResumoPosicoes();
		System.out.println(new Date());
		System.out.println(contador);//2
		contador++;
		montaVocab();
		System.out.println(new Date());
		System.out.println(contador);//3
		contador++;
		montaVetores();//4
		montaVetoresPosicoes();//4
		System.out.println(new Date());
		System.out.println(contador);
		contador++;
		montaDicionario();//5
		System.out.println(new Date());
		System.out.println(contador);
	}


	public static String removerAcentos(String str) {
		str = Normalizer.normalize(str, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
		str = str.replaceAll("[^a-zA-Z0-9]"," ");
		return str;
	}


	protected void carregaNCM() { ///Monta lista NCM hierarquizada
		listaNCM = new ArrayList<String[]>();
		listaCapitulos = new ArrayList<Capitulo>();
		for (int r=0;r<listaTECCompleta.length;r++){
			String linha = listaTECCompleta[r];
			if(linha.indexOf("Cap�tulo ")==0){ ///Procura cap�tulos
				Capitulo capitulo = new Capitulo();
				capitulo.setNome(linha);
				String descricao="";
				r++;
				linha = listaTECCompleta[r];
				while (linha.indexOf("Nota")==-1){
					descricao = descricao + linha + " ";
					r++;
					linha = listaTECCompleta[r];
				}
				capitulo.setDescricao(descricao);
				String notas="";
				r++;
				linha = listaTECCompleta[r];
				while (linha.indexOf("______")==-1){
					notas = notas + linha +"\n";
					r++;
					linha = listaTECCompleta[r];
				}
				capitulo.setNotas(notas);
				listaCapitulos.add(capitulo);
			}
		}
		for (int r=0;r<listaTECCompleta.length;r++){
			String linha = listaTECCompleta[r];
			if(linha.length()<11){ ///Testa se tem at� 10 d�gitos, se � numero e se tem ponto. Se reunir estas condi��es, � prov�vel ser uma entrada v�lida.  
				int i = linha.indexOf(".");
				if ((i>=2) && (Character.isDigit(linha.charAt(0)))){ 
					r=r+1;
					String linha2 = listaTECCompleta[r];
					if ((!linha2.isEmpty()) && (!Character.isDigit(linha2.charAt(0)))){ // Elimina as sequ�ncias de n�meros
						String[] linhacompleta = {"","",""};
						linhacompleta[0] = linha;
						linhacompleta[1] = linha2;
						String linha3 = listaTECCompleta[r+1];
						if ((linha3.isEmpty()) || (Character.isDigit(linha3.charAt(0)))){ // � uma posi��o com IPI (3 colunas)
							r=r+1;
							linhacompleta[2] = linha3;
						}
						listaNCM.add(linhacompleta);
					}
				}
			}
		}
	}


	protected void carregaTECResumo() { // Monta linhas da TEC que cont�m II com descri��o contendo a descri��o dos "pais" - posi��es, subposi��es, etc.
		listaTECResumo = new ArrayList<String>();
		for (int r=0;r<listaNCM.size();r++){
			String codigo = listaNCM.get(r)[0];
			String descricao = listaNCM.get(r)[1];
			String II = listaNCM.get(r)[2];
			if (!II.isEmpty()){ // � uma Classifica��o v�lida, buscar os "pais"
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
					if ((s==0) || ((r-s) > 100)){ // Exce��o encontrada, abortar
						listaTECResumo.add(codigo+" "+descricao);
						break;
					}
				}
			}
		}
	}

	protected void carregaTECResumoPosicoes() { // Monta posi��es com texto completo
		listaTECResumoPosicoes = new ArrayList<String>();
		for (int r=0;r<listaNCM.size();r++){
			String codigo = listaNCM.get(r)[0];
			String descricao = listaNCM.get(r)[1];
			if (codigo.indexOf('.')==2){ // � uma posi��o, buscar os filhos
				while (true){
					r = r+ 1; 
					String codigo2 = listaNCM.get(r)[0];
					String descricao2 = listaNCM.get(r)[1];
					String lcodigo = "";
					lcodigo=codigo2.substring(0, 2)+"."+codigo2.substring(2, 4);
					if(lcodigo.compareTo(codigo)==0){
						descricao=descricao+" "+descricao2;
					} else {
						listaTECResumoPosicoes.add(codigo+" "+descricao);
						//						System.out.println("While..."+codigo+" "+descricao);
						break;
					}
				}
			}
		}
	}




	@SuppressWarnings("unchecked")
	protected void montaVocab() {
		vocab = (ArrayList<String>) deSerialize(caminho+"vocab");
		vocabstem = (ArrayList<String>) deSerialize(caminho+"vocabstem");
		vocabBigrama = (ArrayList<String>) deSerialize(caminho+"vocabBigrama");
		numeroPalavrasTECResumo = (Integer) deSerialize(caminho+"numeroPalavrasTECResumo");
		numeroPalavrasTECResumoPosicoes = (Integer) deSerialize(caminho+"numeroPalavrasTECResumoPosicoes");
		if ((vocab!=null) && (!vocab.isEmpty())){
			return;
		}
		vocab = new ArrayList<String>();
		vocabstem = new ArrayList<String>();
		vocabBigrama = new ArrayList<String>();
		portugueseStemmer stemmer = new portugueseStemmer(); 
		String descricao = "";
		String [] listadepalavras;
		numeroPalavrasTECResumo = 0;
		for (int r=0;r<listaTECResumo.size();r++){
			String linha = listaTECResumo.get(r);
			descricao = removerAcentos(linha);
			listadepalavras = descricao.split(" ");
			for (int s=0;s<listadepalavras.length;s++){
				String palavra = listadepalavras[s];
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
		numeroPalavrasTECResumoPosicoes = 0; // Aproveitar o vocabul�rio de TECResumo para as posi��es (� o mesmo). Gerar apenas nova estat�stica
		for (int r=0;r<listaTECResumoPosicoes.size();r++){
			String linha = listaTECResumoPosicoes.get(r);
			descricao = removerAcentos(linha);
			listadepalavras = descricao.split(" ");
			for (int s=0;s<listadepalavras.length;s++){
				String palavra = listadepalavras[s];
				if (palavra.length()>2) {
					numeroPalavrasTECResumoPosicoes += 1;
				}
			}
		}
		serialize(numeroPalavrasTECResumoPosicoes, caminho+"numeroPalavrasTECResumoPosicoes");
		serialize(numeroPalavrasTECResumo, caminho+"numeroPalavrasTECResumo");
		serialize(vocab, caminho+"vocab");
		serialize(vocabstem, caminho+"vocabstem");
		serialize(vocabBigrama, caminho+"vocabBigrama");
	}

	private void serializeVetor(ArrayList<ArrayList<Integer>> plista, String filename) {
		try{
			FileOutputStream fos= new FileOutputStream(filename);
			ObjectOutputStream oos= new ObjectOutputStream(fos);
			oos.writeObject(plista);
			oos.close();
			fos.close();
		}catch(IOException ioe){
			ioe.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	private ArrayList<ArrayList<Integer>> deSerializeVetor(String filename) {
		ArrayList<ArrayList<Integer>> result = new ArrayList<ArrayList<Integer>>(); 
		try{
			FileInputStream fis= new FileInputStream(filename);
			ObjectInputStream ois= new ObjectInputStream(fis);
			result = (ArrayList<ArrayList<Integer>>) ois.readObject();
			ois.close();
			fis.close();
			return result;
		}catch(IOException | ClassNotFoundException ioe){
			ioe.printStackTrace();
			return null;
		}
	}

	private void serializeVetorShort(short[] plista, String filename) {
		try{
			FileOutputStream fos= new FileOutputStream(filename);
			ObjectOutputStream oos= new ObjectOutputStream(fos);
			oos.writeObject(plista);
			oos.close();
			fos.close();
		}catch(IOException ioe){
			ioe.printStackTrace();
		}
	}


	private short[] deSerializeVetorShort(String filename) {
		short[] result;
		try{
			FileInputStream fis= new FileInputStream(filename);
			ObjectInputStream ois= new ObjectInputStream(fis);
			result = (short[]) ois.readObject();
			ois.close();
			fis.close();
			return result;
		}catch(IOException | ClassNotFoundException ioe){
			ioe.printStackTrace();
			return null;
		}
	}

	protected void montaVetores() {
		portugueseStemmer stemmer = new portugueseStemmer(); 
		String descricao = "";
		String [] listadepalavras;
		Integer index = -1;
		vetorVocab = deSerializeVetorShort(caminho+"vetorVocab");
		vetorVocabstem = deSerializeVetorShort(caminho+"vetorVocabstem");
		vetorVocabBigrama = deSerializeVetorShort(caminho+"vetorVocabBigrama");
		vetorVocabcountdocs = deSerializeVetorShort(caminho+"vetorVocabcountdocs");
		vetorVocabstemcountdocs = deSerializeVetorShort(caminho+"vetorVocabstemcountdocs");
		vetorVocabBigramacountdocs = deSerializeVetorShort(caminho+"vetorVocabBigramacountdocs");
		if ((vetorVocab==null)){
			vetorVocab = new short[vocab.size()];
			vetorVocabstem = new short[vocabstem.size()];
			vetorVocabBigrama = new short[vocabBigrama.size()];
			vetorVocabcountdocs = new short[vocab.size()];
			vetorVocabstemcountdocs = new short[vocabstem.size()];
			vetorVocabBigramacountdocs = new short[vocabBigrama.size()];
			for (int r=0;r<listaTECResumo.size();r++){
				String linha = listaTECResumo.get(r);
				linha = removerAcentos(linha);
				listadepalavras = linha.split(" ");
				int flagBigramacontou = 0; 
				int flagstemcontou = 0; 
				int flagcontou = 0; 
				for (int s=0;s<listadepalavras.length;s++){
					String palavra = listadepalavras[s];
					if(s>0){
						String bigrama = listadepalavras[s-1] + " " + palavra; 
						bigrama = bigrama.toUpperCase();
						index = vocabBigrama.indexOf(bigrama); 
						if (index >=0){
							vetorVocabBigrama[index] = (short) (vetorVocabBigrama[index] + 1);
							if (flagBigramacontou==0){
								vetorVocabBigramacountdocs[index] = (short) (vetorVocabBigramacountdocs[index] + 1);
								flagBigramacontou=1;
							}
						}
					}
					stemmer.setCurrent(palavra);
					palavra = palavra.toUpperCase();
					index = vocab.indexOf(palavra); 
					if (index >=0){
						vetorVocab[index] = (short) (vetorVocab[index] + 1);
						if (flagcontou==0){
							vetorVocabcountdocs[index] = (short) (vetorVocabcountdocs[index] + 1);
							flagcontou=1;
						}
					}
					stemmer.stem();
					palavra = stemmer.getCurrent();
					palavra = palavra.toUpperCase();
					index = vocabstem.indexOf(palavra); 
					if (index >=0){
						vetorVocabstem[index] = (short) (vetorVocabstem[index] + 1);
						if (flagstemcontou==0){
							vetorVocabstemcountdocs[index] = (short) (vetorVocabstemcountdocs[index] + 1);
							flagstemcontou=1;
						}

					}
				}
			}
			serializeVetorShort(vetorVocab, caminho+"vetorVocab");
			serializeVetorShort(vetorVocabstem, caminho+"vetorVocabstem");
			serializeVetorShort(vetorVocabBigrama, caminho+"vetorVocabBigrama");
			serializeVetorShort(vetorVocabcountdocs, caminho+"vetorVocabcountdocs");
			serializeVetorShort(vetorVocabstemcountdocs, caminho+"vetorVocabstemcountdocs");
			serializeVetorShort(vetorVocabBigramacountdocs, caminho+"vetorVocabBigramacountdocs");
		}
		vetoresTECindex = deSerializeVetor(caminho+"vetoresTECindex");
		vetoresTECcount = deSerializeVetor(caminho+"vetoresTECcount");
		vetoresTECstemindex = deSerializeVetor(caminho+"vetoresTECstemindex");
		vetoresTECstemcount = deSerializeVetor(caminho+"vetoresTECstemcount");
		vetoresTECBigramaindex = deSerializeVetor(caminho+"vetoresTECBigramaindex");
		vetoresTECBigramacount = deSerializeVetor(caminho+"vetoresTECBigramacount");
		if ((vetoresTECindex!=null) && (!vetoresTECindex.isEmpty())){
			return;
		}
		vetoresTECindex = new ArrayList<ArrayList<Integer>>();
		vetoresTECcount = new ArrayList<ArrayList<Integer>>();
		vetoresTECstemindex = new ArrayList<ArrayList<Integer>>();
		vetoresTECstemcount = new ArrayList<ArrayList<Integer>>();
		vetoresTECBigramaindex = new ArrayList<ArrayList<Integer>>();
		vetoresTECBigramacount = new ArrayList<ArrayList<Integer>>();
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
		serializeVetor(vetoresTECindex, caminho+"vetoresTECindex");
		serializeVetor(vetoresTECcount, caminho+"vetoresTECcount");
		serializeVetor(vetoresTECstemindex, caminho+"vetoresTECstemindex");
		serializeVetor(vetoresTECstemcount, caminho+"vetoresTECstemcount");
		serializeVetor(vetoresTECBigramaindex, caminho+"vetoresTECBigramaindex");
		serializeVetor(vetoresTECBigramacount, caminho+"vetoresTECBigramacount");
	}

	protected void montaVetoresPosicoes() {
		portugueseStemmer stemmer = new portugueseStemmer(); 
		String descricao = "";
		String [] listadepalavras;
		Integer index = -1;
		vetorVocabPosicoes = deSerializeVetorShort(caminho+"vetorVocabPosicoes");
		vetorVocabstemPosicoes = deSerializeVetorShort(caminho+"vetorVocabstemPosicoes");
		vetorVocabBigramaPosicoes = deSerializeVetorShort(caminho+"vetorVocabBigramaPosicoes");
		vetorVocabPosicoescountdocs = deSerializeVetorShort(caminho+"vetorVocabPosicoescountdocs");
		vetorVocabstemPosicoescountdocs = deSerializeVetorShort(caminho+"vetorVocabstemPosicoescountdocs");
		vetorVocabBigramaPosicoescountdocs = deSerializeVetorShort(caminho+"vetorVocabBigramaPosicoescountdocs");
		if ((vetorVocabPosicoes==null)){
			vetorVocabPosicoes = new short[vocab.size()];
			vetorVocabstemPosicoes = new short[vocabstem.size()];
			vetorVocabBigramaPosicoes = new short[vocabBigrama.size()];
			vetorVocabPosicoescountdocs = new short[vocab.size()];
			vetorVocabstemPosicoescountdocs = new short[vocabstem.size()];
			vetorVocabBigramaPosicoescountdocs = new short[vocabBigrama.size()];
			for (int r=0;r<listaTECResumoPosicoes.size();r++){
				String linha = listaTECResumoPosicoes.get(r);
				linha = removerAcentos(linha);
				listadepalavras = linha.split(" ");
				int flagBigramacontou = 0; 
				int flagstemcontou = 0; 
				int flagcontou = 0; 
				for (int s=0;s<listadepalavras.length;s++){
					String palavra = listadepalavras[s];
					if(s>0){
						String bigrama = listadepalavras[s-1] + " " + palavra; 
						bigrama = bigrama.toUpperCase();
						index = vocabBigrama.indexOf(bigrama); 
						if (index >=0){
							vetorVocabBigramaPosicoes[index] = (short) (vetorVocabBigramaPosicoes[index] + 1);
							if (flagBigramacontou==0){
								vetorVocabBigramaPosicoescountdocs[index] = (short) (vetorVocabBigramaPosicoescountdocs[index] + 1);
								flagBigramacontou=1;
							}
						}
					}
					stemmer.setCurrent(palavra);
					palavra = palavra.toUpperCase();
					index = vocab.indexOf(palavra); 
					if (index >=0){
						vetorVocabPosicoes[index] = (short) (vetorVocabPosicoes[index] + 1);
						if (flagcontou==0){
							vetorVocabPosicoescountdocs[index] = (short) (vetorVocabPosicoescountdocs[index] + 1);
							flagcontou=1;
						}
					}
					stemmer.stem();
					palavra = stemmer.getCurrent();
					palavra = palavra.toUpperCase();
					index = vocabstem.indexOf(palavra); 
					if (index >=0){
						vetorVocabstemPosicoes[index] = (short) (vetorVocabstemPosicoes[index] + 1);
						if (flagstemcontou==0){
							vetorVocabstemPosicoescountdocs[index] = (short) (vetorVocabstemPosicoescountdocs[index] + 1);
							flagstemcontou=1;
						}
					}
				}
			}
			serializeVetorShort(vetorVocabPosicoes, caminho+"vetorVocabPosicoes");
			serializeVetorShort(vetorVocabstemPosicoes, caminho+"vetorVocabstemPosicoes");
			serializeVetorShort(vetorVocabBigramaPosicoes, caminho+"vetorVocabBigramaPosicoes");
			serializeVetorShort(vetorVocabPosicoescountdocs, caminho+"vetorVocabPosicoescountdocs");
			serializeVetorShort(vetorVocabstemPosicoescountdocs, caminho+"vetorVocabstemPosicoescountdocs");
			serializeVetorShort(vetorVocabBigramaPosicoescountdocs, caminho+"vetorVocabBigramaPosicoescountdocs");
		}
		vetoresTECindexPosicoes = deSerializeVetor(caminho+"vetoresTECindexPosicoes");
		vetoresTECcountPosicoes = deSerializeVetor(caminho+"vetoresTECcountPosicoes");
		vetoresTECstemindexPosicoes = deSerializeVetor(caminho+"vetoresTECstemindexPosicoes");
		vetoresTECstemcountPosicoes = deSerializeVetor(caminho+"vetoresTECstemcountPosicoes");
		vetoresTECBigramaindexPosicoes = deSerializeVetor(caminho+"vetoresTECBigramaindexPosicoes");
		vetoresTECBigramacountPosicoes = deSerializeVetor(caminho+"vetoresTECBigramacountPosicoes");
		if ((vetoresTECindexPosicoes!=null) && (!vetoresTECindexPosicoes.isEmpty())){
			return;
		}
		vetoresTECindexPosicoes = new ArrayList<ArrayList<Integer>>();
		vetoresTECcountPosicoes = new ArrayList<ArrayList<Integer>>();
		vetoresTECstemindexPosicoes = new ArrayList<ArrayList<Integer>>();
		vetoresTECstemcountPosicoes = new ArrayList<ArrayList<Integer>>();
		vetoresTECBigramaindexPosicoes = new ArrayList<ArrayList<Integer>>();
		vetoresTECBigramacountPosicoes = new ArrayList<ArrayList<Integer>>();
		for (String linha:listaTECResumoPosicoes){
			ArrayList<Integer> tecvectorindex = new ArrayList<Integer>(); 
			ArrayList<Integer> tecvectorcount = new ArrayList<Integer>(); 
			ArrayList<Integer> tecvectorstemindex = new ArrayList<Integer>(); 
			ArrayList<Integer> tecvectorstemcount = new ArrayList<Integer>(); 
			ArrayList<Integer> tecvectorbigramaindex = new ArrayList<Integer>(); 
			ArrayList<Integer> tecvectorbigramacount = new ArrayList<Integer>(); 
			descricao = linha.substring(5);
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
			vetoresTECindexPosicoes.add(tecvectorindex);
			vetoresTECcountPosicoes.add(tecvectorcount);
			vetoresTECstemindexPosicoes.add(tecvectorstemindex);
			vetoresTECstemcountPosicoes.add(tecvectorstemcount);
			vetoresTECBigramaindexPosicoes.add(tecvectorbigramaindex);
			vetoresTECBigramacountPosicoes.add(tecvectorbigramacount);
		}
		serializeVetor(vetoresTECindexPosicoes, caminho+"vetoresTECindexPosicoes");
		serializeVetor(vetoresTECcountPosicoes, caminho+"vetoresTECcountPosicoes");
		serializeVetor(vetoresTECstemindexPosicoes, caminho+"vetoresTECstemindexPosicoes");
		serializeVetor(vetoresTECstemcountPosicoes, caminho+"vetoresTECstemcountPosicoes");
		serializeVetor(vetoresTECBigramaindexPosicoes, caminho+"vetoresTECBigramaindexPosicoes");
		serializeVetor(vetoresTECBigramacountPosicoes, caminho+"vetoresTECBigramacountPosicoes");
	}


	public double TF_IDF(Double countPalavraItemTEC, short psum, Integer pquantidadeTECs, short countPalavraVocab,
			Integer pnumeroPalavrasLinhaTEC, double pavgLengthlistaTECResumo) {
		/// Pontua��o padr�o BM25+
		double tf =  countPalavraItemTEC/psum;
		tf = ( tf * ( _k + 1 ) ) / (tf + (_k * ((1 - _b) + ( _b * (pnumeroPalavrasLinhaTEC / pavgLengthlistaTECResumo)))));
		double idf = Math.log( (pquantidadeTECs - countPalavraVocab + 0.5) / ( countPalavraVocab + 0.5));
		return idf * (tf + _delta);
	}

	public String pontuaTexto(String ptexto) {
		return pontuaTexto(ptexto, true);
	}

	public String pontuaTexto(String ptexto, boolean ponderado) {
		portugueseStemmer stemmer = new portugueseStemmer(); 
		ptexto = removerAcentos(ptexto);
		String[] listadepalavras = ptexto.split(" ");
		TECsPontos = new ArrayList<Float[]>();
		TECsPontosDescricao = new ArrayList<String[]>();
		TECsPontosPosicao = new ArrayList<Float[]>();
		TECsPontosDescricaoPosicao = new ArrayList<String[]>();
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
							textopontuado = textopontuado + bigrama +". Total de ocorr�ncias: "+ Integer.toString(vetorVocabBigrama[index])+"\n";
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
						textopontuado = textopontuado + palavra +". Total de ocorr�ncias: "+ Integer.toString(vetorVocab[index])+"\n";
					}
				} else { // Procura palavras tamb�m no dicion�rio. Se houver, adiciona termo equilavente no indiceVocab
					if (is_dicionarizado()){
						index = dicionario.get(palavra);
						if (index!=null){
							//Dicionario dict = dicionario.get(index); 
							//indicesVocab.add(dict.getIndicevocab());
							indicesVocab.add(index);
							System.out.println(palavra);
							textopontuado = textopontuado + palavra +". Total de ocorr�ncias: "+ Integer.toString(vetorVocab[index])+"\n";
						}
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
						textopontuado = textopontuado + palavra +". Total de ocorr�ncias: "+ Integer.toString(vetorVocabstem[index])+"\n";
					}
				}
			}
		}
		for (int cont=1;cont<=2;cont++){
			ArrayList<String> listaTEC;
			double avgLengthlistaTEC;
			ArrayList<ArrayList<Integer>> lvetoresTECindex;
			ArrayList<ArrayList<Integer>> lvetoresTECcount;
			ArrayList<ArrayList<Integer>> lvetoresTECstemindex;
			ArrayList<ArrayList<Integer>> lvetoresTECstemcount;
			ArrayList<ArrayList<Integer>> lvetoresTECBigramaindex;
			ArrayList<ArrayList<Integer>> lvetoresTECBigramacount;
			short[] lvetorVocabcount;
			short[] lvetorVocabcountBigrama;
			short[] lvetorVocabcountstem;
			if(cont==1){
				avgLengthlistaTEC = numeroPalavrasTECResumo / listaTECResumo.size();
				listaTEC = listaTECResumo;
				lvetoresTECindex = vetoresTECindex;
				lvetoresTECcount = vetoresTECcount;
				lvetoresTECstemindex = vetoresTECstemindex;
				lvetoresTECstemcount = vetoresTECstemcount;
				lvetoresTECBigramaindex = vetoresTECBigramaindex;
				lvetoresTECBigramacount = vetoresTECBigramacount;
				lvetorVocabcount = vetorVocabcountdocs;
				lvetorVocabcountBigrama = vetorVocabBigramacountdocs;
				lvetorVocabcountstem = vetorVocabstemcountdocs;
			} else {
				avgLengthlistaTEC = numeroPalavrasTECResumoPosicoes / listaTECResumoPosicoes.size();
				listaTEC = listaTECResumoPosicoes;
				lvetoresTECindex = vetoresTECindexPosicoes;
				lvetoresTECcount = vetoresTECcountPosicoes;
				lvetoresTECstemindex = vetoresTECstemindexPosicoes;
				lvetoresTECstemcount = vetoresTECstemcountPosicoes;
				lvetoresTECBigramaindex = vetoresTECBigramaindexPosicoes;
				lvetoresTECBigramacount = vetoresTECBigramacountPosicoes;
				lvetorVocabcount = vetorVocabPosicoescountdocs;
				lvetorVocabcountBigrama = vetorVocabBigramaPosicoescountdocs;
				lvetorVocabcountstem = vetorVocabstemPosicoescountdocs;
			}
			for (int r=0;r<listaTEC.size();r++){
				Float totaltec = (float) 0;
				ArrayList<Integer> vetorLinhaTECindex = lvetoresTECindex.get(r);
				ArrayList<Integer> vetorLinhaTECcount = lvetoresTECcount.get(r);
				ArrayList<Integer> vetorLinhaTECstemindex = lvetoresTECstemindex.get(r);
				ArrayList<Integer> vetorLinhaTECstemcount = lvetoresTECstemcount.get(r);
				ArrayList<Integer> vetorLinhaTECBigramaindex = lvetoresTECBigramaindex.get(r);
				ArrayList<Integer> vetorLinhaTECBigramacount = lvetoresTECBigramacount.get(r);
				Float[] linha = {(float) 0.0,(float) 0.0};
				String[] linhadescricao = {"", ""};
				String linhaTEC = listaTEC.get(r);
				String[] arrayLinhaTEC = linhaTEC.split(" ");  
				Integer numeroPalavrasLinhaTEC = arrayLinhaTEC.length; 
				String linhadescricaopontos = "";
				if (tiposAtivos.contains(Tipo.TEXTO)){
					for (int s=0;s<indicesVocab.size();s++){
						short countPalavraVocab = lvetorVocabcount[indicesVocab.get(s)];
						Integer indexLinha = vetorLinhaTECindex.indexOf(indicesVocab.get(s));
						if (indexLinha>=0){
							Double countPalavraItemTEC = (double) vetorLinhaTECcount.get(indexLinha);
							Double valorPalavraItemTEC = (double) 0.0;
							if(ponderado){
								short sum = 0;
								for (Integer i : vetorLinhaTECcount)
									sum += i;
								valorPalavraItemTEC = TF_IDF(countPalavraItemTEC, sum, listaTEC.size(), countPalavraVocab, numeroPalavrasLinhaTEC, avgLengthlistaTEC);
							} else {
								valorPalavraItemTEC = countPalavraItemTEC;
							}
							linhadescricaopontos = linhadescricaopontos + " Palavra:" + vocab.get(indicesVocab.get(s)) + " Pontos:" + String.format("%.4f" , valorPalavraItemTEC);
							// Dividir o valor pela quantidade de tipos de busca ativos para obter a m�dia
							totaltec = totaltec + valorPalavraItemTEC.floatValue() / tiposAtivos.size();
						}
					}
				}
				if (tiposAtivos.contains(Tipo.STEM)) {
					for (int s=0;s<indicesVocabstem.size();s++){
						short countPalavraVocab = lvetorVocabcountstem[indicesVocabstem.get(s)];
						Integer indexLinha = vetorLinhaTECstemindex.indexOf(indicesVocabstem.get(s));
						if (indexLinha>=0){
							Double countPalavraItemTEC = (double) vetorLinhaTECstemcount.get(indexLinha);
							Double valorPalavraItemTEC = (double) 0.0;
							if(ponderado){
								short sum = 0;
								for (Integer i : vetorLinhaTECstemcount)
									sum += i;
								valorPalavraItemTEC = TF_IDF(countPalavraItemTEC, sum, listaTEC.size(), countPalavraVocab, numeroPalavrasLinhaTEC, avgLengthlistaTEC);
							} else {
								valorPalavraItemTEC = countPalavraItemTEC;
							}
							linhadescricaopontos = linhadescricaopontos + " Palavra:" + vocabstem.get(indicesVocabstem.get(s)) + " Pontos:" + String.format("%.4f" , valorPalavraItemTEC);
							// Dividir o valor pela quantidade de tipos de busca ativos para obter a m�dia
							totaltec = totaltec + valorPalavraItemTEC.floatValue() / tiposAtivos.size();
						}
					}
				}
				if (tiposAtivos.contains(Tipo.BIGRAMA)) {
					for (int s=0;s<indicesVocabBigrama.size();s++){
						short countPalavraVocab = lvetorVocabcountBigrama[indicesVocabBigrama.get(s)];
						Integer indexLinha = vetorLinhaTECBigramaindex.indexOf(indicesVocabBigrama.get(s));
						if (indexLinha>=0){
							Double countPalavraItemTEC = (double) vetorLinhaTECBigramacount.get(indexLinha);
							Double valorPalavraItemTEC = (double) 0.0;
							if(ponderado){
								short sum = 0;
								for (Integer i : vetorLinhaTECBigramacount)
									sum += i;
								valorPalavraItemTEC = TF_IDF(countPalavraItemTEC, sum, listaTEC.size(), countPalavraVocab, numeroPalavrasLinhaTEC, avgLengthlistaTEC);
							} else {
								valorPalavraItemTEC = countPalavraItemTEC;
							}
							linhadescricaopontos = linhadescricaopontos + " Palavra:" + vocabBigrama.get(indicesVocabBigrama.get(s)) + " Pontos:" + String.format("%.4f" , valorPalavraItemTEC);
							// Dividir o valor pela quantidade de tipos de busca ativos para obter a m�dia
							totaltec = totaltec + valorPalavraItemTEC.floatValue() / tiposAtivos.size();
						}
					}
				}
				if (totaltec>0){
					linha[0] = totaltec; // Pontua��o
					linha[1] = (float) r; // �ndice da linha da TEC
					linhadescricao[0] = linhadescricaopontos;
					if (cont==1){
						linhadescricao[1] = listaTEC.get(r).substring(0, 11);
						TECsPontos.add(linha);
						TECsPontosDescricao.add(linhadescricao);
					} else {
						linhadescricao[1] = listaTEC.get(r).substring(0, 5);
						TECsPontosPosicao.add(linha);
						TECsPontosDescricaoPosicao.add(linhadescricao);
					}
				}
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
		Collections.sort(TECsPontosPosicao, new Comparator<Float[]>() {
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
		Collections.reverse(TECsPontosPosicao);
		if (_normalizado){
			normalizar();
		}
		return getEstatisticas()+textopontuado;
	}
	public void normalizar() {
		if (TECsPontos.size() > 0){
			float razao = TECsPontos.get(0)[0];
			for (int r=0;r<TECsPontos.size();r++){
				Float[] posicao =TECsPontos.get(r);
				posicao[0] = posicao[0]/razao; 
				TECsPontos.set(r, posicao);
			}
		}
		if (TECsPontosPosicao.size() > 0){
			float razao = TECsPontosPosicao.get(0)[0];
			for (int r=0;r<TECsPontosPosicao.size();r++){
				Float[] posicao =TECsPontosPosicao.get(r);
				posicao[0] = posicao[0]/razao; 
				TECsPontosPosicao.set(r, posicao);
			}
		}
	}
	/*####################################################################################
	Dicion�rio de sin�nimos utilizado: http://www.nilc.icmc.usp.br/tep2/
	O uso e redistribui��o deste dicion�rio DEVE seguir os termos de licenciamento dos autores originais
	Este m�todo procura palavras do vocabul�rio TEC no dicion�rio e, somente no caso desta palavra estar no dicion�rio, adiciona seus sin�nimos ao dicion�rio
	Depois, caso a pessoa busque por uma palavra que n�o est� no vocabul�rio TEC mas cujo sin�nimo est� no dicion�rio, o dicion�rio pode substitu�-la pela palavra que est�   
	 */
	@SuppressWarnings("unchecked")
	private void montaDicionario(){ 
		dicionario = (TreeMap<String, Integer>) deSerialize(caminho+"listadicionario");
		if ((dicionario==null)){
			SortedMap<String, Integer> vocabordenado = new TreeMap<String, Integer>();
			for (Integer r=0;r<vocab.size();r++){
				vocabordenado.put(vocab.get(r), r);
			}
			dicionario = new TreeMap<String, Integer>();
			InputStream is = VetorizadorTEC.class.getResourceAsStream("/org/classifica/resources/base_tep2.txt");
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			try {
				String linhaTEP2 = br.readLine();
				//int cont=0;
				while (linhaTEP2!=null){
					int inicio = linhaTEP2.indexOf("{");
					int fim = linhaTEP2.indexOf("}");
					if ((fim!=-1) && (inicio!=-1)){
						linhaTEP2 = linhaTEP2.substring(inicio+1, fim);
					}
					String[] palavras = linhaTEP2.split(", ");
					for (String palavra:palavras){
						palavra = removerAcentos(palavra);
						palavra = palavra.toUpperCase();
						Integer i = vocabordenado.get(palavra);
						if (i!=null){
							//System.out.println(palavra);
							String sinonimos = ""; 
							for (String sinonimo:palavras){
								sinonimo = removerAcentos(sinonimo);
								sinonimo = sinonimo.toUpperCase();
								/*Dicionario dict = new Dicionario();
								dict.setIndicevocab(i);
								dict.setPalavra(sinonimo);*/
								dicionario.put(sinonimo, i);
								sinonimos = sinonimos + sinonimo+" ";
							}
							System.out.println(sinonimos);
						}
					}
					linhaTEP2 = br.readLine();
					/*cont++;
					if ((cont % 1000) == 0){
						System.out.println(cont+" linhas...");
					}*/
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			serialize(dicionario, caminho+"listadicionario");
		}
	}

	public ArrayList<String[]> getListaNCM() {
		return listaNCM;
	}

	public String getPalavrasnoVocabulario(String ptexto) {
		if (vocab.isEmpty()){
			return "Aguarde. Vocabul�rio sendo montado...";
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
	public void setK(double k) {
		this._k = k;
		serialize(_k, caminho+"_k");
	}
	public void setB(double b) {
		this._b = b;
		serialize(_b, caminho+"_b");
	}
	public void setDelta(double delta) {
		this._delta = delta;
		serialize(_delta, caminho+"_delta");
	}
	public void exportaTECResumoARFF(String filename) {
		try{
			BufferedWriter bw = new BufferedWriter(new FileWriter(filename));
			bw.write("@RELATION tecresumo");
			bw.newLine();
			bw.newLine();
			System.out.println("inicio");
			for(String palavra:vocabstem){
				bw.write("@ATTRIBUTE "+palavra+" NUMERIC");
				bw.newLine();
			}
			bw.write("@ATTRIBUTE class NUMERIC");
			bw.newLine();
			bw.newLine();
			bw.flush();
			System.out.println("atributos");
			bw.write("@DATA");
			for (int r=0;r<listaTECResumo.size();r++){
				String linha = "";
				for(int s=0;s<vocabstem.size();s++){
					if(vetoresTECstemindex.indexOf(s)==-1){
						linha=linha+"0,";
					} else {
						linha=linha+"1,";
					}
				}
				linha = linha + listaTECResumo.get(r).substring(0, 2);
				bw.write(linha);
				bw.newLine();
				System.out.println(r);
			}
			bw.flush();
			bw.close();
		}catch(IOException ioe){
			ioe.printStackTrace();
		}


	}
	public void setNormalizado(boolean pnormalizado) {
		_normalizado = pnormalizado;
		serialize(_normalizado, caminho+"_normalizado");
	}
	public boolean is_dicionarizado() {
		return _dicionarizado;
	}
	public void set_dicionarizado(boolean _dicionarizado) {
		this._dicionarizado = _dicionarizado;
		serialize(_dicionarizado, caminho+"_dicionarizado");
	}


	public void serialize(Object pobject, String filename) {
		try{
			FileOutputStream fos= new FileOutputStream(filename);
			ObjectOutputStream oos= new ObjectOutputStream(fos);
			oos.writeObject(pobject);
			oos.close();
			fos.close();
		}catch(IOException ioe){
			ioe.printStackTrace();
		}
	}

	public Object deSerialize(String filename) {
		Object result = null; 
		try{
			FileInputStream fis= new FileInputStream(filename);
			ObjectInputStream ois= new ObjectInputStream(fis);
			result = ois.readObject();
			ois.close();
			fis.close();
			return result;
		}catch(IOException | ClassNotFoundException ioe){
			ioe.printStackTrace();
			return result;
		}
	}
	/*
		private <T> Object deSerialize(Class<T> tipo, String filename) {
			Object result = null;
			return result;
	}

	private void serializeBoolean(boolean pnormalizado, String filename) {
		try{
			FileOutputStream fos= new FileOutputStream(filename);
			ObjectOutputStream oos= new ObjectOutputStream(fos);
			oos.writeObject(pnormalizado);
			oos.close();
			fos.close();
		}catch(IOException ioe){
			ioe.printStackTrace();
		}
	}

	private boolean deSerializeBoolean(String filename) {
		Boolean result = true; 
		try{
			FileInputStream fis= new FileInputStream(filename);
			ObjectInputStream ois= new ObjectInputStream(fis);
			result = (Boolean) ois.readObject();
			ois.close();
			fis.close();
			return result;
		}catch(IOException | ClassNotFoundException ioe){
			ioe.printStackTrace();
			return result;
		}
	}
	public void serializeListaPaises(List<Pais> paises, String filename) {
		try{
			FileOutputStream fos= new FileOutputStream(filename);
			ObjectOutputStream oos= new ObjectOutputStream(fos);
			oos.writeObject(paises);
			oos.close();
			fos.close();
		}catch(IOException ioe){
			ioe.printStackTrace();
		}
	}
	@SuppressWarnings("unchecked")
	public ArrayList<Pais> deSerializeListaPaises(String filename) {
		ArrayList<Pais> result = new ArrayList<Pais>(); 
		try{
			FileInputStream fis= new FileInputStream(filename);
			ObjectInputStream ois= new ObjectInputStream(fis);
			result = (ArrayList<Pais>) ois.readObject();
			ois.close();
			fis.close();
			return result;
		}catch(IOException | ClassNotFoundException ioe){
			ioe.printStackTrace();
			return null;
		}
	}


	private void serializeDouble(Double pnum, String filename) {
		try{
			FileOutputStream fos= new FileOutputStream(filename);
			ObjectOutputStream oos= new ObjectOutputStream(fos);
			oos.writeObject(pnum);
			oos.close();
			fos.close();
		}catch(IOException ioe){
			ioe.printStackTrace();
		}
	}
	private void serializeInt(Integer pnum, String filename) {
		try{
			FileOutputStream fos= new FileOutputStream(filename);
			ObjectOutputStream oos= new ObjectOutputStream(fos);
			oos.writeObject(pnum);
			oos.close();
			fos.close();
		}catch(IOException ioe){
			ioe.printStackTrace();
		}
	}

	private Integer deSerializeInt(String filename) {
		Integer result = 0; 
		try{
			FileInputStream fis= new FileInputStream(filename);
			ObjectInputStream ois= new ObjectInputStream(fis);
			result = (Integer) ois.readObject();
			ois.close();
			fis.close();
			return result;
		}catch(IOException | ClassNotFoundException ioe){
			ioe.printStackTrace();
			return null;
		}
	}

	private Double deSerializeDouble(String filename) {
		Double result = 0.0; 
		try{
			FileInputStream fis= new FileInputStream(filename);
			ObjectInputStream ois= new ObjectInputStream(fis);
			result = (Double) ois.readObject();
			ois.close();
			fis.close();
			return result;
		}catch(IOException | ClassNotFoundException ioe){
			ioe.printStackTrace();
			return result;
		}
	}
	@SuppressWarnings("unchecked")
	private ArrayList<String> deSerializeArrayListString(String filename) {
		return (ArrayList<String>) deSerialize(filename);
	}
	 */


}