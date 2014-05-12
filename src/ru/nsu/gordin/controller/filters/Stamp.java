package ru.nsu.gordin.controller.filters;

import ru.nsu.gordin.model.BMPImage;

public class Stamp implements Filter {
    @Override
    public BMPImage apply(BMPImage image) {
        Filter grey = new Grey();
        image = grey.apply(image);
        BMPImage filteredImage = new BMPImage(image);

        BMPImage.BMPColor[][] bitmap = image.getBitMap();
        BMPImage.BMPColor[][] filteredBitmap = filteredImage.getBitMap();
        for(int i = 1; i <= filteredImage.getHeight(); i++) {
            for(int j = 1; j <= filteredImage.getWidth(); j++) {
                int r =
                        1 * bitmap[i - 1][j].red - 1 * bitmap[i][j + 1].red +
                                1 * bitmap[i][j + 1].red - 1 * bitmap[i + 1][j].red;
                r += 128;

                int g =
                        1 * bitmap[i - 1][j].green - 1 * bitmap[i][j + 1].green +
                                1 * bitmap[i][j + 1].green - 1 * bitmap[i + 1][j].green;
                g += 128;

                int b =
                        1 * bitmap[i - 1][j].blue - 1 * bitmap[i][j + 1].blue +
                                1 * bitmap[i][j + 1].blue - 1 * bitmap[i + 1][j].blue;
                b += 128;

                if(r < 0) r = 0;
                if(g < 0) g = 0;
                if(b < 0) b = 0;
                if(r > 255) r = 255;
                if(g > 255) g = 255;
                if(b > 255) b = 255;

                filteredBitmap[i][j].red = r;
                filteredBitmap[i][j].green = g;
                filteredBitmap[i][j].blue = b;
            }
        }

        return filteredImage;
    }
}
