package swishlive.com.swishlive;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;

@Parcel
public class TennisModel extends SportModel {

    public static final int SET_SCORE = 1;
    public static final int FIRST_POINT = 15;
    public static final int SECOND_POINT = 30;
    public static final int THIRD_POINT = 40;
    public static final int ADVANTAGE = -1;

    public String name;
    public TeamModel teamOne;
    public TeamModel teamTwo;
    public int setCounter;

    @ParcelConstructor
    public TennisModel(){

        this.name = "Tennis";
        this.setCounter = 0;
    }


    public void decrementScore(int howMuch, TeamModel teamModel) {

    }

    public void incrementScore(int howMuch, TeamModel teamModel) {

        if (howMuch != 0) {
            switch (howMuch) {

                case FIRST_POINT:
                    teamModel.setBasicScore(FIRST_POINT);
                    break;

                case SECOND_POINT:
                    teamModel.setBasicScore(SECOND_POINT);
                    break;

                case THIRD_POINT:
                    teamModel.setBasicScore(THIRD_POINT);
                    break;

                case ADVANTAGE:
                    teamModel.setBasicScore(ADVANTAGE);
                    break;
            }
        }
    }


    public void endOfSet() {
        this.setCounter ++;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
