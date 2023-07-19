package dao;

/**
 * Fabrique de classes DAO spécialisées sous forme de singletons. Ceci afin
 * d'éviter le dépassement du nombre de connexions acceptées par le serveur.
 *
 * @author Herbert &lt;herbert.caffarel@ldnr.fr&gt;
 */
public final class DAOFactory {

    private static UserDao userDao; // Le seul objet de type UserDao
    private static WordDao wordDao; // Le seul objet de type WordDao

    private DAOFactory() {
    }

    /**
     * Retourne une DAO sur la table user des utilisateurs.
     *
     * @return L'objet de type UserDao
     */
    public static final UserDao getUserDao() {
        if (userDao == null) {
            userDao = new UserDao();
        }
        return userDao;
    }

    /**
     * Retourne une DAO sur la table des mots du dictionnaire.
     *
     * @return L'objet de type WordDao
     */
    public static final WordDao getWordDao() {
        if (wordDao == null) {
            wordDao = new WordDao();
        }
        return wordDao;
    }
}
