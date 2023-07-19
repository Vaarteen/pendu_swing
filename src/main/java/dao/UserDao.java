package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import models.User;

/**
 * Classe de conversion du monde objet au monde relationnel. S'appuie sur le
 * Bean User, ainsi chaque instance de User correspond à une ligne de la table
 * user.
 *
 * @author Herbert Caffarel
 */
public class UserDao extends DAO<User> {

    /**
     * Constructeur. Il fourni le nom de la table à la classe abstraite DAO.
     */
    public UserDao() {
        super("user");
    }

    /* Implémentation des méthodes nécessaires imposées par la classe abstraite DAO */
    @Override
    public User getById(Integer id) {
        User user = null;
        String sql = "SELECT * FROM "
                + table
                + " WHERE id_" + table + "=?";
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) { // SQLite ne supporte pas first()
                user = new User(
                        rs.getInt("id_" + table),
                        rs.getString("name"),
                        rs.getInt("score")
                );
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return user;
    }

    @Override
    public void create(User object) {
        String sql = "INSERT INTO "
                + table
                + " (name, score) VALUES (?, ?)";
        try {
            PreparedStatement pstmt = conn.prepareStatement(
                    sql,
                    PreparedStatement.RETURN_GENERATED_KEYS
            );
            pstmt.setString(1, object.getName());
            // On stocke le mot de passe hashé dans la DB !!!
            pstmt.setInt(2, object.getScore());
            int lines = pstmt.executeUpdate();
            // On ajoute l'identifiant nouvellement créé à l'objet !
            ResultSet keys = pstmt.getGeneratedKeys();
            if (keys.next()) { // SQLite ne supporte pas first()
                object.setId(keys.getInt(1));
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserDao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void update(User object) {
        String sql = "UPDATE "
                + table
                + " SET name=?, score=?"
                + " WHERE id_" + table + "=?";
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, object.getName());
            pstmt.setInt(2, object.getScore());
            pstmt.setInt(3, object.getId());
            int lines = pstmt.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(UserDao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public List<User> getAll() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM " + table
                + " WHERE id_" + table + " <> 1";
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                users.add(new User(
                        rs.getInt("id_" + table),
                        rs.getString("name"),
                        rs.getInt("score")
                ));
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return users;
    }

    /* Implémentation des méthodes nécessaires pour le programme */
    /**
     * Fournit les 10 joueurs avec le meilleurs score.
     *
     * @return Les 10 joueurs ayant le meoilleurs score par ordre décroissant.
     */
    public List<User> getHallOfFame() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM " + table
                + " WHERE id_" + table + " <> 1 ORDER BY score DESC LIMIT 10";
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                users.add(new User(
                        rs.getInt("id_" + table),
                        rs.getString("name"),
                        rs.getInt("score")
                ));
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return users;
    }

    /**
     * Fournit la liste des joueurs classés par nom.
     *
     * @return La liste des joueurs classés par nom.
     */
    public List<User> getAllByName() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM " + table
                + " WHERE id_" + table + " <> 1 ORDER BY name ASC";
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                users.add(new User(
                        rs.getInt("id_" + table),
                        rs.getString("name"),
                        rs.getInt("score")
                ));
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return users;
    }

    /**
     * Fournit un joueur connaissant son nom.
     *
     * @param name Le nom du joueur recherché
     * @return Le joueur s'il existe, null sinon.
     */
    public User getByName(String name) {
        User user = null;
        String sql = "SELECT * FROM "
                + table
                + " WHERE name=?";
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, name);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) { // SQLite ne supporte pas first()
                user = new User(
                        rs.getInt("id_" + table),
                        rs.getString("name"),
                        rs.getInt("score")
                );
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return user;
    }
}
