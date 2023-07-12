package pendu;

import configuration.Helpers;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import models.Playable;
import models.Word;

/**
 *
 * @author Herbert
 */
public class Hangman implements Playable {

    private final int ERROR_MAX;
    private final Collection<Character> proposedLetters;
    private final Word word;
    private int cnt;
    private int errorCnt;
    Properties config;

    public Hangman() {
        this.config = Helpers.readConfig();
        this.errorCnt = 0;
        this.cnt = 0;
        this.word = new Word();
        this.proposedLetters = new ArrayList<>();
        this.ERROR_MAX = Integer.parseInt(this.config.getProperty("maxError"));
    }

    public Word getWord() {
        return word;
    }

    public Collection<Character> getProposedLetters() {
        return proposedLetters;
    }

    public int getCnt() {
        return cnt;
    }

    public int getErrorCnt() {
        return errorCnt;
    }

    @Override
    public void play() {
        word.createShadowedWord(proposedLetters); // Génère le mot masqué
        char proposedLetter; // La lettre proposée par le joueur
        while (!word.isFound() && (errorCnt < ERROR_MAX)) { // Boucle de jeu
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
                if (!word.contains(proposedLetter)) {
                    errorCnt++;
                }
            }
            // Affichage du mot masqué
            word.createShadowedWord(proposedLetters);
            System.out.println(word.getShadowedWord());
            // Affichage du nombre d'erreurs restant
            System.out.println("Il vous reste " + (ERROR_MAX - errorCnt) + " erreurs avant d'être pendu.");
        } // Fin boucle du jeu
        if (word.isFound()) {
            System.out.println("Bravo, vous avez trouvé mon mot en " + cnt + " coups.");
        } else {
            System.out.println("COUIC !");
            System.out.println("Le mot à trouver était : " + this.word.getWordToFind());
        }
    }

    public void showKnownLetters() {
        System.out.print("Voici la liste des lettres que vous avez déjà proposées : ");
        List<Character> letters = new ArrayList<>(proposedLetters);
        Collections.sort(letters);
        for (char c : letters) {
            System.out.print(c + " ");
        }
        System.out.println("");
    }

    private char askForLetter() {
        char c;
        java.util.Scanner sc = new java.util.Scanner(System.in);
        do {
            System.out.print("Entrez une lettre : ");
            c = sc.next().toUpperCase().charAt(0);
        } while ((c < 'A') || (c > 'Z'));
        return c;
    }

}
