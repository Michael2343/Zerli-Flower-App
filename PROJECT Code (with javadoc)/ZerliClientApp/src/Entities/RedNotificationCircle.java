package Entities;

import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

import java.io.Serializable;

// TODO: Auto-generated Javadoc
/**
 * The Class RedNotificationCircle used to show numbers of notifications .
 */
public class RedNotificationCircle implements Serializable {

/** The image. */
private ImageView image;

/** The label. */
private Label label;

/** The Notifications number. */
private int NotificationsNumber;

/**
 * Instantiates a new red notification circle.
 *
 * @param circle image
 * @param label the present the number
 * @param NotificationsNumber the notifications number
 */
public RedNotificationCircle(ImageView image, Label label, int NotificationsNumber) {
	super();
	this.image = image;
	this.label = label;
	this.NotificationsNumber = NotificationsNumber;
}

/**
 * Gets the image.
 *
 * @return the image
 */
public ImageView getImage() {
	return image;
}

/**
 * Sets the image.
 *
 * @param image the new image
 */
public void setImage(ImageView image) {
	this.image = image;
}

/**
 * Gets the label.
 *
 * @return the label
 */
public Label getLabel() {
	return label;
}

/**
 * Sets the label.
 *
 * @param label the new label
 */
public void setLabel(Label label) {
	this.label = label;
}

/**
 * Gets the cart notifications number.
 *
 * @return the cart notifications number
 */
public int getCartNotificationsNumber() {
	return NotificationsNumber;
}

/**
 * Sets the cart notifications number.
 *
 * @param NotificationsNumber the new cart notifications number
 */
public void setCartNotificationsNumber(int NotificationsNumber) {
	this.NotificationsNumber = NotificationsNumber;
}

}
