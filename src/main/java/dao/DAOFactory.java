package dao;

/**
 * Fabrique de classes DAO spécialisées sous forme de singletons. Ceci afin
 * d'éviter le dépassement du nombre de connexions acceptées par le serveur.
 *
 * @author Herbert &lt;herbert.caffarel@ldnr.fr&gt;
 */
public final class DAOFactory {

    private static UserDao userDao;

    private DAOFactory() {
    }

    /**
     * Retourne une DAO sur la table user des utilisateurs.
     *
     * @return
     */
    public static final UserDao getUserDao() {
        if (userDao == null) {
            userDao = new UserDao();
        }
        return userDao;
    }
}
