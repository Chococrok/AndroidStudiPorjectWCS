package swishlive.com.swishlive;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;

@Parcel
public class RugbyModel extends SportModel implements ScoreModifier {

    public static final int ESSAI = 5;
    public static final int TRANSFORMATION = 2;
    public static final int PENALITE_DROP = 3;


    public String name;
    public TeamModel teamOne;
    public TeamModel teamTwo;

    @ParcelConstructor
    public RugbyModel() {
        this.name = "Rugby";
    }

    public void incrementScore(int howMuch, TeamModel teamModel) {

        if (howMuch == ESSAI || howMuch == TRANSFORMATION || howMuch == PENALITE_DROP && teamModel != null) {

            teamModel.setBasicScore(teamModel.getBasicScore() + howMuch);
        }
    }

    public void decrementScore(int howMuch, TeamModel teamModel) {

        if(howMuch == ESSAI || howMuch == TRANSFORMATION || howMuch == PENALITE_DROP && teamModel != null) {

            teamModel.setBasicScore(teamModel.getBasicScore() - howMuch);
        }
    }

    public TeamModel getTeamOne() {
        return teamOne;
    }

    public void setTeamOne(TeamModel teamOne) {
        this.teamOne = teamOne;
    }

    public TeamModel getTeamTwo() {
        return teamTwo;
    }

    public void setTeamTwo(TeamModel teamTwo) {
        this.teamTwo = teamTwo;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }
}
