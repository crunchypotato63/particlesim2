import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ParticleSimulator extends JPanel {

    /*Initialization of immutable variables*/
    //Canvas size: 1280 x 720
    public static final int CANVAS_WIDTH = 1280;
    public static final int CANVAS_HEIGHT = 720;

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

    //Java Swing variables
    private JLabel fpsLabel;
    
    public ParticleSimulator() {
        //sets canvas size
        setPreferredSize(new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT));

        //Initializes particles and walls (if we want to have some set amount of particles and walls already when initialized)
        initializeParticles();
        initializeWalls();

        //for fps counter
        lastTime = System.nanoTime();

        frameCount = 0;

        // Update loop
        new Timer(1000 / 60, e -> {
            long now = System.nanoTime();
            double deltaTime = (now - lastTime) / 1_000_000_000.0; // in seconds
            lastTime = now;

            update(deltaTime);
            repaint();
            frameCount++;
        }).start();

        //fps counter
        new Timer((int) (FPS_UPDATE_INTERVAL * 1000), e -> {
            updateFPS();
        }).start();

        //fps label
        fpsLabel = new JLabel("FPS: ");
        fpsLabel.setForeground(Color.RED);
        this.add(fpsLabel);

        //Add Particle button
        JButton addParticleButton = new JButton("Add Particle");
        addParticleButton.addActionListener(e -> addParticle());
        this.add(addParticleButton);

        //Add Wall button
        JButton addWallButton = new JButton("Add Wall");
        addWallButton.addActionListener(e -> addWall());
        this.add(addWallButton);
    }

    private void addParticle() {
        //TODO: Make user input any number of particles, 
        particles.add(new Particle(CANVAS_WIDTH / 2, CANVAS_HEIGHT / 2, Math.random() * 360, Math.random() * 100 + 50));
    }

    private void addWall() {
        /* 
        //only random horizontal or vertical wall
        //TODO: Make user input x1, x2, y1, y2 to add a wall
        double x1, y1, x2, y2;
        if (Math.random() < 0.5) {
            // Horizontal wall
            x1 = 100;
            x2 = CANVAS_WIDTH - 100;
            y1 = y2 = Math.random() * CANVAS_HEIGHT;
        } else {
            // Vertical wall
            y1 = 100;
            y2 = CANVAS_HEIGHT - 100;
            x1 = x2 = Math.random() * CANVAS_WIDTH;
        }
        walls.add(new Wall(x1, y1, x2, y2)); */

        String stringX1 = JOptionPane.showInputDialog("Enter x1 coordinate:");
        String stringY1 = JOptionPane.showInputDialog("Enter y1 coordinate:");
        String stringX2 = JOptionPane.showInputDialog("Enter x2 coordinate:");
        String stringY2 = JOptionPane.showInputDialog("Enter y2 coordinate:");

        try {
            // Parse the input values
            double x1 = Double.parseDouble(stringX1);
            double y1 = Double.parseDouble(stringY1);
            double x2 = Double.parseDouble(stringX2);
            double y2 = Double.parseDouble(stringY2);
    
            // Add the wall with the specified coordinates
            walls.add(new Wall(x1, y1, x2, y2));
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid input! Please enter numeric values.");
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

    private void initializeParticles() {
        
    }

    private void initializeWalls() {
        
    }

    private void update(double deltaTime) {
        for (Particle particle : particles) {
            particle.update(deltaTime, walls);
        }
    }

    private void updateFPS() {
        fps = (int) (frameCount / FPS_UPDATE_INTERVAL);
        frameCount = 0;

        // Update FPS label
        fpsLabel.setText("FPS: " + fps);
    }

    //drawing on canvas
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
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Particle Simulator");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.getContentPane().add(new ParticleSimulator(), BorderLayout.CENTER);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
