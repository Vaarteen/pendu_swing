package models;

/**
 *
 * @author Herbert Caffarel
 */
public class User extends Bean {

    private String name;
    private int score;

    public User() {
    }

    public User(String name) {
        this.name = name;
    }

    public User(String name, int score) {
        this.name = name;
        this.score = score;
    }

    public User(int id_user, String name, int score) {
        this.name = name;
        this.score = score;
    }

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
