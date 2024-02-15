import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

public class CalculateThread extends Thread{
    public List<Particle> particles;
    public List<Wall> walls;

    public CalculateThread(){
        particles = new ArrayList<Particle>();
        walls = new ArrayList<Wall>();
    }

    /**
     * TODO:
     * - make function to add particle dynamically
     * - make sure thread only recieves particle if it is next in line to recieve particle
     */

        //is called on a timer from main

    public void update(double deltaTime) {
        for (Particle particle : particles) {
            particle.update(deltaTime, walls);
        }
    }

    public List<Particle> getParticles(){
        return particles;
    }

    public void addWall(Wall w){
        walls.add(w);
    }

    public void addParticle(Particle p){
        particles.add(p);
    }
}