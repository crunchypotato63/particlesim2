import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

public class ParticleSimulator extends JPanel{
    /*Initialization of immutable variables*/
    //Screen size: 1280 x 720
    public static final int SCREEN_WIDTH = 1280;
    public static final int SCREEN_HEIGHT = 720;

    //Particle size
    private double PARTICLE_RADIUS = 9.5;

    //Particle and Wall colors
    private static final Color PARTICLE_COLOR = Color.RED;
    // private static final Color WALL_COLOR = Color.BLACK;

    //Updates FPS every 0.5 seconds
    private static final double FPS_UPDATE_INTERVAL = 0.5;

    private static final int SPEED = 1;

    //Initialization of Particles and Walls
    private List<Particle> particles = new ArrayList<>();
    // private List<Wall> walls = new ArrayList<>();

    // Initialization of sprite
    private Sprite sprite;
    private keyHandler keyH = new keyHandler();

    private boolean explorerMode = false;

    //Variables needed to calculate FPS
    private int fps;
    private int frameCount;
    private long lastTime;
    private JLabel fpsLabel;

    private List<CalculateThread> t;

    private int threadToAdd;

    JButton addParticleButton;
    JToggleButton switchToExplorer;
    
    public ParticleSimulator(List<CalculateThread> t) {
        threadToAdd = 0;
        this.t = t;
        
        //Sets canvas size
        setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));

        lastTime = System.nanoTime();
        frameCount = 0;

        //Update loop
        new Timer(1000 / 120, e -> {
            long now = System.nanoTime();
            double deltaTime = (now - lastTime) / 1000000000.0;
            lastTime = now;

            t.forEach((thread) -> thread.update(deltaTime));
            particles = new ArrayList<Particle>();
            for(int i = 0; i < 4; i++){
                List<Particle> part = t.get(i).getParticles();
                particles.addAll(part);
            }

            if (keyH.upPress){
                updateSpritePosition(0, -SPEED); // Move sprite up
                System.out.println("Sprite X: " + sprite.getX() + " Sprite Y: " + sprite.getY());
            }
            if (keyH.dwnPress){
                updateSpritePosition(0, SPEED); // Move sprite down
                
                System.out.println("Sprite X: " + sprite.getX() + " Sprite Y: " + sprite.getY());
            }   
            if (keyH.lftPress){
                updateSpritePosition(-SPEED, 0); // Move sprite left
                
                System.out.println("Sprite X: " + sprite.getX() + " Sprite Y: " + sprite.getY());
            }
            if (keyH.rghtPress){
                updateSpritePosition(SPEED, 0); // Move sprite right
                
                System.out.println("Sprite X: " + sprite.getX() + " Sprite Y: " + sprite.getY());
            }

            repaint();
            frameCount++;
        }).start();

        //FPS counter
        new Timer((int) (FPS_UPDATE_INTERVAL * 1000), e -> {
            updateFPSText();
        }).start();
        
        //FPS label
        fpsLabel = new JLabel("FPS: ");
        fpsLabel.setForeground(Color.BLACK);
        fpsLabel.setHorizontalAlignment(JLabel.LEFT);
        fpsLabel.setFont(new Font("Arial", Font.PLAIN, 30));
        this.add(fpsLabel, BorderLayout.NORTH);

        //Add Particle button
        addParticleButton = new JButton("Add Particle");
        addParticleButton.addActionListener(e -> addParticles());
        this.add(addParticleButton, BorderLayout.SOUTH);

       /* switchToExplorer = new JToggleButton("Explorer Mode");
        switchToExplorer.addActionListener(e -> expModeToggle());
        this.add(switchToExplorer, BorderLayout.SOUTH);*/

        //Add Wall button
        //wall begone for this version
        // JButton addWallButton = new JButton("Add Wall");
        // addWallButton.addActionListener(e -> addWall());
        // this.add(addWallButton, BorderLayout.SOUTH);

        //Add Explorer mode button
        JButton explorerModeButton = new JButton("Explorer Mode");
        explorerModeButton.addActionListener(e -> expModeToggle());
        this.add(explorerModeButton, BorderLayout.SOUTH);
    }

    private void expModeToggle(){
        if(addParticleButton.isEnabled()){
            addParticleButton.setEnabled(false);
            PARTICLE_RADIUS *= 2;
            //TODO
            //choose where to spawn sprite
            JPanel panel = new JPanel(new GridLayout(5, 2));
            JTextField xCoord = new JTextField();
            JTextField yCoord = new JTextField();

            panel.add(new JLabel("X coordinate: "));
            panel.add(xCoord);
            panel.add(new JLabel("Y coordinate: "));
            panel.add(yCoord);

            int result = JOptionPane.showConfirmDialog(this, panel, "Enter Sprite Parameters", JOptionPane.OK_CANCEL_OPTION);

            if (result == JOptionPane.OK_OPTION) {
                try {
                    //get x and y coordinates
                    double xPos = Double.parseDouble(xCoord.getText());
                    double yPos = Double.parseDouble(yCoord.getText());
                    //spawn sprite here

                    sprite = new Sprite(xPos, yPos);
                    explorerMode = true;
                    addKeyboardListener();
                    
                    //set camera fov
                    //sprite moving things here
                    }
                catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(this, "Invalid input! Only Numeric Values!");
                    addParticleButton.setEnabled(true);
                }
            }
        }
        else{
            //TODO
            //sprite begone
            //visibility back to fullscreen
            PARTICLE_RADIUS /= 2;
            explorerMode = false;
            sprite = null;
            repaint();
            addParticleButton.setEnabled(true);
            this.removeKeyListener(keyH);
        }

    }

    private void updateSpritePosition(int dx, int dy) {
        if (sprite != null) {
            // Update the sprite's position based on the change in coordinates
            double newX = sprite.getX() + dx;
            double newY = sprite.getY() + dy;
            sprite.updatePosition(newX, newY);
            
            // Repaint the panel to show the updated sprite position
            repaint();
        }
    }

    private void addKeyboardListener() {
        this.setFocusable(true);
        this.requestFocus();
        this.addKeyListener(keyH);
    }

    // @Override
    // public void keyPressed(KeyEvent e) {
    //     int keyCode = e.getKeyCode();

    //     if (keyCode == KeyEvent.VK_UP){
    //         updateSpritePosition(0, -SPEED); // Move sprite up
    //     }
        
    //     if (keyCode == KeyEvent.VK_DOWN){
    //         updateSpritePosition(0, SPEED); // Move sprite down
    //     }
    //     if (keyCode == KeyEvent.VK_LEFT){
    //         updateSpritePosition(-SPEED, 0); // Move sprite left
    //     }
    //     if (keyCode == KeyEvent.VK_RIGHT){
    //         updateSpritePosition(SPEED, 0); // Move sprite right
    //     }
    // }

    // @Override
    // public void keyReleased(KeyEvent e) {
    //     // Not used in this implementation
    // }

    // @Override
    // public void keyTyped(KeyEvent e) {
    //     // Not used in this implementation
    // }


    /* 
     * ADD PARTICLES
     *
     * This function is responsible for adding the particles onto the program. The program will first ask the user to
     * enter the number of particles they would like to spawn. After that, the user has to pick between three forms of
     * adding a particle: Between Points, Between Angles, Between Velocities.
     * 
     * Between Points - Call addParticlesBetweenPoints()
     * Between Angles - Call addParticlesBetweenAngles()
     * Between Velocities - Call addParticlesBetweenVelocities()
     * 
     */
    private void addParticles() {
        //Message Box that asks the user how many particles they want to add
        String inputParticles = JOptionPane.showInputDialog("Enter the number of particles you would like to spawn:");
        int n = Integer.parseInt(inputParticles);
    
        //User can pick between three forms of adding a particle
        Object[] forms = {"Between Points", "Between Angles", "Between Velocities"};
        int choice = JOptionPane.showOptionDialog(this, "Pick how you want to add the particles:", "Particle Form Selection",
                JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, forms, forms[0]);
    
        switch (choice) {
            //Between points
            case 0:
                addParticlesBetweenPoints(n);
                break;
            //Between angles
            case 1:
                addParticlesBetweenAngles(n);
                break;
            //Between velocities
            case 2:
                addParticlesBetweenVelocities(n);
                break;
        }
    }

    /*
     * ADD PARTICLES BETWEEN POINTS
     * 
     * This function does the first form of the three when adding particles, adding them between points.
     */
    private void addParticlesBetweenPoints(int n) {
        //Separate window to input necessary values
        JPanel panel = new JPanel(new GridLayout(5, 2));
    
        //Input values for start and end points and velocity
        JTextField startXText = new JTextField();
        JTextField startYText = new JTextField();
        JTextField endXText = new JTextField();
        JTextField endYText = new JTextField();
        JTextField velocityText = new JTextField();
    
        panel.add(new JLabel("Start X:"));
        panel.add(startXText);
        panel.add(new JLabel("Start Y:"));
        panel.add(startYText);
        panel.add(new JLabel("End X:"));
        panel.add(endXText);
        panel.add(new JLabel("End Y:"));
        panel.add(endYText);
        panel.add(new JLabel("Velocity:"));
        panel.add(velocityText);
    
        int result = JOptionPane.showConfirmDialog(this, panel, "Enter Particle Parameters", JOptionPane.OK_CANCEL_OPTION);
    
        if (result == JOptionPane.OK_OPTION) {
            try {
                double startX = Double.parseDouble(startXText.getText());
                double startY = Double.parseDouble(startYText.getText());
                double endX = Double.parseDouble(endXText.getText());
                double endY = Double.parseDouble(endYText.getText());
                double velocity = Double.parseDouble(velocityText.getText());
        
                //Each particle will spawned away from each other by an increment value
                double incX = (endX - startX) / n;
                double incY = (endY - startY) / n;
    
                //Adding of particles with uniform distance between start and end points
                for (int i = 0; i < n; i++) {
                    Particle p = new Particle(startX + incX * i, startY + incY * i, 0, velocity);
                    particles.add(p);
                    t.get(threadToAdd).addParticle(p);
                    threadToAdd = (threadToAdd + 1) % 16;
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Invalid input! Only Numeric Values!");
            }
        }
    }

    /*
     * ADD PARTICLES BETWEEN ANGLES
     * 
     * This function does the second form of the three when adding particles, adding them between angles.
     */
    private void addParticlesBetweenAngles(int n) {
        //Separate window to input necessary values
        JPanel panel = new JPanel(new GridLayout(4, 2));
    
        //Input values for start and end angles and velocity
        JTextField startAngleField = new JTextField(5);
        JTextField endAngleField = new JTextField(5);
        JTextField velocityField = new JTextField(5);
    
        panel.add(new JLabel("Start Angle:"));
        panel.add(startAngleField);
        panel.add(new JLabel("End Angle:"));
        panel.add(endAngleField);
        panel.add(new JLabel("Velocity:"));
        panel.add(velocityField);
    
        int result = JOptionPane.showConfirmDialog(this, panel, "Enter Particle Parameters", JOptionPane.OK_CANCEL_OPTION);
    
        if (result == JOptionPane.OK_OPTION) {
            try {
                double startAngle = Double.parseDouble(startAngleField.getText());
                double endAngle = Double.parseDouble(endAngleField.getText());
                double velocity = Double.parseDouble(velocityField.getText());
    
                //Calculating angle increment
                double angleIncrement = (endAngle - startAngle) / n;
    
                //Adding of particles with uniform distance between start and end angles
                for (int i = 0; i < n; i++) {
                    Particle p = new Particle(0, 0, startAngle + angleIncrement * i, velocity);
                    particles.add(p);
                    t.get(threadToAdd).addParticle(p);
                    threadToAdd = (threadToAdd + 1) % 16;
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Invalid input! Only Numeric Values!");
            }
        }
    }

    /*
     * ADD PARTICLES BETWEEN VELOCITY
     * 
     * This function does the third form of the three when adding particles, adding them between velocities
     */
    
    private void addParticlesBetweenVelocities(int n) {
        //Separate window to input necessary values
        JPanel panel = new JPanel(new GridLayout(5, 2));

        //Input values for start and end velocities and angle
        JTextField startVelocityText = new JTextField(5);
        JTextField endVelocityText = new JTextField(5);
        JTextField angleText = new JTextField(5);

        panel.add(new JLabel("Start Velocity:"));
        panel.add(startVelocityText);
        panel.add(new JLabel("End Velocity:"));
        panel.add(endVelocityText);
        panel.add(new JLabel("Angle:"));
        panel.add(angleText);

        int result = JOptionPane.showConfirmDialog(this, panel, "Enter Particle Parameters", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            try {
                double startVelocity = Double.parseDouble(startVelocityText.getText());
                double endVelocity = Double.parseDouble(endVelocityText.getText());
                double angle = Double.parseDouble(angleText.getText());

                //Calculating the velocity difference
                double velocityDifference = (endVelocity - startVelocity) / n;

                //Adding of particles with uniform difference between start and end velocities
                for (int i = 0; i < n; i++) {
                    Particle p = new Particle(0, 0, angle, startVelocity + velocityDifference * i);
                    particles.add(p);
                    t.get(threadToAdd).addParticle(p);
                    threadToAdd = (threadToAdd + 1) % 16;
                    
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Invalid input! Only Numeric Values!");
            }
        }
    }

    //wall begone for this version
    /*
     * ADD WALL
     * 
     * This function is responsible for adding a wall to the program. It would open a new window where the user can input the coordinates for
     * the wall (start point and end point).
     */
    // private void addWall() {
    //     //Panel for adding a wall
    //     JPanel panel = new JPanel(new GridLayout(4, 2));

    //     JTextField x1Field = new JTextField(1);
    //     JTextField y1Field = new JTextField(1);
    //     JTextField x2Field = new JTextField(1);
    //     JTextField y2Field = new JTextField(1);

    //     //Add input fields to the panel with labels
    //     panel.add(new JLabel("X1:"));
    //     panel.add(x1Field);
    //     panel.add(new JLabel("Y1:"));
    //     panel.add(y1Field);
    //     panel.add(new JLabel("X2:"));
    //     panel.add(x2Field);
    //     panel.add(new JLabel("Y2:"));
    //     panel.add(y2Field);

    //     int result = JOptionPane.showConfirmDialog(this, panel, "Enter Wall Coordinates", JOptionPane.OK_CANCEL_OPTION);

    //     if (result == JOptionPane.OK_OPTION) {
    //         try {
    //             double x1 = Double.parseDouble(x1Field.getText());
    //             double y1 = Double.parseDouble(y1Field.getText());
    //             double x2 = Double.parseDouble(x2Field.getText());
    //             double y2 = Double.parseDouble(y2Field.getText());

    //             //Adding the walls with the specified coordinates
    //             Wall w = new Wall(x1, y1, x2, y2);
    //             walls.add(new Wall(x1, y1, x2, y2));
    //             t.forEach((thread) -> thread.addWall(w));
                
    //         } catch (NumberFormatException e) {
    //             JOptionPane.showMessageDialog(this, "Invalid input! Please enter numeric values.");
    //         }
    //     }
    // }

    /*
     * PAINT COMPONENT
     * 
     * This function is responble for the rendering process of the GUI.
     */

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;

        // Render particles within sprite's periphery if in explorer mode
        //TODO
        if(explorerMode){
            // for (Particle particle : particles) {
            //     //TODO
            //     if(/* particle is in periphery */){
            //         drawParticle(g2d, particle);
            //     }
            // }
            drawSprite(g2d, sprite);
        } else{
            // Render all particles if in developer mode
            for (Particle particle : particles) {
                drawParticle(g2d, particle);
            }
        }

        // for (Wall wall : walls) {
        //     drawWall(g2d, wall);
        // }

        //drawSprite(g2d);
    }

    /*
     * UPDATE FPS TEXT
     * 
     * This functions is dealing with updating the fps text when its continuously updating
     */

    private void updateFPSText() {
        fps = (int) (frameCount / FPS_UPDATE_INTERVAL);
        frameCount = 0;

        fpsLabel.setText("FPS: " + fps);
    }

    /*
     * DRAW PARTICLE AND DRAW WALL
     * 
     * These two functions are responsible for drawing the particles and the walls on the program.
     */

    private void drawParticle(Graphics2D g, Particle particle) {
        g.setColor(PARTICLE_COLOR);
        int x = (int) Math.round(particle.getX() - PARTICLE_RADIUS);
        int y = (int) Math.round(particle.getY() - PARTICLE_RADIUS);
        int diameter = (int) Math.round(2 * PARTICLE_RADIUS);
        g.fillOval(x, y, diameter, diameter);
    }

    private void drawSprite(Graphics2D g, Sprite sprite){
        //draw sprite here
        int spriteSize = 38;
        // Calculate sprite position on the canvas
        // Set color and draw the sprite
        g.setColor(Color.BLUE); // You can change the color as desired
        g.fillRect(spriteSize*16, spriteSize*9, spriteSize, spriteSize);
    }

    //wall begone for this version
    // private void drawWall(Graphics2D g, Wall wall) {
    //     g.setColor(WALL_COLOR);
    //     g.drawLine((int) Math.round(wall.getX1()), (int) Math.round(wall.getY1()), (int) Math.round(wall.getX2()), (int) Math.round(wall.getY2()));
    // }

    public static void main(String[] args) {
        List<CalculateThread> t = new ArrayList<>();

        //16 Threads seem to be the most optimal
        for(int i = 0; i< 16; i++){
            t.add(new CalculateThread());
        }

        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Particle Simulator");
            frame.getContentPane().add(new ParticleSimulator(t), BorderLayout.CENTER);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        });
    }
}