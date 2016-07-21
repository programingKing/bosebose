package kr.co.edge.bosebose;


import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class SearchActivity extends Activity {
    EditText searchKeyword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        findViewById(R.id.backBtn).setOnClickListener(mClickListener);
        searchKeyword = (EditText)findViewById(R.id.searchKeyword);
        ImageButton headerSearchBtn = (ImageButton)findViewById(R.id.HeaderSearchBtn);

        headerSearchBtn.setOnClickListener(mClickListener);
        searchKeyword.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    getSearchKeyword();
                }
                return false;
            }
        });

    }
    ImageButton.OnClickListener mClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.backBtn:
                    finish();
                    break;
                case R.id.HeaderSearchBtn:
                    getSearchKeyword();
                    break;
            }
        }
    };

    //TODO 여기에서 searchKeyword.getText().toString()의 값으로 가공하면 됩니다.
    void getSearchKeyword () {
        Toast.makeText(getApplicationContext(), searchKeyword.getText().toString(), Toast.LENGTH_SHORT).show();
    }
}