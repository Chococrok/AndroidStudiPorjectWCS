package swishlive.com.swishlive;


public interface ScoreModifier {

    void incrementScore(int howMuch, TeamModel teamModel);
    void decrementScore(int howMuch, TeamModel teamModel);
}