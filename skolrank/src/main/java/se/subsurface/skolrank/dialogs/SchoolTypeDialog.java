package se.subsurface.skolrank.dialogs;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.Toast;

import se.subsurface.skolrank.R;

public class SchoolTypeDialog extends DialogFragment {
    public static final String SCHOOL_TYPE = "SchoolType";
    // Use this instance of the interface to deliver action events
    private SchoolTypeDialogListener mListener;
    private int schoolType;

    // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        schoolType = getArguments().getInt(SCHOOL_TYPE, 0);
        // Verify that the host activity implements the callback interface
        try {
            Log.d(getClass().getName(), "onAttach");
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (SchoolTypeDialogListener) context;
        } catch (ClassCastException e) {
            Log.e(getClass().getName(), e.getMessage());
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(context.toString()
                    + " must implement " + SchoolTypeDialogListener.class.getName());
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final String[] items = getResources().getStringArray(R.array.school_type_options);
        ListAdapter adapter = new ArrayAdapter<>(getActivity(), android.R.layout.select_dialog_item, items);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle(R.string.school_type_dialog_title)
                .setAdapter(adapter, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        schoolType = item;
                        mListener.onDialogClick(schoolType);
                        Toast.makeText(
                                getActivity(),
                                getResources().getString(R.string.school_type_toast) + " " + items[item],
                                Toast.LENGTH_LONG).show();
                    }
                });

        return builder.create();
    }


    public interface SchoolTypeDialogListener {
        void onDialogClick(int schoolType);
    }
}