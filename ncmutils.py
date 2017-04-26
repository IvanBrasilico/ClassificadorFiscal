##Formato das variáveis geradas:
##
##listaTEC: list de string com cada linha do arquivo texto
##listaCapitulos: list de list contendo título, descrição, e notas de cada capítulo
##listaNCM: list de list com cada linha contendo o código da linha e após a descrição
##listaTECResumo: list de string contendo o código completo e descrição "completa" de linhas com TEC não nulo
##vocab: dict com key palavra e value contagem de ocorrências
##vetoresTEC, vetorVocab: contagem de ocorrências da palavra por linha e contagem total de ocorrências da palavra na Coleção
##Em termos técnicos, cada linha da lista será um documento da coleção, vocab é o vocabulário da coleção

import os
import nltk
stopwords = nltk.corpus.stopwords.words('portuguese')
import re
from unicodedata import normalize
import numpy as np

def montaTEC():
    arq = open("src/org/classifica/resources/tec.txt")
    listaarquivoTEC = arq.readlines()
    return listaarquivoTEC

def montaCapitulos(plistaTEC):
    listaCapitulos = []
    r = 0
    while(r<len(plistaTEC)):
        linha = plistaTEC[r]
        if(linha[0:3]=="Cap"): #Procura capítulos
            capitulo = linha
            descricao=""
            r=r+1
            linha = plistaTEC[r]
            while (linha[0:4]!="Nota"):
                descricao = descricao + linha + " "
                r=r+1
                linha = plistaTEC[r]
            notas=""
            r=r+1
            linha = plistaTEC[r]
            while (linha[0:4]!="____"):
                notas = notas + linha +"\n"
                r=r+1
                linha = plistaTEC[r]
            listaCapitulos.append([capitulo.strip(), descricao.strip(), notas])
        r=r+1
    return listaCapitulos

def montaNCM(plistaTEC):
    listaNCM = []
    r = 0
    while(r<len(plistaTEC)):
        linha = plistaTEC[r]
        if(len(linha)<12):
            i = linha.find(".")
            if ((i>=2) and (linha[0].isnumeric())):
                r=r+1
                linha2 = plistaTEC[r]
                if (not linha2=="\n"): # Elimina as sequências de números (na parte 5 Regra de Tributação para Produtos do Setor Aeronáutico da TEC)
                    if (not linha2[0].isnumeric()): # Primeiro testa se linha não está vazia
                        ncm = linha                             
                        descricao = linha2
                        linha3 = plistaTEC[r+1]
                        tec = ""
                        if ((not linha3) or (linha3[0].isnumeric())):
                            r=r+1
                            tec = linha3
                        listaNCM.append([ncm.strip(), descricao.strip(), tec.strip()])
        r=r+1
    return listaNCM
    
def remover_acentos(txt):
    return normalize('NFKD', txt).encode('ASCII','ignore').decode('ASCII')

def somente_letras_e_numeros(raw):
    raw = remover_acentos(raw)
    clean = re.sub("[^a-zA-Z0-9]"," ", raw)
    return clean


def montaTECResumo(plistaNCM): # Monta linhas da TEC que contém II com descrição contendo a concatenação da descrição da linha e dos "pais" - posições, subposições, etc.
    listaTECResumo = []
    r = 0
    while (r<len(plistaNCM)):
        linha = plistaNCM[r]
        II = linha[2]
        if (not II==''): # É uma Classificação válida/"escolhível", buscar os "pais"
            codigo = linha[0]
            descricao = linha[1]
            s = r - 1
            while (True): #Loop DNA. Percorre a lista "para cima" procurando a árvore genealógica...
                linha = plistaNCM[s]
                codigo2 = linha[0]
                descricao2 = linha[1]
                lcodigo=codigo[0:2]+"."+codigo[2:4]
                if(lcodigo==codigo2):
                    descricao=descricao+" "+descricao2
                    listaTECResumo.append(codigo+" "+descricao)
                    break
                lcodigo=codigo[0:6]
                if(lcodigo==codigo2):
                    descricao=descricao+" "+descricao2
                lcodigo=codigo[0:7]
                if(lcodigo==codigo2):
                    descricao=descricao+" "+descricao2
                lcodigo=codigo[0:8]
                if(lcodigo==codigo2):
                    descricao=descricao+" "+descricao2
                lcodigo=codigo[0:9]
                if(lcodigo==codigo2):
                    descricao=descricao+" "+descricao2
                s = s - 1
                if ((s==-1) or ((r-s) > 100)): #Exceção encontrada, abortar!
                    listaTECResumo.append(codigo+" "+descricao)
                    break
        r = r + 1
    return listaTECResumo


def montaDictVocabulario(plistaTEC):
    vocab = {} # Percorre todos os subitens NCM COM descrição completa. Cria vocabulário através desta descrição completa
    index = 0
    for linha in (plistaTEC):
        codigo = linha[:10]
        descricao = linha[11:]
        listadepalavras = descricao.split()
        for palavra in listadepalavras:
            if ((len(palavra)>3) and (stopwords.count(palavra)==0)):
                palavra=somente_letras_e_numeros(palavra) # Tira tudo que não for A-B e 0-9
                palavra=palavra.upper()
                if palavra not in vocab:
                    vocab[palavra]=index
                    index+=1
    return vocab

# Percorre todos os subitens NCM COM descrição completa. Cria um dict de vetores para cada item da TEC. Cria um vetor do vocabulário primeiro
def montaVetores(plistaTEC, pvocab):
    vetorVocab = np.zeros(len(pvocab), dtype=np.int16)
    vetoresTEC = {}
    for linha in (plistaTEC):
        codigo = linha[:10]
        descricao = linha[11:]
        listadepalavras = descricao.split()
        tecvector = np.zeros(len(pvocab), dtype=np.int16)
        for palavra in listadepalavras:
            palavra=somente_letras_e_numeros(palavra) # Tira tudo que não for A-B e 0-9
            palavra = palavra.upper()
            if palavra in pvocab:
                index = pvocab[palavra]
                tecvector[index]+=1
                vetorVocab[index]+=1
                
        vetoresTEC[codigo] = tecvector    
    return vetoresTEC, vetorVocab


def pontuaVetores(ptexto, pvocab, pvetoresTEC, vetorVocab, ponderado=False):
###Por eficiência, selecionar somente as colunas com palavras que ocorrem na busca
##Portanto, primeiro converter a lista vetores de TEC em uma Matriz de dimensões
## númerodeTECs x tamanhodoVocabulario
## Depois criar uma matriz somando os valores das colunas do vocabulário da consulta
    matrizVetores = np.asarray(list(pvetoresTEC.values()), dtype=np.int16)
    matrizCodigos = np.asarray(list(pvetoresTEC.keys()))
    matrizSoma = np.zeros(len(pvetoresTEC))
    listadepalavras = ptexto.split()
    explicacao = ""
    for palavra in listadepalavras:
        palavra=somente_letras_e_numeros(palavra) # Tira tudo que não for A-B e 0-9
        palavra = palavra.upper()
        if palavra in pvocab:
            index = pvocab[palavra]
            vetor = matrizVetores[:, index]
            explicacao = explicacao + palavra+' '+str(vetorVocab[index])+' '
            matrizSoma = np.add(matrizSoma, vetor)
    indicesnaozero = np.nonzero(matrizSoma)
    matrizTemp = np.vstack((matrizCodigos[indicesnaozero], matrizSoma[indicesnaozero]))
    indices = matrizTemp[1,:].argsort()
    indices = indices[::-1]
    matrizCodigoePontuacao = matrizTemp[:, indices]
    return matrizCodigoePontuacao, explicacao


def Visualiza():
    listaTEC = montaTEC()
    print("Arquivo RAW")
    print(listaTEC[0:19])
    print(listaTEC[5001:5020])

    listaCapitulos = montaCapitulos(listaTEC)
    print("listaCapitulos")
    print(listaCapitulos[1])
    print(listaCapitulos[50])

    listaNCM = montaNCM(listaTEC)
    print("listaNCM")
    for linha in listaNCM[0:9]:
        print(linha)
    for linha in listaNCM[1001:1010]:
        print(linha)

    print("Montando documentos...")
    listaTECResumo = montaTECResumo(listaNCM)
    print("Total de documentos na coleção:")
    print(len(listaTECResumo))
    print("Primeiras linhas:")
    for linha in listaTECResumo[0:9]:
        print(linha)
    print("Algumas linha aleatórias:")
    from random import randint
    for i in range(1,10):
        print(listaTECResumo[randint(0, len(listaTECResumo))])
    print("Montando vocabulário...")
    vocab = montaDictVocabulario(listaTECResumo)
    print("Total de palavras do Vocabulário da TEC:")
    print(len(vocab))

    print("Montando vetores...")
    vetoresTEC, vetorVocab = montaVetores(listaTECResumo, vocab)
    print("Vetor de vocabulário:")
    print(vetorVocab.shape)
    print("Vetor de documentos:")
    print(len(vetoresTEC))

    todasaspalavrasTEC = str(listaTEC)
    listatodasaspalavrasTEC = todasaspalavrasTEC.split()
    print("Total de palavras da TEC:")
    print(len(listatodasaspalavrasTEC))
    
    todasaspalavrasTECResumo = str(listaTECResumo)
    listatodasaspalavrasTECResumo = todasaspalavrasTECResumo.split()
    print("Total de palavras das linhas da NCM da TEC com descrição completa (Coleção de documentos):")
    print(len(listatodasaspalavrasTECResumo))
    
    print("Média de palavras por linha (Coleção de documentos):")
    print(len(listatodasaspalavrasTECResumo)/len(listaTECResumo))

    print("Número de documentos da coleção:")
    print(len(vetoresTEC))

    print("Estatísticas das ocorrências das palavras no vocabulário:")
    print("Média {}, mínimo {}, máximo {}".format(vetorVocab.mean(), vetorVocab.min(), vetorVocab.max()))

    teste = "T"
    while (teste!=""):
        teste = input("Digite umas palavras-descrição de produto. Digite simplemente Enter para encerrar:")
        matrizPontos, explica = pontuaVetores(teste, vocab, vetoresTEC, vetorVocab)
        print(explica)
        ind = 5
        if (ind > matrizPontos.shape[1]):
            ind = matrizPontos.shape[1]
        print("5 mais:")
        for codigo in (matrizPontos[0, :ind]):
            for linha in (listaTECResumo):
                codigo2 = linha[:10]
                if(codigo2==codigo):
                    print(linha)

    
    

Visualiza()

    







