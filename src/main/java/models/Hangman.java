package models;

import configuration.Helpers;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

/**
 * Le moteur du jeu. À noter que sans la console, l'interface Playable devient
 * caduque, mais je la garde pour l'historique.
 *
 * @author Herbert
 */
public class Hangman implements Playable {

    private final int ERROR_MAX; // Le nombre d'erreurs acceptables avant perte du jeu
    private final Collection<Character> proposedLetters; // La liste des lettres déjà prposées
    private final WordManager wordManager; // Le gestionnaire de Word
    private int cnt; // Le compteur de coups
    private int errorCnt; // Le compteur d'erreurs
    private static final Properties config = Helpers.readConfig(); // La configuration du jeu
    private boolean gameEnded; // Le jeu est-il fini ?

    /**
     * Constructeur.
     */
    public Hangman() {
        this.errorCnt = 0;
        this.cnt = 0;
        this.wordManager = new WordManager();
        this.proposedLetters = new ArrayList<>();
        this.ERROR_MAX = Integer.parseInt(config.getProperty("maxError"));
        this.gameEnded = true;
    }

    /* getters et setters */
    public Collection<Character> getProposedLetters() {
        return proposedLetters;
    }

    public int getCnt() {
        return cnt;
    }

    public int getErrorCnt() {
        return errorCnt;
    }

    public WordManager getWordManager() {
        return wordManager;
    }

    public boolean isGameEnded() {
        return gameEnded;
    }

    /**
     * La boucle principale de jeu en console.
     */
    @Override
    public void play() {
        wordManager.getShadowedWord(proposedLetters); // Génère le mot masqué
        char proposedLetter; // La lettre proposée par le joueur
        while (!wordManager.isFound() && (errorCnt < ERROR_MAX)) { // Boucle de jeu
            // On demande et récupère une lettre
            showKnownLetters();
            proposedLetter = askForLetter();
            // Si la lettre est déjà parmi celles proposées, on l'indique au joueur
            if (proposedLetters.contains(proposedLetter)) {
                System.out.println("Lettre déjà proposée.");
            } else { // sinon on ajoute la lettre à la collection et on la traite
                proposedLetters.add(proposedLetter); // Ajout à la collection
                cnt++; // On ajoute un coup au compteur
                // Si le mot à trouver ne contient pas la lettre proposée on
                // compte une erreur
                if (!wordManager.contains(proposedLetter)) {
                    errorCnt++;
                }
            }
            // Affichage du mot masqué
            System.out.println(wordManager.getShadowedWord(proposedLetters));
            // Affichage du nombre d'erreurs restant
            System.out.println("Il vous reste " + (ERROR_MAX - errorCnt) + " erreurs avant d'être pendu.");
        } // Fin boucle du jeu
        if (wordManager.isFound()) {
            System.out.println("Bravo, vous avez trouvé mon mot en " + cnt + " coups.");
        } else {
            System.out.println("COUIC !");
            System.out.println("Le mot à trouver était : " + this.wordManager.getWord());
        }
    }

    /**
     * Affiche la liste des lettres déjà proposées pour le jeu en console.
     */
    public void showKnownLetters() {
        System.out.print("Voici la liste des lettres que vous avez déjà proposées : ");
        List<Character> letters = new ArrayList<>(proposedLetters);
        Collections.sort(letters);
        for (char c : letters) {
            System.out.print(c + " ");
        }
        System.out.println("");
    }

    /**
     * Demande une lettre pour le jeu en console.
     *
     * @return
     */
    private char askForLetter() {
        char c;
        java.util.Scanner sc = new java.util.Scanner(System.in);
        do {
            System.out.print("Entrez une lettre : ");
            c = sc.next().toUpperCase().charAt(0);
        } while ((c < 'A') || (c > 'Z'));
        return c;
    }

    /**
     * Vérifie une lettre proposée par l'interface graphique. L'ajoute à la
     * liste des lettres proposées, augmente les compteurs.
     *
     * @param letter
     */
    public void proposeLetter(char letter) {
        if (!proposedLetters.contains(letter)) { // sinon on ajoute la lettre à la collection et on la traite
            proposedLetters.add(letter); // Ajout à la collection
            cnt++; // On ajoute un coup au compteur
            // Si le mot à trouver ne contient pas la lettre proposée on
            // compte une erreur
            if (!wordManager.contains(letter)) {
                errorCnt++;
            }
        }
    }

    /**
     * Retourne l'état du jeu.
     *
     * @return 0 : jeu en cours / 1 : jeu gagné / 2 : jeu perdu
     */
    public int checkState() {
        if (errorCnt >= ERROR_MAX) {
            gameEnded = true;
            return 2;
        }
        if (wordManager.isFound()) {
            gameEnded = true;
            return 1;
        }
        return 0;
    }

    /**
     * Crée un nouveau jeu. Réinitialisation des atriburs du jeu et nouveau mot.
     */
    public void newGame(User player) {
        cnt = 0;
        errorCnt = 0;
        proposedLetters.clear();
        wordManager.initializeRandomWord(player);
        gameEnded = false;
    }

}
