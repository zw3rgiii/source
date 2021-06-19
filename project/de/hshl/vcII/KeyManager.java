package project.de.hshl.vcII;

import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.shape.StrokeType;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import project.de.hshl.vcII.entities.moving.Ball;
import project.de.hshl.vcII.entities.stationary.Scissors;
import project.de.hshl.vcII.entities.stationary.Wall;
import project.de.hshl.vcII.mvc.MainWindowModel;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import project.de.hshl.vcII.utils.MyVector;

/**
 * Manages Key inputs.
 * Also handles mouse inputs.
 */
public class KeyManager {
    private MainWindowModel mainWindowModel;

    public KeyManager(){
        mainWindowModel = MainWindowModel.get();
    }


    /**
     * Toggles only if the 'W' key is pushed.
     * chooses a Ball, Wall or Scissors and highlights it green,
     * @param e to know where the cursor currently is
     */
    public void manageMouse(MouseEvent e){
       // mainWindowModel.setChoiceMade(false);
        mainWindowModel.setCurrentlySelected(null);
        Rectangle clickingHitBox = new Rectangle((int) e.getX(), (int) e.getY(),1,1);
        for(Wall w : mainWindowModel.getWallManager().getWalls()){
            if(clickingHitBox.intersects(w.getPosVec().x - Wall.DEFAULT_WIDTH/2, w.getPosVec().y - Wall.DEFAULT_HEIGHT/2, Wall.DEFAULT_WIDTH, Wall.DEFAULT_HEIGHT)){
                mark(w);
                mainWindowModel.setChoiceMade(true);
                mainWindowModel.setCurrentlySelected(w);
                mainWindowModel.getWallManager().setW(w);
                if(!mainWindowModel.getADrawingPane().getChildren().contains(w.getCollision())) {
                    mainWindowModel.getADrawingPane().getChildren().add(w.getCollision());
                    return;
                }
            }
        }
        if (mainWindowModel.getScissorsManager().getS() != null) {
            Scissors s = mainWindowModel.getScissorsManager().getS();
            if(clickingHitBox.intersects(s.getPosVec().x, s.getPosVec().y, s.getRectangle().getWidth(), s.getRectangle().getHeight())){
                mark(s);
                mainWindowModel.setChoiceMade(true);
                mainWindowModel.setCurrentlySelected(s);
                mainWindowModel.getScissorsManager().setS(s);
                if(!mainWindowModel.getADrawingPane().getChildren().contains(s.getG())) {
                    mainWindowModel.getADrawingPane().getChildren().add(s.getG());
                    return;
                }
            }
        }

        MyVector clickHitBox = new MyVector(e.getX(), e.getY());
        for(Ball b : mainWindowModel.getBallManager().getBalls()){
            if(MyVector.distance(clickHitBox, b.getPosVec()) < b.getRadius()){
                mark(b);
                mainWindowModel.setChoiceMade(true);
                mainWindowModel.setCurrentlySelected(b);
            }
        }
    }

    /**
     * Manages all key inputs (currently handled: W, E, Q and DEL)
     * @param keyCode to know which key is pressed
     */
    public void manageInputs(KeyCode keyCode){
        switch (keyCode){
            case W:
                // The block can be picked
                mainWindowModel.setChoiceEnabled(!mainWindowModel.isChoiceEnabled());
                // Remove stroke
                if(mainWindowModel.isChoiceMade()) unMark();
                break;
            case E:
                // The chosen block is rotated left
                if(mainWindowModel.isChoiceMade() & mainWindowModel.getCurrentlySelected() instanceof Wall) MainWindowModel.get().getSpin().rotateRight((Wall) mainWindowModel.getCurrentlySelected());
                else if(mainWindowModel.isChoiceMade() & mainWindowModel.getCurrentlySelected() instanceof Scissors) MainWindowModel.get().getSpin().rotateRight((Scissors) mainWindowModel.getCurrentlySelected());
                break;
            case Q:
                // The chosen block is rotated right
                if(mainWindowModel.isChoiceMade() & mainWindowModel.getCurrentlySelected() instanceof Wall) MainWindowModel.get().getSpin().rotateLeft((Wall) mainWindowModel.getCurrentlySelected());
                else if(mainWindowModel.isChoiceMade() & mainWindowModel.getCurrentlySelected() instanceof Scissors) MainWindowModel.get().getSpin().rotateLeft((Scissors) mainWindowModel.getCurrentlySelected());
                break;
            case S:
                //close scissors
                if(mainWindowModel.getScissorsManager().getS() != null) {
                    mainWindowModel.getScissorsManager().getS().applyRotation(2);
                    mainWindowModel.getScissorsManager().getS().setClosing(true);
                    mainWindowModel.getScissorsManager().getS().getRectangle().setStrokeWidth(0);
                }
                break;
            case DELETE:
                // The chosen block is deleted
                if(mainWindowModel.isChoiceMade()) deleteBlockOrWall();
            case SPACE:
                mainWindowModel.getSimulator().run();

        }
    }

    public void choose(String s){
        unMarkAll();
        if (!mainWindowModel.isChoiceMade()) {
            String[] strings = s.split(" ");
            if (strings[0].equals("Ball")) {
                for (Ball b : mainWindowModel.getBallManager().getBalls()) {
                    if ((b.getNumber() + "").equals(strings[2])) {
                        unMark();
                        mainWindowModel.setCurrentlySelected(b);
                        mark(b);
                        mainWindowModel.getBallManager().setB(b);
                    }
                }
            } else if (strings[0].equals("Wand")) {
                for (Wall w : mainWindowModel.getWallManager().getWalls()) {
                    if ((w.getNumber() + "").equals(strings[2])) {
                        unMark();
                        mainWindowModel.setCurrentlySelected(w);
                        mark(w);
                        mainWindowModel.getWallManager().setW(w);
                        if(!mainWindowModel.getADrawingPane().getChildren().contains(w.getCollision())) {
                            mainWindowModel.getADrawingPane().getChildren().add(w.getCollision());
                            return;
                        }
                    }
                }
            }else {
                mainWindowModel.getScissorsManager().getS().setMarkedStroke();
                mainWindowModel.setCurrentlySelected(mainWindowModel.getScissorsManager().getS());
                if(!mainWindowModel.getADrawingPane().getChildren().contains(mainWindowModel.getScissorsManager().getS().getG())) {
                    mainWindowModel.getADrawingPane().getChildren().add(mainWindowModel.getScissorsManager().getS().getG());
                    return;
                }
            }
            mainWindowModel.setChoiceMade(true);
        }else{
            unMark();
            mainWindowModel.setChoiceMade(false);
        }
    }

    // Helper
    private void mark(Object o){
        if(o instanceof Ball) {
            ((Ball) o).setStrokeType(StrokeType.OUTSIDE);
            ((Ball) o).setStrokeWidth(2);
            ((Ball) o).setStroke(new Color(0, 0.8, 0, 1));
        } else if (o instanceof Wall){
            ((Wall) o).getCollision().setStrokeType(StrokeType.OUTSIDE);
            ((Wall) o).getCollision().setStrokeWidth(2);
            ((Wall) o).getCollision().setStroke(new Color(0, 0.8, 0, 1));
            ((Wall) o).getCollision().setFill(Color.TRANSPARENT);
        } else if (o instanceof Scissors){
            ((Scissors) o).setMarkedStroke();
        }
    }

    public void unMarkAll() {
        for(Ball b : mainWindowModel.getBallManager().getBalls()) {
            b.setStroke(b.getStrokeColor());
            b.setStrokeWidth(5);
            b.setStrokeType(StrokeType.CENTERED);
        }
        for(Wall w : mainWindowModel.getWallManager().getWalls()) {
            w.getCollision().setStrokeWidth(0);
        }
        if(mainWindowModel.getScissorsManager().getS() != null) {
            mainWindowModel.getScissorsManager().getS().setUnmarkedStroke();
        }
        mainWindowModel.setChoiceMade(false);
    }

    private void unMark(){
        if(mainWindowModel.getCurrentlySelected() instanceof Wall)
            ((Wall) mainWindowModel.getCurrentlySelected()).getCollision().setStrokeWidth(0);
        else if(mainWindowModel.getCurrentlySelected() instanceof Ball){
            ((Ball) mainWindowModel.getCurrentlySelected()).setStroke(((Ball) mainWindowModel.getCurrentlySelected()).getStrokeColor());
            ((Ball) mainWindowModel.getCurrentlySelected()).setStrokeWidth(5);
            ((Ball) mainWindowModel.getCurrentlySelected()).setStrokeType(StrokeType.CENTERED);
        }
        else if(mainWindowModel.getCurrentlySelected() instanceof Scissors)
            ((Scissors) mainWindowModel.getCurrentlySelected()).setUnmarkedStroke();
        mainWindowModel.setChoiceMade(false);
    }

    private void deleteBlockOrWall() {
        if (mainWindowModel.getCurrentlySelected() instanceof Wall){
            mainWindowModel.getADrawingPane().getChildren().remove(((Wall) mainWindowModel.getCurrentlySelected()).getCollision());
            mainWindowModel.getADrawingPane().getChildren().remove(((Wall) mainWindowModel.getCurrentlySelected()).getTexture());
            mainWindowModel.getWallManager().getWalls().remove(((Wall) mainWindowModel.getCurrentlySelected()));
        }else if(mainWindowModel.getCurrentlySelected() instanceof Ball){
            mainWindowModel.getADrawingPane().getChildren().remove((Ball) mainWindowModel.getCurrentlySelected());
            mainWindowModel.getBallManager().getBalls().remove((Ball) mainWindowModel.getCurrentlySelected());
        }
            mainWindowModel.setCurrentlySelected(null);
            mainWindowModel.setChoiceMade(false);
    }

    /**
     * Just an old artifact left in because it man be needed.
     * @param b     the Ball focused
     * @param posX  cursor position (on X)
     * @param posY  cursor position (on Y)
     */
    private void hoverBox(Ball b, double posX, double posY){
        AnchorPane aRoot = new AnchorPane();
        VBox vbParams = new VBox();

        //Label pos = new Label("Position:\t(" + b.getPosVec().x + "/" + b.getPosVec().y + ")");
        Label v = new Label("Geschw.\t(" + b.getVelVec().x + "/" + b.getVelVec().y + ")");
        Label a = new Label("Beschl.\t(" + b.getAccVec().x + "/" + b.getAccVec().y + ")");
        Label radius = new Label("Radius:\t" + b.getRadius());
        Label mass = new Label("Masse:\t" + b.getMass());
        Label elasticity = new Label("Elastizität:\t" + b.getElasticity());
        Label totE = new Label("ges. Energie:\t" + b.getTotE_c());
        Label potE = new Label("pot. Energie:\t" + b.getPotE_c());
        Label kinE = new Label("kin. Energie:\t" + b.getKinE_c());
        Label lostE = new Label("Verlust:\t" + b.getLostE_c());

        vbParams.getChildren().addAll(v, a, radius, mass, elasticity, totE, potE, kinE,lostE);
        aRoot.getChildren().add(vbParams);
        aRoot.setMaxSize(150, 300);
        aRoot.setMinSize(150, 300);
        aRoot.setPrefSize(150, 300);

        Scene hoverBox = new Scene(aRoot);
        Stage secondary = new Stage();
        secondary.initStyle(StageStyle.UNDECORATED);
        secondary.setScene(hoverBox);
        secondary.show();
    }
}