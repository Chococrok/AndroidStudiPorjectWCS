package swishlive.com.swishlive;


import org.parceler.Parcel;

@Parcel
public class TeamModel {

    public String name;
    public int score;

    public TeamModel(){}

    public TeamModel(String name){
        this.name = name;
        this.score = 0;
    }

    public int getBasicScore(){
        return this.score;
    }

    public void setBasicScore(int newScore){
        this.score = newScore;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
