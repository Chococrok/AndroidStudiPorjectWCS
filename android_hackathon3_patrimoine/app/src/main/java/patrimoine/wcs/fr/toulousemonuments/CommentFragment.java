package patrimoine.wcs.fr.toulousemonuments;

import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import patrimoine.wcs.fr.toulousemonuments.models.Fields;
import patrimoine.wcs.fr.toulousemonuments.models.MonumentModel;


public class CommentFragment extends BaseFragment {

    public static final String COMMENT = "comment";

    private DatabaseReference mDatabaseReference;

    public CommentFragment(int position, MonumentModel model) {
        super(position, model);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        if (container == null) {
            // We have different layouts, and in one of them this
            // fragment's containing frame doesn't exist.  The fragment
            // may still be created from its saved state, but there is
            // no reason to try to create its view hierarchy because it
            // won't be displayed.  Note this is not needed -- we could
            // just run the code below, where we would create and return
            // the view hierarchy; it would just never be used.
            return null;
        }
        View v = inflater.inflate(R.layout.fragment_comment, container, false);


        Fields currentFields = mMonumentModel.getRecords().get(mPosition).getFields();
        mDatabaseReference = FirebaseDatabase.getInstance()
                .getReference()
                .child(currentFields.getNomCdt())
                .child(COMMENT);
        CommentAdapter commentAdapter = new CommentAdapter(mDatabaseReference, String.class, R.layout.comment_item, getActivity());

        ListView listViewComment = (ListView) v.findViewById(R.id.listViewComment);
        listViewComment.setAdapter(commentAdapter);

        FloatingActionButton fab = (FloatingActionButton) v.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder dayDialog = new AlertDialog.Builder(container.getContext());
                dayDialog.setTitle(container.getResources().getString(R.string.comment));
                final EditText input = new EditText(container.getContext());
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                input.setLayoutParams(lp);
                dayDialog.setView(input);


                dayDialog.setPositiveButton(container.getResources().getString(R.string.validate), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mDatabaseReference.push().setValue(input.getText().toString());
                        dialog.dismiss();
                    }
                });

                dayDialog.setNegativeButton(container.getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();

                    }
                }).show();
            }
        });
        return v;
    }

}
