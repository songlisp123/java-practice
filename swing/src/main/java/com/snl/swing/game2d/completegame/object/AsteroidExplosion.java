package com.snl.swing.game2d.completegame.object;

import com.snl.swing.game2d.tool.Matrix3x3f;
import com.snl.swing.game2d.tool.Vector2D;

import java.awt.*;
import java.util.Iterator;
import java.util.Random;
import java.util.Vector;

public class AsteroidExplosion {
    private static final int MAX_PARTICLES = 150;
    private Vector<Particle> particles;
    private Random random = new Random();
    private Vector2D pos;

    public AsteroidExplosion(Vector2D pos) {
        this.pos = pos;
        createParticles();
    }

    private void createParticles() {
        particles = new Vector<Particle>();
        for (int i = 0; i < MAX_PARTICLES; ++i) {
            particles.add(createRandomParticle());
        }
    }

    private Particle createRandomParticle() {
        Particle p = new Particle();
        p.setPosition(pos);
        p.setRadius(0.002f + random.nextFloat() * 0.004f);
        p.setLifeSpan(random.nextFloat() * 1.0f);
        switch (random.nextInt(4)) {
            case 0:
                p.setColor(Color.WHITE);
                break;
            case 1:
                p.setColor(Color.GRAY);
                break;
            case 2:
                p.setColor(Color.LIGHT_GRAY);
                break;
            case 3:
                p.setColor(Color.DARK_GRAY);
                break;
        }
        float angle = (float) Math.toRadians(random.nextInt(360));
        float velocity = random.nextFloat() * 2.0f;
        p.setVector(angle, velocity);
        return p;
    }

    public void update(double time) {
        Iterator<Particle> it = particles.iterator();
        while (it.hasNext()) {
            Particle p = it.next();
            p.update(time);
            if (p.hasDied()) {
                it.remove();
            }
        }
    }

    public void render(Graphics2D g, Matrix3x3f view) {
        for (Particle p : particles) {
            p.draw(g, view);
        }
    }

    public boolean isFinished() {
        return particles.isEmpty();
    }
}
