Nome do criador: Ivan da Silva Brasílico

******Inclui código do SnowballStemmer******

Sistema de auxílio em Classificação Fiscal

https://medium.com/@brasilico.ivan/utiliza%C3%A7%C3%A3o-das-t%C3%A9cnicas-de-recupera%C3%A7%C3%A3o-e-ranqueamento-de-texto-e-t%C3%A9cnicas-de-ia-para-aux%C3%ADlio-25d047d4fe06

Este sistema utiliza técnicas de busca e processamento em textos para fazer uma pontuação probabilística.

O sistema roda totalmente embutido, podendo ser usado totalmente sem acesso à redes, em quaisquer computadores que possuam o Java instalado.

Foi incorporado ao arquivo do sistema o texto completo da última TEC publicada pelo MDIC. Quando carregado, o sistema realiza alguns pré-processamentos, que consistem em percorrer todo o texto da TEC e montá-lo de diversas formas estruturadas, utilizando técnicas de inteligência artificial e processamento de linguagem (Vetorização de palavras, TF-IDF, Stemização, Bigramas, entre outras).

O Sistema oferece quatro abas para pesquisas. Abaixo descreve-se cada uma.

NCM Completa

Exibe o texto da TEC original, sem pré-processamento.

Botão "Pesquisar Texto" -  pesquisa por palavras ou parte delas ou simplesmente rolar o texto. Importante lembrar que nesta aba a busca é exata, sensível à caixa e a sinais gráficos (ex.:acentos) isto é, "capítulo", "Capítulo", "Capitulo" e "CAPITULO" são buscas diferentes. Na TEC está grafado "Capítulo".

(Esta é a única aba fica disponível para uso imediatamente após o carregamento. As outras abas devem esperar o pré-processamento acabar, o que pode demorar de 1 a 2 minutos dependendo do computador utilizado)

Pesquisar NCM 

Exibe a TEC propriamente dita, hierarquizada, apenas o texto e código das posições e subposições até o nível de subitem com a classificação final e a alítquota de Imposto de Importação.
Botão "Pesquisa Código" -pesquisa por código ou parte dele.


Sugestões

Esta aba é onde realmente ocorre o uso de algoritmos "inteligentes"

Exibe os subitens TEC como um texto "completo", isto é, o texto do subitem adicionado de todas as posições hierarquicamente superiores. Além disso, pontua as palavras deste subitem que coincidem com o texto buscado de acordo com a quantidade de ocorrências no texto. O resultado é ordenado por pontuação obitida, da maior para o menor.

Botão "Pesquisa pontuação" - pesquisa por palavras, pontua, e exibe o resultado na aba "Sugestões". 

Parâmetros: 

Utilizar pesos ponderados TF/IDF (S/N)

Ao invés de contar simplesmente as ocorrências dos termos pesquisados na linha da TEC, considera "pesos" pontuando mais as palavras menos frequentes na TEC e também o peso relativo de uma palavra em cada linha.

Texto puro (S/N)

Procura exatamente a(s) palavra(s) digitada(s). Insensível a caixa e sinais gráficos.

Stemização (S/N)

Procura pelos radicais das palavras. Estampado vira ESTAMP, Tinto vira TINT.

Bigramas (S/N)

Procura exatamente as palavras digitadas. Insensível a caixa e sinais gráficos. Só que procura as palavras em duplas, na ordem digitada. "Outros tecidos", "tecidos de", "de malha", etc.

Explicação

Exibe os termos pesquisados e algumas estatísticas. Permite saber se as palavras digitadas foram encontradas na TEC, e sua frequência. Serve para refinamento e correção de pesquisas. Como exemplo, caso pesquisado "Tylenol", esta aba não vai exibir estatísticas porque a palavra não existe na TEC. Caso seja pesquisado por "Paracetamol", será exibida a informação "PARACETAMOL. Total de ocorrências: 3". Igualmente, a pesquisa por "Estampados" retornará "ESTAMPADOS. Total de ocorrências: 45". Já "Estampado" sem o plural não retornará nada.
