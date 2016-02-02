package se.subsurface.skolrank;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;

import se.subsurface.skolrank.list.GymnasieSkolaAdapter;
import se.subsurface.skolrank.list.SkolaAdapter;

public class MainActivity extends AbstractMainActivity implements LoaderManager.LoaderCallbacks<Cursor> {


    private static final String TAG = MainActivity.class.getName();

    private ListView mListView;

    private GymnasieSkolaAdapter mGymnasieAdapter;
    private SkolaAdapter mGrundAdapter;

    @Override
    void restartLoader() {
        Log.e(TAG, "restartLoader()");
        getSupportLoaderManager().restartLoader(0, null, this);
    }

    @Override
    int getContentView() {
        return R.layout.activity_main;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate savedInstanceState=" + savedInstanceState);
        super.onCreate(savedInstanceState);

        mGymnasieAdapter = new GymnasieSkolaAdapter(this, mSortComparator);
        mGrundAdapter = new SkolaAdapter(this, mSortComparator);
//final Toolbar toolbar = (Toolbar)findViewById(R.id.skolrank_toolbar);
        mListView = (ListView) this.findViewById(R.id.schoolList);


    }

    public Loader<Cursor> onCreateLoader(int loaderID, Bundle args) {
        switch (loaderID) {
            case 0:
                Log.v(TAG, "onCreateLoader");
                mProgressBar.setVisibility(View.VISIBLE);
                mNumSchoolTV.setVisibility(View.GONE);
                return new SchoolCursorLoader(this, mSortComparator, mSearchQueryTemp, mSchoolType, year);

            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        Log.v(TAG, "onLoadFinnished ENTER");
        CursorAdapter adapter;
        if (SchoolType.values()[mSchoolType] == SchoolType.GRUND) {
            mGrundAdapter.setSortOrder(mSortComparator);
            adapter = mGrundAdapter;
        } else {
            mGymnasieAdapter.setSortOrder(mSortComparator);
            adapter = mGymnasieAdapter;
        }


        adapter.swapCursor(cursor);

        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener((AdapterView.OnItemClickListener) adapter);

        updateStatus(cursor.getCount());
        Log.v(TAG, "onLoadFinnished RETURN");
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        Log.e(TAG, "onLoaderReset");
        if (mSchoolType == 0) {
            mGrundAdapter.swapCursor(null);
        } else if (mSchoolType == 1) {
            mGymnasieAdapter.swapCursor(null);
        }

    }


    @Override
    public void onStart() {
        super.onStart();
        if (mSchoolType != -1) {
         //   if (getSupportLoaderManager().getLoader(0) == null) {
                getSupportLoaderManager().initLoader(0, null, this);
         //   }
        }
    }


    public final static class SchoolCursorLoader extends CursorLoader {
        private final CompareBy compareBy;
        private final String filterString;
        private final int schoolType;
        private final int year;

        public SchoolCursorLoader(Context context, CompareBy compareBy, String filterString, int schoolType, int year) {
            super(context);
            this.compareBy = compareBy;
            this.filterString = filterString;
            this.schoolType = schoolType;
            this.year = year;
            Log.v(TAG, "SchoolCursorLoader compareBy=" + compareBy + " filterString=" + filterString + " mSchoolType=" + schoolType);
        }

        @Override
        public Cursor loadInBackground() {
            Cursor cursor;
            if (schoolType == 0) {
                cursor = AppController.getInstance().getGrundDatabase().getCity(GrundDatabase.getSortString(compareBy), filterString, year);
            } else {
                cursor = AppController.getInstance().getGymnasieDatabase().getSkola(compareBy, filterString, year);
            }
            return cursor;
        }
    }
}