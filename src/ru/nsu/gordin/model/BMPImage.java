package ru.nsu.gordin.model;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

public class BMPImage {
    private FileHeader fileHeader;
    private ImageHeader imageHeader;
    private PixelData pixelData;
    static private int FILE_HEADER_SIZE = 14;
    static private int IMAGE_HEADER_SIZE = 40;

    public static class BMPColor {
        public int red;
        public int green;
        public int blue;

        public BMPColor(){}

        public BMPColor(BMPColor color) {
            red = color.red;
            green = color.green;
            blue = color.blue;
        }

        public BMPColor(int r, int g, int b) {
            red = r;
            green = g;
            blue = b;
        }
    }

    protected class FileHeader {
        private short bfType;
        private int bfSize;
        private short bfReserved1;
        private short bfReserved2;
        private int bfOffBits;

        public FileHeader(){}

        public FileHeader(FileHeader header) {
            this.bfOffBits = header.bfOffBits;
            this.bfReserved2 = header.bfReserved2;
            this.bfReserved1 = header.bfReserved1;
            this.bfSize= header.bfSize;
            this.bfType = header.bfType;
        }

        public FileHeader(InputStream in) throws IOException {
            read(in);
        }

        public void write(OutputStream out) throws IOException {
            out.write(invertBytes(shortToBytes(bfType)));
            out.write(invertBytes(intToBytes(bfSize)));
            out.write(invertBytes(shortToBytes(bfReserved1)));
            out.write(invertBytes(shortToBytes(bfReserved2)));
            out.write(invertBytes(intToBytes(bfOffBits)));
        }

        public void read(InputStream in) throws IOException {
            byte[] byte2 = new byte[2];
            byte[] byte4 = new byte[4];
            if(-1 == in.read(byte2))
                throw new IOException("in.read() = -1");
            bfType = bytesToShort(invertBytes(byte2));
            if(-1 == in.read(byte4))
                throw new IOException("in.read() = -1");
            bfSize = bytesToInt(invertBytes(byte4));
            if(-1 == in.read(byte2))
                throw new IOException("in.read() = -1");
            bfReserved1 = bytesToShort(invertBytes(byte2));
            if(-1 == in.read(byte2))
                throw new IOException("in.read() = -1");
            bfReserved2 = bytesToShort(invertBytes(byte2));
            if(-1 == in.read(byte4))
                throw new IOException("in.read() = -1");
            bfOffBits = bytesToInt(invertBytes(byte4));
        }


    }

    protected class ImageHeader {
        private int biSize;
        private int biWidth;
        private int biHeight;
        private short biPlanes;
        private short biBitCount;
        private int biCompression;
        private int biSizeImage;
        private int biXPelsPerMeter;
        private int biYPelsPerMeter;
        private int biClrUsed;
        private int biClrImportant;

        public ImageHeader() {}

        public ImageHeader(ImageHeader header) {
            this.biSize = header.biSize;
            this.biWidth = header.biWidth;
            this.biHeight = header.biHeight;
            this.biPlanes = header.biPlanes;
            this.biBitCount = header.biBitCount;
            this.biCompression = header.biCompression;
            this.biSizeImage = header.biSizeImage;
            this.biXPelsPerMeter = header.biXPelsPerMeter;
            this.biYPelsPerMeter = header.biYPelsPerMeter;
            this.biClrUsed = header.biClrUsed;
            this.biClrImportant = header.biClrImportant;

        }

        public ImageHeader(InputStream in) throws IOException {
            read(in);
        }

        public void write(OutputStream out) throws IOException {
            out.write(invertBytes(intToBytes(biSize)));
            out.write(invertBytes(intToBytes(biWidth)));
            out.write(invertBytes(intToBytes(biHeight)));
            out.write(invertBytes(shortToBytes(biPlanes)));
            out.write(invertBytes(shortToBytes(biBitCount)));
            out.write(invertBytes(intToBytes(biCompression)));
            out.write(invertBytes(intToBytes(biSizeImage)));
            out.write(invertBytes(intToBytes(biXPelsPerMeter)));
            out.write(invertBytes(intToBytes(biYPelsPerMeter)));
            out.write(invertBytes(intToBytes(biClrUsed)));
            out.write(invertBytes(intToBytes(biClrImportant)));
        }

        public void read(InputStream in) throws IOException{
            byte[] byte2 = new byte[2];
            byte[] byte4 = new byte[4];
            in.read(byte4);
            biSize = bytesToInt(invertBytes(byte4));
            in.read(byte4);
            biWidth = bytesToInt(invertBytes(byte4));
            in.read(byte4);
            biHeight = bytesToInt(invertBytes(byte4));
            in.read(byte2);
            biPlanes = bytesToShort(invertBytes(byte2));
            in.read(byte2);
            biBitCount = bytesToShort(invertBytes(byte2));
            if(biBitCount != 24) {
                throw new IOException("only 24-bit bmp support, " + biBitCount + "-bit");
            }
            in.read(byte4);
            biCompression = bytesToInt(invertBytes(byte4));
            if(biCompression != 0) {
                throw new IOException("compression bmp image");
            }
            in.read(byte4);
            biSizeImage = bytesToInt(invertBytes(byte4));
            in.read(byte4);
            biXPelsPerMeter = bytesToInt(invertBytes(byte4));
            in.read(byte4);
            biYPelsPerMeter = bytesToInt(invertBytes(byte4));
            in.read(byte4);
            biClrUsed = bytesToInt(invertBytes(byte4));
            in.read(byte4);
            biClrImportant = bytesToInt(invertBytes(byte4));
        }
    }

    protected class PixelData {
        private BMPColor bitmap[][];

        public PixelData(){}

        public PixelData(PixelData data) {
//            this.bitmap = data.bitmap.clone();
            int n = data.bitmap.length;
            int m = data.bitmap[0].length;
            bitmap = new BMPColor[n][];
            for(int i = 0; i < n; i++) {
                bitmap[i] = new BMPColor[m];
                for(int j = 0; j < m; j++) {
                    bitmap[i][j] = new BMPColor(data.bitmap[i][j]);
                }
            }
        }

        public PixelData(InputStream in) throws IOException {
            read(in);
        }

        public void write(OutputStream out) throws IOException {
            int width = imageHeader.biWidth;
            int height = imageHeader.biHeight;
            int nullBytePad = (4 - (width * 3) % 4) % 4;
            if(height < 0) {
                height = -height;
                for(int i = 1; i <= height; i++) {
                    for(int j = 1; j <= width; j++) {
                        out.write(bitmap[i][j].blue);
                        out.write(bitmap[i][j].green);
                        out.write(bitmap[i][j].red);
                    }
                    for(int j = 0; j < nullBytePad; j++) {
                        out.write(0);
                    }
                }
            }
            else {
                for(int i = height; i > 0; i--) {
                    for(int j = 1; j < width + 1; j++) {
//                        System.out.println(i + " " + j);
                        out.write(bitmap[i][j].blue);
                        out.write(bitmap[i][j].green);
                        out.write(bitmap[i][j].red);
                    }
                    for(int j = 0; j < nullBytePad; j++) {
                        out.write(0);
                    }
                }
            }
        }

        public void read(InputStream in) throws IOException {
            int width = imageHeader.biWidth;
            int height = imageHeader.biHeight;
            int nullBytePad = (4 - (width * 3) % 4) % 4;
            bitmap = new BMPColor[height + 2][];
            bitmap[0] = new BMPColor[width + 2];
            bitmap[height + 1] = new BMPColor[width + 2];
            if(height < 0) {
                height = -height;
                for(int i = 1; i <= height; i++) {
                    bitmap[i] = new BMPColor[width + 2];
                    for(int j = 1; j <= width; j++) {
                        bitmap[i][j] = new BMPColor();
                        int b = in.read();
                        if(-1 == b)
                            throw new IOException("in.read() = -1");
                        bitmap[i][j].blue= b;

                        b = in.read();
                        if(-1 == b)
                            throw new IOException("in.read() = -1");
                        bitmap[i][j].green = b;

                        b = in.read();
                        if(-1 == b)
                            throw new IOException("in.read() = -1");

                        bitmap[i][j].red = b;
                    }
                    for(int j = 0; j < nullBytePad; j++) {
                        in.read();
                    }
                }
            }
            else {
                for(int i = height; i > 0; i--) {
                    bitmap[i] = new BMPColor[width + 2];
                    for(int j = 1; j <= width; j++) {
                        bitmap[i][j] = new BMPColor();
                        int b = in.read();
                        if(-1 == b) {
                            throw new IOException("in.read() = -1");
                        }
                        bitmap[i][j].blue = b;

                        b = in.read();
                        if(-1 == b) {
                            throw new IOException("in.read() = -1");
                        }
                        bitmap[i][j].green = b;

                        b = in.read();
                        if(-1 == b) {
                            throw new IOException("in.read() = -1");
                        }

                        bitmap[i][j].red = b;
                    }
                    for(int j = 0; j < nullBytePad; j++) {
                        in.read();
                    }
                }
            }
            for(int i = 1; i <= width; i++) {
                bitmap[0][i] = new BMPColor(bitmap[1][i]);
                bitmap[height + 1][i] = new BMPColor(bitmap[height][i]);
            }
            for(int i = 1; i <= height; i++) {
                bitmap[i][0] = new BMPColor(bitmap[i][1]);
                bitmap[i][width+1] = new BMPColor(bitmap[i][width]);
            }

            bitmap[0][0] = new BMPColor();
            bitmap[height+1][0] = new BMPColor();
            bitmap[0][width+1] = new BMPColor();
            bitmap[height+1][width+1] = new BMPColor();

            bitmap[0][0].blue = (bitmap[1][0].blue + bitmap[0][1].blue)/2;
            bitmap[height+1][0].blue = (bitmap[height][0].blue + bitmap[height+1][1].blue)/2;
            bitmap[0][width+1].blue = (bitmap[0][width].blue + bitmap[1][width+1].blue)/2;
            bitmap[height+1][width+1].blue = (bitmap[height+1][width].blue + bitmap[height][width+1].blue)/2;

            bitmap[0][0].green = (bitmap[1][0].green + bitmap[0][1].green)/2;
            bitmap[height+1][0].green = (bitmap[height][0].green + bitmap[height+1][1].green)/2;
            bitmap[0][width+1].green = (bitmap[0][width].green + bitmap[1][width+1].green)/2;
            bitmap[height+1][width+1].green = (bitmap[height+1][width].green + bitmap[height][width+1].green)/2;

            bitmap[0][0].red = (bitmap[1][0].red + bitmap[0][1].red)/2;
            bitmap[height+1][0].red = (bitmap[height][0].red + bitmap[height+1][1].red)/2;
            bitmap[0][width+1].red = (bitmap[0][width].red + bitmap[1][width+1].red)/2;
            bitmap[height+1][width+1].red = (bitmap[height+1][width].red + bitmap[height][width+1].red)/2;
        }
    }

    public BMPImage() {
        fileHeader = new FileHeader();
        imageHeader = new ImageHeader();
        pixelData = new PixelData();
    }

    public BMPImage(BMPImage image) {
        fileHeader = new FileHeader(image.fileHeader);
        imageHeader = new ImageHeader(image.imageHeader);
        pixelData = new PixelData(image.pixelData);
    }

    public BMPImage(InputStream in) throws IOException {
        fileHeader = new FileHeader(in);
        imageHeader = new ImageHeader(in);
        pixelData = new PixelData(in);
    }

    public void read(InputStream in) throws IOException {
        fileHeader.read(in);
        imageHeader.read(in);
        pixelData.read(in);
    }

    public void write(OutputStream out) throws IOException {
        fileHeader.write(out);
        imageHeader.write(out);
        pixelData.write(out);
    }

    public BMPColor[][] getBitMap() {
        return  pixelData.bitmap;
    }

    public BMPImage copyPart(int x, int y, int width, int height) {
        BMPImage image = new BMPImage(this);
        image.imageHeader.biHeight = height;
        image.imageHeader.biWidth = width;
        image.fileHeader.bfSize = FILE_HEADER_SIZE + IMAGE_HEADER_SIZE + height * width;

        BMPColor[][] bitmap = new BMPColor[width + 2][height + 2];
        if(height > pixelData.bitmap.length) {
            height = pixelData.bitmap.length;
        }
        if(width > pixelData.bitmap[0].length) {
            width = pixelData.bitmap[0].length;
        }
        for(int j = 0; j <= height + 1; j++) {
            for(int i = 0; i <= width + 1; i++) {
                bitmap[j][i] = pixelData.bitmap[y + j][x + i];
            }
        }

        for(int i = 1; i <= width; i++) {
            bitmap[0][i] = new BMPColor(bitmap[1][i]);
            bitmap[height + 1][i] = new BMPColor(bitmap[height][i]);
        }
        for(int i = 1; i <= height; i++) {
            bitmap[i][0] = new BMPColor(bitmap[i][1]);
            bitmap[i][width+1] = new BMPColor(bitmap[i][width]);
        }

        bitmap[0][0] = new BMPColor();
        bitmap[height+1][0] = new BMPColor();
        bitmap[0][width+1] = new BMPColor();
        bitmap[height+1][width+1] = new BMPColor();

        bitmap[0][0].blue = (bitmap[1][0].blue + bitmap[0][1].blue)/2;
        bitmap[height+1][0].blue = (bitmap[height][0].blue + bitmap[height+1][1].blue)/2;
        bitmap[0][width+1].blue = (bitmap[0][width].blue + bitmap[1][width+1].blue)/2;
        bitmap[height+1][width+1].blue = (bitmap[height+1][width].blue + bitmap[height][width+1].blue)/2;

        bitmap[0][0].green = (bitmap[1][0].green + bitmap[0][1].green)/2;
        bitmap[height+1][0].green = (bitmap[height][0].green + bitmap[height+1][1].green)/2;
        bitmap[0][width+1].green = (bitmap[0][width].green + bitmap[1][width+1].green)/2;
        bitmap[height+1][width+1].green = (bitmap[height+1][width].green + bitmap[height][width+1].green)/2;

        bitmap[0][0].red = (bitmap[1][0].red + bitmap[0][1].red)/2;
        bitmap[height+1][0].red = (bitmap[height][0].red + bitmap[height+1][1].red)/2;
        bitmap[0][width+1].red = (bitmap[0][width].red + bitmap[1][width+1].red)/2;
        bitmap[height+1][width+1].red = (bitmap[height+1][width].red + bitmap[height][width+1].red)/2;

        image.pixelData.bitmap = bitmap;
        return image;
    }

    public int getWidth() {
        return imageHeader.biWidth;
    }

    public int getHeight() {
        return imageHeader.biHeight;
    }

    static byte[] shortToBytes(short value) {
        ByteBuffer buffer = ByteBuffer.allocate(2);
        buffer.putShort(value);
        buffer.flip();
        return buffer.array();
    }

    static byte[] intToBytes(int value) {
        ByteBuffer buffer = ByteBuffer.allocate(4);
        buffer.putInt(value);
        buffer.flip();
        return buffer.array();
    }

    static short bytesToShort(byte[] bytes) {
        ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
        return byteBuffer.getShort();
    }

    static int bytesToInt(byte[] bytes) {
        ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
        return byteBuffer.getInt();
    }

    //invert endianness
    static byte[] invertBytes(byte[] bytes) {
        int length = bytes.length;
        byte b;
        for(int i = 0; i < length / 2; i++) {
            b = bytes[length - 1 - i];
            bytes[length - 1 - i] = bytes[i];
            bytes[i] = b;
        }
        return bytes;
    }

}
