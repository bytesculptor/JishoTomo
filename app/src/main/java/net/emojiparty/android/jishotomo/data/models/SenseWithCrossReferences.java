package net.emojiparty.android.jishotomo.data.models;

import androidx.room.Embedded;
import androidx.room.Ignore;

import net.emojiparty.android.jishotomo.data.room.Sense;

import java.util.List;

public class SenseWithCrossReferences {
    @Embedded
    public Sense sense;

    @Ignore
    public List<CrossReferencedEntry> crossReferences;

    public Sense getSense() {
        return sense;
    }

    public List<CrossReferencedEntry> getCrossReferences() {
        return crossReferences;
    }

    public void setCrossReferences(List<CrossReferencedEntry> crossReferences) {
        this.crossReferences = crossReferences;
    }
}
