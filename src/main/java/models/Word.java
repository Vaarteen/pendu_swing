package models;

public class Word extends Bean {

    private String word;

    public Word() {
    }

    public Word(String word) {
        this.word = word;
    }

    public Word(Integer id, String word) {
        this.id = id;
        this.word = word;
    }

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
