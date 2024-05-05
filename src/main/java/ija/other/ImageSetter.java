/**
 * @package ija.other
 * @file ImageSetter.java
 * @class ImageSetter
 *
 * Class for setting the image to button or shape.
 *
 * @author Adam Valik
 */

package ija.other;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.ImagePattern;

import java.util.Objects;

/**
 * Class for setting the image to button or shape.
 */
public class ImageSetter {
    /**
     * Default constructor for ImageSetter.
     */
    public ImageSetter() {}

    /**
     * Sets the image to the button
     * @param path Path to the image
     * @return ImageView with the image
     */
    public static ImageView setImageView(String path) {
        return new ImageView(new Image(Objects.requireNonNull(ImageSetter.class.getResourceAsStream(path))));
    }

    /**
     * Sets the image to the shape
     * @param path Path to the image
     * @return ImagePattern with the image
     */
    public static ImagePattern setImagePattern(String path) {
        return new ImagePattern(new Image(Objects.requireNonNull(ImageSetter.class.getResourceAsStream(path))));
    }
}