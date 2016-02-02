package se.subsurface.skolrank.list;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import se.subsurface.skolrank.GymnasieSkolaActivity;
import se.subsurface.skolrank.MainActivity;
import se.subsurface.skolrank.R;
import se.subsurface.skolrank.SchoolUtils;
import se.subsurface.skolrank.model.Skola;

import static se.subsurface.skolrank.model.Skola.COLUMN_SKOLA_ID;

public class GymnasieSkolaAdapter extends CursorAdapter implements AdapterView.OnItemClickListener {
    @SuppressWarnings("unused")
    private static final String TAG = "GymnasieSkolaAdapter";
    private final Context mContext;
    private MainActivity.CompareBy sortOrder;

    public GymnasieSkolaAdapter(Context context, MainActivity.CompareBy sortOrder) {
        super(context, null, 0);
        this.sortOrder = sortOrder;
        this.mContext = context;

    }

    public void setSortOrder(MainActivity.CompareBy sortOrder) {
        this.sortOrder = sortOrder;
    }

    // The newView method is used to inflate a new view and return it,
    // you don't bind any data to the view at this point.
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_item_gymnasie, parent, false);
        ViewHolder viewHolder = new ViewHolder();
        viewHolder.title = (TextView) view.findViewById(R.id.list_item_school_name);
        viewHolder.numStudentsTV = (TextView) view.findViewById(R.id.numStudents);
        viewHolder.kommunNameTV = (TextView) view.findViewById(R.id.kommun_namn);
        viewHolder.foreignTV = (TextView) view.findViewById(R.id.foreign_parents);
        viewHolder.educatedTV = (TextView) view.findViewById(R.id.educated_parents);
        viewHolder.girlRatioTV = (TextView) view.findViewById(R.id.andel_flickor);
        viewHolder.schoolType = (ImageView) view.findViewById(R.id.school_type_image);
        viewHolder.programsView = (ViewGroup) view.findViewById(R.id.programs);
        viewHolder.programsViewHeader = viewHolder.programsView.getChildAt(0);
        view.setTag(viewHolder);
        return view;
    }

    private void setTextAppearance(TextView view, @SuppressWarnings("SameParameterValue") int resId) {
        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            //noinspection deprecation
            view.setTextAppearance(mContext, resId);
        } else {
            view.setTextAppearance(resId);
        }
    }


    // The bindView method is used to bind all data to a given view
    // such as setting the text on a TextView.
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder viewHolder = (ViewHolder) view.getTag();
        Skola skola = new Skola(cursor, 1, true);

        viewHolder.title.setText(skola.name);
        viewHolder.kommunNameTV.setText(skola.kommun);
        viewHolder.educatedTV.setText(SchoolUtils.isMinusOne(skola.educatedParents, "%"));
        viewHolder.foreignTV.setText(SchoolUtils.isMinusOne(skola.foregin, "%"));
        viewHolder.girlRatioTV.setText(SchoolUtils.isMinusOne(skola.girls, "%"));

        switch (sortOrder) {
            case GRADE:
                break;
            case NUM_STUDENTS:
                setTextAppearance(viewHolder.numStudentsTV, R.style.sort_option_highlight);
                break;
            case NAME:
                setTextAppearance(viewHolder.title, R.style.sort_option_highlight);
                break;
            case QUALIFIED_FOR_HIGH_SCHOOL:
                break;
            case EDUCATED_PARENTS:
                setTextAppearance(viewHolder.educatedTV, R.style.sort_option_highlight);
                break;
            case GIRLS:
                setTextAppearance(viewHolder.girlRatioTV, R.style.sort_option_highlight);
                break;
            case FOREIGN_PARENTS:
                setTextAppearance(viewHolder.foreignTV, R.style.sort_option_highlight);
                break;
        }


        //Bilden
        int shieldId = SchoolUtils.getIconResourceId(skola, context);
        viewHolder.schoolType.setImageResource(shieldId);


        viewHolder.numStudentsTV.setText(SchoolUtils.isMinusOne(skola.antalElever, context.getString(R.string.unit_num_students)));


        //  View programHeader = ((ViewGroup) viewHolder.programsView).getChildAt(0);
        viewHolder.programsView.removeAllViews();
        viewHolder.programsView.addView(viewHolder.programsViewHeader);


        for (int i = 0; i < skola.programs.length; i++) {

            View programView = LayoutInflater.from(context).inflate(R.layout.list_item_gymnasie_program, viewHolder.programsView, false);
            //   ((ViewGroup) viewHolder.programsView).addView(programView);
            viewHolder.programsView.addView(programView);
            TextView programTV = (TextView) programView.findViewById(R.id.program_namn);
            programTV.setText(skola.programs[i]);

            TextView qualifiedTV = (TextView) programView.findViewById(R.id.qualifiedForHighSchool);

            qualifiedTV.setText(SchoolUtils.isMinusOne(skola.qualifieds[i], "%"));

            TextView avgGradeTV = (TextView) programView.findViewById(R.id.avgGrade);

            avgGradeTV.setText(SchoolUtils.isMinusOne(skola.grades[i], ""));

            TextView antal = (TextView) programView.findViewById(R.id.numStudents_grade_3);

            antal.setText(SchoolUtils.isMinusOne(skola.totaltAntals[i], context.getString(R.string.unit_num_students)));

            if (sortOrder == MainActivity.CompareBy.GRADE) {
                setTextAppearance(avgGradeTV, R.style.sort_option_highlight);
            } else if (sortOrder == MainActivity.CompareBy.QUALIFIED_FOR_HIGH_SCHOOL) {
                setTextAppearance(qualifiedTV, R.style.sort_option_highlight);
            }

        }

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
        final int enhetskod = getCursor().getInt(getCursor().getColumnIndexOrThrow(COLUMN_SKOLA_ID));
        Intent myIntent = new Intent(mContext, GymnasieSkolaActivity.class);
        myIntent.putExtra(COLUMN_SKOLA_ID, enhetskod); //Optional parameters
        mContext.startActivity(myIntent);
    }


    public class ViewHolder {
        TextView title;
        TextView numStudentsTV;
        TextView kommunNameTV;
        TextView foreignTV;
        TextView educatedTV;
        TextView girlRatioTV;

        ImageView schoolType;
        ViewGroup programsView;
        View programsViewHeader;
    }

}
