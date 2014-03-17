package net.lermex.soft3d;

import net.lermex.soft3d.engine.Camera;
import net.lermex.soft3d.engine.Device;
import net.lermex.soft3d.engine.Mesh;
import net.lermex.soft3d.math.Vector3;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.lang.reflect.InvocationTargetException;

public class CubeDemo {

    private final static int WIDTH = 1280;
    private final static int HEIGHT = 800;

    private final static int PIXELS = WIDTH * HEIGHT;

    private static long lastFrameTime = 1;
    private static long frameNumber;

    private static Mesh mesh;
    private static Camera camera;
    private static Device device;

    public static void main(String[] args) throws InvocationTargetException, InterruptedException {
        mesh = new Mesh("Cube", 8, 6);
        camera = new Camera();
        device = new Device(WIDTH, HEIGHT);

        mesh.getVertices()[0] = new Vector3(-1, 1, 1);
        mesh.getVertices()[1] = new Vector3(1, 1, 1);
        mesh.getVertices()[2] = new Vector3(-1, -1, 1);
        mesh.getVertices()[3] = new Vector3(-1, -1, -1);
        mesh.getVertices()[4] = new Vector3(-1, 1, -1);
        mesh.getVertices()[5] = new Vector3(1, 1, -1);
        mesh.getVertices()[6] = new Vector3(1, -1, 1);
        mesh.getVertices()[7] = new Vector3(1, -1, -1);

        mesh.getFaces()[0] = new Mesh.Face(0, 1, 2);
        mesh.getFaces()[1] = new Mesh.Face(1, 2, 6);
        mesh.getFaces()[2] = new Mesh.Face(0, 1, 4);
        mesh.getFaces()[3] = new Mesh.Face(2, 6, 3);

        mesh.getFaces()[4] = new Mesh.Face(7, 6, 3);
        mesh.getFaces()[5] = new Mesh.Face(7, 6, 5);
//        mesh.getFaces()[7] = new Mesh.Face(3, 6, 7);
//        mesh.getFaces()[8] = new Mesh.Face(0, 2, 7);
//        mesh.getFaces()[9] = new Mesh.Face(0, 4, 7);
//        mesh.getFaces()[10] = new Mesh.Face(4, 5, 6);
//        mesh.getFaces()[11] = new Mesh.Face(4, 6, 7);

        mesh.setRotation(new Vector3(mesh.getRotation().getX() + 0.05f, mesh.getRotation().getY() + 0.05f, mesh.getRotation().getZ()));

        camera.setPosition(new Vector3(0, 0, 10.0f));
        camera.setTarget(Vector3.ZERO);

        CubeDemo cubeDemo = new CubeDemo();
        cubeDemo.start();
    }

    public void start() {
        try {
            Display.setDisplayMode(new DisplayMode(WIDTH, HEIGHT));
            Display.setTitle("Cube Demo"); //title of our window
            Display.setResizable(false); //whether our window is resizable
            Display.setVSyncEnabled(false); //whether hardware VSync is enabled
            Display.setFullscreen(false); //whether fullscreen is enabled
            Display.create();
        } catch (LWJGLException e) {
            e.printStackTrace();
            System.exit(0);
        }

        GL11.glDisable(GL11.GL_DEPTH_TEST);

        // Enable blending
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        // Set clear to transparent black
        GL11.glClearColor(0f, 0f, 0f, 0f);

        // Call this before running to set up our initial size
        //resize();
        GL11.glViewport(0, 0, Display.getWidth(), Display.getHeight());

        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glOrtho(0, WIDTH, HEIGHT, 0, 10, -10);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);

        while (!Display.isCloseRequested()) {

           // GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);

//            if (Display.wasResized())
//                resize();

            final long currentTime = System.currentTimeMillis();
            final String fps = (1000 / (currentTime - lastFrameTime + 1)) + "";

            mainPaint();
            processKeyboard();

            Display.setTitle("Cube Demo" + " Frame: " + frameNumber++ + ", fps: " + fps);

            lastFrameTime = currentTime;

            Display.update();
        }

        Display.destroy();
    }

    private void mainPaint() {

        // rotating slightly the cube during each frame rendered
        if (Mouse.isButtonDown(0)) {
            final int mouseX = Mouse.getDX();
            final int mouseY = Mouse.getDY();
            mesh.setRotation(new Vector3(mesh.getRotation().getX() + 0.1f * mouseX, mesh.getRotation().getY() + 0.1f * mouseY, mesh.getRotation().getZ()));
        }

        device.clear(0, 0, 0, 255);
        device.render(camera, mesh);

        GL11.glPointSize(20);
        GL11.glBegin(GL11.GL_POINTS);
        final int[][] backBuffer = device.getBackBuffer();
        for (int i = 0; i < PIXELS; i++) {
            final int[] pixel = backBuffer[i];


            GL11.glColor4ub((byte) pixel[0], (byte) pixel[1], (byte) pixel[2], (byte) pixel[3]);
            final int x = i % WIDTH;
            final int y = i / WIDTH;
            GL11.glVertex2i(x, y);

        }
        GL11.glEnd();
    }

    private void processKeyboard() {
        if (Keyboard.isKeyDown(Keyboard.KEY_UP)) {
            camera.setPosition(new Vector3(0, 0, camera.getPosition().getZ() - 0.5f));
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)) {
            camera.setPosition(new Vector3(0, 0, camera.getPosition().getZ() + 0.5f));
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)) {

        }
        if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {

        }
    }

}
