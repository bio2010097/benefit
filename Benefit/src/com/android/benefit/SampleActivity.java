/**
앱 화면 샘플
새로운 화면 등록 시에 참고할 항목
 
1. SampleActivity.java
2. res/layout/activity_sample.xml
3. Manifest.xml
  
 */
package com.android.benefit;
import android.os.Bundle;
import android.widget.Toast;

public class SampleActivity extends BaseActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample);
        init_toolbar();

        Toast.makeText(getApplicationContext(), "test", 1000).show();
    }
}
