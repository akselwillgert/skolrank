package se.subsurface.skolrank;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Html;
import android.text.util.Linkify;
import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

class AboutDialog extends Dialog {
    private static final String TAG = "AboutDialog";
    private static Context mContext = null;

    public AboutDialog(Context context) {
        super(context);
        mContext = context;

    }

    private static String readRawTextFile(int id) {
        InputStream inputStream = mContext.getResources().openRawResource(id);
        InputStreamReader in = new InputStreamReader(inputStream);
        BufferedReader buf = new BufferedReader(in);
        String line;

        StringBuilder text = new StringBuilder();
        try {
            while ((line = buf.readLine()) != null) text.append(line);
        } catch (IOException e) {
            return null;
        }
        Log.e(TAG, text.toString());
        return text.toString();
    }

    /**
     * }
     * Standard Android on create method that gets called when the activity initialized.
     */
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.about);
        TextView tv = (TextView) findViewById(R.id.legal_text);
        tv.setText(readRawTextFile(R.raw.legal));

        tv = (TextView) findViewById(R.id.info_text);
        tv.setMaxLines(10);
        tv.setText(Html.fromHtml(readRawTextFile(R.raw.info)));

        Linkify.addLinks(tv, Linkify.ALL);
    }
}