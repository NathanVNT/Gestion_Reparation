package org.nathanvernet.gestion_reparation;
import net.glxn.qrgen.QRCode;
import net.glxn.qrgen.image.ImageType;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class QRCodeGenerator {

    /**
     * Génère un code QR à partir de la référence fournie.
     * @param reference La référence à encoder dans le code QR.
     * @return Le contenu du code QR sous forme de tableau d'octets.
     * @throws IOException En cas d'erreur lors de la génération du code QR.
     */
    public byte[] generateQRCode(String reference) throws IOException {
        // Générer le code QR à partir de la référence
        ByteArrayOutputStream out = QRCode.from(reference).to(ImageType.PNG).stream();
        return out.toByteArray();
    }

    /**
     * Enregistre le code QR dans un fichier.
     * @param reference La référence à encoder dans le code QR.
     * @param filePath Le chemin du fichier où enregistrer le code QR.
     * @throws IOException En cas d'erreur lors de l'écriture du fichier.
     */
    public void saveQRCode(String reference, String filePath) throws IOException {
        byte[] qrCodeBytes = generateQRCode(reference);
        Path qrCodePath = Path.of(filePath);
        Files.write(qrCodePath, qrCodeBytes);
    }
}
