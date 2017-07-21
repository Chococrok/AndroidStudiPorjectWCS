package swishlive.com.swishlive;


import org.parceler.Parcel;
import org.parceler.ParcelConstructor;

@Parcel
public class SportModel {

    public TeamModel teamOne;
    public TeamModel teamTwo;
    public String name;

    @ParcelConstructor
    public SportModel(){}

    public SportModel(TeamModel teamOne, TeamModel teamTwo){

        this.teamOne = teamOne;
        this.teamTwo = teamTwo;
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

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }
}
