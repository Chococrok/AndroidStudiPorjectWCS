package swishlive.com.swishlive;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.widget.Chronometer;


public abstract class SportFragment extends Fragment {

    public OnFragmentClickerInteractionListener mListener;
    protected Chronometer chronometer;
    protected String sportSequence;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentClickerInteractionListener) {
            mListener = (OnFragmentClickerInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    public interface OnFragmentClickerInteractionListener {
        void onFragmentClickerInteraction();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public String getChronoTime(){

        if (chronometer != null) {
            return String.valueOf(chronometer.getText());
        }
        return "null";
    }

    public String getSportSequence() {
        return sportSequence;
    }
}
