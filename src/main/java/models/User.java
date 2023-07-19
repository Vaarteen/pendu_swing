package models;

/**
 * Bean permettant de passer du monde objet au monde relationnel. Une instance
 * de ce bean correspond à une ligne dans la table correspondante en DB. Il y a
 * donc correspondance entre les attributs de ce bean et les colonnes de la
 * table. Le bean est utilisé par la couche d'accès aux données de la DB, la
 * DAO. Par définition un bean ne contient que des attributs privés, un
 * constructeur sans paramètre et les getters et setters de ces attributs.
 * Potentiellement un toString() si on veut pouvoir l'afficher. Il peut
 * également être utilie qu'il implémente l'interface Serializable si on veut le
 * persister en fichier.
 *
 * @author Herbert Caffarel
 */
public class User extends Bean {

    private String name;
    private int score;

    /**
     * Constructeur
     */
    public User() {
    }

    /**
     * Constructeur.
     *
     * @param name Le nom du joueur
     */
    public User(String name) {
        this.name = name;
        this.score = 0;
    }

    /**
     * Constructeur.
     *
     * @param name Le nom du joueur
     * @param score Le score du joueur
     */
    public User(String name, int score) {
        this.name = name;
        this.score = score;
    }

    /**
     * Constructeur.
     *
     * @param id_user L'identifiant en Db du joueur. Auto-généré par la DB !
     * @param name Le nom du joueur
     * @param score Le score du joueur
     */
    public User(int id_user, String name, int score) {
        this.id = id_user;
        this.name = name;
        this.score = score;
    }

    /* Getters et setters */
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(name);
        sb.append(" (").append(score);
        sb.append(" point");
        sb.append((score > 1) ? "s)" : ")");
        return sb.toString();
    }

}
