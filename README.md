# Outilssgdf

Un jeu d'outils en Java pour faciliter la conversion des donn�es de l'internet SGDF ([Scouts et Guides de France](https://www.sgdf.fr))

## Contenu des outils
- extraction-convertisseur : Code permettant de construire une structure java avec les donn�es intranet
- exemple-convertisseur : Exemple d'utilisation en ligne de commande
- extraction-servlet : Servlet utilisant la librairie d'extraction (une page html est disponible ainsi qu'une API swagger)
- extraction-embeddedtomcat : Tomcat embedded lan�ant la servlet sur le port 8080 (sous l'url http://127.0.0.1:8080/extraction)

## Comment convertir un fichier "adh�rents" ?
- Utiliser le menu d'extraction de l'intranet (VOUS SAVEZ CE QUE VOUS FAITES ICI)
- Sauver le fichier au format xlsx (important) : Cette �tape est malheureusement manuel...
- Passer le fichier au convertisseur (via la servlet)

## Le r�sultat
On obtient une archive zip avec des fichiers :
- tout.csv : Fichier CSV importable dans Gmail cr�ant un groupe par unit�
- maitrises.cvs : Fichier CSV importable dans Gmail avec les coordonn�es des chefs et cheftaines
<unit�>_enfants.cvs : Fichier CSV importable dans Gmail pour une unit� avec les coordonn�es des parents sous la forme "Papa de..." et "Maman de..."
<unit�>_parents.cvs : Fichier CSV importable dans Gmail pour une unit� avec les coordonn�es des parents avec leurs noms
chefs_<unit�>.txt : Fichiers textes contenant les adresses email des chefs et cheftaines
parents.txt : Fichiers textes contenant les adresses email des parents
parents_<unit�>.txt : Fichiers textes contenant les adresses email des parents pour une unit�

## Outils
- Java 8
- Maven
- Eclipse