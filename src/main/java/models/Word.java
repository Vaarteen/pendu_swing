package models;

import configuration.Helpers;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.List;
import java.util.Properties;
import java.util.Random;

/**
 *
 * @author Herbert
 */
public class Word {

    private String wordToFind;
    private String shadowedWord;
    private final Properties config;

    public Word() {
        this.config = Helpers.readConfig();
        initializeRandomWord();
    }

    public String getWordToFind() {
        return wordToFind;
    }

    public String getShadowedWord() {
        return shadowedWord;
    }

    /**
     * Tire un mot au hasard selon la source fournie dans les propriétés.
     *
     */
    private void initializeRandomWord() {
        String word;
        if (this.config.getProperty("wordSource").equals("file")
                && this.config.get("dictName") != null) {
            try {
                word = initializeRandomWordFromFile(
                        this.config.getProperty("dictName"));
            } catch (IOException ex) {
                System.err.println("Erreur d'accès au fichier dictionnaire. Utilisation d'un tableau de mots.");
                word = initializeRandomWordFromArray();
            }
        } else {
            word = initializeRandomWordFromArray();
        }
        this.wordToFind = cleanWord(word).toUpperCase();
    }

    /**
     * Retourne un mot constitué de lettres et d'étoiles.
     *
     * @param knownLetters : les lettres connues
     */
    public void createShadowedWord(Collection<Character> knownLetters) {
        StringBuilder shadow = new StringBuilder();
        for (int i = 0; i < wordToFind.length(); i++) {
            if (knownLetters.contains(wordToFind.charAt(i))) {
                shadow.append(wordToFind.charAt(i));
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
        return wordToFind.contains(Character.toString(letter));
    }

    private String initializeRandomWordFromFile(String name)
            throws IOException {
        String word = null;
        Path filename = Paths.get(name);
        if (Files.isReadable(filename)) {
            List<String> words = Files.readAllLines(filename);
            Random rd = new Random();
            word = words.get(rd.nextInt(words.size()));
        } else {
            throw new IOException("Fichier introuvable ou corrompu.");
        }
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
