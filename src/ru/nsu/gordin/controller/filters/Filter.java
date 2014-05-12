package ru.nsu.gordin.controller.filters;

import ru.nsu.gordin.model.BMPImage;

public interface Filter {
    BMPImage apply(final BMPImage image);
}
