from PalavrasLaudoUtils import *
from ncmutils import *

##Formato das variáveis (e arquivos) gerados:
##
##listaTEC: list com cada linha contendo o código do subitem e após a descrição completa
##vocab: dict com key palavra e value contagem de ocorrências
##vetoresTEC, vetorVocab: 
##
##Este script possui 5 passos:
##
##1 - Processa as tabelas TEC (Classificações fiscais aprox. 4.000) e TitulosTEC (Posicões, subposições, etc, hierarquicamente acima da TEC)
## e faz uma lista com código TEC e descrição completa da hierarquia
##2 - Pega esta lista, percorre e faz um vetor de vocabulário (eliminando stopwords e palavras com menos de 3 letras) e sua frequência de toda a TEC
##3 - Monta também um vetor de vocabulário para cada código TEC, com as palavras que ocorrem e sua frequência, uma matriz de tamanho TEC x Vocabulário
##4 - Salva tudo, de forma a não precisar processar novamente.
##5 - Recebe uma string, vê posição no vetor de vocabulário, e extrai as colunas da Matriz TEC que correspondem às palavras pesquisadas. Aí basta somar 
##as ocorrências e criar o score das palavras pesquisadas. O score pode ser absoluto ou ponderado TF-IDF (ocorrencias na TEC * (1  raiz quarta do número de ocorrências no vocabulário))
##
##Observações:
##
##- O algoritmo está funcionando bem, MAS a palavra NÃO não entrou no vocabulário
##- A importação da tabela TitulosNCM teve erros, algumas linhas tiveram a última letra cortada (revisar o script Java que importou)
##- Pensar em fazer vocabulário com digrama e ou trigrama também
##- Pensar em um tipo de rede para extrair semântica (sinônimos, etc)
##- Desenvolvidos modelos adicionais, tirar uma média dos pesos



listaTEC = inicializaTEC()
vocab = inicializaVocab(listaTEC)
vetoresTEC, vetorVocab = inicializaVetores(listaTEC, vocab)

print("Amostras e estatísticas do vetor de vocabulário")
print(vetorVocab[1:20])
print("Média {}, mínimo {}, máximo {}".format(vetorVocab.mean(), vetorVocab.min(), vetorVocab.max()))
print("Amostras e estatísticas dos vetores de TEC")
print("Total de TECs {}".format(len(vetoresTEC)))
items = list(vetoresTEC.items())
print(items[0])
values = list(vetoresTEC.values())
print(values[0:3])
M = np.asarray(values)
print(M[0:3, :])
print("Pronto para começar a classificar!")


def printResult():
    ind = 7
    if (ind > matrizPontos.shape[1]):
        ind = matrizPontos.shape[1]
    print("7 mais:")
    print(matrizPontos[:, :ind])
    for codigo in (matrizPontos[0, :ind]):
        #print(listaTEC[codigo]) Fazer dict de listaTEC
        for linha in (listaTEC):
            codigo2 = linha[:10]
            if(codigo2==codigo):
                print(linha)
    


'''
for file in glob.glob('D:/Users/25052288840/Desktop/Laudos/historico/*.pdf'):
    palavraslimpo, _ = read_words(file)
    palavraslimpo = list(set(palavraslimpo))
    palavraspesquisa = ' '.join(palavraslimpo)
    matrizPontos, explica = pontuaVetores(palavraspesquisa, vocab, vetoresTEC, vetorVocab, True)
    print(explica)
    printResult()
'''


teste = "T"

while (teste!=""):
    teste = input("Digite umas palavras-descrição de um laudo ou produto. Digite simplemente Enter para encerrar:")
    matrizPontos, explica = pontuaVetores(teste, vocab, vetoresTEC, vetorVocab)
    print(explica)
    printResult()
    matrizPontos, explica = pontuaVetores(teste, vocab, vetoresTEC, vetorVocab, True)
    printResult()




