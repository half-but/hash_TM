package root.hash_tm.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import root.hash_tm.Manager.TTSManager;
import root.hash_tm.R;
import root.hash_tm.Util.BaseActivity;

/**
 * Created by root1 on 2017. 8. 29..
 */

public class SharePoemActivity extends BaseActivity {

    ImageButton removeButton, editButton;
    TextView titleText, contentText, writerText;

    TTSManager ttsManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poem_no_out);

        removeButton = (ImageButton)findViewById(R.id.removeButton);
        editButton = (ImageButton)findViewById(R.id.editButton);
        titleText = (TextView)findViewById(R.id.titleText);
        contentText = (TextView)findViewById(R.id.contentText);
        writerText = (TextView)findViewById(R.id.writerText);
        ttsManager = new TTSManager(this);

        editButton.setVisibility(View.GONE);
        removeButton.setImageResource(R.drawable.ico_tts);
        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ttsManager.readTTS(titleText.getText().toString() + "\n" + contentText.getText().toString());
            }
        });
        setData(getIntent());

    }

    private void setData(Intent intent){
        String title = intent.getStringExtra("title");
        String content = intent.getStringExtra("content");
        String writer = intent.getStringExtra("writer");
        int alignment = intent.getIntExtra("alignment", 0);
        titleText.setText(title);
        contentText.setText(content);
        writerText.setText(writer);

        switch (alignment){
            case 3:
                contentText.setGravity(Gravity.LEFT);
                break;
            case 2:
                contentText.setGravity(Gravity.CENTER);
                break;
            case 1:
                contentText.setGravity(Gravity.RIGHT);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ttsManager.shutDownTTS();
    }
}
