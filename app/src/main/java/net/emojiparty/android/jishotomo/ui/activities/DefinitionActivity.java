package net.emojiparty.android.jishotomo.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import net.emojiparty.android.jishotomo.R;

import java.util.Locale;

import static net.emojiparty.android.jishotomo.ui.activities.DefinitionFragment.ENTRY_EMPTY;
import static net.emojiparty.android.jishotomo.ui.activities.DefinitionFragment.ENTRY_ID_EXTRA;

public class DefinitionActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {

    private TextToSpeech tts;
    private static final int REQUEST_CHECK_TTS_DATA = 10;
    public String currentWord;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_definition);
        setupToolbar();
        int entryId = findEntryId(getIntent());
        DefinitionFragment fragment = DefinitionFragment.instance(entryId);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.definition_activity_fragment_container, fragment)
                .commit();

        tts = new TextToSpeech(getApplication(), this);
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private int findEntryId(Intent intent) {
        if (intent == null) {
            return ENTRY_EMPTY;
        }
        if (intent.hasExtra(ENTRY_ID_EXTRA)) {
            return intent.getIntExtra(ENTRY_ID_EXTRA, ENTRY_EMPTY);
        } else {
            return ENTRY_EMPTY;
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_tts:
                String last_utterance_id = Long.toString(System.currentTimeMillis());
                tts.speak(currentWord, android.speech.tts.TextToSpeech.QUEUE_FLUSH,
                        null, last_utterance_id);
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_definition, menu);
        return true;
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // language packages available?
        if (requestCode == REQUEST_CHECK_TTS_DATA) {
            super.onActivityResult(requestCode, resultCode, data);
            if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
                Locale loc = Locale.JAPANESE;
                tts.setLanguage(loc);
            } else {
                Toast.makeText(this, R.string.tts_package_missing, Toast.LENGTH_SHORT).show();
            }
        }
    }


    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            Intent intent = new Intent();
            intent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
            startActivityForResult(intent, REQUEST_CHECK_TTS_DATA);
        } else {
            Toast.makeText(this, R.string.tts_init_failed, Toast.LENGTH_LONG).show();
            Log.e("TTS", "Initialization failed");
        }
    }

}
