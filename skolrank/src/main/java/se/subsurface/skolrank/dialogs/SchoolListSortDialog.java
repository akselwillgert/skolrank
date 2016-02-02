package se.subsurface.skolrank.dialogs;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.ListAdapter;

import se.subsurface.skolrank.R;

public class SchoolListSortDialog extends DialogFragment {
    public static final String SORT_OPTION = "SortOption";
    // Use this instance of the interface to deliver action events
    private SortOptionDialogListener mListener;
    private int sortOption;

    // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        sortOption = getArguments().getInt(SORT_OPTION, 0);
        // Verify that the host activity implements the callback interface
        try {
            Log.d(getClass().getName(), "onAttach");
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (SortOptionDialogListener) activity;
        } catch (ClassCastException e) {
            Log.e(getClass().getName(), e.getMessage());
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement " + SortOptionDialogListener.class.getName());
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final String[] items = getResources().getStringArray(R.array.sort_options);
        final Integer[] colors = new Integer[]{
                ContextCompat.getColor((Context) mListener, R.color.black),  //grade
                ContextCompat.getColor((Context) mListener, R.color.skolrank_yellow),  //numStudents
                ContextCompat.getColor((Context) mListener, R.color.black), //schoolname
                ContextCompat.getColor((Context) mListener, R.color.skolrank_green), //qualified
                ContextCompat.getColor((Context) mListener, R.color.skolrank_blue), //educated parents
                ContextCompat.getColor((Context) mListener, R.color.skolrank_pink), //girls
                ContextCompat.getColor((Context) mListener, R.color.skolrank_red), //foreign
        };
        ListAdapter adapter = new ArrayAdapterWithColor(getActivity(), items, colors);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.Theme_MyTheme_AlertDialog);
        builder.setTitle(mListener.getSortLabel())
                .setAdapter(adapter, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        sortOption = item;
                        mListener.onDialogPositiveClick(sortOption);

                    }
                });

        return builder.create();
    }


    public interface SortOptionDialogListener {
        void onDialogPositiveClick(int sortOption);

        String getSortLabel();
    }
}