package se.subsurface.skolrank;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.util.Log;

import com.google.android.gms.maps.model.LatLngBounds;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import static se.subsurface.skolrank.model.Skola.COLUMN_ID;
import static se.subsurface.skolrank.model.Skola.COLUMN_LASAR;
import static se.subsurface.skolrank.model.Skola.COLUMN_SKOLA_ID;
import static se.subsurface.skolrank.model.Skola.COLUMN_andel_elever_med_foraldrar_med_eftergymnasial_utb_ak_1_9;
import static se.subsurface.skolrank.model.Skola.COLUMN_andel_elever_med_utlandsk_bakgrund_ak_1_9;
import static se.subsurface.skolrank.model.Skola.COLUMN_andel_flickor_arskurs_1_9;
import static se.subsurface.skolrank.model.Skola.COLUMN_andel_flickor_forskoleklass;
import static se.subsurface.skolrank.model.Skola.COLUMN_andel_som_uppnatt_kunskapskraven_i_alla_amnen;
import static se.subsurface.skolrank.model.Skola.COLUMN_antal_elever;
import static se.subsurface.skolrank.model.Skola.COLUMN_elever_arskurs_1;
import static se.subsurface.skolrank.model.Skola.COLUMN_elever_arskurs_1_9;
import static se.subsurface.skolrank.model.Skola.COLUMN_elever_arskurs_2;
import static se.subsurface.skolrank.model.Skola.COLUMN_elever_arskurs_3;
import static se.subsurface.skolrank.model.Skola.COLUMN_elever_arskurs_4;
import static se.subsurface.skolrank.model.Skola.COLUMN_elever_arskurs_5;
import static se.subsurface.skolrank.model.Skola.COLUMN_elever_arskurs_6;
import static se.subsurface.skolrank.model.Skola.COLUMN_elever_arskurs_7;
import static se.subsurface.skolrank.model.Skola.COLUMN_elever_arskurs_8;
import static se.subsurface.skolrank.model.Skola.COLUMN_elever_arskurs_9;
import static se.subsurface.skolrank.model.Skola.COLUMN_elever_forskoleklass;
import static se.subsurface.skolrank.model.Skola.COLUMN_genomsnittligt_meritvarde_16;
import static se.subsurface.skolrank.model.Skola.COLUMN_huvudman;
import static se.subsurface.skolrank.model.Skola.COLUMN_kommunnamn;
import static se.subsurface.skolrank.model.Skola.COLUMN_skola;
import static se.subsurface.skolrank.model.Skola.COLUMN_xpos;
import static se.subsurface.skolrank.model.Skola.COLUMN_ypos;
import static se.subsurface.skolrank.model.Skola.TABLE_SKOLA;

public class GrundDatabase extends SQLiteAssetHelper {

    private static final String DATABASE_NAME = "school.db";
    private static final int DATABASE_VERSION = 11;
    private static final String TAG = "GrundDatabase";

    public GrundDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        setForcedUpgrade();
    }

    public static String getSortString(MainActivity.CompareBy sortComparator) {
        String sortString = COLUMN_genomsnittligt_meritvarde_16;
        if (sortComparator.equals(MainActivity.CompareBy.GRADE)) {
            sortString = COLUMN_genomsnittligt_meritvarde_16;
        } else if (sortComparator == MainActivity.CompareBy.NUM_STUDENTS) {
            sortString = COLUMN_elever_arskurs_1_9;
        } else if (sortComparator == MainActivity.CompareBy.QUALIFIED_FOR_HIGH_SCHOOL) {
            sortString = COLUMN_andel_som_uppnatt_kunskapskraven_i_alla_amnen;
        } else if (sortComparator == MainActivity.CompareBy.NAME) {
            sortString = COLUMN_skola;
        } else if (sortComparator == MainActivity.CompareBy.FOREIGN_PARENTS) {
            sortString = COLUMN_andel_elever_med_utlandsk_bakgrund_ak_1_9;
        } else if (sortComparator == MainActivity.CompareBy.EDUCATED_PARENTS) {
            sortString = COLUMN_andel_elever_med_foraldrar_med_eftergymnasial_utb_ak_1_9;
        } else if (sortComparator == MainActivity.CompareBy.GIRLS) {
            sortString = COLUMN_andel_flickor_arskurs_1_9;
        }
        return sortString;
    }

    public Cursor getGrundskola(int enhetskod) {
        Log.v(TAG, "getGrundskola() enhetskod=" + enhetskod);
        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        //   String[] sqlSelect = {"0 _id", "longname"};
        String sqlTables = TABLE_SKOLA;

        String[] sqlSelect = {
                COLUMN_ID,
                COLUMN_SKOLA_ID,
                COLUMN_LASAR,
                COLUMN_skola,
                COLUMN_genomsnittligt_meritvarde_16,
                COLUMN_huvudman,
                COLUMN_antal_elever,
                COLUMN_kommunnamn,
                COLUMN_andel_som_uppnatt_kunskapskraven_i_alla_amnen,
//                COLUMN_andel_som_ej_uppnatt_kunskapskraven_i_ett_amne,
//                COLUMN_andel_som_ej_uppnatt_kunskapskraven_i_tva_eller_fler_amnen,
//                COLUMN_andel_som_saknar_betyg_i_alla_amnen,
//                COLUMN_andel_behoriga_till_yrkesprogram,
//                COLUMN_andel_behoriga_till_estetiskt_program,
//                COLUMN_andel_behoriga_till_ekonomi_humanistiska_och_samhallsvetenskapsprogram,
//                COLUMN_andel_behoriga_till_naturvetenskapligt_och_tekniskt_program,

                COLUMN_elever_forskoleklass,
                COLUMN_andel_flickor_forskoleklass,
                COLUMN_elever_arskurs_1_9,
                COLUMN_andel_flickor_arskurs_1_9,
                COLUMN_andel_elever_med_utlandsk_bakgrund_ak_1_9,
                COLUMN_andel_elever_med_foraldrar_med_eftergymnasial_utb_ak_1_9,
                COLUMN_xpos,
                COLUMN_ypos,
                COLUMN_elever_arskurs_1,
                COLUMN_elever_arskurs_2,
                COLUMN_elever_arskurs_3,
                COLUMN_elever_arskurs_4,
                COLUMN_elever_arskurs_5,
                COLUMN_elever_arskurs_6,
                COLUMN_elever_arskurs_7,
                COLUMN_elever_arskurs_8,
                COLUMN_elever_arskurs_9

        };

        String where = COLUMN_SKOLA_ID + " = " + enhetskod;
        String orderBy = "lasar ASC";

        qb.setTables(sqlTables);
        Cursor c = qb.query(db, sqlSelect, where, null,
                null, null, orderBy);

        c.moveToFirst();
        return c;

    }

    public Cursor getCity(String sortColumn, String filterString, int year, LatLngBounds bounds) {
        Log.e(TAG, "sortColumn=" + sortColumn + " filter=" + filterString);
        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        //   String[] sqlSelect = {"0 _id", "longname"};
        String sqlTables = TABLE_SKOLA;

        String[] sqlSelect = {
                COLUMN_ID,
                COLUMN_SKOLA_ID,
                COLUMN_LASAR,
                COLUMN_skola,
                COLUMN_genomsnittligt_meritvarde_16,
                COLUMN_huvudman,
                COLUMN_antal_elever,
                COLUMN_kommunnamn,
                COLUMN_andel_som_uppnatt_kunskapskraven_i_alla_amnen,
//                COLUMN_andel_som_ej_uppnatt_kunskapskraven_i_ett_amne,
//                COLUMN_andel_som_ej_uppnatt_kunskapskraven_i_tva_eller_fler_amnen,
//                COLUMN_andel_som_saknar_betyg_i_alla_amnen,
//                COLUMN_andel_behoriga_till_yrkesprogram,
//                COLUMN_andel_behoriga_till_estetiskt_program,
//                COLUMN_andel_behoriga_till_ekonomi_humanistiska_och_samhallsvetenskapsprogram,
//                COLUMN_andel_behoriga_till_naturvetenskapligt_och_tekniskt_program,

                COLUMN_elever_forskoleklass,
                COLUMN_andel_flickor_forskoleklass,
                COLUMN_elever_arskurs_1_9,
                COLUMN_andel_flickor_arskurs_1_9,
                COLUMN_andel_elever_med_utlandsk_bakgrund_ak_1_9,
                COLUMN_andel_elever_med_foraldrar_med_eftergymnasial_utb_ak_1_9,
                COLUMN_elever_arskurs_1,
                COLUMN_elever_arskurs_2,
                COLUMN_elever_arskurs_3,
                COLUMN_elever_arskurs_4,
                COLUMN_elever_arskurs_5,
                COLUMN_elever_arskurs_6,
                COLUMN_elever_arskurs_7,
                COLUMN_elever_arskurs_8,
                COLUMN_elever_arskurs_9,
                COLUMN_xpos,
                COLUMN_ypos,

        };

        String trimFilter = filterString.trim();
        String where = "";
        if (bounds != null) {
            where += COLUMN_xpos + " BETWEEN " + bounds.southwest.latitude + " AND " + bounds.northeast.latitude +
                    " AND " + COLUMN_ypos + " BETWEEN " + bounds.southwest.longitude + " AND " + bounds.northeast.longitude + " AND ";
        }
        where += COLUMN_LASAR + " = " + year + " AND (" + COLUMN_kommunnamn + " like '%" + trimFilter + "%'" +
                " or " + COLUMN_skola + " like '%" + trimFilter + "%'" +
                " or " + COLUMN_huvudman + " like '%" + trimFilter + "%')";
        String orderBy = sortColumn + " DESC";
        qb.setTables(sqlTables);
        Cursor c = qb.query(db, sqlSelect, where, null,
                null, null, orderBy);

        c.moveToFirst();
        return c;
    }

    public Cursor getCity(String sortColumn, String filterString, int year) {
        return getCity(sortColumn, filterString, year, null);
    }

}
