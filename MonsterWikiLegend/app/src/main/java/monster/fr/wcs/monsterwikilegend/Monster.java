package monster.fr.wcs.monsterwikilegend;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

public class Monster implements Parcelable {
    private int mTypeChoice;
    private String mMonsterName;
    private int mLife;
    private int mStrenght;
    private int mSpeed;
    private int mStamina;
    private String monsterFeatures;
    private Context mContext;
    private int mGrowState;

    public Monster(Context  context, String name, int type, int life, int strenght, int speed, int stamina){

        mTypeChoice = type;
        mMonsterName = name;
        mLife = life;
        mStrenght = strenght;
        mSpeed = speed;
        mStamina = stamina;
        mContext = context;
        mGrowState = 0;
    }

    public void grow() {

        if (mGrowState < 3){
            mGrowState = mGrowState + 1;
        }
        else {
            mGrowState = 0;
        }
    }

    protected Monster(Parcel in) {
        mTypeChoice = in.readInt();
        mMonsterName = in.readString();
        mLife = in.readInt();
        mStrenght = in.readInt();
        mSpeed = in.readInt();
        mStamina = in.readInt();
        monsterFeatures = in.readString();
        mGrowState = in.readInt();
    }

    public static final Creator<Monster> CREATOR = new Creator<Monster>() {
        @Override
        public Monster createFromParcel(Parcel in) {
            return new Monster(in);
        }

        @Override
        public Monster[] newArray(int size) {
            return new Monster[size];
        }
    };

    public String toString(){

        String[] typeNameString;
        typeNameString = mContext.getResources().getStringArray(R.array.typeNameString);
        monsterFeatures =  mMonsterName + " - " + typeNameString[mTypeChoice] + " - " + mLife + " - " + mStrenght + " - " + mSpeed +" - " + mStamina;
        return monsterFeatures;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mTypeChoice);
        dest.writeString(mMonsterName);
        dest.writeInt(mLife);
        dest.writeInt(mStrenght);
        dest.writeInt(mSpeed);
        dest.writeInt(mStamina);
        dest.writeString(monsterFeatures);
        dest.writeInt(mGrowState);
    }

    public int getmTypeChoice(){
        return this.mTypeChoice;
    }

    public int getmLife(){
        return this.mLife;
    }

    public int getmStrenght(){
        return this.mStrenght;
    }

    public int getmSpeed(){
        return this.mSpeed;
    }

    public int getmStamina(){
        return this.mStamina;
    }

    public String getmMonsterName(){
        return this.mMonsterName;
    }

    public int getmGrowState() { return  this.mGrowState; }
}
