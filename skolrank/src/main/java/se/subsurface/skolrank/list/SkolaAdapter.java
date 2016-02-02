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

import se.subsurface.skolrank.GrundSkolaActivity;
import se.subsurface.skolrank.MainActivity;
import se.subsurface.skolrank.R;
import se.subsurface.skolrank.SchoolUtils;
import se.subsurface.skolrank.model.Skola;

import static se.subsurface.skolrank.model.Skola.COLUMN_SKOLA_ID;

public class SkolaAdapter extends CursorAdapter implements AdapterView.OnItemClickListener {
    @SuppressWarnings("unused")
    private static final String TAG = "SkolaAdapter";
    private final Context mContext;
    private MainActivity.CompareBy sortOrder;

    public SkolaAdapter(Context context, MainActivity.CompareBy sortOrder) {
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
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_item_grund, parent, false);
        ViewHolder viewHolder = new ViewHolder();
        viewHolder.title = (TextView) view.findViewById(R.id.list_item_school_name);
        viewHolder.numStudentsTV = (TextView) view.findViewById(R.id.numStudents);
        viewHolder.kommunNameTV = (TextView) view.findViewById(R.id.kommun_namn);
        viewHolder.utlandskTV = (TextView) view.findViewById(R.id.andel_elever_med_utlandsk_bakgrund_ak_1_9);
        viewHolder.educatedTV = (TextView) view.findViewById(R.id.andel_elever_med_foraldrar_med_eftergymnasial_utb_ak_1_9);
        viewHolder.girlRatioTV = (TextView) view.findViewById(R.id.andel_flickor_arskurs_1_9);
        viewHolder.qualifiedTV = (TextView) view.findViewById(R.id.qualifiedForHighSchool);
        viewHolder.avgGradeTV = (TextView) view.findViewById(R.id.avgGrade);
        viewHolder.grades = (TextView) view.findViewById(R.id.grades);
        viewHolder.schoolType = (ImageView) view.findViewById(R.id.school_type_image);
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
        Skola skola = new Skola(cursor, 0, false);


        viewHolder.kommunNameTV.setText(skola.kommun);
        viewHolder.utlandskTV.setText(SchoolUtils.isMinusOne(skola.foregin, "%"));
        viewHolder.educatedTV.setText(SchoolUtils.isMinusOne(skola.educatedParents, "%"));
        viewHolder.girlRatioTV.setText(SchoolUtils.isMinusOne(skola.girls, "%"));
        viewHolder.grades.setText(SchoolUtils.getGradeText(skola.firstGrade, skola.lastGrade));
        viewHolder.qualifiedTV.setText(SchoolUtils.isMinusOne(skola.qualified, "%"));
        viewHolder.numStudentsTV.setText(SchoolUtils.isMinusOne(skola.numStudents, context.getResources().getString(R.string.unit_num_students)));
        viewHolder.title.setText(skola.name);
        viewHolder.avgGradeTV.setText(SchoolUtils.isMinusOne(skola.betyg, "p"));


        //Bilden
        int shieldId = SchoolUtils.getIconResourceId(skola, context);
        viewHolder.schoolType.setImageResource(shieldId);

        switch (sortOrder) {
            case GRADE:
                setTextAppearance(viewHolder.avgGradeTV, R.style.sort_option_highlight);
                break;
            case NUM_STUDENTS:
                setTextAppearance(viewHolder.numStudentsTV, R.style.sort_option_highlight);
                break;
            case NAME:
                setTextAppearance(viewHolder.title, R.style.sort_option_highlight);
                break;
            case QUALIFIED_FOR_HIGH_SCHOOL:
                setTextAppearance(viewHolder.qualifiedTV, R.style.sort_option_highlight);
                break;
            case EDUCATED_PARENTS:
                setTextAppearance(viewHolder.educatedTV, R.style.sort_option_highlight);
                break;
            case GIRLS:
                setTextAppearance(viewHolder.girlRatioTV, R.style.sort_option_highlight);
                break;
            case FOREIGN_PARENTS:
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
        final int enhetskod = getCursor().getInt(getCursor().getColumnIndexOrThrow(COLUMN_SKOLA_ID));
        Intent myIntent = new Intent(mContext, GrundSkolaActivity.class);
        myIntent.putExtra(COLUMN_SKOLA_ID, enhetskod); //Optional parameters
        mContext.startActivity(myIntent);
    }


    public class ViewHolder {
        TextView title;
        TextView numStudentsTV;
        TextView kommunNameTV;
        TextView utlandskTV;
        TextView educatedTV;
        TextView girlRatioTV;
        TextView qualifiedTV;
        TextView avgGradeTV;
        TextView grades;
        ImageView schoolType;
    }

}
