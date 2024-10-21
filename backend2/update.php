<?php
// Activer l'affichage des erreurs pour le débogage
ini_set('display_errors', 1);
ini_set('display_startup_errors', 1);
error_reporting(E_ALL);

// Informations de connexion à la base de données
$host = "localhost";
$dbname = "school1";
$username = "root";
$password = "";

// Connexion à la base de données avec gestion des erreurs
try {
    $conn = new PDO("mysql:host=$host;dbname=$dbname;charset=utf8", $username, $password);
    $conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
} catch (PDOException $e) {
    die(json_encode(['status' => 'error', 'message' => "Erreur de connexion : " . $e->getMessage()]));
}

// Vérification des données POST
if (isset($_POST['id'], $_POST['nom'], $_POST['prenom'], $_POST['ville'], $_POST['sexe'])) {
    $id = $_POST['id'];
    $nom = $_POST['nom'];
    $prenom = $_POST['prenom'];
    $ville = $_POST['ville'];
    $sexe = $_POST['sexe'];

    // Initialiser le chemin de l'image à NULL (aucun changement par défaut)
    $imagePath = null;

    // Vérification si une nouvelle image a été envoyée
    if (isset($_POST['image']) && !empty($_POST['image'])) {
        $image = $_POST['image'];

        // Décodez l'image
        $image = str_replace('data:image/png;base64,', '', $image);
        $image = str_replace(' ', '+', $image);
        $imageData = base64_decode($image);

        // Vérifiez si l'image a été décodée correctement
        if ($imageData === false) {
            echo json_encode(['status' => 'error', 'message' => 'Erreur lors du décodage de l\'image.']);
            exit;
        }

        // Générez un nom de fichier unique
        $imageName = uniqid() . '.png';
        $imagePath = 'uploads/' . $imageName;

        // Sauvegarde de l'image dans le dossier 'uploads'
        if (file_put_contents($imagePath, $imageData) === false) {
            echo json_encode(['status' => 'error', 'message' => 'Erreur lors de l\'enregistrement de l\'image.']);
            exit;
        }
    }

    try {
        // Construction de la requête SQL
        $sql = "UPDATE etudiant SET nom = :nom, prenom = :prenom, ville = :ville, sexe = :sexe";

        // Ajouter la colonne photo à la requête uniquement si une nouvelle image a été fournie
        if ($imagePath !== null) {
            $sql .= ", photo = :photo";
        }
        $sql .= " WHERE id = :id";

        $stmt = $conn->prepare($sql);

        // Construction des paramètres de la requête
        $params = [
            ':id' => $id,
            ':nom' => $nom,
            ':prenom' => $prenom,
            ':ville' => $ville,
            ':sexe' => $sexe
        ];

        // Ajouter le paramètre photo si une nouvelle image a été fournie
        if ($imagePath !== null) {
            $params[':photo'] = $imagePath;
        }

        // Exécution de la requête
        $stmt->execute($params);

        // Réponse de succès
        echo json_encode(['status' => 'ok', 'message' => 'Informations de l\'étudiant mises à jour avec succès']);
    } catch (PDOException $e) {
        // Gestion des erreurs de mise à jour
        echo json_encode(['status' => 'error', 'message' => "Erreur lors de la mise à jour : " . $e->getMessage()]);
    }
} else {
    // Réponse en cas de données manquantes
    echo json_encode(['status' => 'error', 'message' => 'Données manquantes. Veuillez fournir id, nom, prénom, ville et sexe.']);
}
?>
