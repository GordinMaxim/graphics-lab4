package ru.nsu.gordin.controller.actions;

import ru.nsu.gordin.view.DrawPanel;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class StampAction extends AbstractAction {
    private DrawPanel panel;

    public StampAction(String text, ImageIcon icon,
                           String desc, Integer mnemonic, DrawPanel panel) {
        super(text, icon);
        putValue(SHORT_DESCRIPTION, desc);
        putValue(MNEMONIC_KEY, mnemonic);
        this.panel = panel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(panel.isImageLoaded())
            panel.stamp();
    }
}
