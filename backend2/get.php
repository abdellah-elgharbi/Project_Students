<?php
// Informations de connexion à la base de données
$host = "localhost"; // Adresse de votre serveur MySQL
$dbname = "school1"; // Nom de votre base de données
$username = "root"; // Nom d'utilisateur MySQL
$password = ""; // Mot de passe MySQL

// Connexion à la base de données
try {
    $conn = new PDO("mysql:host=$host;dbname=$dbname;charset=utf8", $username, $password);
    $conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
} catch (PDOException $e) {
    die("Erreur de connexion : " . $e->getMessage());
}

// Requête pour récupérer les données de la table "utilisateurs"
$sql = "SELECT * FROM etudiant";
$stmt = $conn->prepare($sql);
$stmt->execute();

// Stocker les résultats dans un tableau
$utilisateurs = array();
while ($row = $stmt->fetch(PDO::FETCH_ASSOC)) {
    $utilisateurs[] = $row;
}

// Envoyer les données en JSON à l'application Android
header('Content-Type: application/json');
echo json_encode($utilisateurs);
?>
