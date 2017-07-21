package swishlive.com.swishlive;


import org.parceler.Parcel;
import org.parceler.ParcelConstructor;

@Parcel
public class GameModel {

    public TeamModel teamOne;
    public TeamModel teamTwo;
    public SportModel sportModel;

    @ParcelConstructor
    public GameModel(TeamModel teamOne, TeamModel teamTwo, SportModel sportModel){

        this.teamOne = teamOne;
        this.teamTwo = teamTwo;
        this.sportModel = sportModel;
    }

    public String getTeamScores() {

        StringBuilder sb = new StringBuilder();
        sb.append(teamOne.getName());
        sb.append(" ");
        sb.append(teamOne.getBasicScore());
        sb.append(" : ");
        sb.append(teamTwo.getBasicScore());
        sb.append(" ");
        sb.append(teamTwo.getName());

        return sb.toString();
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

    public SportModel getSportModel() {
        return sportModel;
    }

    public void setSportModel(SportModel sportModel) {
        this.sportModel = sportModel;
    }

}
