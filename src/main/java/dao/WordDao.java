package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import models.Word;

/**
 * Classe de conversion du monde objet au monde relationnel. S'appuie sur le
 * Bean Word, ainsi chaque instance de Word correspond à une ligne de la table
 * word.
 *
 * @author Herbert Caffarel
 */
public class WordDao extends DAO<Word> {

    /**
     * Constructeur. Il fournit le nom de la table cible à la classe mère DAO.
     */
    public WordDao() {
        super("word");
    }

    /* Implémentation des méthodes nécessaires imposées par la classe abstraite DAO */
    @Override
    public Word getById(Integer id) {
        Word word = null;
        String sql = "SELECT * FROM "
                + table
                + " WHERE id_" + table + "=?";
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) { // SQLite ne supporte pas first()
                word = new Word(
                        rs.getInt("id_" + table),
                        rs.getString("word")
                );
            }
        } catch (SQLException ex) {
            Logger.getLogger(WordDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return word;
    }

    @Override
    public void create(Word object) {
        String sql = "INSERT INTO "
                + table
                + " (word) VALUES (?)";
        try {
            PreparedStatement pstmt = conn.prepareStatement(
                    sql,
                    PreparedStatement.RETURN_GENERATED_KEYS
            );
            pstmt.setString(1, object.getWord());
            int lines = pstmt.executeUpdate();
            // On ajoute l'identifiant nouvellement créé à l'objet !
            ResultSet keys = pstmt.getGeneratedKeys();
            if (keys.next()) { // SQLite ne supporte pas first()
                object.setId(keys.getInt(1));
            }
        } catch (SQLException ex) {
            Logger.getLogger(WordDao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void update(Word object) {
        String sql = "UPDATE "
                + table
                + " SET word=?"
                + " WHERE id_" + table + "=?";
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, object.getWord());
            pstmt.setInt(2, object.getId());
            int lines = pstmt.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(WordDao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public List<Word> getAll() {
        List<Word> words = new ArrayList<>();
        String sql = "SELECT * FROM " + table;
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                words.add(new Word(
                        rs.getInt("id_" + table),
                        rs.getString("word")
                ));
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return words;
    }
    /* Implémentation des méthodes nécessaires pour le programme */
    /**
     * Retourne un mot au hasard depuis la table word.
     *
     * @return Un mot au hasard
     */
    public Word getRandomWord() {
        Word word = null;
        String sql = "SELECT * FROM "
                + table
                + " ORDER BY RANDOM() LIMIT 1";
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) { // SQLite ne supporte pas first()
                word = new Word(
                        rs.getInt("id_" + table),
                        rs.getString("word")
                );
            }
        } catch (SQLException ex) {
            Logger.getLogger(WordDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return word;
    }

    /**
     * Crée un mot dans la table word.
     *
     * @param word Le mot à ajouter à la table
     */
    public void create(String word) {
        create(new Word(word));
    }

    /**
     * Retourne un Word connaissant le mot.
     *
     * @param word Le mot à trouver dans la DB
     * @return L'instance de Word correspondante
     */
    public Word getByWord(String word) {
        Word retValue = null;
        String sql = "SELECT * FROM "
                + table
                + " WHERE word=?";
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, word);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) { // SQLite ne supporte pas first()
                retValue = new Word(
                        rs.getInt("id_" + table),
                        rs.getString("word")
                );
            }
        } catch (SQLException ex) {
            Logger.getLogger(WordDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return retValue;
    }

}
