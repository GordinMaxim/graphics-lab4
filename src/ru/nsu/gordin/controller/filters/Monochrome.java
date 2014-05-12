package ru.nsu.gordin.controller.filters;

import ru.nsu.gordin.model.BMPImage;

public class Monochrome implements Filter {
    private int blackness;

    public Monochrome(int blackness) {
        this.blackness = blackness;
    }

    @Override
    public BMPImage apply(BMPImage image) {
        BMPImage filteredImage = new BMPImage(image);

        BMPImage.BMPColor[][] bitmap = image.getBitMap();
        BMPImage.BMPColor[][] filteredBitmap = filteredImage.getBitMap();
        for(int i = 1; i <= filteredImage.getHeight(); i++) {
            for(int j = 1; j <= filteredImage.getWidth(); j++) {
                int r = filteredBitmap[i][j].red;
                int g = filteredBitmap[i][j].green;
                int b = filteredBitmap[i][j].blue;

                int y = (int)(0.299 * r + 0.587 * g + 0.114 * b);
                if(y < blackness) {
                    r = 0;
                    g = 0;
                    b = 0;
                } else {
                    r = 255;
                    g = 255;
                    b = 255;
                }

                filteredBitmap[i][j].red = r;
                filteredBitmap[i][j].green = g;
                filteredBitmap[i][j].blue = b;
            }
        }
        return filteredImage;
    }
}
