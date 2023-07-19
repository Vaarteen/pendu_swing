package configuration;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Ce fichier contient les constantes utilisables dans le programme. En
 * particulier, une fonte personnalisée importée d'un fichier de fonte présent
 * dans le répertoire "resources" du programme.
 *
 * @author Herbert Caffarel
 */
public class GameConstants {
    public static Font GAME_FONT; // Une fonte personnalisée

    static { // Ce code s'exécute AVANT le constructeur
        // Récupération du fichier de fonte personnalisée
        try {
            InputStream stream = GameConstants.class.getResourceAsStream("/fonts/retro-flower.regular.otf");
            GAME_FONT = Font.createFont(Font.TRUETYPE_FONT, stream).deriveFont(96f);
        } catch (FontFormatException | IOException ex) {
            GAME_FONT = new Font("Arial", Font.PLAIN, 96);
        }
    }

}
