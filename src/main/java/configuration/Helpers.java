package configuration;

import dao.DAOFactory;
import dao.SQLiteConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
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

    private static void createDB() {
        try {
            // Récupération de la connexion
            Connection conn = SQLiteConnection.getInstance();
            // Création des tables
            String req = "CREATE TABLE IF NOT EXISTS user ("
                    + "id_user integer PRIMARY KEY,"
                    + "name text NOT NULL,"
                    + "score integer NOT NULL"
                    + ");";
            Statement stmt = conn.createStatement();
            stmt.execute(req);
            req = "CREATE TABLE IF NOT EXISTS dictionnary ("
                    + "id_dictionnary integer PRIMARY KEY,"
                    + "word text NOT NULL"
                    + ");";
            stmt.execute(req);
            req = "CREATE TABLE IF NOT EXISTS word_by_user ("
                    + "id_user integer,"
                    + "id_dictionnary integer,"
                    + "PRIMARY KEY (id_user, id_dictionnary)"
                    + ");";
            stmt.execute(req);
            // Ajout des données dans la table user
            req = "INSERT INTO user (name, score) VALUES "
                    + "('admin', 0), ('Alf', 5), ('Sophie', 6);";
            stmt.execute(req);
            // Ajout des données dans la table word à partir du fichier
            // dictionnaire s'il existe, en dur sinon
            try (BufferedReader br = new BufferedReader(
                    new InputStreamReader(Hangman.class
                            .getResourceAsStream("/configuration/dictionary.dat")))) {
                String word;
                while ((word = br.readLine()) != null) {
                    DAOFactory.getWordDao().create(word);
                }
            } catch (IOException ex) { // Dictionniare non trouvé
                req = "INSERT INTO dictionnary (word) VALUES "
                        + "('PARAPLUIE'), ('VOITURE');";
                stmt.execute(req);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Helpers.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(1);
        }
    }

}
