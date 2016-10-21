package main.java;

import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

public class Controller {
    private Scene scene;
    private Editor editor;
    public Controller(Scene scene, Editor editor) {

        this.scene = scene;
        this.editor = editor;
        handleKeyboard(scene);
    }

    private double mousePosX;
    private double mousePosY;
    private double mouseOldX;
    private double mouseOldY;
    private double mouseDeltaX;
    private double mouseDeltaY;
    private static final double MOUSE_SPEED = 0.1;
    private static final double ROTATION_SPEED = 2.0;
    private static final double TRACK_SPEED = 0.3;

    private void switchOnCameraMode() {
        System.out.println("switchOnCameraMode ...");
        scene.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent me) {
                //System.out.println("Click");
                mousePosX = me.getSceneX();
                mousePosY = me.getSceneY();
                mouseOldX = me.getSceneX();
                mouseOldY = me.getSceneY();
            }
        });
        scene.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent me) {
                //System.out.println("Dragged");
                mouseOldX = mousePosX;
                mouseOldY = mousePosY;
                mousePosX = me.getSceneX();
                mousePosY = me.getSceneY();
                mouseDeltaX = (mousePosX - mouseOldX);
                mouseDeltaY = (mousePosY - mouseOldY);

                double modifier = 1.0;

                if (me.isPrimaryButtonDown()) {
                    editor.cameraXform.ry.setAngle(
                            editor.cameraXform.ry.getAngle() - mouseDeltaX*MOUSE_SPEED*modifier*ROTATION_SPEED);
                    editor.cameraXform.rx.setAngle(
                            editor.cameraXform.rx.getAngle() + mouseDeltaY*MOUSE_SPEED*modifier*ROTATION_SPEED);
                }
                else if (me.isSecondaryButtonDown()) {
                    double z = editor.camera.getTranslateZ();
                    double newZ = z + mouseDeltaX*MOUSE_SPEED*modifier;
                    editor.camera.setTranslateZ(newZ);
                }
                else if (me.isMiddleButtonDown()) {
                    editor.cameraXform2.t.setX(
                            editor.cameraXform2.t.getX() + mouseDeltaX*MOUSE_SPEED*modifier*TRACK_SPEED);
                    editor.cameraXform2.t.setY(
                            editor.cameraXform2.t.getY() + mouseDeltaY*MOUSE_SPEED*modifier*TRACK_SPEED);
                }
            }
        });
    }

    private void switchOnObjAMode() {
        System.out.println("switchOnObjAMode ...");
        double z = editor.camera.getTranslateZ();
        double newZ = z + 5;
        editor.camera.setTranslateZ(newZ);
    }

    private void switchOnObjBMode() {

    }

    private void handleKeyboard(Scene scene) {
        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                switch (event.getCode()) {
                    case DIGIT1:
                        switchOnObjAMode();
                        break;
                    case DIGIT2:
                        switchOnObjBMode();
                        break;
                    case DIGIT3:
                        switchOnCameraMode();
                        break;
                }
            }
        });
    }
}
