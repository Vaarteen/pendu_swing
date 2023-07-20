package models;

import configuration.Helpers;
import dao.DAOFactory;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Gestionnaire de mot. On ne peut pas utiliser le bean pour gérer le mot, car
 * le bean est utilisé par la DAO et a une structure imposée et simpliste. On
 * doit donc l'encapsuler dans un gestionnaire, qui propose toutes les
 * fonctionnalités inhérentes au mot.
 *
 * @author Herbert
 */
public class WordManager {

    private Word word; // Le mot encapsulé
    private String shadowedWord; // Le mot masqué
    private static final Properties config = Helpers.readConfig(); // La configuration du jeu

    /**
     * Constructeur.
     */
    public WordManager() {
        this.word = new Word();
    }

    /**
     * Constructeur.
     *
     * @param word Le mot encapsulé
     */
    public WordManager(Word word) {
        this.word = word;
    }

    /* Getters et setters */
    public String getWord() {
        return word.getWord();
    }

    /**
     * Fournit le mot masqué en fonction des lettres passées en paramètre.
     *
     * @param knownLetters Les lettres connues
     * @return Le mot partiellement masqué
     */
    public String getShadowedWord(Collection<Character> knownLetters) {
        createShadowedWord(knownLetters);
        return shadowedWord;
    }

    @Override
    public String toString() {
        return word.toString();
    }

    /**
     * Tire un mot au hasard selon la source fournie dans les propriétés. Ce
     * peut être file, database ou array. Si une méthode demandée échoue, on se
     * rabat dans tous les cas sur la création par tableau codé en dur dans le
     * programme.
     *
     */
    public void initializeRandomWord(User player) {
        String w = null;
        switch (config.getProperty("wordSource")) {
            case "file": // dictionnaire fichier
                w = initializeRandomWordFromFile(config.getProperty("dictName"));
                break;
            case "database": // dictionnaire DB
                w = initializeRandomWordFromDb(config.getProperty("dbName"), player);
                break;
            default: // Dictionnaire en dur
                initializeRandomWordFromArray();
        }
        // Nettoyage et affectation du mot au jeu
        this.word.setWord(cleanWord(w).toUpperCase());
    }

    /**
     * Crée le mot masqué constitué de lettres et d'étoiles en fonction des
     * lettres connues passées en paramètre.
     *
     * @param knownLetters : les lettres connues
     */
    private void createShadowedWord(Collection<Character> knownLetters) {
        StringBuilder shadow = new StringBuilder();
        String w = this.word.getWord();
        for (int i = 0; i < w.length(); i++) {
            if (knownLetters.contains(w.charAt(i))) {
                shadow.append(w.charAt(i));
            } else {
                shadow.append("*"); // On ajoute une étoile
            }
        }
        shadowedWord = shadow.toString();
    }

    /**
     * Indique si le mot a été trouvé ou pas.
     *
     * @return true si le mot est trouvé, false sinon
     */
    public boolean isFound() {
        return !shadowedWord.contains("*");
    }

    /**
     * Indique si la lettre fournie existe dans le mot.
     *
     * @param letter la lettre à trouver dans le mot
     * @return true si la lettre se trouve dans le mot, false sinon
     */
    public boolean contains(char letter) {
        return word.getWord().contains(Character.toString(letter));
    }

    /**
     * Retourne un mot pris au hasard dans le fichier dictionnaire.
     *
     * @param fileName Le nom du fichier dictionnaire
     * @return Un mot tiré au hasard dans le doctionnaire
     */
    private String initializeRandomWordFromFile(String fileName) {
        String w = null;
        Path filePath = Paths.get(fileName);
        try {
            List<String> words;
            // On met toutes les lignes du dictionnaire dans la liste
            words = Files.readAllLines(filePath);
            Random rd = new Random();
            w = words.get(rd.nextInt(words.size()));
        } catch (Exception ex) {
            // Si la lecture du fichier echoue on tire un mot dans le tableau en dur
            Logger.getLogger(WordManager.class.getName()).log(Level.SEVERE, null, ex);
            w = initializeRandomWordFromArray();
        }
        return w;
    }

    /**
     * Retourne un mot pris au hasard dans la DB dictionnaire.
     *
     * @param dbName Le nom de la DB SQLite
     * @return un mot tiré au hasard dans la DB
     */
    private String initializeRandomWordFromDb(String dbName, User player) {
        String w = null;
        try {
            w = DAOFactory.getWordDao().getRandomWordForPlayer(player).getWord();
        } catch (Exception ex) { // En cas d'erreur d'accès à la DB on tire dans un tableau en dur
            Logger.getLogger(WordManager.class.getName()).log(Level.SEVERE, null, ex);
            w = initializeRandomWordFromArray();
        }
        return w;
    }

    /**
     * Retourne un mot pris au hasard dans un tableau en dur.
     *
     * @return un mot au hasard
     */
    private String initializeRandomWordFromArray() {
        String[] words = {"AMELIORATION", "PARAPLUIE", "VOITURE",
            "ECOLE", "MONTAGNE"};
        Random rd = new Random();
        return words[rd.nextInt(words.length)];
    }

    /**
     * Nettoie le mot sélectionné pour être utilisable dans le jeu.
     *
     * @param word le mot à nettoyer
     * @return le mot avec les 26 lettres standard
     */
    private String cleanWord(String word) {
        return word.toLowerCase()
                .replaceAll("[àâä]", "a")
                .replaceAll("[éèêë]", "e")
                .replaceAll("[îï]", "i")
                .replaceAll("[ôö]", "o")
                .replaceAll("[ùûü]", "u")
                .replaceAll("ÿ", "y")
                .replaceAll("[ç]", "c");
    }
}
