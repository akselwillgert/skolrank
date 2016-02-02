package se.subsurface.skolrank;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import se.subsurface.skolrank.model.Skola;

public class SchoolUtils {
    private static final int SCHOOL_NAME_LENGTH = 12;

    public static final int GRUND = 0;
    public static final int GYMNASIE = 1;

    public static final String MAP_POSITION = "map_position";
    public static String isMinusOne(int i, String unit) {
        if (i == -1)
            return "";
        else
            return Integer.toString(i) + unit;
    }

    public static String isMinusOne(double i, String unit) {
        if (i == -1)
            return "";
        else
            return Double.toString(i) + unit;
    }

    public static String getGradeText(int firstGrade, int lastGrade) {
        if (firstGrade == -1 && lastGrade == -1) {
            return "n/a";
        } else if (firstGrade == lastGrade) {
            return Integer.toString(firstGrade);
        } else if (firstGrade == -1) {
            return Integer.toString(lastGrade);
        } else if (lastGrade == -1) {
            return Integer.toString(firstGrade);
        } else {
            return firstGrade + "-" + lastGrade;
        }
    }

    public static int getIconResourceId(Skola skola, Context context) {

        if (skola.huvudman == Skola.Huvudman.FRI) {
            return R.drawable.ic_shield_fri_36;
        } else {
            String idString = "ic_kommun_" +
                    skola.kommun
                            .toLowerCase()
                            .replace("ö", "o")
                            .replace("ä", "a")
                            .replace("å", "a")
                            .replace(" ", "_")
                            .replace("-", "_");
            return context.getResources().getIdentifier(idString, "drawable",
                    context.getPackageName());
        }
    }

    public static String formatSchoolName(String name, int length) {
        String newName = name.replace("Skolan", "Sk.").replace("skolan", "sk.").replace("skola", "sk.");
        int realLength = newName.length();
        newName = newName.substring(0, Math.min(newName.length(), length));
        if (realLength > length) {
            newName += ".";
        }

        return newName;
    }


    public static void getMarkerView(Skola skola, Activity activity, View markerView) {

        ImageView mImageView = (ImageView) markerView.findViewById(R.id.kommun_shield);
        TextView nameTV = (TextView) markerView.findViewById(R.id.skolnamn);
        GoneTextView avgGradeTV = (GoneTextView) markerView.findViewById(R.id.avg_grade);
        GoneTextView foreignTV = (GoneTextView) markerView.findViewById(R.id.foreign);
        GoneTextView educatedTV = (GoneTextView) markerView.findViewById(R.id.educated_parents);
        GoneTextView girlsTV = (GoneTextView) markerView.findViewById(R.id.girls);
        nameTV.setText(SchoolUtils.formatSchoolName(skola.name, SCHOOL_NAME_LENGTH));
        if (skola.schoolType == 0) {
            avgGradeTV.setTextOrGone(skola.betyg + "", "p");
        } else if (skola.grades != null && skola.grades.length > 0) {
            avgGradeTV.setTextOrGone(skola.grades[0] + "", "p");
        }
        foreignTV.setTextOrGone(skola.foregin + "", "%");
        educatedTV.setTextOrGone(skola.educatedParents + "", "%");
        mImageView.setImageResource(SchoolUtils.getIconResourceId(skola, activity));
        if (skola.girls > 60 || skola.girls < 40) {
            girlsTV.setTextOrGone(skola.girls + "", "%");
        } else {
            girlsTV.setTextOrGone("", "");
        }

    }

}
