package com.snl.swing.game2d.completegame.object;

import com.snl.swing.game2d.tool.Matrix3x3f;
import com.snl.swing.game2d.tool.Vector2D;
import com.snl.swing.game2d.util.Sprite;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class Ship {
    private static final int MAX_PARTICLES = 300;
    private double angle;
    private double acceleration;
    private double friction;
    private double maxVelocity;
    private double rotationDelta;
    private double curAcc;
    private Vector2D position;
    private Vector2D velocity;
    private ArrayList<Particle> particles;
    private Random random;
    private PolygonWrapper wrapper;
    private boolean alive;
    private boolean invincible;
    private double invincibleDelta;
    private Sprite ship;
    private Sprite glow;
    private Vector2D[] polygon;
    private ArrayList<Vector2D[]> collisionList;
    private ArrayList<Vector2D> positionList;

    public Ship(PolygonWrapper wrapper) {
        this.wrapper = wrapper;
        friction = 0.25f;
        rotationDelta = (double) Math.toRadians(180.0);
        acceleration = 1.0f;
        maxVelocity = 0.5f;
        velocity = new Vector2D();
        position = new Vector2D();
        particles = new ArrayList<Particle>();
        random = new Random();
        collisionList = new ArrayList<Vector2D[]>();
        positionList = new ArrayList<Vector2D>();
    }

    public void setPolygon(Vector2D[] polygon) {
        this.polygon = polygon;
    }

    public void setShipSprite(Sprite ship) {
        this.ship = ship;
    }

    public void setGlowSprite(Sprite glow) {
        this.glow = glow;
    }

    public double getWidth() {
        double min = Double.POSITIVE_INFINITY;
        double max = -Double.POSITIVE_INFINITY;
        for (Vector2D v : polygon) {
            min = Math.min(min, v.x);
            max = Math.max(max, v.x);
        }
        return Math.abs(min) + Math.abs(max);
    }

    public double getHeight() {
        double min = Double.POSITIVE_INFINITY;
        double max = -Double.POSITIVE_INFINITY;
        for (Vector2D v : polygon) {
            min = Math.min(min, v.y);
            max = Math.max(max, v.y);
        }
        return Math.abs(min) + Math.abs(max);
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public boolean isAlive() {
        return alive;
    }

    public void setPotition(Vector2D position) {
        this.position = position;
    }

    public void rotateLeft(double delta) {
        angle += rotationDelta * delta;
    }

    public void rotateRight(double delta) {
        angle -= rotationDelta * delta;
    }

    public void reset() {
        setAlive(true);
        setPotition(new Vector2D());
        setAngle(0.0f);
        positionList.clear();
        collisionList.clear();
        velocity = new Vector2D();
        particles.clear();
    }

    public void setThrusting(boolean thrusting) {
        if (isAlive()) {
            curAcc = thrusting ? acceleration : 0.0f;
            if (thrusting) {
                while (particles.size() < MAX_PARTICLES) {
                    particles.add(createRandomParticle());
                }
            }
        }
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }

    public Bullet launchBullet() {
        Vector2D bulletPos = position.add(Vector2D.polar(angle, 0.0325f));
        return new Bullet(bulletPos, angle);
    }

    public void update(double delta) {
        if (isAlive()) {
            updatePosition(delta);
            updateInvincible(delta);
            updateParticles(delta);
            collisionList.clear();
            Vector2D[] world = transformPolygon();
            collisionList.add(world);
            wrapper.wrapPolygon(world, collisionList);
            positionList.clear();
            positionList.add(position);
            wrapper.wrapPositions(world, position, positionList);
        }
    }

    private Vector2D[] transformPolygon() {
        Matrix3x3f mat = Matrix3x3f.rotate(angle);
        mat = mat.mul(Matrix3x3f.translate(position));
        return transform(polygon, mat);
    }

    private void updatePosition(double time) {
        Vector2D accel = Vector2D.polar(angle, curAcc);
        velocity = velocity.add(accel.mul(time));
        double maxSpeed = Math.min(maxVelocity / velocity.len(), 1.0f);
        velocity = velocity.mul(maxSpeed);
        double slowDown = 1.0f - friction * time;
        velocity = velocity.mul(slowDown);
        position = position.add(velocity.mul(time));
        position = wrapper.wrapPosition(position);
    }

    private Vector2D[] transform(Vector2D[] poly, Matrix3x3f mat) {
        Vector2D[] copy = new Vector2D[poly.length];
        for (int i = 0; i < poly.length; ++i) {
            copy[i] = mat.mul(poly[i]);
        }
        return copy;
    }

    public void draw(Graphics2D g, Matrix3x3f view) {
        if (isAlive()) {
            for (Vector2D pos : positionList) {
                if (isInvincible()) {
                    glow.render(g, view, pos, angle);
                } else {
                    ship.render(g, view, pos, angle);
                }
            }
            for (Particle p : particles) {
                p.draw(g, view);
            }
        }
    }

    public Vector2D isTouching(Asteroid asteroid) {
        if (isAlive()) {
            for (Vector2D[] poly : collisionList) {
                for (Vector2D v : poly) {
                    if (asteroid.contains(v)) {
                        return v;
                    }
                }
            }
        }
        return null;
    }

    public boolean isInvincible() {
        return invincible;
    }

    public void setInvincible() {
        invincible = true;
    }

    private void updateInvincible(double time) {
        if (isInvincible()) {
            invincibleDelta += time;
            if (invincibleDelta > 3.0f) {
                invincibleDelta = 0.0f;
                invincible = false;
            }
        }
    }

    private Particle createRandomParticle() {
        Particle p = new Particle();
        p.setRadius(0.002f + random.nextDouble() * 0.004f);
        p.setLifeSpan(random.nextDouble() * 0.5f);
        switch (random.nextInt(5)) {
            case 0:
                p.setColor(Color.WHITE);
                break;
            case 1:
                p.setColor(Color.RED);
                break;
            case 2:
                p.setColor(Color.YELLOW);
                break;
            case 3:
                p.setColor(Color.ORANGE);
                break;
            case 4:
                p.setColor(Color.PINK);
                break;
        }
        int thrustAngle = 100;
        double a = (double) Math.toRadians(random.nextInt(thrustAngle)
                - (thrustAngle / 2));
        double velocity = random.nextDouble() * 0.375f;
        Vector2D bulletPos = position.add(Vector2D.polar(angle, -0.0325f));
        p.setPosition(bulletPos);
        p.setVector(angle + (double) Math.PI + a, velocity);
        return p;
    }

    private void updateParticles(double delta) {
        Iterator<Particle> part = particles.iterator();
        while (part.hasNext()) {
            Particle p = part.next();
            Vector2D bulletPos = position.add(Vector2D.polar(angle, -0.0325f));
            p.setPosition(bulletPos);
            p.update(delta);
            if (p.hasDied()) {
                part.remove();
            }
        }
    }
}
