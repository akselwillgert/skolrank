package se.subsurface.skolrank.dialogs;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.Toast;

import se.subsurface.skolrank.R;

public class YearDialog extends DialogFragment {
    public static final String YEAR = "year";
    // Use this instance of the interface to deliver action events
    private YearDialogListener mListener;
    private int year;

    // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        year = getArguments().getInt(YEAR);
        // Verify that the host activity implements the callback interface
        try {
            Log.d(getClass().getName(), "onAttach");
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (YearDialogListener) activity;
        } catch (ClassCastException e) {
            Log.e(getClass().getName(), e.getMessage());
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement " + YearDialogListener.class.getName());
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final String[] items = getResources().getStringArray(R.array.year_dialog_options);
        ListAdapter adapter = new ArrayAdapter<>(getActivity(), android.R.layout.select_dialog_item, items);
        //      ListAdapter adapter = new ArrayAdapterWithIcon(getActivity(), items, icons);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle(R.string.year_dialog_title)
                .setAdapter(adapter, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        year = Integer.valueOf(items[item]);
                        mListener.onYearDialogClick(year);
                        Toast.makeText(
                                getActivity(),
                                getResources().getString(R.string.school_type_toast) + " " + items[item],
                                Toast.LENGTH_LONG).show();
                    }
                });

        return builder.create();
    }


    public interface YearDialogListener {
        void onYearDialogClick(int year);
    }
}