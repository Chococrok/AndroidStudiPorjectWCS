package swishlive.com.swishlive;


import org.parceler.Parcel;
import org.parceler.ParcelConstructor;

@Parcel
public class HandballModel extends SportModel implements ScoreModifier {

    public static final int BUT = 1;

    public String name;
    public TeamModel teamOne;
    public TeamModel teamTwo;

    @ParcelConstructor
    public HandballModel() {
        this.name = "Handball";
    }

    public void incrementScore(int howMuch, TeamModel teamModel) {

        if(howMuch == BUT && teamModel != null){

            teamModel.setBasicScore(teamModel.getBasicScore() + howMuch);
        }
    }

    @Override
    public void decrementScore(int howMuch, TeamModel teamModel) {

        if(howMuch == BUT && teamModel != null){

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
