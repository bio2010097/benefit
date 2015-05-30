package com.android.benefit.nfcread;

import java.util.Arrays;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.tech.NfcF;
import android.os.Bundle;
import android.os.Parcelable;
import android.widget.TextView;

import com.android.benefii.utils.Util;
import com.android.benefit.BaseActionBarActivity;
import com.android.benefit.R;


public class NFCTagReadActivity extends BaseActionBarActivity {

    private NfcAdapter nfcAdapter;
    private PendingIntent pendingIntent;

    IntentFilter[] mIntentFilters; // 인텐트 필터
    String[][] mNFCTechLists;

    private TextView tagText;
    private TextView tagActionText;


    @Override
    protected void onResume(){
        super.onResume();
        if(nfcAdapter!=null)
            nfcAdapter.enableForegroundDispatch(this, pendingIntent, null, null);

        if( NfcAdapter.ACTION_NDEF_DISCOVERED.equals(getIntent().getAction()) ) {
            onNewIntent(getIntent());

        }
    }

    @Override
    protected void onPause(){
        if(nfcAdapter != null){
            nfcAdapter.disableForegroundDispatch(this);
        }
        super.onPause();
    }

    @Override
    protected void onNewIntent(Intent intent){
        super.onNewIntent(intent);

        // 인텐트에서 액션을 추출
        String action = intent.getAction();
        // 인텐트에서 태그 정보 추출
        String tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG).toString();
        String strMsg = action + "\n\n" + tag;
        // 액션 정보와 태그 정보를 화면에 출력

        showMsg(strMsg);

        // 인텐트에서 NDEF 메시지 배열을 구한다
        Parcelable[] messages = intent.getParcelableArrayExtra(
                NfcAdapter.EXTRA_NDEF_MESSAGES);
        if(messages == null) return;

        for(int i=0; i < messages.length; i++)
            // NDEF 메시지를 화면에 출력
            showMsg((NdefMessage)messages[i]);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfctag_read);
        init_toolbar();
        
        tagText = (TextView)findViewById(R.id.tagText);
        tagActionText = (TextView)findViewById(R.id.tagActionText);

        
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);

        Intent intent2 = new Intent(this, getClass());
        intent2.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        pendingIntent = PendingIntent.getActivity(this, 0, intent2, 0);
        // NFC 데이터 활성화에 필요한 인텐트 필터를 생성
        IntentFilter iFilter = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
        try {
            iFilter.addDataType("*/*");
            mIntentFilters = new IntentFilter[] { iFilter };
        } catch (Exception e) {

        }
        mNFCTechLists = new String[][] { new String[] { NfcF.class.getName() } };

    }


    // NDEF 메시지를 화면에 출력
    public void showMsg(NdefMessage mMessage) {
        String strMsg = "", strRec="";
        // NDEF 메시지에서 NDEF 레코드 배열을 구한다
        NdefRecord[] recs = mMessage.getRecords();
        for (int i = 0; i < recs.length; i++) {
            // 개별 레코드 데이터를 구한다
            NdefRecord record = recs[i];
            byte[] payload = record.getPayload();
            // 레코드 데이터 종류가 텍스트 일때
            if( Arrays.equals(record.getType(), NdefRecord.RTD_TEXT) ) {
                // 버퍼 데이터를 인코딩 변환
                strRec = Util.byteDecoding(payload);
                strRec = "Text: " + strRec;
            }
            // 레코드 데이터 종류가 URI 일때
            else if( Arrays.equals(record.getType(), NdefRecord.RTD_URI) ) {
                strRec = new String(payload, 0, payload.length);
                strRec = "URI: " + strRec;
                strRec += "  " + record.getType();
            }
            strMsg += ("\n\nNdefRecord[" + i + "]:\n" + strRec);
        }

        tagText.setText(strMsg);
    }

    public void showMsg(String mMessage) {
        tagActionText.setText(mMessage);
    }

}
