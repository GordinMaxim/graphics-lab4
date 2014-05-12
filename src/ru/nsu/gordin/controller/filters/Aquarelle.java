package ru.nsu.gordin.controller.filters;

import ru.nsu.gordin.model.BMPImage;

import javax.swing.*;
import java.util.Arrays;

public class Aquarelle implements Filter {

    @Override
    public BMPImage apply(BMPImage image) {
        BMPImage medianImage = new BMPImage(image);
        BMPImage filteredImage = new BMPImage(image);
        double ratio = 0.75;
        double[][] core = {{-ratio, -ratio, -ratio},
                {-ratio, 1.0 + 8*ratio, -ratio},
                {-ratio, -ratio, -ratio}};

        BMPImage.BMPColor[][] bitmap = image.getBitMap();
        BMPImage.BMPColor[][] medianBitmap = medianImage.getBitMap();
        BMPImage.BMPColor[][] filteredBitmap = filteredImage.getBitMap();
        for(int i = 1; i <= filteredImage.getHeight(); i++) {
            for(int j = 1; j <= filteredImage.getWidth(); j++) {
                int[] r = new int[9];
                int[] g = new int[9];
                int[] b = new int[9];
                for(int k = 0; k < 3; k++) {
                    for(int l = 0; l < 3; l++) {
                        r[k*3 + l] = bitmap[i + (k-1)][j + (l-1)].red;
                    }
                }

                for(int k = 0; k < 3; k++) {
                    for(int l = 0; l < 3; l++) {
                        g[k*3 + l] = bitmap[i + (k-1)][j + (l-1)].green;
                    }
                }

                for(int k = 0; k < 3; k++) {
                    for(int l = 0; l < 3; l++) {
                        b[k*3 + l] = bitmap[i + (k-1)][j + (l-1)].blue;
                    }
                }
                Arrays.sort(r);
                Arrays.sort(g);
                Arrays.sort(b);
                medianBitmap[i][j].red = r[5];
                medianBitmap[i][j].green = g[5];
                medianBitmap[i][j].blue = b[5];
            }
        }

        for(int i = 1; i <= filteredImage.getHeight(); i++) {
            for(int j = 1; j <= filteredImage.getWidth(); j++) {
                filteredBitmap[i][j].red = (int) (core[0][0] * medianBitmap[i - 1][j - 1].red +
                        core[0][1] * medianBitmap[i - 1][j].red +
                        core[0][2] * medianBitmap[i - 1][j + 1].red + core[1][0] * medianBitmap[i][j + 1].red +
                        core[1][1] * medianBitmap[i][j].red + core[1][2] * medianBitmap[i][j + 1].red +
                        core[2][0] * medianBitmap[i + 1][j - 1].red + core[2][1] * medianBitmap[i + 1][j].red +
                        core[2][2] * medianBitmap[i + 1][j + 1].red);
                if(filteredBitmap[i][j].red < 0) filteredBitmap[i][j].red = 0;
                if(filteredBitmap[i][j].red > 255) filteredBitmap[i][j].red = 255;

                filteredBitmap[i][j].green = (int) (core[0][0] * medianBitmap[i - 1][j - 1].green +
                        core[0][1] * medianBitmap[i - 1][j].green +
                        core[0][2] * medianBitmap[i - 1][j + 1].green + core[1][0] * medianBitmap[i][j + 1].green +
                        core[1][1] * medianBitmap[i][j].green + core[1][2] * medianBitmap[i][j + 1].green +
                        core[2][0] * medianBitmap[i + 1][j - 1].green + core[2][1] * medianBitmap[i + 1][j].green +
                        core[2][2] * medianBitmap[i + 1][j + 1].green);

                if(filteredBitmap[i][j].green < 0) filteredBitmap[i][j].green = 0;
                if(filteredBitmap[i][j].green > 255) filteredBitmap[i][j].green = 255;

                filteredBitmap[i][j].blue = (int) (core[0][0] * medianBitmap[i - 1][j - 1].blue +
                        core[0][1] * medianBitmap[i - 1][j].blue +
                        core[0][2] * medianBitmap[i - 1][j + 1].blue + core[1][0] * medianBitmap[i][j + 1].blue +
                        core[1][1] * medianBitmap[i][j].blue + core[1][2] * medianBitmap[i][j + 1].blue +
                        core[2][0] * medianBitmap[i + 1][j - 1].blue + core[2][1] * medianBitmap[i + 1][j].blue +
                        core[2][2] * medianBitmap[i + 1][j + 1].blue);

                if(filteredBitmap[i][j].blue < 0) filteredBitmap[i][j].blue = 0;
                if(filteredBitmap[i][j].blue > 255) filteredBitmap[i][j].blue = 255;
            }
        }

        return filteredImage;
    }
}
