package net.emojiparty.android.jishotomo.data.csv

import androidx.annotation.VisibleForTesting
import net.emojiparty.android.jishotomo.data.CJKUtil
import net.emojiparty.android.jishotomo.data.SemicolonSplit
import net.emojiparty.android.jishotomo.data.models.EntryWithAllSenses
import net.emojiparty.android.jishotomo.data.room.Entry
import net.emojiparty.android.jishotomo.data.room.Sense
import net.emojiparty.android.jishotomo.ui.presentation.SenseDisplay

class CsvEntry(private val entry: EntryWithAllSenses, private val senseDisplay: SenseDisplay) {
  fun toArray(): Array<String> {
    return arrayOf(entry.kanjiOrReading, meaning(), reading())
  }

  @VisibleForTesting
  fun meaning(): String {
    val builder = StringBuilder()
    val numberOfSenses = entry.senses.size
    var glossIndex = 1;
    for (i in 0 until numberOfSenses) {
      val sense = entry.senses[i]

      val newPartOfSpeech = appendPartsOfSpeech(builder, sense.sense)
      if (newPartOfSpeech) {
        glossIndex = 1
      }
      if (numberOfSenses > 1) {
        builder.append(glossIndex)
        builder.append(". ")
        glossIndex++
      }
      builder.append(SemicolonSplit.splitAndJoin(sense.sense.glosses))
      builder.append("<br/>")
    }
    return builder.toString()
  }

  private fun appendPartsOfSpeech(builder: StringBuilder, sense: Sense): Boolean {
    if (sense.partsOfSpeech != null) {
      builder.append(senseDisplay.formatPartsOfSpeech(sense.partsOfSpeech))
      builder.append("<br/>")
      return true
    }
    return false
  }

  @VisibleForTesting
  fun reading(): String {
    return if (entry.hasKanji()) {
      formatReading(entry.entry)
    } else {
      entry.entry.primaryReading
    }
  }

  private fun formatReading(entry: Entry): String {
    val kanji = entry.primaryKanji
    val reading = entry.primaryReading

    val builder = StringBuilder()

    val kanjiKanaPairIndices = mutableListOf<Int>()
    var lastWasKana = false

    for (i in kanji.indices) {
      val codePoint = Character.codePointAt(kanji, i)
      if (CJKUtil.isKana(codePoint)) {
        lastWasKana = true
      } else {
        if (lastWasKana || i == 0) {
          kanjiKanaPairIndices.add(i)
        }
        lastWasKana = false
      }
    }

    if (kanjiKanaPairIndices.first() != 0) {
      // kana at beginning
      builder.append(kanji.substring(0 until kanjiKanaPairIndices.first()))
    }

    for (kanjiStart in kanjiKanaPairIndices) {
      if (builder.isNotEmpty()) {
        builder.append(" ")
      }

      val kanjiEnd = getEnd(kanji, kanjiStart, kanjiKanaPairIndices)
      val readingEnd = getEnd(reading, kanjiStart, kanjiKanaPairIndices)

      addKanjiReadingPair(
          builder,
          kanji.substring(kanjiStart, kanjiEnd),
          reading.substring(kanjiStart, readingEnd)
      )
    }

    return builder.toString()
  }

  private fun getEnd(string: String, index: Int, list: List<Int>): Int {
    return if ((index + 1) < list.size) {
      list.get(index + 1)
    } else {
      string.length
    }
  }

  private fun addKanjiReadingPair(builder: StringBuilder, kanji: String, reading: String): StringBuilder {
    val commonSuffix = kanji.commonSuffixWith(reading)
    if (commonSuffix.isNotEmpty()) {

      builder.append(differenceFrom(kanji, commonSuffix))

      builder.append("[")

      builder.append(differenceFrom(reading, commonSuffix))

      builder.append("]")

      builder.append(commonSuffix)
    } else {
      builder.append("$kanji[$reading]")
    }
    return builder
  }

  private fun differenceFrom(string1: String, string2: String): String {
    // given a string like 嬉しい and another string like しい,
    // return the part that is not the same (嬉)

    val subStart = string1.indexOf(string2)
    return string1.slice(0 until subStart)
  }
}
