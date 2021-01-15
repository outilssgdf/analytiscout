MIT License# AnalytiScout

Un outil en Java pour faciliter l'analyse des maitrises de l'intranet SGDF ([Scouts et Guides de France](https://www.sgdf.fr))

## Présentation
Ce document donne une description de l’outil analytiscout, développé pour faciliter l’extraction et l’analyse des maîtrises et le contrôle de l’âge des adhérents à partir de l’Intranet SGDF.
L’outil est disponible pour les équipes territoriales sur la plateforme collaborative, ou sinon sur demande adressée via le groupe Facebook outilssgdf.

## Informations importantes
- Les données extraites sont limitées à l'accès que vous donne votre identifiant sur Intranet : utilisable par les DT, RPAF, RPP, RG et secrétaires de groupe 
- Ces outils sont non officiels et ne bénéficient pas du support du National SGDF
- Les données extraites peuvent être sensibles, traitez-les en accord avec les règles émises par le National et la règlementation RGPD.
- Après extraction, l'outil peut créer une feuille Excel synthétique
- Des exemples d’utilisation sont les préparations des déclarations DDCS  (présentation par groupe des qualifications et formations, et statut de stagiaire BAFA potentiel), le suivi des quotas de qualifications, ou la vérification des contraintes d'âge


## Outils
- Java 8 (minimum)
- Maven
- Eclipse

## Build
- git clone https://github.com/outilssgdf/analytiscout.git
- cd analytiscout
- mvn install

## Licence
MIT License
