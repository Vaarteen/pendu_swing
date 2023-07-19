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
public class Word extends Bean {

    private String word;

    /**
     * Constructeur.
     */
    public Word() {
    }

    /**
     * Constructeur.
     *
     * @param word Le mot
     */
    public Word(String word) {
        this.word = word;
    }

    /**
     * Constructeur.
     *
     * @param id L'identifiant du mot dans la DB. Auto-généré par la DB !
     * @param word Le mot
     */
    public Word(Integer id, String word) {
        this.id = id;
        this.word = word;
    }

    /* Getters et Stters */
    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    @Override
    public String toString() {
        return word;
    }

}
