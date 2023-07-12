package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;

/**
 * Singleton de connexion à la base de données. La configuration se trouve dans
 * le fichier /resources/config.properties.
 *
 * @author Herbert &lt;herbert.caffarel@ldnr.fr&gt;
 */
public final class SQLiteConnection {

    private static Connection instance;

    // Constructeur privé pour interdire l'instanciation
    private SQLiteConnection() {
    }

    /**
     * Retourne une connexion à la DB ou arrête le programme. Si le programme
     * est arrêté, le code d'erreur correspond à :
     * <ul>
     * <li>Erreur 2 : driver non trouvé</li>
     * <li>Erreur 3 : erreur globale de connexion à la DB</li>
     * </ul>
     *
     * @return une connexion
     */
    public static Connection getInstance() {
        // Est-ce que j'ai déjà une connection construite ?
        if (instance == null) {  // Si non, je la construis et je la retourne
            try {
                //SQLiteConnection.class.getResourceAsStream("/resources/config.properties")
                String url = "jdbc:sqlite:resources/pendu.db";
                // Forcer Java à trouver le driver dans les bibliothèques
                // Normalement inutile, mais parfois JEE ne trouve pas tout seul
                Class.forName("org.sqlite.JDBC");
                // Obtenir une connexion
                instance = DriverManager.getConnection(url);
            } catch (ClassNotFoundException ex) {
                String errorMsg = "Driver SQLite introuvable.\n"
                        + "Vérifiez que le driver se trouve bien dans les "
                        + "bibliothèques du projet.";
                JOptionPane.showMessageDialog(
                        null,
                        errorMsg,
                        "Une erreur est survenue",
                        JOptionPane.ERROR_MESSAGE
                );
                System.exit(2);
            } catch (SQLException ex) {
                String errorMsg = "Connexion au serveur SQLite impossible.\n"
                        + "Vérifiez le chemin de votre base de données.";
                JOptionPane.showMessageDialog(
                        null,
                        errorMsg,
                        "Une erreur est survenue",
                        JOptionPane.ERROR_MESSAGE
                );
                System.exit(3);
            }
        }
        // Je la retourne
        return instance;
    }
}
