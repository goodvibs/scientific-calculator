package com.company.gui.customcomponents;

import javax.swing.*;
import java.awt.*;

public class MaxableJFrame extends JFrame {

    public MaxableJFrame() throws HeadlessException {
    }

    public MaxableJFrame(GraphicsConfiguration gc) {
        super(gc);
    }

    public MaxableJFrame(String title) throws HeadlessException {
        super(title);
    }

    public MaxableJFrame(String title, GraphicsConfiguration gc) {
        super(title, gc);
    }

    @Override
    public void pack() {
        Dimension preferredSize = getPreferredSize();
        Dimension maximumSize = getMaximumSize();
        setSize(Math.min(preferredSize.width, maximumSize.width), Math.min(preferredSize.height, maximumSize.height));
        validate();
    }
}
