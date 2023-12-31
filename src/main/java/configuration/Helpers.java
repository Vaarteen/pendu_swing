package configuration;

import dao.DAOFactory;
import dao.SQLiteConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Ce fichier contient des méthodes de support pour le projet. Toutes les
 * méthodes sont statiques.
 *
 * @author Herbert
 */
public class Helpers {

    /**
     * Retourne la configuration du projet. Cette configuration est lue dans le
     * fichier config.properties qui se trouve dans le répertoires "resources".
     * Si le fichier n'existe pas, des valeurs par défaut sont crées.
     *
     * @return Le dictionnaire des propriétés de configuration du programme.
     */
    public static final Properties readConfig() {
        Properties config = new Properties();
        try (InputStream in = Helpers.class
                .getResourceAsStream("/config.properties")) {
            config.load(in);
        } catch (Exception ex) {
            System.err.println("Fichier de configuration introuvable. Application des paramètres par défaut.");
            config.put("maxError", "10");
            config.put("wordSource", "array");
        }
        return config;
    }

    /**
     * Vérifie l'existence de la DB. Si elle n'existe pas, la crée ainsi que sa
     * structure et son contenu de test.
     */
    public static void activateDB() {
        Path path = Paths.get("pendu.db");
        if (!Files.exists(path)) {
            createDB();
        }
    }

    /**
     * Crée la DB avec des valeurs par défaut.
     */
    private static void createDB() {
        try {
            // Récupération de la connexion
            Connection conn = SQLiteConnection.getInstance();
            // Création des tables
            String req = "CREATE TABLE IF NOT EXISTS user ("
                    + "id_user integer PRIMARY KEY,"
                    + "name text NOT NULL UNIQUE COLLATE NOCASE," // unique et insensible à la casse
                    + "score integer NOT NULL"
                    + ");";
            Statement stmt = conn.createStatement();
            stmt.execute(req);
            req = "CREATE TABLE IF NOT EXISTS word ("
                    + "id_word integer PRIMARY KEY,"
                    + "word text NOT NULL UNIQUE COLLATE NOCASE" // unique et insensible à la casse
                    + ");";
            stmt.execute(req);
            req = "CREATE TABLE IF NOT EXISTS word_by_user ("
                    + "id_user integer,"
                    + "id_word integer,"
                    + "PRIMARY KEY (id_user, id_word)"
                    + ");";
            stmt.execute(req);
            // Ajout des données dans la table user
            req = "INSERT INTO user (name, score) VALUES "
                    + "('admin', 0), ('Alf', 1), ('Sophie', 2), "
                    + "('Guy', 3), ('Zoé', 4), ('Muriel', 5), "
                    + "('Alex', 6), ('Roxane', 7), ('Julia', 8), "
                    + "('Terminator', 9), ('Erasor', 10), ('Beast', 12);";
            stmt.execute(req);
            // Ajout des données dans la table word à partir du fichier
            // dictionnaire s'il existe, en dur sinon
            try {
                InputStream is = Helpers.class
                        .getResourceAsStream("/dictionary.dat");
                // Préciser qu'on lit en UTF-8, sinon on aura des problèmes d'encodage.
                // Par défaut Java lit les fichiers texte en ISO 8859-1
                InputStreamReader isr = new InputStreamReader(is, StandardCharsets.UTF_8);
                BufferedReader br = new BufferedReader(isr);
                String word;
                while ((word = br.readLine()) != null) {
                    DAOFactory.getWordDao().create(word);
                }
            } catch (IOException ex) { // Dictionniare non trouvé
                req = "INSERT INTO word (word) VALUES "
                        + "('PARAPLUIE'), ('VOITURE');";
                stmt.execute(req);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Helpers.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(1);
        }
    }

}
