package ru.nsu.gordin.controller.filters;

import ru.nsu.gordin.model.BMPImage;

public class Gauss implements Filter{

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
                filteredBitmap[i][j].red = (int) (core[0][0] * bitmap[i - 1][j - 1].red +
                        core[0][1] * bitmap[i - 1][j].red +
                        core[0][2] * bitmap[i - 1][j + 1].red + core[1][0] * bitmap[i][j + 1].red +
                        core[1][1] * bitmap[i][j].red + core[1][2] * bitmap[i][j + 1].red +
                        core[2][0] * bitmap[i + 1][j - 1].red + core[2][1] * bitmap[i + 1][j].red +
                        core[2][2] * bitmap[i + 1][j + 1].red);

                filteredBitmap[i][j].green = (int) (core[0][0] * bitmap[i - 1][j - 1].green +
                        core[0][1] * bitmap[i - 1][j].green +
                        core[0][2] * bitmap[i - 1][j + 1].green + core[1][0] * bitmap[i][j + 1].green +
                        core[1][1] * bitmap[i][j].green + core[1][2] * bitmap[i][j + 1].green +
                        core[2][0] * bitmap[i + 1][j - 1].green + core[2][1] * bitmap[i + 1][j].green +
                        core[2][2] * bitmap[i + 1][j + 1].green);

                filteredBitmap[i][j].blue = (int) (core[0][0] * bitmap[i - 1][j - 1].blue +
                        core[0][1] * bitmap[i - 1][j].blue +
                        core[0][2] * bitmap[i - 1][j + 1].blue + core[1][0] * bitmap[i][j + 1].blue +
                        core[1][1] * bitmap[i][j].blue + core[1][2] * bitmap[i][j + 1].blue +
                        core[2][0] * bitmap[i + 1][j - 1].blue + core[2][1] * bitmap[i + 1][j].blue +
                        core[2][2] * bitmap[i + 1][j + 1].blue);
            }
        }

        return filteredImage;
    }
}
