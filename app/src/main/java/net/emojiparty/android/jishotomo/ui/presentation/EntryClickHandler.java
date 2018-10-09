package net.emojiparty.android.jishotomo.ui.presentation;

import android.content.Context;
import android.content.Intent;
import net.emojiparty.android.jishotomo.ui.activities.DefinitionActivity;

import static net.emojiparty.android.jishotomo.ui.activities.DefinitionFragment.ENTRY_ID_EXTRA;

public class EntryClickHandler {
  public static void open(Context context, int entryId) {
    Intent intent = new Intent(context, DefinitionActivity.class);
    intent.putExtra(ENTRY_ID_EXTRA, entryId);
    context.startActivity(intent);
  }
}
