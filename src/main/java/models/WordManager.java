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
 *
 * @author Herbert
 */
public class WordManager {
    
    private Word word;
    private String shadowedWord;
    private static final Properties config = Helpers.readConfig();

    public WordManager() {
        this.word = new Word();
        initializeRandomWord();
    }

    public WordManager(Word word) {
        this.word = word;
    }

    public String getWord() {
        return word.getWord();
    }

    public String getShadowedWord() {
        return shadowedWord;
    }

    @Override
    public String toString() {
        return word.toString();
    }

    /**
     * Tire un mot au hasard selon la source fournie dans les propriétés.
     *
     */
    public void initializeRandomWord() {
        String w = null;
        switch (config.getProperty("wordSource")) {
            case "file":
                w = initializeRandomWordFromFile(config.getProperty("dictName"));
                break;
            case "database":
                w = initializeRandomWordFromDb(config.getProperty("dbName"));
                break;
            default:
                initializeRandomWordFromArray();
        }
        this.word.setWord(cleanWord(w).toUpperCase());
    }

    /**
     * Retourne un mot constitué de lettres et d'étoiles.
     *
     * @param knownLetters : les lettres connues
     */
    public void createShadowedWord(Collection<Character> knownLetters) {
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

    public boolean isFound() {
        return !shadowedWord.contains("*");
    }

    public boolean contains(char letter) {
        return word.getWord().contains(Character.toString(letter));
    }

    private String initializeRandomWordFromFile(String fileName) {
        String w = null;
        Path filePath = Paths.get(fileName);
        try {
            List<String> words;
            words = Files.readAllLines(filePath);
            Random rd = new Random();
            w = words.get(rd.nextInt(words.size()));
        } catch (Exception ex) {
            Logger.getLogger(WordManager.class.getName()).log(Level.SEVERE, null, ex);
            w = initializeRandomWordFromArray();
        }
        return w;
    }

    private String initializeRandomWordFromDb(String dbName) {
        String w;
        try {
            w = DAOFactory.getWordDao().getRandomWord().getWord();
        } catch (Exception ex) {
            Logger.getLogger(WordManager.class.getName()).log(Level.SEVERE, null, ex);
            w = initializeRandomWordFromArray();
        }
        return w;
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
