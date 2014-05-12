package ru.nsu.gordin.controller.filters;

import ru.nsu.gordin.model.BMPImage;

public class DoubleSize implements Filter {
    @Override
    public BMPImage apply(BMPImage image) {
        BMPImage filteredImage = new BMPImage(image);
        BMPImage.BMPColor[][] bitmap = image.getBitMap();
        BMPImage.BMPColor[][] filteredBitmap = filteredImage.getBitMap();
        int height = filteredImage.getHeight();
        int width = filteredImage.getWidth();
        for(int i = 1; i <= height; i++) {
            for(int j = 1; j <= width; j++) {
                int r;
                int g;
                int b;

                if((i - 1) % 2 == 0 && (j - 1) % 2 == 0) {
                    int i1 = width / 4 + (i - 1) / 2;
                    int j1 = height / 4 + (j - 1) / 2;
                    r = bitmap[i1][j1].red;
                    g = bitmap[i1][j1].green;
                    b = bitmap[i1][j1].blue;
                } else
                if((i - 1) % 2 == 0) {
                    int i1 = width / 4 + (i - 1) / 2;
                    int j1 = height / 4 + (j - 2) / 2;
                    r = (bitmap[i1][j1].red + bitmap[i1][j1+1].red) / 2;
                    g = (bitmap[i1][j1].green + bitmap[i1][j1+1].green) / 2;
                    b = (bitmap[i1][j1].blue + bitmap[i1][j1+1].blue) / 2;
                } else
                if((j - 1) % 2 == 0) {
                    int i1 = width / 4 + (i - 2) / 2;
                    int j1 = height / 4 + (j - 1) / 2;
                    r = (bitmap[i1][j1].red + bitmap[i1+1][j1].red) / 2;
                    g = (bitmap[i1][j1].green + bitmap[i1+1][j1].green) / 2;
                    b = (bitmap[i1][j1].blue + bitmap[i1+1][j1].blue) / 2;
                } else {
                    int i1 = width / 4 + (i - 2) / 2;
                    int j1 = height / 4 + (j - 2) / 2;
                    r = (bitmap[i1][j1].red + bitmap[i1][j1+1].red +
                            bitmap[i1+1][j1].red + bitmap[i1+1][j1+1].red) / 4;
                    g = (bitmap[i1][j1].green + bitmap[i1][j1+1].green +
                            bitmap[i1][j1+1].green + bitmap[i1+1][j1+1].green) / 4;
                    b = (bitmap[i1][j1].blue + bitmap[i1][j1+1].blue +
                            bitmap[i1][j1+1].blue + bitmap[i1+1][j1+1].blue) / 4;
                }
                filteredBitmap[i][j].red = r;
                filteredBitmap[i][j].green = g;
                filteredBitmap[i][j].blue = b;
            }
        }

        return filteredImage;
    }
}
