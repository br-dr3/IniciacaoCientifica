#!/bin/bash

# -------------------------------------------------------- Variaveis de Ambiente
WORK=`cat ex.config | grep DIR_WORK | cut -d '=' -f 2`
OUT=$WORK/OUT
IN=$WORK/IN
JAVA=$WORK/JAVA
SHELL=$WORK/SHELL
LOG=$WORK/LOG

loop=0

printLog ()
{
    echo $(date +%d/%m/%Y-%H:%M:%S)" - "$1
}

helpp ()
{
    echo "Parametros informados incorretos"
    echo "Numero de parametros = 2"
    echo "Exemplo de uso: "
    echo "./ex.sh -[lr] [valor de k] -a"
    echo "-l -> Limpa Log"
    echo "-r -> Limpa saída antiga da pasta OUT"
    echo "k  -> numero de membros na banca"
    echo "-a -> para todo numero menor ou igual a k"
}

# ------------------------------------------------------------------------------
# ---------------------------------------------------- Tratamento dos parametros
if [ $# -eq 1 ] && [ $1 == '-h' ]
then
    helpp
    exit 0
elif [ $# -eq 2 ] && [ $1 == '-r' ] && [ "$2" -eq "$2" ]
then

    k=$2

    if [ "$(ls -A $OUT)" ]
    then
        printLog "Limpando conteudo antigo na pasta de saida. (-r)"
        rm $OUT/* -r
    fi

    if [ $3 == '-a' ]
    then
        let "loop+=1"
    fi

elif [ $# -ge 2 ] && [ $1 == '-l' ] && [ "$2" -eq "$2" ]
then

    k=$2

    printLog "Limpando conteudo antigo na pasta de log. (-l)"
    rm $LOG/* -f

    if [ $3 == '-a' ]
    then
        let "loop+=1"
    fi

elif [ $# -ge 2 ] && ([ $1 == '-rl' ] || [ $1 == '-lr' ]) && [ "$2" -eq "$2" ]
then

    k=$2

    if [ "$(ls -A $OUT)" ]
    then

        printLog "Limpando conteudo antigo na pasta de saida. (-r)"
        rm $OUT/* -r
        printLog "Limpando conteudo antigo na pasta de log. (-l)"
        rm $LOG/*

    fi

    if [ $3 == '-a' ]
    then
        let "loop+=1"
    fi
elif [ $# -eq 1 ] && [ "$1" -ne "$1" ]
then
    k=$1
    printLog "Erro ao processar parametros ./ex.sh -[rl] [valor de k] [-a]"
    printLog "Finalizado com erro."
    exit -1
else
    helpp
    exit -1
fi
# ------------------------------------------------------------------------------
# ---------------------------------------------------------------------- Funcoes


compileJava()
{
    javac $1/*.java
}

runLabels ()
{
    cd $JAVA
    nohup java slPickAxe $1-membros.ris $2-listaDeRotulos.txt $3 2> /dev/null
    cd - > /dev/null
}

makeOutDirectories ()
{
    flag=0
    for f in $1/*
    do
        name=`echo $f | rev | cut -d '/' -f 1 | rev`

        if [ -d $2/$name ]
        then
            printLog "Diretorio ja processado."
        else
            flag=1
            mkdir $2/$name
            printLog "Processando pasta = "$name"."
            runLabels $f/$name $f/$name $2/$name/$name-rotulos $name
            cat $f/$name-matrizDeAdjacencia.txt | sed s@'\.'@','@g > $OUT/$name/$name-matrizDeAdjacencia.txt
        fi
    done

    if [ $flag -eq 1 ]
    then
        printLog "Dormindo por 2 segundos."
        sleep 2
        mv $JAVA/nohup.out $LOG/ex_all_$date.log
    fi
}

runGroups ()
{
    for f in $1/*
    do
        name=`echo $f | rev | cut -d '/' -f 1 | rev`
        cd $JAVA
        java -Xms2G -Xmx4G CalculateGroups $f/$name-matrizDeAdjacencia.txt $f/$name-rotulos.txt $f/$name $2 2> /dev/null
        cd - > /dev/null
    done

    printLog "Dormindo por 2 segundos."
    sleep 2
    #mv $JAVA/nohup.out $LOG/ex_groups_$date.log
}

runAllGroups ()
{
    for f in $1/*
    do
        c=1
        name=`echo $f | rev | cut -d '/' -f 1 | rev`
        cd $JAVA
        while [ $c -le $2 ]
        do
            { time java -Xms2G -Xmx4G CalculateGroups $f/$name-matrizDeAdjacencia.txt $f/$name-rotulos.txt "$f/$name_$c" $c 2> /dev/null ; } 2> $f/time_$c.time
            let "c++"
        done
        cd - > /dev/null
    done

    printLog "Dormindo por 5 segundos."
    sleep 5
    #mv $JAVA/nohup.out $LOG/ex_groups_$date.log
}
# ------------------------------------------------------------------------------
# ------------------------------------------------------------------------- Main
printLog "Compilando classes Java."
compileJava $JAVA
ret=$?
if [ $ret -ne 0 ]
then
    printLog "Erro ao compilar as classes Java."
    printLog "Finalizado com erro."
    exit -1
fi

printLog "Criando diretorios."
makeOutDirectories $IN $OUT

printLog "Executando contagem de grupos."
if [ $loop -eq 0 ]
then
    echo $loop
    runGroups $OUT $k
else
    echo $loop
    runAllGroups $OUT $k

    printLog "Executando analise"
    cd $WORK/SHELL
    #./an.sh $k
    cd - > /dev/null

fi

printLog "Finalizado com sucesso."
exit 0
# ------------------------------------------------------------------------------
