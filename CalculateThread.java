import java.util.ArrayList;
import java.util.List;

public class CalculateThread extends Thread{
    public List<Particle> particles;
    public List<Wall> walls;

    public CalculateThread(){
        particles = new ArrayList<Particle>();
        // walls = new ArrayList<Wall>();
    }

    
    //for every particle assigned to this thread, particle updates position.
    public void update(double deltaTime) {
        for (Particle particle : particles) {
            particle.update(deltaTime);
        }
    }


    //returns list of particles assigned to the thread
    public List<Particle> getParticles(){
        return particles;
    }


    //adds a wall to the list of walls
    // public void addWall(Wall w){
    //     walls.add(w);
    // }


    //adds a particle to the list of particles
    public void addParticle(Particle p){
        particles.add(p);
    }
}