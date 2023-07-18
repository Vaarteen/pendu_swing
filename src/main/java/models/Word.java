package models;

import configuration.Helpers;
import dao.DAOFactory;
import java.io.IOException;
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
 *
 * @author Herbert
 */
public class Word extends Bean {

    private String word;
    private String shadowedWord;
    private final Properties config;

    public Word() {
        this.config = Helpers.readConfig();
        initializeRandomWord();
    }

    public Word(String word) {
        this();
        this.word = word;
    }

    public Word(int id, String word) {
        this();
        this.id = id;
        this.word = word;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getShadowedWord() {
        return shadowedWord;
    }

    /**
     * Tire un mot au hasard selon la source fournie dans les propriétés.
     *
     */
    private void initializeRandomWord() {
        String word = null;
        switch (this.config.getProperty("wordSource")) {
            case "file":
                word = initializeRandomWordFromFile(this.config.getProperty("dictName"));
                break;
            case "database":
                word = initializeRandomWordFromDb(this.config.getProperty("dbName"));
                break;
            default:
                initializeRandomWordFromArray();
        }
        this.word = cleanWord(word).toUpperCase();
    }

    /**
     * Retourne un mot constitué de lettres et d'étoiles.
     *
     * @param knownLetters : les lettres connues
     */
    public void createShadowedWord(Collection<Character> knownLetters) {
        StringBuilder shadow = new StringBuilder();
        for (int i = 0; i < word.length(); i++) {
            if (knownLetters.contains(word.charAt(i))) {
                shadow.append(word.charAt(i));
            } else {
                shadow.append("*"); // On ajoute une étoile
            }
        }
        shadowedWord = shadow.toString();
    }

    public boolean isFound() {
        return !shadowedWord.contains("*");
    }

    public boolean contains(char letter) {
        return word.contains(Character.toString(letter));
    }

    private String initializeRandomWordFromFile(String fileName) {
        String word = null;
        Path filePath = Paths.get(fileName);
        List<String> words;
        try {
            words = Files.readAllLines(filePath);
            Random rd = new Random();
            word = words.get(rd.nextInt(words.size()));
        } catch (IOException ex) {
            Logger.getLogger(Word.class.getName()).log(Level.SEVERE, null, ex);
            word = initializeRandomWordFromArray();
        }
        return word;
    }

    private String initializeRandomWordFromDb(String dbName) {
        String word = null;
        Path filename = Paths.get(dbName);
        word = DAOFactory.getWordDao().getRandomWord().getWord();
        return word;
    }

    private String initializeRandomWordFromArray() {
        String[] words = {"AMELIORATION", "PARAPLUIE", "VOITURE",
            "ECOLE", "MONTAGNE"};
        Random rd = new Random();
        return words[rd.nextInt(words.length)];
    }

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
