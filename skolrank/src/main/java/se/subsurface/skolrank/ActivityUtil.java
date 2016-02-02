package se.subsurface.skolrank;

import android.content.Context;
import android.content.Intent;

import static se.subsurface.skolrank.model.Skola.COLUMN_SKOLA_ID;

class ActivityUtil {

    public static void openSkolaActivity(Context context, int enhetskod, int type) {

        Intent myIntent;
        if (type == 0) {
            myIntent = new Intent(context, GrundSkolaActivity.class);
        } else {
            myIntent = new Intent(context, GymnasieSkolaActivity.class);
        }
        myIntent.putExtra(COLUMN_SKOLA_ID, enhetskod); //Optional parameters
        context.startActivity(myIntent);
    }
}
