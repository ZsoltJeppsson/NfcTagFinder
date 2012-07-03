package com.zoltjeppsson.nfctagfinder;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class NfcTagFinder extends Activity {
    private static final String TAG = "NfcTagFinder";
	private TextView mText;
    private int mCount = 0;

    @Override
    public void onCreate(Bundle savedState) {
        super.onCreate(savedState);

        setContentView(R.layout.foreground_dispatch);
        mText = (TextView) findViewById(R.id.text);
        mText.setText("Scan a tag");
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter tagDetected = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
        IntentFilter[] tagFilters = new IntentFilter[] {
            tagDetected
        };

        PendingIntent nfcPendingIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);

        NfcAdapter.getDefaultAdapter(this).enableForegroundDispatch(this, nfcPendingIntent, tagFilters, null);
    }

    @Override
    public void onNewIntent(Intent intent) {
        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        byte tagID[] = intent.getByteArrayExtra(NfcAdapter.EXTRA_ID);
        StringBuilder hexStr = null;

        if (tagID != null) {
            hexStr = new StringBuilder();
            for (int i = 0; i < tagID.length; i++) {
                hexStr.append(String.format("%1$02X", tagID[i]));
                hexStr.append(" ");
            }
            Log.i(TAG, "TagID: " + hexStr.toString());
        }

        mText.setText("Discovered tag " + ++mCount + " with intent: " + intent + " tag: " +
                tag.toString() + " tagID: " + hexStr);
    }
}