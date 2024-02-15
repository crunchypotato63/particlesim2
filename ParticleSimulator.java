import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ParticleSimulator extends JPanel {
    /*Initialization of immutable variables*/
    //Screen size: 1280 x 720
    public static final int SCREEN_WIDTH = 1280;
    public static final int SCREEN_HEIGHT = 720;

    //Particle size
    private static final double PARTICLE_RADIUS = 10;

    //Particle and Wall colors
    private static final Color PARTICLE_COLOR = Color.BLUE;
    private static final Color WALL_COLOR = Color.BLACK;

    //Updates FPS every 0.5 seconds
    private static final double FPS_UPDATE_INTERVAL = 0.5;

    //Initialization of Particles and Walls
    private List<Particle> particles = new ArrayList<>();
    private List<Wall> walls = new ArrayList<>();

    //Variables needed to calculate FPS
    private int fps;
    private int frameCount;
    private long lastTime;
    private JLabel fpsLabel;

    private List<CalculateThread> t;

    private int threadToAdd;
    
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
            double deltaTime = (now - lastTime) / 1_000_000_000.0;
            lastTime = now;

            t.forEach((thread) -> thread.update(deltaTime));
            //t.forEach((thread) -> thread.getParticle)
            //clear out old list of particles
            particles = new ArrayList<Particle>();
            for(int i = 0; i < 4; i++){
                List<Particle> part = t.get(i).getParticles();
                particles.addAll(part);
            }
            repaint();
            frameCount++;
        }).start();

        //fps counter
        new Timer((int) (FPS_UPDATE_INTERVAL * 1000), e -> {
            updateFPS();
        }).start();
        
        //fps label
        fpsLabel = new JLabel("FPS: ");
        fpsLabel.setForeground(Color.BLACK);
        this.add(fpsLabel);

        //Add Particle button
        JButton addParticleButton = new JButton("Add Particle");
        addParticleButton.addActionListener(e -> addParticles());
        this.add(addParticleButton);

        //Add Wall button
        JButton addWallButton = new JButton("Add Wall");
        addWallButton.addActionListener(e -> addWall());
        this.add(addWallButton);
    }

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
     * 
     */
    
    private void addParticlesBetweenPoints(int n) {
        //Separate window to input necessary values
        JPanel panel = new JPanel(new GridLayout(5, 2));
    
        //Input values for start and end points and velocity
        JTextField startXField = new JTextField();
        JTextField startYField = new JTextField();
        JTextField endXField = new JTextField();
        JTextField endYField = new JTextField();
        JTextField velocityField = new JTextField();
    
        panel.add(new JLabel("Start X:"));
        panel.add(startXField);
        panel.add(new JLabel("Start Y:"));
        panel.add(startYField);
        panel.add(new JLabel("End X:"));
        panel.add(endXField);
        panel.add(new JLabel("End Y:"));
        panel.add(endYField);
        panel.add(new JLabel("Velocity:"));
        panel.add(velocityField);
    
        //Show the panel in a dialog box
        int result = JOptionPane.showConfirmDialog(this, panel, "Enter Particle Parameters", JOptionPane.OK_CANCEL_OPTION);
    
        if (result == JOptionPane.OK_OPTION) {
            try {
                double startX = Double.parseDouble(startXField.getText());
                double startY = Double.parseDouble(startYField.getText());
                double endX = Double.parseDouble(endXField.getText());
                double endY = Double.parseDouble(endYField.getText());
                double velocity = Double.parseDouble(velocityField.getText());
        
                //Calculate increment in position
                double incrementX = (endX - startX) / n;
                double incrementY = (endY - startY) / n;
    
                //Add particles with uniform distance between start and end points
                for (int i = 0; i < n; i++) {
                    Particle p = new Particle(startX + incrementX * i, startY + incrementY * i, 0, velocity); // Angle is not relevant
                    particles.add(p);
                    t.get(threadToAdd).addParticle(p);
                    threadToAdd = (threadToAdd + 1)%4;
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Invalid input! Please enter numeric values.");
            }
        }
    }
    
    private void addParticlesBetweenAngles(int n) {
        //Create a panel to hold input fields
        JPanel panel = new JPanel(new GridLayout(4, 2));
    
        //Create input fields for start and end angles, and velocity
        JTextField startAngleField = new JTextField(5);
        JTextField endAngleField = new JTextField(5);
        JTextField velocityField = new JTextField(5);
    
        //Add input fields to the panel with labels
        panel.add(new JLabel("Start Angle:"));
        panel.add(startAngleField);
        panel.add(new JLabel("End Angle:"));
        panel.add(endAngleField);
        panel.add(new JLabel("Velocity:"));
        panel.add(velocityField);
    
        //Show the panel in a dialog box
        int result = JOptionPane.showConfirmDialog(this, panel, "Enter Particle Parameters", JOptionPane.OK_CANCEL_OPTION);
    
        if (result == JOptionPane.OK_OPTION) {
            try {
                //Parse input values
                double startAngle = Double.parseDouble(startAngleField.getText());
                double endAngle = Double.parseDouble(endAngleField.getText());
                double velocity = Double.parseDouble(velocityField.getText());
    
                //Calculate angle increment
                double angleIncrement = (endAngle - startAngle) / n;
    
                //Add particles with uniform distance between start and end angles
                for (int i = 0; i < n; i++) {
                    Particle p = new Particle(0, 0, startAngle + angleIncrement * i, velocity); // Position is not relevant
                    particles.add(p);
                    t.get(threadToAdd).addParticle(p);
                    threadToAdd = (threadToAdd + 1)%4;
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Invalid input! Please enter numeric values.");
            }
        }
    }
    
    private void addParticlesBetweenVelocities(int n) {
        //Create a panel to hold input fields
        JPanel panel = new JPanel(new GridLayout(3, 2));

        //Create input fields for start and end velocities, and angle
        JTextField startVelocityField = new JTextField(5);
        JTextField endVelocityField = new JTextField(5);
        JTextField angleField = new JTextField(5);

        //Add input fields to the panel with labels
        panel.add(new JLabel("Start Velocity:"));
        panel.add(startVelocityField);
        panel.add(new JLabel("End Velocity:"));
        panel.add(endVelocityField);
        panel.add(new JLabel("Angle:"));
        panel.add(angleField);

        //Show the panel in a dialog box
        int result = JOptionPane.showConfirmDialog(this, panel, "Enter Particle Parameters", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            try {
                //Parse input values
                double startVelocity = Double.parseDouble(startVelocityField.getText());
                double endVelocity = Double.parseDouble(endVelocityField.getText());
                double angle = Double.parseDouble(angleField.getText());

                //Calculate velocity difference
                double velocityDifference = (endVelocity - startVelocity) / n;

                //Add particles with uniform difference between start and end velocities
                for (int i = 0; i < n; i++) {
                    Particle p = new Particle(0, 0, angle, startVelocity + velocityDifference * i); //Position is not relevant
                    particles.add(p);
                    t.get(threadToAdd).addParticle(p);
                    threadToAdd = (threadToAdd + 1)%4;
                    
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Invalid input! Please enter numeric values.");
            }
        }
    }

    /*
     * ADD WALL
     * 
     * This function is responsible for adding a wall to the program. It would open a new window where the user can input the coordinates for
     * the wall (start point and end point).
     */
    private void addWall() {
        //Panel for adding a wall
        JPanel panel = new JPanel(new GridLayout(4, 2));

        //Input fields for x1, y1, x2, and y2 coordinates
        JTextField x1Field = new JTextField(1);
        JTextField y1Field = new JTextField(1);
        JTextField x2Field = new JTextField(1);
        JTextField y2Field = new JTextField(1);

        //Add input fields to the panel with labels
        panel.add(new JLabel("X1:"));
        panel.add(x1Field);
        panel.add(new JLabel("Y1:"));
        panel.add(y1Field);
        panel.add(new JLabel("X2:"));
        panel.add(x2Field);
        panel.add(new JLabel("Y2:"));
        panel.add(y2Field);

        //Show the panel in a dialog box
        int result = JOptionPane.showConfirmDialog(this, panel, "Enter Wall Coordinates", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            try {
                //Parse input values
                double x1 = Double.parseDouble(x1Field.getText());
                double y1 = Double.parseDouble(y1Field.getText());
                double x2 = Double.parseDouble(x2Field.getText());
                double y2 = Double.parseDouble(y2Field.getText());

                //Add the wall with the specified coordinates
                Wall w = new Wall(x1, y1, x2, y2);
                walls.add(new Wall(x1, y1, x2, y2));
                t.forEach((thread) -> thread.addWall(w));
                
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Invalid input! Please enter numeric values.");
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;
        for (Particle particle : particles) {
            drawParticle(g2d, particle);
        }

        for (Wall wall : walls) {
            drawWall(g2d, wall);
        }
    }

    //delegated to a thread
    //calculating part
    // private void update(double deltaTime) {
    //     for (Particle particle : particles) {
    //         particle.update(deltaTime, walls);
    //     }
    // }

    private void updateFPS() {
        fps = (int) (frameCount / FPS_UPDATE_INTERVAL);
        frameCount = 0;

        //Update FPS label
        fpsLabel.setText("FPS: " + fps);
    }

    private void drawParticle(Graphics2D g, Particle particle) {
        g.setColor(PARTICLE_COLOR);
        int x = (int) Math.round(particle.getX() - PARTICLE_RADIUS);
        int y = (int) Math.round(particle.getY() - PARTICLE_RADIUS);
        int diameter = (int) Math.round(2 * PARTICLE_RADIUS);
        g.fillOval(x, y, diameter, diameter);
    }

    private void drawWall(Graphics2D g, Wall wall) {
        g.setColor(WALL_COLOR);
        g.drawLine((int) Math.round(wall.getX1()), (int) Math.round(wall.getY1()),
                (int) Math.round(wall.getX2()), (int) Math.round(wall.getY2()));
    }

    public static void main(String[] args) {
        // CalculateThread t1 = new CalculateThread();
        // CalculateThread t2 = new CalculateThread();
        // CalculateThread t3 = new CalculateThread();
        // CalculateThread t4 = new CalculateThread();
        
        List<CalculateThread> t = new ArrayList<>();
        t.add(new CalculateThread());
        t.add(new CalculateThread());
        t.add(new CalculateThread());
        t.add(new CalculateThread());

        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Particle Simulator");
            //create threads before particle simulator is made
            frame.getContentPane().add(new ParticleSimulator(t), BorderLayout.CENTER);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        });
    }
}

//new thread class
//thread class have list of particles to calculate for
//whenever new particle added in, particle is added to thread list
//flag to indicate which thread set to add the particle to