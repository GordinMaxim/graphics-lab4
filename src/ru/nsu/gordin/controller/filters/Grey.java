package ru.nsu.gordin.controller.filters;

import ru.nsu.gordin.model.BMPImage;

public class Grey implements Filter {
    @Override
    public BMPImage apply(BMPImage image) {
        BMPImage filteredImage = new BMPImage(image);
        double[][] core = {{0, 0, 0},
                {0, 0, 7},
                {5, 3, 1}};

        BMPImage.BMPColor[][] bitmap = image.getBitMap();
        BMPImage.BMPColor[][] filteredBitmap = filteredImage.getBitMap();
        for(int i = 1; i <= filteredImage.getHeight(); i++) {
            for(int j = 1; j <= filteredImage.getWidth(); j++) {
                int r = filteredBitmap[i][j].red;
                int g = filteredBitmap[i][j].green;
                int b = filteredBitmap[i][j].blue;
                int y = (int)(((0.333) * r + (0.333) * g + (0.333) * b)/
                        (0.999));
                if(y < 0) {
                    y = 0;
                }
                if(y > 255) y = 255;
                filteredBitmap[i][j].red = y;
                filteredBitmap[i][j].green = y;
                filteredBitmap[i][j].blue = y;
            }
        }

        return filteredImage;
    }
}
