package swishlive.com.swishlive;


import org.parceler.Parcel;
import org.parceler.ParcelConstructor;

@Parcel
public class BasketballModel extends SportModel implements ScoreModifier{

    public static final int ONE_POINT = 1;
    public static final int TWO_POINTS = 2;
    public static final int THREE_POINTS = 3;

    public String name;
    public TeamModel teamOne;
    public TeamModel teamTwo;

    @ParcelConstructor
    public BasketballModel() {
        this.name = "Basketball";
    }

    public void incrementScore(int howMuch, TeamModel teamModel) {

        if(howMuch == ONE_POINT || howMuch == TWO_POINTS || howMuch == THREE_POINTS && teamModel != null){

            teamModel.setBasicScore(teamModel.getBasicScore() + howMuch);
        }
    }

    public void decrementScore(int howMuch, TeamModel teamModel) {

        if(howMuch == ONE_POINT || howMuch == TWO_POINTS || howMuch == THREE_POINTS && teamModel != null) {

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
