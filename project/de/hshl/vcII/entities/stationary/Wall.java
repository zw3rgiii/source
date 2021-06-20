package project.de.hshl.vcII.entities.stationary;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;

import project.de.hshl.vcII.utils.MyVector;

import java.util.Arrays;
import java.util.Objects;

/**
 * Wall is an abstract class.
 * It is a Framework for all non-moving (wall-/floor-like) entities that should be a Wall.
 */

public class Wall {
    public static final double DEFAULT_WIDTH = 300, DEFAULT_HEIGHT = 100;

    private Image texture;
    private ImageView viewTexture;
    private Rectangle collision;
    private MyVector posVec;
    private double e_alpha;
    private double spin;
    private int number;
    private int orientation; //repräsentiert die Orientierung der Wand: 0 → level; 1 → rotateLeft; 2 → rotateRight

    public Wall(){
        texture = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/img/blocks/BlockNormal.png")));
        collision = new Rectangle(0, 0, DEFAULT_WIDTH, DEFAULT_HEIGHT);
        viewTexture = new ImageView(texture);
        posVec = new MyVector(0, 0);
        number = 0;
        orientation = 2;
    }

    public Wall(String texture, String collision, String posVec, String e_alpha, String spin, String number, String orientation) {
        this.texture = new Image(Objects.requireNonNull(getClass().getResourceAsStream(texture)));
        this.collision = constructRectFromString(collision);
        this.posVec = new MyVector(posVec);
        this.e_alpha = Double.parseDouble(e_alpha);
        this.spin = Double.parseDouble(spin);
        System.out.println("spin (String): " + spin + " spin (double): " + getSpin());
        this.number = Integer.parseInt(number);
        this.orientation = Integer.parseInt(orientation);
        viewTexture = new ImageView(texture);
    }

    private Rectangle constructRectFromString(String stringRect) {
        Rectangle rect;

        String sRect = stringRect.replace("Rectangle", "");
        sRect = sRect.replace("[", "");
        sRect = sRect.replace("]", "");
        String[] params = sRect.split(", ");
        double[] rawRect = new double[4];
        for(int i = 0; i < 4; i++){
            rawRect[i] = Double.parseDouble(params[i].split("=")[1]);
        }
        rect = new Rectangle(rawRect[0], rawRect[1], rawRect[2], rawRect[3]);

        return rect;
    }

    //_GETTER_ AND_SETTER_______________________________________________________________________________________________
    public Rectangle getCollision(){
        return collision;
    }

    public ImageView getTexture() {
        return viewTexture;
    }

    public void setPosVec(MyVector posVec) {
        this.posVec = posVec;
        viewTexture.setX(posVec.x - collision.getWidth()/2);
        viewTexture.setY(posVec.y - collision.getHeight()/2);
        collision.setX(posVec.x - collision.getWidth()/2);
        collision.setY(posVec.y - collision.getHeight()/2);
    }
    public MyVector getPosVec() {
        return posVec;
    }

    public void setSpin(double spin) {
        this.spin = spin;
    }
    public double getSpin() {
        return spin;
    }

    public void setNumber(int number) {
        this.number = number;
    }
    public int getNumber() {
        return number;
    }

    public void setE_alpha(double e_alpha) {
        this.e_alpha = e_alpha;
    }
    public double getE_alpha() {
        return e_alpha;
    }

    public void setOrientation(int orientation) {
        this.orientation = orientation;
    }
    public int getOrientation() {
        return orientation;
    }

    /*@Override
    public String toString() {
        return "No: " + this.number + ". -- Pos: (" + Math.round(this.posVec.x)+"/"+ Math.round(this.posVec.y)
                + ") -- image: (" + Math.round(Float.parseFloat(this.texture.toString()))
                + ") -- image view: (" + Math.round(Float.parseFloat(this.viewTexture.toString()))
                + ") -- rotation: " + this.spin + " -- bounding box: " + this.collision.toString();
    }*/

    public String save(){
        return ";texture: " + texture.getUrl()
                + ",,collision: " + collision.toString()
                + ",,posVec: " + posVec.toString()
                + ",,e_alpha: " + e_alpha
                + ",,spin: " + spin
                + ",,wall : " + number
                + ",,orientation: " + orientation;
    }
}
