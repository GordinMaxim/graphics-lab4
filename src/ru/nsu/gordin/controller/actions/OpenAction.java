package ru.nsu.gordin.controller.actions;

import ru.nsu.gordin.model.BMPImage;
import ru.nsu.gordin.view.DrawPanel;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.io.FileInputStream;
import java.io.IOException;

public class OpenAction extends AbstractAction {
    private DrawPanel panel;

    public OpenAction(String text, ImageIcon icon,
                       String desc, Integer mnemonic, DrawPanel panel) {
        super(text, icon);
        putValue(SHORT_DESCRIPTION, desc);
        putValue(MNEMONIC_KEY, mnemonic);
        this.panel = panel;
    }

    public void actionPerformed(ActionEvent e) {
        final JFileChooser fc = new JFileChooser();
        String fileName = null;
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "BMP Images", "bmp");
        fc.setFileFilter(filter);
        int returnVal = fc.showOpenDialog(panel);
        if(returnVal == JFileChooser.APPROVE_OPTION) {
            fileName = fc.getCurrentDirectory() + "/" + fc.getSelectedFile().getName();
            BMPImage image = new BMPImage();
            try {
                image.read(new FileInputStream(fileName));
                panel.setImage(image);
            } catch (IOException e1) {
//            e1.printStackTrace();
                JOptionPane.showMessageDialog(panel,"Bad file");
            }
        }
    }
}
