package javax.microedition.lcdui;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

public final class Image {
    private final java.awt.Image peer;
    private Graphics graphics;

    private Image(java.awt.Image image) {
        peer = image;
    }

    public static Image createImage(String location) throws IOException {
        URL url = Image.class.getResource(location);
        return new Image(ImageIO.read(url));
    }

    public static Image createImage(int width, int height) {
        return new Image(new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB));
    }

    public Graphics getGraphics() {
        if (graphics == null) {
            graphics = Graphics.forGraphics(peer.getGraphics());
        }
        return graphics;
    }

    public int getHeight() {
        return peer.getHeight(null);
    }

    java.awt.Image getPeer() {
        return peer;
    }

    public int getWidth() {
        return peer.getWidth(null);
    }
}
