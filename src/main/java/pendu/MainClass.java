package pendu;

/**
 * Ce programme de pendu souffre d'un certain nombre de maux, mais il
 * fonctionne. Le but est de trouver ce qu'on peut améliorer, comment, et
 * enchainer sur l'apprentissage des différentes solutions que propose Java pour
 * mettre en oeuvre ce qu'on a imaginé.
 *
 * @author Herbert
 * @version 1.0
 */
public class MainClass {

    /**
     * Le programme principal. Il se lance dès qu'on lance le programme. Les
     * paramètres sont ceux passés en ligne de commande récupérés sous forme de
     * tableau de String.
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Hangman pendu = new Hangman();
        pendu.play();
    }

}
