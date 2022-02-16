package com.company.gui.customcomponents;

import javax.swing.text.*;

public class PartialDocFilter extends DocumentFilter {

    public void insertString(final FilterBypass fb, final int offset, final String string, final AttributeSet attr)
            throws BadLocationException {
        if (canEdit(fb.getDocument(), offset)) {
            super.insertString(fb, offset, string, attr);
        }
    }

    public void remove(final FilterBypass fb, final int offset, final int length) throws BadLocationException {
        if (canEdit(fb.getDocument(), offset)) {
            super.remove(fb, offset, length);
        }
    }

    public void replace(final FilterBypass fb, final int offset, final int length, final String text, final AttributeSet attrs)
            throws BadLocationException {
        if (canEdit(fb.getDocument(), offset)) {
            super.replace(fb, offset, length, text, attrs);
        }
    }

    private boolean canEdit(Document doc, int pos) throws BadLocationException {
        return pos > doc.getText(0, doc.getLength()).lastIndexOf("\n");
    }
}
