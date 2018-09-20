# Iniciação Científica
## Heurísticas Computacionais para Identificação e Redução de Conflitos de Interesse em Bancas de Concurso
### Como usar:
1. Alterar o arquivo **ex.config**, em **SHELL/** para a pasta raiz do processo
2. Criar pastas **IN/** e **OUT/** na raiz.
3. Colocar dentro da pasta **IN/** o conteudo a ser processado, como explicado abaixo:
    - pastas no formato **PQYYYY-curso-exemplo/**
        - arquivo formato **.ris** (saida [ScriptLattes](http://scriptlattes.sourceforge.net))
        - **listaDeRotulos.txt** (classificação dos bolsistas de produtividade, na mesma ordem do arquivo .ris)
        - **matrizDeAdjacencia.txt** (matriz relacionando os candidatos e avaliadores)
4. Executar o script 
    - **./ex.sh [-lr] [k] [-a]**
  
| Parâmetros          |  Ação                                  |
| ------------------- | -------------------------------------  |
|         -l          | limpa pasta LOG/ (função removida)      |
|         -r          | limpa pasta OUT/ (usar com cuidado)     |
|         k           |   número de avaliadores na banca       |
|         -a          | executará para todo valor entre 1 e k (usar com cuidado)| 
