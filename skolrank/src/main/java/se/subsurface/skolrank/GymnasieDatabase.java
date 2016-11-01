package se.subsurface.skolrank;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.google.android.gms.maps.model.LatLngBounds;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import static se.subsurface.skolrank.model.Skola.COLUMN_LASAR;
import static se.subsurface.skolrank.model.Skola.COLUMN_andel_kvinnor;
import static se.subsurface.skolrank.model.Skola.COLUMN_andel_m_hogutb_foraldrar;
import static se.subsurface.skolrank.model.Skola.COLUMN_andel_m_utl_bakgr;
import static se.subsurface.skolrank.model.Skola.COLUMN_andel_med_grundl_behorighet;
import static se.subsurface.skolrank.model.Skola.COLUMN_antal_elever;
import static se.subsurface.skolrank.model.Skola.COLUMN_gbp_for_elever_med_examen;
import static se.subsurface.skolrank.model.Skola.COLUMN_huvudman;
import static se.subsurface.skolrank.model.Skola.COLUMN_kommunnamn;
import static se.subsurface.skolrank.model.Skola.COLUMN_skola;
import static se.subsurface.skolrank.model.Skola.COLUMN_xpos;
import static se.subsurface.skolrank.model.Skola.COLUMN_ypos;


public class GymnasieDatabase extends SQLiteAssetHelper {
    private static final String DATABASE_NAME = "gymnasie.db";
    private static final int DATABASE_VERSION = 13;
    private static final String TAG = "GymnasieDatabase";

    public GymnasieDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        setForcedUpgrade();
    }

    public Cursor getSkola(MainActivity.CompareBy compareBy, String filterString, int year) {
        return getSkola(compareBy, filterString, year, null);
    }

    public Cursor getSkola(MainActivity.CompareBy compareBy, String filterString, int year, LatLngBounds bounds) {
        final String SORT_COLUMN = "sort_column";
        String programSortString = COLUMN_gbp_for_elever_med_examen;
        String schoolSortString = SORT_COLUMN;
        switch (compareBy) {

            case GRADE:
                programSortString = COLUMN_gbp_for_elever_med_examen;
                schoolSortString = SORT_COLUMN;
                break;
            case NUM_STUDENTS:
                schoolSortString = COLUMN_antal_elever;
                break;
            case NAME:
                schoolSortString = COLUMN_skola;
                break;
            case QUALIFIED_FOR_HIGH_SCHOOL:
                programSortString = COLUMN_andel_med_grundl_behorighet;
                schoolSortString = SORT_COLUMN;
                break;
            case EDUCATED_PARENTS:
                schoolSortString = COLUMN_andel_m_hogutb_foraldrar;
                break;
            case GIRLS:
                schoolSortString = COLUMN_andel_kvinnor;
                break;
            case FOREIGN_PARENTS:
                schoolSortString = COLUMN_andel_m_utl_bakgr;
                break;
        }

        Log.v(TAG, "schoolSortString=" + schoolSortString + " programSortString=" + programSortString + " filter=" + filterString);
        String trimFilter = filterString.trim();
        String where = "";
        if (bounds != null) {
            where += COLUMN_xpos + " BETWEEN " + bounds.southwest.latitude + " AND " + bounds.northeast.latitude +
                    " AND " + COLUMN_ypos + " BETWEEN " + bounds.southwest.longitude + " AND " + bounds.northeast.longitude + " AND ";
        }

        where += COLUMN_LASAR + "=" + year + " AND (" + COLUMN_kommunnamn + " like '%" + trimFilter + "%'" +
                " or " + COLUMN_skola + " like '%" + trimFilter + "%'" +
                " or " + COLUMN_huvudman + " like '%" + trimFilter + "%')";

        String programSql = "SELECT " + programSortString + " as sort_column, _id, skola_id, program, totalt_antal, andel_med_examen, andel_med_studiebevis, andel_med_grundl_behorighet,andel_med_utokat_prog, gbp_for_elever_med_examen_eller_studiebevis, gbp_for_elever_med_examen, lasar as program_lasar " +
                "from SKOLA_PROGRAM where program_lasar = " + year + " order by sort_column DESC";


        String sql = "select s._id as _id, s.skola as skola, s.huvudman as huvudman, kommunnamn,antal_elever, andel_kvinnor, andel_m_utl_bakgr, andel_m_hogutb_foraldrar, s.lasar as lasar, xpos, ypos, p._id as pid, " +
                "max(sort_column), p.skola_id as skola_id, group_concat(program) as program, group_concat(totalt_antal) as totalt_antal, group_concat(andel_med_examen) as andell_med_examen, group_concat(andel_med_studiebevis) as andel_med_studiebevis, group_concat(andel_med_grundl_behorighet) as andel_med_grundl_behorighet,group_concat(andel_med_utokat_prog) as andel_med_utokat_prog, group_concat(gbp_for_elever_med_examen_eller_studiebevis) as gbp_for_elever_med_examen_eller_studiebevis, group_concat(gbp_for_elever_med_examen) as gbp_for_elever_med_examen " +
                "from SKOLA as s, " +
                "(" + programSql + ") as p" +
                " where  p.skola_id = s.skola_id AND (" + where + ") group by s._id order by " + schoolSortString + " DESC";


        SQLiteDatabase db = getReadableDatabase();
        return db.rawQuery(sql, new String[]{});

    }

    public Cursor getProgram(int id) {
        Log.v(TAG, "getProgram() id=" + id);
        String sql = "SELECT _id, skola_id, program, totalt_antal, andel_med_examen, andel_med_studiebevis, andel_med_grundl_behorighet,andel_med_utokat_prog, gbp_for_elever_med_examen_eller_studiebevis, gbp_for_elever_med_examen, lasar " +
                "from SKOLA_PROGRAM where skola_id=" + id + " order by program, lasar ASC";

        SQLiteDatabase db = getReadableDatabase();
        return db.rawQuery(sql, new String[]{});

    }

    public Cursor getSkola(int id) {
        Log.v(TAG, "getSkola() id=" + id);
        String sql = "select _id, xpos, ypos, skola_id, skola, huvudman, kommunnamn, antal_elever, andel_kvinnor, andel_m_utl_bakgr, andel_m_hogutb_foraldrar, lasar from skola where skola_id=" + id + " ORDER BY lasar ASC";
        SQLiteDatabase db = getReadableDatabase();

        return db.rawQuery(sql, new String[]{});

    }
}
