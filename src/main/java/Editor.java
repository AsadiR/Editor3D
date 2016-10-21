package main.java;

import javafx.application.Application;
import javafx.geometry.Point3D;
import javafx.scene.*;
import javafx.scene.effect.Light;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.*;
import javafx.stage.Stage;

import com.interactivemesh.jfx.importer.stl.StlMeshImporter;

import java.io.File;

public class Editor extends Application {
    private static final String MESH_A_FILENAME = "Cube.stl";
    private static final String MESH_B_FILENAME = "Cube.stl";

    //root of tree
    final Group root = new Group();
    final Xform world = new Xform();
    final Xform moleculeGroup = new Xform();
    private Controller controller;
    //camera and 3 Xform
    final PerspectiveCamera camera = new PerspectiveCamera(true);
    final Xform cameraXform = new Xform();
    final Xform cameraXform2 = new Xform();
    final Xform cameraXform3 = new Xform();
    private static final double CAMERA_INITIAL_DISTANCE = -450;
    private static final double CAMERA_INITIAL_X_ANGLE = 70.0;
    private static final double CAMERA_INITIAL_Y_ANGLE = 320.0;
    private static final double CAMERA_NEAR_CLIP = 0.1;
    private static final double CAMERA_FAR_CLIP = 10000.0;
    //objects
    final Xform groupA = new Xform();
    final Xform groupB = new Xform();

    MeshView[] loadMeshViews(String fileName) {
        ClassLoader classLoader = getClass().getClassLoader();
        //System.out.println(classLoader.getResource(fileName));
        File file = new File(classLoader.getResource(fileName).getFile());
        StlMeshImporter importer;
        importer = new StlMeshImporter();
        importer.read(file);
        Mesh mesh = importer.getImport();
        importer.close();
        return new MeshView[] { new MeshView(mesh) };
    }

    private void buildCamera() {
        System.out.println("buildCamera()");
        root.getChildren().add(cameraXform);
        cameraXform.getChildren().add(cameraXform2);
        cameraXform2.getChildren().add(cameraXform3);
        cameraXform3.getChildren().add(camera);
        cameraXform3.setRotateZ(180.0);

        camera.setNearClip(CAMERA_NEAR_CLIP);
        camera.setFarClip(CAMERA_FAR_CLIP);
        camera.setTranslateZ(CAMERA_INITIAL_DISTANCE);
        cameraXform.ry.setAngle(CAMERA_INITIAL_Y_ANGLE);
        cameraXform.rx.setAngle(CAMERA_INITIAL_X_ANGLE);
    }

    public void meshTest(MeshView obj, Xform parent) {
        TriangleMesh m = (TriangleMesh)obj.getMesh();
        System.out.println(m.getVertexFormat());
        System.out.println(m.getFaces().size());
        System.out.println(m.getPoints().size());
        int[] faces_array = new int[m.getFaces().size()];
        m.getFaces().toArray(faces_array);
        int step = m.getPoints().size()/3;
        for (int i=0; i<faces_array.length; i++) {
            if (i%2 == 0) {
                int point_index = faces_array[i]*3;
                System.out.println(point_index);
                Point3D point = new Point3D(
                        m.getPoints().get(point_index),
                        m.getPoints().get(point_index + 1),
                        m.getPoints().get(point_index + 2)
                );

                Xform groupPoint = new Xform();
                Box box = new Box(.1f, .1f, .1f);
                groupPoint.setTranslate(point.getX(), point.getY(), point.getZ());
                groupPoint.getChildren().add(box);
                parent.getChildren().add(groupPoint);

                System.out.println(point);
            }
        }
        //8 point in cube
        //24 point in points array

    }

    public void addObjects() {
        final PhongMaterial redMaterial = new PhongMaterial();
        redMaterial.setDiffuseColor(Color.RED);
        redMaterial.setSpecularColor(Color.WHITE);

        final PhongMaterial blueMaterial = new PhongMaterial();
        blueMaterial.setDiffuseColor(Color.BLUE);
        blueMaterial.setSpecularColor(Color.WHITE);

        //Sphere objA = new Sphere(40.0);
        MeshView[] meshViews;
        meshViews = loadMeshViews(MESH_A_FILENAME);
        //System.out.println(objA.length);
        MeshView objA = meshViews[0];


        objA.setMaterial(redMaterial);
        objA.setDrawMode(DrawMode.LINE);
        groupA.getChildren().add(objA);
        groupA.setTx(25);
        groupA.setTy(25);
        groupA.setTz(25);
        groupA.setScale(20, 20, 20);


        meshViews = loadMeshViews(MESH_B_FILENAME);
        MeshView objB = meshViews[0];
        objB.setMaterial(blueMaterial);
        objB.setDrawMode(DrawMode.LINE);
        groupB.setScale(20, 20, 20);
        groupB.getChildren().add(objB);
        meshTest(objB, groupB);

        world.getChildren().add(groupA);
        world.getChildren().add(groupB);

    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        buildCamera();
        addObjects();

        root.getChildren().add(world);
        root.setDepthTest(DepthTest.ENABLE);
        Scene scene = new Scene(root, 1024, 768, true);
        scene.setCamera(camera);
        controller = new Controller(scene, this);

        primaryStage.setTitle("Creating of stage...");
        primaryStage.setScene(scene);
        scene.setFill(Color.GREY);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
