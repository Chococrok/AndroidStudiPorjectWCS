package patrimoine.wcs.fr.toulousemonuments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.net.Uri;

import patrimoine.wcs.fr.toulousemonuments.models.MonumentModel;

/**
 * Created by apprenti on 08/06/17.
 */

public abstract class BaseFragment extends Fragment {

    protected int mPosition;
    protected MonumentModel mMonumentModel;


    public BaseFragment(int mPosition, MonumentModel mMonumentModel) {
        this.mPosition = mPosition;
        this.mMonumentModel = mMonumentModel;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }
}
