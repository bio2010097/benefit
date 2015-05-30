package com.android.benefit.nfcwrite;

import java.nio.charset.Charset;
import java.util.Locale;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import com.android.benefii.utils.NFCWriteUtil;
import com.android.benefit.BaseActionBarActivity;
import com.android.benefit.R;


public class NFCTagWriteActivity extends BaseActionBarActivity {

    private EditText typeEdit;
    private EditText tagEdit;


    private static final String TAG = "NFCWriteTag";
    private NfcAdapter mNfcAdapter;
    private IntentFilter[] mWriteTagFilters;
    private PendingIntent mNfcPendingIntent;
    private boolean silent=false;

    private Context context;



    @Override
    protected void onResume(){
        super.onResume();
        mNfcAdapter.enableForegroundDispatch(this, mNfcPendingIntent, mWriteTagFilters, null);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mNfcAdapter != null) mNfcAdapter.disableForegroundDispatch(this);
    }
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if(NfcAdapter.ACTION_TAG_DISCOVERED.equals(intent.getAction())) {
            // validate that this tag can be written
            Tag detectedTag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            if(NFCWriteUtil.supportedTechs(detectedTag.getTechList())) {
                // check if tag is writable (to the extent that we can
                if(WriteResponse.writableTag(getApplicationContext(),detectedTag)) {
                    //writeTag here
                    WriteResponse wr = WriteResponse.writeTag(getTagAsNdef(), detectedTag);
                    String message = (wr.getStatus() == 1? "Success: " : "Failed: ") + wr.getMessage();
                    Toast.makeText(context,message,Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context,"This tag is not writable",Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(context,"This tag type is not supported",Toast.LENGTH_SHORT).show();
            }
        }
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfctag_write);
        init_toolbar();
        
        typeEdit = (EditText) findViewById(R.id.typeEdit);
        tagEdit = (EditText) findViewById(R.id.tagEdit);


        context = getApplicationContext();
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        mNfcPendingIntent = PendingIntent.getActivity(this, 0, new Intent(this,
                getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP
                | Intent.FLAG_ACTIVITY_CLEAR_TOP), 0);
        IntentFilter discovery=new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
        IntentFilter ndefDetected = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
        IntentFilter techDetected = new IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED);
        // Intent filters for writing to a tag
        mWriteTagFilters = new IntentFilter[] { discovery };

    }
    
    
    private NdefMessage getTagAsNdef() {

        NdefRecord mimeRecord = new NdefRecord(
                NdefRecord.TNF_MIME_MEDIA ,typeEdit.getText().toString().getBytes(Charset.forName("US-ASCII")),
        new byte[0], tagEdit.getText().toString().getBytes(Charset.forName("US-ASCII")));

        return new NdefMessage(new NdefRecord[] {
                mimeRecord, NFCWriteUtil.createTextRecord(tagEdit.getText().toString(), Locale.ENGLISH)});
    }

  
  

  
}
