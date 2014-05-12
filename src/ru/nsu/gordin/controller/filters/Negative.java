package ru.nsu.gordin.controller.filters;

import ru.nsu.gordin.model.BMPImage;

public class Negative implements Filter {
    @Override
    public BMPImage apply(BMPImage image) {
        BMPImage filteredImage = new BMPImage(image);
        double[][] core = {{0.5/6, 0.75/6, 0.5/6},
                {0.75/6, 1.0/6, 0.75/6},
                {0.5/6, 0.75/6, 0.5/6}};

        BMPImage.BMPColor[][] bitmap = image.getBitMap();
        BMPImage.BMPColor[][] filteredBitmap = filteredImage.getBitMap();
        for(int i = 1; i <= filteredImage.getHeight(); i++) {
            for(int j = 1; j <= filteredImage.getWidth(); j++) {
                filteredBitmap[i][j].red = 255 - filteredBitmap[i][j].red;
                filteredBitmap[i][j].green = 255 - filteredBitmap[i][j].green;
                filteredBitmap[i][j].blue = 255 - filteredBitmap[i][j].blue;
            }
        }
        return filteredImage;
    }
}
