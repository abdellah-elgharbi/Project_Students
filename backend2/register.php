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
if (isset($_POST['nom'], $_POST['prenom'], $_POST['ville'], $_POST['sexe'])) {
    $nom = $_POST['nom'];
    $prenom = $_POST['prenom'];
    $ville = $_POST['ville'];
    $sexe = $_POST['sexe'];

    try {
        // Insertion des données sans la photo (id auto-incrémenté)
        $sql = "INSERT INTO etudiant (nom, prenom, ville, sexe) VALUES (:nom, :prenom, :ville, :sexe)";
        $stmt = $conn->prepare($sql);
        $stmt->execute([
            ':nom' => $nom,
            ':prenom' => $prenom,
            ':ville' => $ville,
            ':sexe' => $sexe,
        ]);

        // Récupération de l'ID inséré
        $id = $conn->lastInsertId();

        // Vérification si une image a été envoyée
        $imagePath = null; // Initialisation du chemin de l'image
        if (isset($_POST['image'])) {
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

            // Générez un nom de fichier avec id, nom, et prénom
            $imageName = $id . '_' . strtolower(str_replace(' ', '_', $nom . '_' . $prenom)) . '.png';
            $imagePath = 'uploads/' . $imageName; // Assurez-vous que le dossier 'uploads' existe

            // Sauvegarde de l'image dans le dossier 'uploads'
            if (file_put_contents($imagePath, $imageData) === false) {
                echo json_encode(['status' => 'error', 'message' => 'Erreur lors de l\'enregistrement de l\'image.']);
                exit;
            }

            // Mise à jour du chemin de la photo dans la base de données
            $updateSql = "UPDATE etudiant SET photo = :photo WHERE id = :id";
            $updateStmt = $conn->prepare($updateSql);
            $updateStmt->execute([':photo' => $imagePath, ':id' => $id]);
        }

        // Réponse de succès
        echo json_encode(['status' => 'ok', 'message' => 'Étudiant ajouté avec succès']);
    } catch (PDOException $e) {
        // Gestion des erreurs d'insertion
        echo json_encode(['status' => 'error', 'message' => "Erreur lors de l'ajout : " . $e->getMessage()]);
    }
} else {
    // Réponse en cas de données manquantes
    echo json_encode(['status' => 'error', 'message' => 'Données manquantes. Veuillez fournir nom, prénom, ville et sexe.']);
}
?>
