<?php
// Vérifie que la requête est une requête POST
if ($_SERVER['REQUEST_METHOD'] == 'POST') {
    // Vérifie si l'image a été reçue
    if (isset($_POST['image'])) {
        // Récupère l'image encodée en Base64
        $image = $_POST['image'];

        // Enlève le préfixe "data:image/png;base64," si présent
        $image = str_replace('data:image/png;base64,', '', $image);
        $image = str_replace(' ', '+', $image); // Remplace les espaces par des '+'
        
        // Décodage de l'image
        $decodedImage = base64_decode($image);

        // Vérifie que le décodage a réussi
        if ($decodedImage === false) {
            echo json_encode(array("status" => "error", "message" => "Failed to decode image."));
            exit;
        }

        // Génère un nom de fichier unique pour l'image
        $filePath = 'uploads/image_' . uniqid() . '.png';

        // Stocke l'image décodée sur le serveur
        if (file_put_contents($filePath, $decodedImage)) {
            echo json_encode(array("status" => "success", "message" => "Image uploaded successfully.", "path" => $filePath));
        } else {
            echo json_encode(array("status" => "error", "message" => "Failed to upload image."));
        }
    } else {
        echo json_encode(array("status" => "error", "message" => "No image received."));
    }
} else {
    echo json_encode(array("status" => "error", "message" => "Invalid request."));
}
?>
