package dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import models.Bean;

/**
 * Classe mère de DAO. Cette classe fournit les fonctionnalités CRUD de base.
 * Elle implémente l'interface Crudable<T> où T est un Bean. Elle fournit aussi
 * les fonctionnalités standards count et getAll.
 *
 * @author Herbert &lt;herbert.caffarel@ldnr.fr&gt;
 * @param <T> Une classe héritant de Bean.
 */
public abstract class DAO<T extends Bean> implements Crudable<T> {

    /**
     * Connexion à la base de données
     */
    protected Connection conn = SQLiteConnection.getInstance();
    /**
     * La table de DB associée à cette DAO
     */
    protected final String table;

    /**
     * Constructeur : prend le nom de l atable en paramètre.
     *
     * @param table La table associée à cette DAO
     */
    public DAO(String table) {
        this.table = table;
    }

    @Override
    public void delete(T object) {
        String sql = "DELETE FROM " + table + " "
                + "WHERE id_" + table + "=" + object.getId();
        Statement stmt;
        try {
            stmt = conn.createStatement();
            stmt.executeUpdate(sql);
        } catch (SQLException ex) {
            Logger.getLogger(DAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void persist(T object) {
        if (object.isNew()) {
            create(object);
        } else {
            update(object);
        }
    }

    /**
     * Retourne tous les éléments de la table sous forme de liste de beans.
     *
     * @return Liste de beans hydratés par la table.
     */
    public abstract List<T> getAll();

    /**
     * Retourne le nombre d'instances de cette table.
     *
     * @return Le nombre d'instances de la table.
     */
    public int count() {
        String sql = "SELECT COUNT(*) AS count FROM " + table;
        Statement stmt;
        try {
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.first()) {
                return rs.getInt("count");
            }
        } catch (SQLException ex) {
            Logger.getLogger(DAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

}
