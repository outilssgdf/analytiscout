# Outilssgdf

Un jeu d'outils en Java pour faciliter la conversion des données de l'internet SGDF ([Scouts et Guides de France](https://www.sgdf.fr))

## Contenu des outils
- extraction-convertisseur : Code permettant de construire une structure java avec les données intranet
- exemple-convertisseur : Exemple d'utilisation en ligne de commande
- extraction-servlet : Servlet utilisant la librairie d'extraction (une page html est disponible ainsi qu'une API swagger)
- extraction-embeddedtomcat : Tomcat embedded lançant la servlet sur le port 8080 (sous l'url http://127.0.0.1:8080/extractionsgdf)

## Comment convertir un fichier "adhérents" ?
- Utiliser le menu d'extraction de l'intranet (VOUS SAVEZ CE QUE VOUS FAITES ICI)
- Sauver le fichier au format xlsx (important) : Cette étape est malheureusement manuel...
- Passer le fichier au convertisseur (via la servlet)

## Le résultat
On obtient une archive zip avec plein de fichiers :
- tout.csv : Fichier CSV importable dans Gmail créant un groupe par unité
- maitrises.cvs : Fichier CSV importable dans Gmail avec les coordonnées des chefs et cheftaines
- <unité>_enfants.cvs : Fichier CSV importable dans Gmail pour une unité avec les coordonnées des parents sous la forme "Papa de..." et "Maman de..."
- <unité>_parents.cvs : Fichier CSV importable dans Gmail pour une unité avec les coordonnées des parents avec leurs noms
- chefs_<unité>.txt : Fichiers textes contenant les adresses email des chefs et cheftaines
- parents.txt : Fichiers textes contenant les adresses email des parents
- parents_<unité>.txt : Fichiers textes contenant les adresses email des parents pour une unité

## Outils
- Java 8
- Maven
- Eclipse

## Build
- git clone https://github.com/sbouchex/outilssgdf.git
- cd outilssgdf
- mvn install
- Le .war est "extraction-servlet/target/extractionsgdf.war"