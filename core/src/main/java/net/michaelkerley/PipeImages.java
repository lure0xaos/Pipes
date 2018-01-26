package net.michaelkerley;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import java.io.IOException;
import java.util.logging.Logger;

public final class PipeImages {
    private static final int[] BITMAP_SIZES = {42, 21, 15, 9}; // Descending order
    private static final int NUM_IMAGES = 16;
    private static final Logger logger = Logger.getLogger(PipeImages.class.getName());
    private static final String LOCATION_D = "/%d.png";
    public static int size = 9;
    public static boolean imagesLoaded;
    public static Image[] connectedImages;
    public static Image[] disconnectedImages;

    private PipeImages() {
    }

    public static void decrementSize() {
        setSize(size - 1);
    }

    @Contract(pure = true)
    private static int getBestBitmapSize(int preferredSize) {
        for (int bitmapSize : BITMAP_SIZES) {
            if (bitmapSize <= preferredSize) {
                return bitmapSize;
            }
        }
        return 0;
    }

    private static @NotNull Image extractImage(Image source, int xOffset, int yOffset, int width, int height) {
        Image img = Image.createImage(width, height);
        img.getGraphics().drawImage(source, -xOffset, -yOffset, Graphics.TOP | Graphics.LEFT);
        return img;
    }

    @Contract(pure = true)
    public static int getSize() {
        return size;
    }

    public static void setSize(int preferredSize) {
        size = preferredSize;
        int bestSize = getBestBitmapSize(preferredSize);
        if (bestSize > 0) {
            size = bestSize;
        }
    }

    public static void incrementSize() {
        int bestSize = size;
        for (int bitmapSize : BITMAP_SIZES) {
            if (bitmapSize > size) {
                bestSize = bitmapSize;
            }
        }
        setSize(bestSize);
    }

    public static void loadBitmaps() {
        if (size > 0) {
            try {
                Image allPipes = Image.createImage(String.format(LOCATION_D, size));
                connectedImages = new Image[NUM_IMAGES];
                disconnectedImages = new Image[NUM_IMAGES];
                for (int n = 0; n < NUM_IMAGES; ++n) {
                    disconnectedImages[n] = extractImage(allPipes, n * size, 0, size, size);
                    connectedImages[n] = extractImage(allPipes, n * size, size, size, size);
                }
                imagesLoaded = true;
                return;
            } catch (IOException e) {
                logger.severe(e.getLocalizedMessage());
                imagesLoaded = false;
                return;
            }
        }
        imagesLoaded = false;
    }

    public static void zoom(boolean in) {
        if (in) {
            incrementSize();
        } else {
            decrementSize();
        }
        loadBitmaps();
    }

    public static @Nullable Image loadImage(@Nullable String[] fileNames, int maxWidth, int maxHeight) {
        if (fileNames == null || fileNames.length == 0) {
            return null;
        }
        for (String filename : fileNames) {
            Image img = null;
            try {
                img = Image.createImage(filename);
            } catch (IOException e) {
//                PipesCanvas.logger.severe(e.getLocalizedMessage());
            }
            if (img != null && img.getWidth() <= maxWidth && img.getHeight() <= maxHeight) {
                return img;
            }
        }
        return null;
    }
}
