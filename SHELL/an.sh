#!/bin/bash

# -------------------------------------------------------- Variaveis de Ambiente

WORK=`cat ex.config | grep DIR_WORK | cut -d '=' -f 2`
WORK_OUT=$WORK/OUT
DIR_OUT=""

# -------------------------------------------------------- Variaveis de Ambiente

printLog ()
{
    echo "[" `date +%d/%m/%Y" "%H:%M:%S` "] -" $1
}

printFile ()
{
    echo $1 >> $2
}

# --------------------------------------------------------- Criacao de Diretorio
createDir ()
{
    cd $WORK
    DIR=`pwd`
    if [ ! -d "$DIR"/ANALYSIS ]
    then
        printLog "Criando Diretorio de Analise"
        printLog "$DIR/ANALYSIS"

        mkdir ANALYSIS
        cd ANALYSIS
        DIR_OUT=`pwd`
        cd -

        printLog "Diretorio Criado"

    else
        cd ANALYSIS
        DIR_OUT=`pwd`
        printLog "Diretorio ja existente"
    fi

    cd $WORK/SHELL
}
# --------------------------------------------------------- Criacao de Diretorio
# ---------------------------------------------------------------------- Funcoes
Analyse ()
{
    cd $WORK_OUT
    printFile "Área;#Avaliadores;#Av Elegiveis;#Av Isolados;#Regioes" $DIR_OUT/table.csv

    str="Área;"

    for (( i=2; i<=$1; i++ ))
    do
        str=$str"$i isolados/total;t$i;"
    done

    printFile "$str" $DIR_OUT/table2.csv

    for f in `pwd`/*
    do
        echo
        printLog "Analisando pasta $f"

        areaName=`echo $f | sed s/"_"/" "/g | cut -d - -f 2`
        printLog "Nome da area: $areaName"

        nAv=`cat $f/1.ava | grep "Avaliadores =" | sed s/" "/""/g | cut -d "=" -f 2`
        printLog "Numero de Avaliadores: $nAv"

        avEleg=`cat $f/1.ele | grep "Elegiveis =" | sed s/" "/""/g | cut -d "=" -f 2`
        printLog "Avaliadores Elegiveis: $avEleg"

        avIns=`cat $f/1.ins | grep "Isolados =" | sed s/" "/""/g | cut -d "=" -f 2`
        printLog "Avaliadores Isolados: $avIns"

        nReg=`cat $f/1.reg | grep "Regioes =" | sed s/" "/""/g | cut -d "=" -f 2`
        printLog "Numero de Regioes: $nReg"

        printFile "$areaName;$nAv;$avEleg;$avIns;$nReg;" $DIR_OUT/table.csv
        echo

        str="$areaName;"
        for (( count=2; count<=$1; count++ ))
        do
            numGr=`cat $f/$count.num | sed s/" "/""/g | cut -d "=" -f 2`
            printLog "Numero de grupos com k = $count: $numGr"

            if [ -f $f/$count.csv ]
            then
                x=$((8+$count))
                numGrKIns=`cat $f/$count.csv | cut -d ";" -f "$x" | grep "$count" | wc -l`
            else
                numGrKIns=0
            fi

            if [ $numGr -ne "0" ]
            then
                y=`echo "scale=2; 100 * $numGrKIns / $numGr " | bc -l`
            else
                y="N/A"
            fi
            time=`cat $f/time_$count.time | grep "real" | cut -f 2`
            str=$str"$y;$time;"
            printLog "Numero de Grupos com todos Isolados: $numGrKIns"
            printLog "Relacao: $y"
            printLog "Tempo: $time"
        done

        printFile "$str" $DIR_OUT/table2.csv


    done

}

Table ()
{
    cd $WORK_OUT

    str2="Área;Label;Candidatos;Total;Elegíveis;Isolados;"

    for (( k=2; k<=$1; k++ ))
    do
        str2=$str2" k = $k;"
    done

    printFile "$str2" $DIR_OUT/table3.csv

    count=1
    str=""

    for f in `pwd`/*
    do

        if [ -f "$f/$1.csv" ]
        then
            x=$((8+$1))
            numGrKIns=`cat $f/$1.csv | cut -d ";" -f $x | grep $1 | wc -l`

            if [ $numGrKIns -ne 0 ]
            then
                echo

                printLog "$f/5.num"

                areaName=`echo $f | sed s/"_"/" "/g | cut -d - -f 2`
                printLog "Encontrado área A$count com pelo menos um grupo com 5 av isolados: $areaName"
                str="$areaName;A$count;"

                tot=`cat $f/1.ava | sed s/" "/""/g | cut -d "=" -f 2`
                printLog "$areaName possui $tot avaliadores"
                str=$str"$tot;"

                ele=`cat $f/1.ele | grep "Elegiveis =" | sed s/" "/""/g | cut -d "=" -f 2`
                printLog "$areaName possui $ele av elegiveis"
                str=$str"$ele;"

                iso=`cat $f/1.ins | grep "Isolados =" | sed s/" "/""/g | cut -d "=" -f 2`
                printLog "$areaName possui $iso av isolados"
                str=$str"$iso;"

                for (( i=2; i<=$1; i++ ))
                do
                    gr=`cat $f/$i.num | sed s/" "/""/g | cut -d "=" -f 2`
                    printLog "$areaName possui $gr grupos com k = $i"
                    str=$str"$gr;"
                done

                printFile "$str" $DIR_OUT/table3.csv
                let "count++"
            fi
        fi

    done
}

# ---------------------------------------------------------------------- Funcoes
# ------------------------------------------------------------------------- Main
createDir
printLog $DIR_OUT

printLog "Criando tabelas"

cd $DIR_OUT

if [ -f "table.csv" ] || [ -f "table2.csv" ] || [ -f "table3.csv" ]
then
    rm table*
fi

touch table.csv table2.csv table3.csv

cd - > /dev/null

printLog "Tabela Criadas"

Analyse $1

Table $1