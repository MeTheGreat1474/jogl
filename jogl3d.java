import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.FPSAnimator;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;


import javax.swing.*;
import java.awt.event.*;

public class jogl3d implements GLEventListener, KeyListener, MouseListener, MouseMotionListener {

    private float camX = 0.0f, camY = 0.0f, camZ = 5.0f; // camera position
    private float rotY = 0.0f; // rotation angle
    private float rotX = 0.0f; // rotation on X-axis
    private float lastMouseX, lastMouseY;
    private boolean dragging = false;


    public static void main(String[] args) {
        JFrame frame = new JFrame("Red Cube Scene - JOGL");
        GLProfile profile = GLProfile.get(GLProfile.GL2);
        GLCapabilities caps = new GLCapabilities(profile);
        GLCanvas canvas = new GLCanvas(caps);

        jogl3d renderer = new jogl3d();
        canvas.addGLEventListener(renderer);
        canvas.addKeyListener(renderer);
        canvas.addMouseListener(renderer);
        canvas.addMouseMotionListener(renderer);


        frame.getContentPane().add(canvas);
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        canvas.requestFocus();

        FPSAnimator animator = new FPSAnimator(canvas, 60);
        animator.start();
    }

    @Override
    public void init(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();
        gl.glEnable(GL.GL_DEPTH_TEST); // Enable depth test
        gl.glClearColor(0.5f, 0.8f, 1f, 1f); // Sky blue background
    }

    @Override
    public void display(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();

        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);

        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glLoadIdentity();

        gl.glTranslatef(-camX, -camY, -camZ);
        gl.glRotatef(rotY, 0, 1, 0);
        gl.glRotatef(rotX, 1, 0, 0);

        //implementation example
        gl.glPushMatrix();
        gl.glTranslatef(.5f, 0f, 0f);
        drawBarnObject(gl);
        gl.glTranslatef(-1.5f, 0f, 0f);
        drawWheatObject(gl);
        gl.glTranslatef(-.5f, 0f, 0f);
        drawFenceObject(gl);
        gl.glPopMatrix();

    }

    private void drawFenceObject(GL2 gl){
        float scale = .05f;
        gl.glPushMatrix();
        gl.glScalef(scale, scale, scale);
        drawFenceSegment(gl, 0.5f, 6.0f, 0.2f, 5.0f, 0.5f, 0.1f);
        gl.glPopMatrix();
    }

    private void drawWheatObject(GL2 gl){
        float scale = .1f; // Half the original size
        gl.glPushMatrix();
        gl.glScalef(scale, scale, scale);
        drawWheat(gl, 1f, 0.5f, 2f);
        gl.glPopMatrix(); // End of scale context
    }

    private void drawBarnObject(GL2 gl){
        // Draw barn
        gl.glPushMatrix();
        gl.glTranslatef(0.0f, 0.5f, 0.0f); // Shift origin to top center of box
        drawBarn(gl, 2.0f, 1.0f, 3f); // width, height, depth
        gl.glPushMatrix();
        gl.glTranslatef(0.0f, 0.2f, 0.0f); // Shift origin to top center of box
        drawRoof(gl, 2.25f, 1.0f, 3.25f);
        gl.glTranslatef(0.0f, 0.1f, 0.5f); // Shift origin to top center of box
        drawWindow2D(gl, 0.4f, 0.4f, 1.0f / 2);
        drawDoor2D(gl, 1f, 0.5f, 0.0f);
        gl.glPopMatrix();
        drawLeftWindows(gl, 1.0f, 3.0f, 0.25f, 0.5f);
        drawRightWindows(gl, 1.0f, 3.0f, 0.25f, 0.5f);
        drawBackWindows(gl, 1.0f, 3.0f, 0.5f, 0.5f);
        gl.glPopMatrix();
    }

    private void drawFenceSegment(GL2 gl, float postWidth, float postHeight, float postDepth, float sidebarWidth, float sidebarHeight, float sidebarDepth) {
        float tipHeight = 0.5f;

        gl.glColor3f(1.0f, 1.0f, 1.0f); // white

        // Leftmost post
        gl.glPushMatrix();
        gl.glTranslatef(-sidebarWidth/2.5f, postHeight / 2, 0f);
        drawFenceSidebar(gl, postWidth, postHeight, postDepth);
        gl.glTranslatef(0f, postHeight / 2 + tipHeight / 2, 0f);
        drawFencePyramid(gl, postWidth, postDepth, tipHeight);
        gl.glPopMatrix();

        // Left post
        gl.glPushMatrix();
        gl.glTranslatef(-sidebarWidth / 4.5f, postHeight / 2, 0f);
        drawFenceSidebar(gl, postWidth, postHeight, postDepth);
        gl.glTranslatef(0f, postHeight / 2 + tipHeight / 2, 0f);
        drawFencePyramid(gl, postWidth, postDepth, tipHeight);
        gl.glPopMatrix();

        // Mid post
        gl.glPushMatrix();
        gl.glTranslatef(0, postHeight / 2, 0f);
        drawFenceSidebar(gl, postWidth, postHeight, postDepth);
        gl.glTranslatef(0f, postHeight / 2 + tipHeight / 2, 0f);
        drawFencePyramid(gl, postWidth, postDepth, tipHeight);
        gl.glPopMatrix();

        // Right post
        gl.glPushMatrix();
        gl.glTranslatef(sidebarWidth / 5f, postHeight / 2, 0f);
        drawFenceSidebar(gl, postWidth, postHeight, postDepth);
        gl.glTranslatef(0f, postHeight / 2 + tipHeight / 2, 0f);
        drawFencePyramid(gl, postWidth, postDepth, tipHeight);
        gl.glPopMatrix();

        // Rightmost post
        gl.glPushMatrix();
        gl.glTranslatef(sidebarWidth/2.5f, postHeight / 2, 0f);
        drawFenceSidebar(gl, postWidth, postHeight, postDepth);
        gl.glTranslatef(0f, postHeight / 2 + tipHeight / 2, 0f);
        drawFencePyramid(gl, postWidth, postDepth, tipHeight);
        gl.glPopMatrix();


        // Lower horizontal bar
        gl.glPushMatrix();
        gl.glTranslatef(0f, postHeight / 6, -sidebarDepth);
        drawFenceSidebar(gl, sidebarWidth, sidebarHeight, sidebarDepth);
        gl.glPopMatrix();

        // Upper horizontal bar
        gl.glPushMatrix();
        gl.glTranslatef(0f, postHeight - sidebarHeight*2, -sidebarDepth);
        drawFenceSidebar(gl, sidebarWidth, sidebarHeight, sidebarDepth);
        gl.glPopMatrix();
    }

    private void drawFenceSidebar(GL2 gl, float width, float height, float depth) {
        float x = width / 2;
        float y = height / 2;
        float z = depth / 2;

        gl.glBegin(GL2.GL_QUADS);

        // Front face
        gl.glVertex3f(-x, -y, z);
        gl.glVertex3f(x, -y, z);
        gl.glVertex3f(x, y, z);
        gl.glVertex3f(-x, y, z);

        // Back face
        gl.glVertex3f(-x, -y, -z);
        gl.glVertex3f(-x, y, -z);
        gl.glVertex3f(x, y, -z);
        gl.glVertex3f(x, -y, -z);

        // Left face
        gl.glVertex3f(-x, -y, -z);
        gl.glVertex3f(-x, -y, z);
        gl.glVertex3f(-x, y, z);
        gl.glVertex3f(-x, y, -z);

        // Right face
        gl.glVertex3f(x, -y, -z);
        gl.glVertex3f(x, y, -z);
        gl.glVertex3f(x, y, z);
        gl.glVertex3f(x, -y, z);

        // Top face
        gl.glVertex3f(-x, y, -z);
        gl.glVertex3f(-x, y, z);
        gl.glVertex3f(x, y, z);
        gl.glVertex3f(x, y, -z);

        // Bottom face
        gl.glVertex3f(-x, -y, -z);
        gl.glVertex3f(x, -y, -z);
        gl.glVertex3f(x, -y, z);
        gl.glVertex3f(-x, -y, z);

        gl.glEnd();
    }

    private void drawFencePyramid(GL2 gl, float baseWidth, float baseDepth, float height) {
        float halfW = baseWidth / 2f;
        float halfD = baseDepth / 2f;

        gl.glBegin(GL2.GL_TRIANGLES);

        // Front face
        gl.glVertex3f(0, height / 2f, 0);               // Apex
        gl.glVertex3f(-halfW, -height / 2f, halfD);     // Bottom left
        gl.glVertex3f(halfW, -height / 2f, halfD);      // Bottom right

        // Right face
        gl.glVertex3f(0, height / 2f, 0);
        gl.glVertex3f(halfW, -height / 2f, halfD);
        gl.glVertex3f(halfW, -height / 2f, -halfD);

        // Back face
        gl.glVertex3f(0, height / 2f, 0);
        gl.glVertex3f(halfW, -height / 2f, -halfD);
        gl.glVertex3f(-halfW, -height / 2f, -halfD);

        // Left face
        gl.glVertex3f(0, height / 2f, 0);
        gl.glVertex3f(-halfW, -height / 2f, -halfD);
        gl.glVertex3f(-halfW, -height / 2f, halfD);

        gl.glEnd();
    }

    private void drawWheat(GL2 gl, float grainSize, float width, float height) {
        gl.glColor3f(1.0f, 0.8f, 0.0f); // yellow-orange
        float stemHeight = 1f;
        float stemWidth = 0.05f;

        float grainWidth = grainSize/5;
        float grainHeight = grainSize*1.5f;

        //draw 3D grain
        gl.glPushMatrix();
        gl.glTranslatef(0f, stemHeight, 0f);
//        drawPyramid(gl, grainSize * 2f, grainSize/4);
        drawWheatCube(gl, grainWidth, grainHeight, grainWidth);
        gl.glTranslatef(0f, grainHeight, 0f);
        drawWheatPyramid(gl, grainWidth, grainWidth);
        gl.glTranslatef(0f, -grainHeight, 0f);
        gl.glRotatef(-180, 0f, 0f, 1f); // opposite tilt
        drawWheatPyramid(gl, grainWidth, grainWidth);
        gl.glPopMatrix();

        // draw 3D stem
        gl.glPushMatrix();
        gl.glTranslatef(0f, 0f, 0f);  // base at origin
        drawWheatStem(gl, stemWidth, stemHeight, stemWidth);  // width, height, depth
        gl.glPopMatrix();

        //draw 2D leafs
        drawWheatLeafs(gl, width, height);
    }

    private void drawWheatLeafs(GL2 gl, float width, float height){
        float leafWidth = 0.3f;

        // draw leaf left
        gl.glBegin(GL2.GL_QUADS);
        gl.glVertex3f(0, 0, 0.0f);         // bottom leaf
        gl.glVertex3f(-width, height/2, 0.0f);         // left leaf
        gl.glVertex3f(-width, height, 0.0f);         // top leaf
        gl.glVertex3f(-width + leafWidth, height/2, 0.4f);         // right leaf
        gl.glEnd();

        // draw leaf right
        gl.glBegin(GL2.GL_QUADS);
        gl.glVertex3f(0, 0, 0.0f);         // bottom leaf
        gl.glVertex3f(width - leafWidth, height/2, -0.4f);         // left leaf
        gl.glVertex3f(width + leafWidth, height, 0.0f);         // top leaf
        gl.glVertex3f(width, height/2, 0.0f);         // right leaf
        gl.glEnd();
    }

    private void drawWheatPyramid(GL2 gl, float size, float sideLength) {
        float h = size;       // height
        float hs = sideLength;  // half side length

        gl.glBegin(GL2.GL_TRIANGLES);
        // Front face
        gl.glVertex3f(0, h, 0);
        gl.glVertex3f(-hs, 0, hs);
        gl.glVertex3f(hs, 0, hs);

        // Right face
        gl.glVertex3f(0, h, 0);
        gl.glVertex3f(hs, 0, hs);
        gl.glVertex3f(hs, 0, -hs);

        // Back face
        gl.glVertex3f(0, h, 0);
        gl.glVertex3f(hs, 0, -hs);
        gl.glVertex3f(-hs, 0, -hs);

        // Left face
        gl.glVertex3f(0, h, 0);
        gl.glVertex3f(-hs, 0, -hs);
        gl.glVertex3f(-hs, 0, hs);
        gl.glEnd();
    }

    private void drawWheatCube(GL2 gl, float width, float height, float depth) {
        float hw = width;
        float hh = height;
        float hd = depth;

        gl.glBegin(GL2.GL_QUADS);

        // Front face
        gl.glVertex3f(-hw, 0, hd);
        gl.glVertex3f(hw, 0, hd);
        gl.glVertex3f(hw, hh, hd);
        gl.glVertex3f(-hw, hh, hd);

        // Back face
        gl.glVertex3f(-hw, 0, -hd);
        gl.glVertex3f(-hw, hh, -hd);
        gl.glVertex3f(hw, hh, -hd);
        gl.glVertex3f(hw, 0, -hd);

        // Left face
        gl.glVertex3f(-hw, 0, -hd);
        gl.glVertex3f(-hw, 0, hd);
        gl.glVertex3f(-hw, hh, hd);
        gl.glVertex3f(-hw, hh, -hd);

        // Right face
        gl.glVertex3f(hw, 0, -hd);
        gl.glVertex3f(hw, hh, -hd);
        gl.glVertex3f(hw, hh, hd);
        gl.glVertex3f(hw, 0, hd);

        // Top face
        gl.glVertex3f(-hw, hh, -hd);
        gl.glVertex3f(-hw, hh, hd);
        gl.glVertex3f(hw, hh, hd);
        gl.glVertex3f(hw, hh, -hd);

        // Bottom face
        gl.glVertex3f(-hw, 0, -hd);
        gl.glVertex3f(hw, 0, -hd);
        gl.glVertex3f(hw, 0, hd);
        gl.glVertex3f(-hw, 0, hd);

        gl.glEnd();
    }

    private void drawWheatStem(GL2 gl, float width, float height, float depth) {
        float hw = width / 2;
        float hh = height;
        float hd = depth / 2;

        gl.glColor3f(0.0f, 0.6f, 0.0f); // green

        gl.glBegin(GL2.GL_QUADS);

        // Front face
        gl.glVertex3f(-hw, 0, hd);
        gl.glVertex3f(hw, 0, hd);
        gl.glVertex3f(hw, hh, hd);
        gl.glVertex3f(-hw, hh, hd);

        // Back face
        gl.glVertex3f(-hw, 0, -hd);
        gl.glVertex3f(-hw, hh, -hd);
        gl.glVertex3f(hw, hh, -hd);
        gl.glVertex3f(hw, 0, -hd);

        // Left face
        gl.glVertex3f(-hw, 0, -hd);
        gl.glVertex3f(-hw, 0, hd);
        gl.glVertex3f(-hw, hh, hd);
        gl.glVertex3f(-hw, hh, -hd);

        // Right face
        gl.glVertex3f(hw, 0, -hd);
        gl.glVertex3f(hw, hh, -hd);
        gl.glVertex3f(hw, hh, hd);
        gl.glVertex3f(hw, 0, hd);

        // Top face
        gl.glVertex3f(-hw, hh, -hd);
        gl.glVertex3f(-hw, hh, hd);
        gl.glVertex3f(hw, hh, hd);
        gl.glVertex3f(hw, hh, -hd);

        // Bottom face
        gl.glVertex3f(-hw, 0, -hd);
        gl.glVertex3f(hw, 0, -hd);
        gl.glVertex3f(hw, 0, hd);
        gl.glVertex3f(-hw, 0, hd);

        gl.glEnd();
    }

    private void drawBarn(GL2 gl, float width, float height, float depth) {
        float hx = width / 2;
        float hy = height / 2;
        float hz = depth / 2;
        float roofHeight = 0.25f; // extra height for the peak

        float peakY = hy + roofHeight;

        gl.glColor3f(1.0f, 0.0f, 0.0f); //red

        // Front face
        gl.glBegin(GL2.GL_POLYGON);
        gl.glVertex3f(-hx, hy, hz);         // bottom left roof
        gl.glVertex3f(-hx, -hy, hz);        // bottom left
        gl.glVertex3f(hx, -hy, hz);         // bottom right
        gl.glVertex3f(hx, hy, hz);          // bottom right roof
        gl.glVertex3f(0, peakY, hz);        // roof peak
        gl.glEnd();

        // Back face
        gl.glBegin(GL2.GL_POLYGON);
        gl.glVertex3f(-hx, hy, -hz);        // bottom left roof
        gl.glVertex3f(-hx, -hy, -hz);       // bottom left
        gl.glVertex3f(hx, -hy, -hz);        // bottom right
        gl.glVertex3f(hx, hy, -hz);         // bottom right roof
        gl.glVertex3f(0, peakY, -hz);       // roof peak
        gl.glEnd();

        // Left face
        gl.glBegin(GL2.GL_QUADS);
        gl.glVertex3f(-hx, hy, hz);         // front top left
        gl.glVertex3f(-hx, -hy, hz);        // front bottom left
        gl.glVertex3f(-hx, -hy, -hz);       // back bottom left
        gl.glVertex3f(-hx, hy, -hz);        // back top left
        gl.glEnd();

        // Right face
        gl.glBegin(GL2.GL_QUADS);
        gl.glVertex3f(hx, hy, hz);          // front top right
        gl.glVertex3f(hx, -hy, hz);         // front bottom right
        gl.glVertex3f(hx, -hy, -hz);        // back bottom right
        gl.glVertex3f(hx, hy, -hz);         // back top right
        gl.glEnd();

        // Roof left slope
        gl.glBegin(GL2.GL_QUADS);
        gl.glVertex3f(-hx, hy, hz);         // front left roof
        gl.glVertex3f(-hx, hy, -hz);        // back left roof
        gl.glVertex3f(0, peakY, -hz);       // back peak
        gl.glVertex3f(0, peakY, hz);        // front peak
        gl.glEnd();

        // Roof right slope
        gl.glBegin(GL2.GL_QUADS);
        gl.glVertex3f(hx, hy, hz);          // front right roof
        gl.glVertex3f(hx, hy, -hz);         // back right roof
        gl.glVertex3f(0, peakY, -hz);       // back peak
        gl.glVertex3f(0, peakY, hz);        // front peak
        gl.glEnd();

        // Bottom face
        gl.glBegin(GL2.GL_QUADS);
        gl.glVertex3f(-hx, -hy, hz);        // front bottom left
        gl.glVertex3f(hx, -hy, hz);         // front bottom right
        gl.glVertex3f(hx, -hy, -hz);        // back bottom right
        gl.glVertex3f(-hx, -hy, -hz);       // back bottom left
        gl.glEnd();
    }

    private void drawRoof(GL2 gl, float width, float height, float depth) {
        float hx = width / 2;
        float hy = height / 2;
        float hz = depth / 2;
        float rh = 0.15f; //roof height

        gl.glColor3f(0.76f, 0.60f, 0.42f);

        //front left
        gl.glBegin(GL2.GL_QUADS);
        gl.glVertex3f(-hx, hy/2, hz); // bot left
        gl.glVertex3f(-hx, (hy/2)+rh, hz); // top left
        gl.glVertex3f(0, (hy)+rh, hz); // top right
        gl.glVertex3f(0, (hy), hz); // bot right
        gl.glEnd();

        //front right
        gl.glBegin(GL2.GL_QUADS);
        gl.glVertex3f(hx, (hy/2), hz); // bot right
        gl.glVertex3f(hx, (hy/2)+rh, hz); // top right
        gl.glVertex3f(0, (hy)+rh, hz); // top left
        gl.glVertex3f(0, hy, hz); // bot left
        gl.glEnd();

        // back left
        gl.glBegin(GL2.GL_QUADS);
        gl.glVertex3f(-hx, hy/2, -hz); // bot left
        gl.glVertex3f(-hx, (hy/2)+rh, -hz); // top left
        gl.glVertex3f(0, (hy)+rh, -hz); // top right
        gl.glVertex3f(0, hy, -hz); // bot right
        gl.glEnd();

        // back right
        gl.glBegin(GL2.GL_QUADS);
        gl.glVertex3f(hx, (hy/2), -hz); // bot right
        gl.glVertex3f(hx, (hy/2)+rh, -hz); // top right
        gl.glVertex3f(0, (hy)+rh, -hz); // top left
        gl.glVertex3f(0, hy, -hz); // bot left
        gl.glEnd();

        // left roof (connect front and back)
        gl.glBegin(GL2.GL_QUADS);
        gl.glVertex3f(-hx, hy/2, hz);       // front bottom
        gl.glVertex3f(-hx, (hy/2)+rh, hz);  // front top
        gl.glVertex3f(-hx, (hy/2)+rh, -hz); // back top
        gl.glVertex3f(-hx, hy/2, -hz);      // back bottom
        gl.glEnd();

        // right roof (connect front and back)
        gl.glBegin(GL2.GL_QUADS);
        gl.glVertex3f(hx, hy/2, hz);       // front bottom
        gl.glVertex3f(hx, (hy/2)+rh, hz);  // front top
        gl.glVertex3f(hx, (hy/2)+rh, -hz); // back top
        gl.glVertex3f(hx, hy/2, -hz);      // back bottom
        gl.glEnd();

        // top ridge (connect the peaks front to back)
        gl.glBegin(GL2.GL_QUADS);
        gl.glVertex3f(0, hy + rh, hz);  // front peak
        gl.glVertex3f(0, hy + rh, -hz); // back peak
        gl.glVertex3f(0, hy, -hz);      // back base of ridge
        gl.glVertex3f(0, hy, hz);       // front base of ridge
        gl.glEnd();

        //left slope top
        gl.glBegin(GL2.GL_QUADS);
        gl.glVertex3f(-hx, (hy/2)+rh, hz);   // front top edge
        gl.glVertex3f(0, hy+rh, hz);         // front ridge peak
        gl.glVertex3f(0, hy+rh, -hz);        // back ridge peak
        gl.glVertex3f(-hx, (hy/2)+rh, -hz);  // back top edge
        gl.glEnd();

        //right slope top
        gl.glBegin(GL2.GL_QUADS);
        gl.glVertex3f(hx, (hy/2)+rh, hz);    // front top edge
        gl.glVertex3f(0, hy+rh, hz);         // front ridge peak
        gl.glVertex3f(0, hy+rh, -hz);        // back ridge peak
        gl.glVertex3f(hx, (hy/2)+rh, -hz);   // back top edge
        gl.glEnd();
    }

    private void drawWindow2D(GL2 gl, float width, float height, float hy) {
        float windowWidth = width;
        float windowHeight = height;
        float zOffset = 1.01f;  // Slightly in front of the barn wall

        float xLeft = -windowWidth / 2;
        float xRight = windowWidth / 2;

        // Place the window near the top of the barn (adjust y offset if needed)
        float yTop = hy - 0.2f;
        float yBottom = yTop - windowHeight;

        gl.glColor3f(1.0f, 1.0f, 1.0f);
        gl.glBegin(GL2.GL_QUADS);
        gl.glVertex3f(xLeft, yTop, zOffset);     // Top Left
        gl.glVertex3f(xRight, yTop, zOffset);    // Top Right
        gl.glVertex3f(xRight, yBottom, zOffset); // Bottom Right
        gl.glVertex3f(xLeft, yBottom, zOffset);  // Bottom Left
        gl.glEnd();

        // Draw window inside (darker red) on window
        xLeft += 0.05f;
        xRight -= 0.05f;
        yTop -= 0.05f;
        yBottom += 0.05f;
        zOffset = 1.02f;
        gl.glColor3f(0.5f, 0.0f, 0.0f);
        gl.glBegin(GL2.GL_QUADS);
        // inside window
        gl.glVertex3f(xLeft, yTop, zOffset);     // Top Left
        gl.glVertex3f(xRight, yTop, zOffset);    // Top Right
        gl.glVertex3f(xRight, yBottom, zOffset); // Bottom Right
        gl.glVertex3f(xLeft, yBottom, zOffset);  // Bottom Left
        gl.glEnd();
    }

    private void drawDoor2D(GL2 gl, float width, float height, float hy) {
        float doorWidth = width;
        float doorHeight = height;
        float zOffset = 1.01f;  // Slightly in front of the barn wall

        float xLeft = -doorWidth / 2;
        float xRight = doorWidth / 2;

        // Place the door near the bottom of the barn (adjust y offset if needed)
        float yTop = hy - 0.25f;
        float yBottom = yTop - doorHeight;

        gl.glColor3f(1.0f, 1.0f, 1.0f);
        gl.glBegin(GL2.GL_QUADS);
        gl.glVertex3f(xLeft, yTop, zOffset);     // Top Left
        gl.glVertex3f(xRight, yTop, zOffset);    // Top Right
        gl.glVertex3f(xRight, yBottom, zOffset); // Bottom Right
        gl.glVertex3f(xLeft, yBottom, zOffset);  // Bottom Left
        gl.glEnd();

        // Draw door inside (darker red) on door
        xLeft += 0.025f;
        xRight -= 0.025f;
        yTop -= 0.025f;
        yBottom += 0.025f;
        zOffset = 1.02f;
        gl.glColor3f(0.8f, 0.0f, 0.0f);
        gl.glBegin(GL2.GL_QUADS);
        // inside door
        gl.glVertex3f(xLeft, yTop, zOffset);     // Top Left
        gl.glVertex3f(xRight, yTop, zOffset);    // Top Right
        gl.glVertex3f(xRight, yBottom, zOffset); // Bottom Right
        gl.glVertex3f(xLeft, yBottom, zOffset);  // Bottom Left
        gl.glEnd();

        drawDoorX2D(gl, width, height, yBottom+ (yTop-yBottom)/2);
    }

    private void drawDoorX2D(GL2 gl, float doorWidth, float doorHeight, float centerY) {
        float zOffset = 1.03f; // Slightly in front of door
        float thickness = 0.1f; // Thickness of the X beams

        float halfW = doorWidth / 2;
        float halfH = doorHeight / 2;

        // First diagonal: bottom-left to top-right
        gl.glColor3f(1.0f, 1.0f, 1.0f);
        gl.glBegin(GL2.GL_POLYGON);
        gl.glVertex3f(-halfW, centerY - halfH, zOffset);
        gl.glVertex3f(-halfW + thickness, centerY - halfH, zOffset);
        gl.glVertex3f(halfW, centerY + halfH, zOffset);
        gl.glVertex3f(halfW - thickness, centerY + halfH, zOffset);
        gl.glEnd();

        // Second diagonal: top-left to bottom-right
        gl.glBegin(GL2.GL_QUADS);
        gl.glVertex3f(-halfW, centerY + halfH, zOffset);
        gl.glVertex3f(-halfW + thickness, centerY + halfH, zOffset);
        gl.glVertex3f(halfW, centerY - halfH, zOffset);
        gl.glVertex3f(halfW - thickness, centerY - halfH, zOffset);
        gl.glEnd();
    }

    private void drawLeftWindows(GL2 gl, float barnHeight, float barnDepth, float windowWidth, float windowHeight) {
        float zOffsetStart = barnDepth / 2.75f;  // front to back placement
        float zStep = barnDepth / 4;               // spacing between windows
        float yStart = (barnHeight / 2);       // from near top
        float xLeftWall = 0.01f;                   // slightly outside the left wall (x = -hx - epsilon)

        for (int i = 0; i < 4; i++) {
            float z = zOffsetStart - i * zStep;

            // Push matrix so we can transform without affecting global space
            gl.glPushMatrix();
            gl.glTranslatef(xLeftWall, 0f, z);      // move to left wall
            gl.glRotatef(90f, 0f, 1f, 0f);          // rotate to face left wall
            drawWindow2D(gl, windowWidth, windowHeight, yStart);
            gl.glPopMatrix();
        }
    }

    private void drawRightWindows(GL2 gl, float barnHeight, float barnDepth, float windowWidth, float windowHeight) {
        float zOffsetStart = barnDepth / 2.75f;       // front to back placement
        float zStep = barnDepth / 4;                  // spacing between windows
        float yStart = barnHeight / 2;                // near top
        float xRightWall = -0.01f;     // slightly outside the right wall (x = +hx + epsilon)

        for (int i = 0; i < 4; i++) {
            float z = zOffsetStart - i * zStep;

            gl.glPushMatrix();
            gl.glTranslatef(xRightWall, 0f, z);       // move to right wall
            gl.glRotatef(-90f, 0f, 1f, 0f);           // rotate to face right wall
            drawWindow2D(gl, windowWidth, windowHeight, yStart);
            gl.glPopMatrix();
        }
    }

    private void drawBackWindows(GL2 gl, float barnHeight, float barnDepth, float windowWidth, float windowHeight) {
        float zBackWall = -barnDepth/6;  // Slightly behind the back face

        // Top window: near the top
        gl.glPushMatrix();
        gl.glTranslatef(0f, barnHeight/2, zBackWall);          // Move to back wall
        gl.glRotatef(180f, 0f, 1f, 0f);              // Rotate to face backwards
        drawWindow2D(gl, windowWidth, windowHeight, barnHeight / 2 - 0.2f);
        gl.glPopMatrix();

        // Bottom window: near the bottom
        gl.glPushMatrix();
        gl.glTranslatef(0f, barnHeight/2 + 0.1f, zBackWall);
        gl.glRotatef(180f, 0f, 1f, 0f);
        drawWindow2D(gl, windowWidth, windowHeight, -barnHeight / 2 + 0.2f);
        gl.glPopMatrix();
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        GL2 gl = drawable.getGL().getGL2();

        float aspect = (float) width / height;
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();
        new GLU().gluPerspective(45.0, aspect, 1.0, 100.0);
    }

    @Override
    public void dispose(GLAutoDrawable drawable) {
    }

    // Basic WASD & Arrow Key Navigation
    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_W -> camZ -= 0.1f;
            case KeyEvent.VK_S -> camZ += 0.1f;
            case KeyEvent.VK_A -> camX -= 0.1f;
            case KeyEvent.VK_D -> camX += 0.1f;
            case KeyEvent.VK_LEFT -> rotY -= 2.0f;
            case KeyEvent.VK_RIGHT -> rotY += 2.0f;
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        dragging = true;
        lastMouseX = e.getX();
        lastMouseY = e.getY();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        dragging = false;
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (dragging) {
            float dx = e.getX() - lastMouseX;
            float dy = e.getY() - lastMouseY;

            rotY += dx * 0.5f;
            rotX += dy * 0.5f;

            lastMouseX = e.getX();
            lastMouseY = e.getY();
        }
    }

    @Override public void mouseMoved(MouseEvent e) {}
    @Override public void mouseClicked(MouseEvent e) {}
    @Override public void mouseEntered(MouseEvent e) {}
    @Override public void mouseExited(MouseEvent e) {}
    @Override public void keyReleased(KeyEvent e) {}
    @Override public void keyTyped(KeyEvent e) {}

}
