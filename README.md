

---

# Project Students

## Introduction

Ce projet a pour objectif de développer une application mobile qui interagit avec une base de données MySQL via des web services PHP. L'application permet l'ajout, la modification, la suppression et l'affichage des données des étudiants.

## Fonctionnalités

- **Ajouter un étudiant** : Formulaire pour ajouter un étudiant avec photo.
- **Modifier un étudiant** : Modifier les informations d'un étudiant existant.
- **Supprimer un étudiant** : Supprimer un étudiant de la base de données.
- **Afficher la liste des étudiants** : Affichage de la liste avec recyclerview, support des images via Picasso.
- **Support des animations** : Gestion des images animées avec `android-gif-drawable`.

## Technologies utilisées

- **Back-end** : Web services PHP, base de données MySQL
- **Front-end mobile** : Android avec Java, Volley pour les requêtes HTTP, Picasso pour l'affichage d'images, RecyclerViewSwipeDecorator pour des interactions stylisées.
- **Autres bibliothèques** : Volley, Picasso, android-gif-drawable, RecyclerViewSwipeDecorator

## Installation

1. Clonez le dépôt :  
   ```bash
   git clone https://github.com/abdellahelgharbi/Project_Students.git
   ```

2. **Back-end (PHP/MySQL)** :
   - Lancer XAMPP, démarrer Apache et MySQL.
   - Créer une base de données nommée `school1`.
   - Importer le fichier `school1.sql` dans votre base de données.

3. **Mobile (Android)** :
   - Ouvrir le projet Android dans Android Studio.
   - Ajouter les permissions internet dans `AndroidManifest.xml` pour l'accès au web.
   - Ajouter les dépendances de Volley, Picasso, android-gif-drawable dans le fichier `build.gradle`.

## Web Services

- `registre.php` : Ajouter un étudiant via une requête POST.
- `get.php` : Récupérer la liste des étudiants au format JSON.
- `update.php` : Modifier les informations d'un étudiant.
- `sup.php` : Supprimer un étudiant.

## Comment utiliser

1. **Ajout d’un étudiant** : Remplir le formulaire et cliquer sur “Ajouter”.
2. **Modification** : Cliquer sur un étudiant pour afficher ses informations, puis modifier et enregistrer.
3. **Suppression** : Glisser vers la gauche pour supprimer un étudiant de la liste.
4. **Affichage** : La liste des étudiants est affichée via RecyclerView avec leurs photos.

## Démos

https://github.com/user-attachments/assets/23f14d15-b62a-47f7-a2f6-ff79e017c271

## Auteur

- **Réalisé par** : Abdellah El Gharbi  
- **Encadré par** : Mr. Lachgar

## Lien GitHub

[https://github.com/abdellahelgharbi/Project_Students](https://github.com/abdellahelgharbi/Project_Students)

---
