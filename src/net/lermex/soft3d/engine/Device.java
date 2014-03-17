package net.lermex.soft3d.engine;

import net.lermex.soft3d.math.Matrix;
import net.lermex.soft3d.math.Vector2;
import net.lermex.soft3d.math.Vector3;

import java.awt.*;

public class Device {
    private final int screenWidth;
    private final int screenHeight;
    private int[][] backBuffer;
    private float[] depthBuffer;

    private static Color[] colors = new Color[] {Color.RED, Color.GREEN};
    private static Color color = Color.YELLOW;
    private static int colorCounter = 0;

    public Device(final int width, final int height) {
        this.screenWidth = width;
        this.screenHeight = height;
        backBuffer = new int[width * height][4];
        depthBuffer = new float[width * height];
    }

    public int[][] getBackBuffer() {
        return backBuffer;
    }

    public void clear(int r, int g, int b, int a) {
        for (int[] pixel : backBuffer) {
            pixel[0] = r;
            pixel[1] = g;
            pixel[2] = b;
            pixel[3] = a;
        }

        // Clearing Depth Buffer
        for (int i = 0; i < depthBuffer.length; i++)
        {
            depthBuffer[i] = Float.MAX_VALUE;
        }
    }

    public void render(final Camera camera, final Mesh... meshes) {
        // To understand this part, please read the prerequisites resources
        Matrix viewMatrix = Matrix.LookAtLH(camera.getPosition(), camera.getTarget(), Vector3.UNIT_Y);
        Matrix projectionMatrix = Matrix.PerspectiveFovRH(80f, (float)screenWidth / screenHeight, 0.01f, 1.0f);

        for (Mesh mesh : meshes)
        {
            //Beware to apply rotation before translation
            final Matrix worldMatrix = Matrix.RotationYawPitchRoll(mesh.getRotation().getY(), mesh.getRotation().getX(), mesh.getRotation().getZ())
                    .times( Matrix.getTranslationMatrix(mesh.getPosition()));



            final Matrix transformMatrix = worldMatrix.times(viewMatrix).times(projectionMatrix);
            //System.out.println(transformMatrix);

            for (Vector3 vertex : mesh.getVertices())
            {
                // First, we project the 3D coordinates into the 2D space
                Vector2 point = project(vertex, transformMatrix);
                // Then we can draw on screen
                drawPoint(new Pixel(point));
            }

            for (Mesh.Face face : mesh.getFaces()) {
                Vector3 vertexA = mesh.getVertices()[face.getA()];
                Vector3 vertexB = mesh.getVertices()[face.getB()];
                Vector3 vertexC = mesh.getVertices()[face.getC()];

                Vector2 pixelA = project(vertexA, transformMatrix);
                Vector2 pixelB = project(vertexB, transformMatrix);
                Vector2 pixelC = project(vertexC, transformMatrix);

                drawLine(pixelA, pixelB);
                drawLine(pixelB, pixelC);
                drawLine(pixelC, pixelA);

                color = colors[++colorCounter % 2];
                drawPoint(new Pixel(pixelA));
                drawPoint(new Pixel(pixelB));
                drawPoint(new Pixel(pixelC));
                color = color.YELLOW;
            }

//            for (int i = 0; i < mesh.getVertices().length - 1; i++)
//            {
//                Vector2 point0 = project(mesh.getVertices()[i], transformMatrix);
//                Vector2 point1 = project(mesh.getVertices()[i + 1], transformMatrix);
//                drawLine(point0, point1);
//            }
        }
    }

    private static class Pixel extends Vector2 { // TODO: this is a hack to deal with float precision problems.
                                                 // TODO: Have to get rid of this ASAP

        public Pixel(int x, int y) {
            super(x, y);
        }

        public Pixel(final Vector2 vector) {
            super((int)vector.getX(),(int)vector.getY());
        }
    }

    public void drawLine(Vector2 point0, Vector2 point1)
    {
        int x0 = (int)point0.getX();
        int y0 = (int)point0.getY();
        int x1 = (int)point1.getX();
        int y1 = (int)point1.getY();

        int dx = Math.abs(x1 - x0);
        int dy = Math.abs(y1 - y0);
        int sx = (x0 < x1) ? 1 : -1;
        int sy = (y0 < y1) ? 1 : -1;
        int err = dx - dy;

        while (true) {
            drawPoint(new Vector2(x0, y0));

            if ((x0 == x1) && (y0 == y1)) break;
            int e2 = 2 * err;
            if (e2 > -dy) { err -= dy; x0 += sx; }
            if (e2 < dx) { err += dx; y0 += sy; }
        }
    }

    public Vector2 project(Vector3 coord, Matrix transMat)
    {
        // transforming the coordinates
        final Vector3 point = Vector3.transformCoordinate(coord, transMat);
        // The transformed coordinates will be based on coordinate system
        // starting on the center of the screen. But drawing on screen normally starts
        // from top left. We then need to transform them again to have x:0, y:0 on top left.
        final float x =   (point.getX() * screenWidth + screenWidth / 2.0f);
        final float y =  (-point.getY() * screenHeight + screenHeight / 2.0f);


        return new Vector2(x, y);
    }

    private void drawPoint (final Vector2 point) {
        //System.out.println("Drawing point: " + point.getX() + " " + point.getY());
        // Clipping what's visible on screen
        if (point.getX() >= 0 && point.getY() >= 0 && point.getX() < screenWidth && point.getY() < screenHeight)
        {
            // Drawing a yellow point
            float x = point.getX();
            float y = point.getY();
            final int index = (int) (x + y * screenWidth); // TODO: can we do without a cast here?

            backBuffer[index][0] = (color.getRed());
            backBuffer[index][1] = (color.getGreen());
            backBuffer[index][2] = (color.getBlue());
            backBuffer[index][3] = (255);
        }
    }

}
