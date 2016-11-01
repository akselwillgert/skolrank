package se.subsurface.skolrank;


import android.app.DialogFragment;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import de.psdev.licensesdialog.LicensesDialogFragment;
import se.subsurface.skolrank.dialogs.SchoolListSortDialog;
import se.subsurface.skolrank.dialogs.SchoolTypeDialog;
import se.subsurface.skolrank.dialogs.YearDialog;

public abstract class AbstractMainActivity extends AppCompatActivity implements YearDialog.YearDialogListener, SchoolTypeDialog.SchoolTypeDialogListener, SchoolListSortDialog.SortOptionDialogListener {
    private static final String PERSISTENT_CACHE = "School cache";
    private static final String SEARCH_QUERY = "SearchQuery";
    private static final String TAG = AbstractMainActivity.class.getName();
    //State
    CompareBy mSortComparator;
    //Misc
    private SharedPreferences mPreferences;
    //Views
    private SearchView mSearchView;
    private TextView mSchoolTypeTV;
    TextView mNumSchoolTV;
    private TextView mFilterTV;
    ProgressBar mProgressBar;
    int year = 2014;
    int mSchoolType = 0;
    String mSearchQuery = "";
    String mSearchQueryTemp = "";
    private TextView mSortLabel;
    private TextView mSortOption;

    @Override
    public void onYearDialogClick(int year) {
        Log.e(TAG, "year=" + year);
        this.year = year;
        mTracker.send(new HitBuilders.EventBuilder()
                .setCategory("newYearOption")
                .setAction(year + "")
                .build());
        restartLoader();
    }

    abstract void restartLoader();

    Tracker mTracker;

    @Override
    public void onDialogPositiveClick(int sortOptionInt) {
        mPreferences.edit().putInt(SchoolListSortDialog.SORT_OPTION, sortOptionInt).apply();
        this.mSortComparator = CompareBy.values()[sortOptionInt];

        final String[] items = getResources().getStringArray(R.array.sort_options);

        mTracker.send(new HitBuilders.EventBuilder()
                .setCategory("newSortOption")
                .setAction(items[sortOptionInt])
                .build());

        Toast.makeText(
                this,
                getSortLabel() + " " + items[sortOptionInt],
                Toast.LENGTH_LONG).show();
        restartLoader();
    }

    public String getSortLabel() {
        String sortLabel;
        if (getClass().getSimpleName().equals(MapsActivity.class.getSimpleName())) {
            sortLabel = getResources().getString(R.string.map_symbol_shows);
        } else {
            sortLabel = getResources().getString(R.string.sorting_on);
        }
        return sortLabel;
    }

    abstract int getContentView();

    void updateStatus(int count) {
        //Grund eller gymnasie
        final String[] items = getResources().getStringArray(R.array.school_type_options);
        String statusMsg = String.format(getResources().getString(R.string.status_bar), items[mSchoolType], year);
        mSchoolTypeTV.setText(statusMsg);

        String statusMsgCount = String.format(getResources().getString(R.string.status_bar_count), count);
        mNumSchoolTV.setText(statusMsgCount);

        if (mSearchQueryTemp != null && !mSearchQueryTemp.equals("")) {
            mFilterTV.setText(mSearchQueryTemp);
        } else {
            mFilterTV.setText("");
        }
        String sortLabel = getSortLabel();

        mSortLabel.setText(sortLabel);

        mProgressBar.setVisibility(View.GONE);
        mNumSchoolTV.setVisibility(View.VISIBLE);
        final String[] sort_options = getResources().getStringArray(R.array.sort_options);
        mSortOption.setText(sort_options[mSortComparator.ordinal()]);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentView());

        AppController application = (AppController) getApplication();
        mTracker = application.getDefaultTracker();

//        Toolbar toolbar = (Toolbar) getLayoutInflater().inflate(R.layout.toolbar,null);
        Toolbar toolbar = (Toolbar) findViewById(R.id.skolrank_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setLogo(R.drawable.ic_launcher);
        Log.d(TAG, "onCreate savedInstanceState=" + savedInstanceState);

        //recover Settings
        mPreferences = getSharedPreferences(PERSISTENT_CACHE, 0);
        int sortComparatorInt = mPreferences.getInt(SchoolListSortDialog.SORT_OPTION, 0);
        this.mSortComparator = CompareBy.values()[sortComparatorInt];

        mSearchQuery = mPreferences.getString(SEARCH_QUERY, mSearchQuery);
        mSearchQueryTemp = mSearchQuery;

        this.mSchoolType = mPreferences.getInt(SchoolTypeDialog.SCHOOL_TYPE, -1);


        mSchoolTypeTV = (TextView) this.findViewById(R.id.school_type);
        mNumSchoolTV = (TextView) this.findViewById(R.id.num_schools);
        mFilterTV = (TextView) this.findViewById(R.id.filter);
        mProgressBar = (ProgressBar) this.findViewById(R.id.listProgress);

        mSortOption = (TextView) this.findViewById(R.id.sort_option);
        mSortLabel = (TextView) this.findViewById(R.id.sort_label);

        if (mSchoolType == -1) {
            //first time, show selection
            showSchoolTypeDialog();
        }

        Log.d(TAG, "onCreate RETURN");
    }

    @Override
    public void onDialogClick(int schoolType) {
        mPreferences.edit().putInt(SchoolTypeDialog.SCHOOL_TYPE, schoolType).apply();
        this.mSchoolType = schoolType;
        final String[] items = getResources().getStringArray(R.array.school_type_options);
        mTracker.send(new HitBuilders.EventBuilder()
                .setCategory("newSchoolTypeOption")
                .setAction(items[schoolType])
                .build());
        restartLoader();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_licenses:
                final LicensesDialogFragment fragment = LicensesDialogFragment.newInstance(R.raw.notices, false, true);
                fragment.show(getSupportFragmentManager(), null);
                return true;
            case R.id.action_about:
                AboutDialog about = new AboutDialog(this);
                about.setTitle(R.string.dialog_about);
                about.show();
                return true;
            case R.id.action_school_type_dialog:
                showSchoolTypeDialog();
                return true;
            case R.id.action_year_dialog:
                showYearDialog();
                return true;
            case R.id.action_sort_dialog:
//                final Dialog dialog = new Dialog(this);
//                dialog.setTitle("Skolrank - Karta");
//                dialog.setContentView(R.layout.dialog_options);
//                dialog.show();
                final DialogFragment dialog = new SchoolListSortDialog();

                Bundle args = new Bundle();
                args.putInt(SchoolListSortDialog.SORT_OPTION, mSortComparator.ordinal());
                dialog.setArguments(args);
                dialog.show(getFragmentManager(), SchoolListSortDialog.class.getName());
                return true;

            case R.id.action_map:

                Intent myIntent = new Intent(this, MapsActivity.class);
                //myIntent.putExtra(COLUMN_SKOLA_ID, enhetskod); //Optional parameters
                this.startActivity(myIntent);
                return true;
            case R.id.action_list:
                Intent listIntent = new Intent(this, MainActivity.class);
                //myIntent.putExtra(COLUMN_SKOLA_ID, enhetskod); //Optional parameters
                this.startActivity(listIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.main_activity_actions, menu);
        if (this.getClass().getSimpleName().equals(MapsActivity.class.getSimpleName())) {
            menu.findItem(R.id.action_map).setVisible(false);
            menu.findItem(R.id.action_sort_dialog).setIcon(R.drawable.circle);
        } else if (this.getClass().getSimpleName().equals(MainActivity.class.getSimpleName())) {
            menu.findItem(R.id.action_list).setVisible(false);
        }
        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.search));
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        mSearchView = searchView;
        searchView.setMaxWidth(Integer.MAX_VALUE);
        String hint = "<font color = #CCCCCC>" + getResources().getString(R.string.search_hint) + "</font>";

        Spanned hintHtml;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            hintHtml = Html.fromHtml(hint, Html.FROM_HTML_MODE_LEGACY);
        } else {
            //noinspection deprecation
            hintHtml = Html.fromHtml(hint);
        }
        searchView.setQueryHint(hintHtml);

        Log.v(TAG, "mSearchQuery=" + mSearchQuery);
        mSearchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "setOnSearchClickListener mSearchQuery=" + mSearchQuery);
                mSearchView.setQuery(mSearchQuery, false);
            }
        });

        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            private boolean collapsed = true;

            @Override
            public boolean onQueryTextSubmit(String query) {
                collapsed = true;
                Log.d(TAG, "onQueryTextSubmit query=" + query);

                //this is a mess, but one of them works..
                //menu.findItem(R.id.search).collapseActionView();
                // MenuItemCompat.collapseActionView(menu.findItem(R.id.search));
                mSearchView.onActionViewCollapsed();


                mSearchQuery = query;
                mPreferences.edit().putString(SEARCH_QUERY, mSearchQuery).apply();
                //           SchoolListFragment listFragment = (SchoolListFragment) getFragmentManager().findFragmentByTag(LIST_FRAGMENT);
                //           listFragment.updateFilter(query);

                mTracker.send(new HitBuilders.EventBuilder()
                        .setCategory("newSearchStringSubmit")
                        .setAction(query)
                        .build());
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                Log.v(TAG, "onQueryTextChange query=" + query);
                if (!collapsed) {

                    mSearchQueryTemp = query;
                    if (!query.equals(mSearchQuery)) {
                        mSearchQuery = query;
                        restartLoader();
                    }
                }
                collapsed = false;
                return false;
            }
        });
        return true;
    }

    private void showSchoolTypeDialog() {
        final DialogFragment dialog = new SchoolTypeDialog();
        Bundle args = new Bundle();
        args.putInt(SchoolTypeDialog.SCHOOL_TYPE, mSchoolType);
        dialog.setArguments(args);
        dialog.show(getFragmentManager(), SchoolTypeDialog.class.getName());
    }

    private void showYearDialog() {
        final DialogFragment dialog = new YearDialog();
        Bundle args = new Bundle();
        args.putInt(YearDialog.YEAR, year);
        dialog.setArguments(args);
        dialog.show(getFragmentManager(), YearDialog.class.getName());
    }

    public enum CompareBy {GRADE, NUM_STUDENTS, NAME, QUALIFIED_FOR_HIGH_SCHOOL, EDUCATED_PARENTS, GIRLS, FOREIGN_PARENTS}

    public enum SchoolType {GRUND, GYMNASIE}
}
