package swishlive.com.swishlive;


import org.parceler.Parcel;
import org.parceler.ParcelConstructor;

@Parcel
public class VolleyModel extends SportModel {

    public static final int POINT = 1;
    public static final int SET = 1;

    public String name;
    public TeamModel teamOne;
    public TeamModel teamTwo;

    @ParcelConstructor
    public VolleyModel() {
        this.name = "VolleyBall";
    }

    public void incrementScore(int howMuch, TeamModel teamModel) {

        if(howMuch == POINT && teamModel != null) {

            teamModel.setBasicScore(teamModel.getBasicScore() + howMuch);
        }
    }

    public void decrementScore(int howMuch, TeamModel teamModel) {

        if(howMuch == POINT && teamModel != null) {

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
