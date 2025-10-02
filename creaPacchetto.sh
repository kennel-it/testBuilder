#!/bin/bash

# ATTENZIONE: questo script fa affidamento su una serie di cose più o meno standard
# - la variabile JAVA_HOME deve essere impostata
# - maven deve essere installato e sul path
# - il progetto usa maven 
# - mvn package mette tutti i jar in target/jars
# - le icone sono nella cartella icone
# - su windows deve essere installato 7-Zip

if [ -z "$JAVA_HOME" ]; then
    echo "la variabile JAVA_HOME non è stata impostata"
    exit 0
fi

# comando per jpackage
JPACKAGE=$JAVA_HOME/bin/jpackage
# dove stanno i jar (cartella impostata nel file pom.xml)
CARTELLA_JARS=target/jars
# cartella di lavoro
CARTELLA_LAVORO=target/lavoro
# dove mettere il file compilato
DESTINAZIONE=target
# versione (recuperata dal file pom)
VERSIONE=$(grep -m 1 version pom.xml | sed 's/[^0-9\\.]//g')
# nome del jar principale (contiene anche il numero di versione)
JAR_PRINCIPALE="testBuilder-$VERSIONE.jar"
# nome dell'archivio da creare
CPU=$(uname -m)
# nome icona, dipende dal sistema operativo
# windows meglio lasciarlo in else (perché?)
if [[ "$OSTYPE" == "darwin"*  ]]; then
    # icona per macOS
    ICONA=icone/icona.icns
    TIPO_PACCHETTO="dmg"
    COMANDO="$JPACKAGE --name testBuilder --app-version $VERSIONE --icon $ICONA --type $TIPO_PACCHETTO \
    --input $CARTELLA_LAVORO --dest $DESTINAZIONE \
    --add-modules javafx.controls,javafx.media,javafx.fxml,javafx.web,jdk.charsets \
    --main-class it.aspix.scuola.test.Main --main-jar $JAR_PRINCIPALE \
    --mac-package-name testBuilder \
    --mac-sign \
    --mac-package-identifier it.aspix.scuola.test"
    BUNDLE_NAME="testBuilder-$VERSIONE-macOS-$CPU.dmg"
elif [[ "$OSTYPE" == "linux"* ]]; then
    # icona le Linux (l'unico normale visto il tipo del file!)
    ICONA=icone/icona.png
    TIPO_PACCHETTO="app-image"
    COMANDO="$JPACKAGE --name testBuilder --app-version $VERSIONE --icon $ICONA --type $TIPO_PACCHETTO \
    --input $CARTELLA_LAVORO --dest $DESTINAZIONE \
    --add-modules javafx.controls,javafx.media,javafx.fxml,javafx.web,jdk.charsets \
    --main-class it.aspix.scuola.test.Main --main-jar $JAR_PRINCIPALE"
    BUNDLE_NAME="testBuilder-$VERSIONE-linux-$CPU.tgz"
else
    # icona per Windows
    ICONA=icone/icona.ico
    TIPO_PACCHETTO="app-image"
    COMANDO="$JPACKAGE --name testBuilder --app-version $VERSIONE --icon $ICONA --type $TIPO_PACCHETTO \
    --input $CARTELLA_LAVORO --dest $DESTINAZIONE \
    --add-modules javafx.controls,javafx.media,javafx.fxml,javafx.web,jdk.charsets \
    --main-class it.aspix.scuola.test.Main --main-jar $JAR_PRINCIPALE"
    BUNDLE_NAME="testBuilder-$VERSIONE-win-$CPU.zip"
fi

echo "----- ambiente di lavoro -------------------------------------"
echo "JAVA_HOME        : $JAVA_HOME"
echo "JPACKAGE         : $JPACKAGE"
echo "MAVEN            : $(which mvn)"
echo "OSTYPE           : $OSTYPE"
echo ""
echo "----- cartelle -----------------------------------------------"
echo "CARTELLA_JARS  : $CARTELLA_JARS"
echo "JAR_PRINCIPALE : $JAR_PRINCIPALE"
echo "CARTELLA_LAVORO: $CARTELLA_LAVORO"
echo "DESTINAZIONE   : $DESTINAZIONE"
echo "TIPO_PACCHETTO : $TIPO_PACCHETTO"
echo ""
echo "----- artefatto -----------------------------------------------"
echo "ICONA          : $ICONA"
echo "VERSIONE       : $VERSIONE"
echo "COMANDO        : $COMANDO"
echo "BUNDLE_NAME    : $BUNDLE_NAME"
echo "DESTINAZIONE   : $DESTINAZIONE"
echo ""
echo "--------------------------------------------------------------"

mvn -q clean
mvn -q package -DskipTests

mkdir $CARTELLA_LAVORO
cp $CARTELLA_JARS/* $CARTELLA_LAVORO

$COMANDO

# piccoli aggiustamenti che dipendono dal sistema operativo
if [[ "$OSTYPE" == "darwin"*  ]]; then
    mv "target/testBuilder-$VERSIONE.dmg" "target/$BUNDLE_NAME"
elif [[ "$OSTYPE" == "linux"* ]]; then
    cd target
    tar -cvzf $BUNDLE_NAME testBuilder/
    cd ..
else
    cd target
    cd testBuilder
    /c/Program\ Files/7-Zip/7z a -tzip ../$BUNDLE_NAME *
    cd ..
fi
