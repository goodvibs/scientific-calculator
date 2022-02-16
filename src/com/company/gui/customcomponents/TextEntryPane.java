package com.company.gui.customcomponents;

import javax.swing.*;
import javax.swing.text.*;
import java.beans.JavaBean;
import java.util.*;

@JavaBean
@SwingContainer
public class TextEntryPane extends JTextPane {

    private class Entry {

        private final String text;
        private final int p0;
        private final int p1;

        public Entry(String text) {
            this.text = text;
            if (entries.isEmpty())
                p0 = 1;
            else
                p0 = entries.get(entries.size() - 1).p1 + 1;
            p1 = p0 + text.length();
        }
    }

    private List<Entry> entries;
    int  currIndex;

    public TextEntryPane() {
        super();
        entries = new ArrayList<>();
        currIndex = 0;
    }

    public TextEntryPane(StyledDocument doc) {
        super(doc);
        entries = new LinkedList<>();
        currIndex = 0;
    }

    public void addEntry(String t) {
        try {
            if (t.equals(""))
                return;
            Document doc = getDocument();
            doc.insertString(doc.getLength(), "\n" + t, getCharacterAttributes());
            entries.add(new Entry(t));
            currIndex = entries.size();
        } catch (BadLocationException e) {
            UIManager.getLookAndFeel().provideErrorFeedback(TextEntryPane.this);
        }
    }

    public void selectEntryAtCurrIndex() {
        Entry entry = entries.get(currIndex);
        select(entry.p0, entry.p1);
    }

    public void resetSelection() {
        select(0, 0);
        currIndex = entries.size();
    }

    public boolean selectPrevEntry() {
        if (--currIndex >= 0)
            selectEntryAtCurrIndex();
        return currIndex == entries.size() - 1;
    }

    public boolean selectNextEntry() {
        if (++currIndex >= entries.size()) {
            resetSelection();
            return true;
        }
        selectEntryAtCurrIndex();
        return false;
    }

    public Entry getLastEntry() {
        return (entries.isEmpty() ? null : entries.get(entries.size() - 1));
    }

    public String getInput() {
        Document doc = getDocument();
        try {
            int lastPos = 0;
            Entry lastEntry = getLastEntry();
            if (lastEntry != null)
                lastPos = lastEntry.p1;
            return doc.getText(lastPos, doc.getLength());
        } catch (BadLocationException e) {
            UIManager.getLookAndFeel().provideErrorFeedback(TextEntryPane.this);
        }
        return "";
    }
}
