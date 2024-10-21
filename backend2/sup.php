<?php
// Connexion à la base de données
$host = 'localhost';   // Hôte
$user = 'root';         // Utilisateur MySQL
$password = '';         // Mot de passe MySQL
$database = 'school1';  // Nom de la base de données

$conn = new mysqli($host, $user, $password, $database);

// Vérifier la connexion
if ($conn->connect_error) {
    die("Échec de la connexion : " . $conn->connect_error);
}

// Vérifier si l'ID est fourni via une requête POST
if (isset($_POST['id'])) {
    $id = intval($_POST['id']);  // Sécurisation de l'entrée utilisateur

    // Préparation de la requête SQL
    $stmt = $conn->prepare("DELETE FROM etudiant WHERE id = ?");
    $stmt->bind_param("i", $id);  // 'i' indique que le paramètre est un entier

    // Exécution de la requête
    if ($stmt->execute()) {
        echo json_encode([
            'success' => true,
            'message' => "L'étudiant avec l'ID $id a été supprimé avec succès."
        ]);
    } else {
        echo json_encode([
            'success' => false,
            'message' => "Erreur lors de la suppression de l'étudiant : " . $stmt->error
        ]);
    }

    // Fermeture de la requête et de la connexion
    $stmt->close();
} else {
    echo json_encode([
        'success' => false,
        'message' => "ID manquant dans la requête."
    ]);
}

$conn->close();
?>
