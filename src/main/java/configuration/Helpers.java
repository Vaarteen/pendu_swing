package configuration;

import java.io.InputStream;
import java.util.Properties;
import pendu.Hangman;

/**
 *
 * @author Herbert
 */
public class Helpers {

    public static final Properties readConfig() {
        Properties config = new Properties();
        try (InputStream in = Hangman.class
                .getResourceAsStream("/configuration/config.properties")) {
            config.load(in);
        } catch (Exception ex) {
            System.err.println("Fichier de configuration introuvable. Application des paramètres par défaut.");
            config.put("maxError", "10");
            config.put("wordSource", "array");
        }
        return config;
    }
}
