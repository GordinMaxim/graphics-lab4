package ru.nsu.gordin.view;

import ru.nsu.gordin.model.BMPImage;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class BMPPanel extends JPanel {
    protected BMPImage image;

    public BMPPanel() {}

    public BMPPanel(BMPImage image) {
        this.image = image;
    }

    public BMPImage getImage() {
        return image;
    }

    public void setImage(BMPImage image) {
        this.image = image;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if(null == image) {
            return;
        }

        BMPImage.BMPColor[][] bitmap = image.getBitMap();
        int height = image.getHeight();
        int width = image.getWidth();
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int i = 1; i <= height; i++) {
            for(int j = 1; j <= width; j++) {
                Color color = new Color(bitmap[i][j].red, bitmap[i][j].green, bitmap[i][j].blue);
                bufferedImage.setRGB(j-1, i-1, color.getRGB());
            }
        }

        float k = (float)(width)/height;
        if(k >= 1) {
            g.drawImage(bufferedImage, 0, 0, DrawPanel.subPanelWidth, (int) (DrawPanel.subPanelHeight / k), null);
        }
        else {
            g.drawImage(bufferedImage, 0, 0, (int) (DrawPanel.subPanelWidth * k), DrawPanel.subPanelHeight, null);
        }
    }
}
