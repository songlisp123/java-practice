package com.snl.swing.game2d.completegame.state;

import com.snl.swing.game2d.completegame.admin.Acme;
import com.snl.swing.game2d.completegame.admin.QuickLooper;
import com.snl.swing.game2d.completegame.admin.QuickRestart;
import com.snl.swing.game2d.completegame.object.*;
import com.snl.swing.game2d.input.KeyBoardEvent;
import com.snl.swing.game2d.tool.Matrix3x3f;
import com.snl.swing.game2d.tool.Vector2D;
import com.snl.swing.game2d.util.Sprite;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Random;

public class LevelPlaying extends State {
    private ArrayList<AsteroidExplosion> explosions;
    private ArrayList<Asteroid> asteroids;
    private ArrayList<Bullet> bullets;
    private double respawn = 0.0;
    private Sprite background;
    private AsteroidFactory factory;
    private Acme acme;
    private QuickLooper thruster;
    private QuickRestart laser;
    private QuickRestart[] explosion;
    private Random rand = new Random();
    private ShipExplosion shipExplosion;
    private ShipFactory shipFactory;
    private Ship ship;
    private boolean thrusting;
    private GameState state;
    private PolygonWrapper wrapper;
    private KeyBoardEvent keys;

    public LevelPlaying(GameState state) {
        this.state = state;
    }

    @Override
    public void enter() {
        background = (Sprite) controller.getAttribute("background");
        factory = (AsteroidFactory) controller.getAttribute("factory");
        keys = (KeyBoardEvent) controller.getAttribute("keys");
        laser = (QuickRestart) controller.getAttribute("fire-clip");
        explosion = (QuickRestart[]) controller.getAttribute("explosions");
        thruster = (QuickLooper) controller.getAttribute("thruster-clip");
        shipFactory = (ShipFactory) controller.getAttribute("ship-factory");
        wrapper = (PolygonWrapper) controller.getAttribute("wrapper");
        acme = (Acme) controller.getAttribute("ACME");
        ship = shipFactory.createShip();
        ship.setAngle((float) Math.toRadians(90));
        explosions = new ArrayList<AsteroidExplosion>();
        asteroids = new ArrayList<Asteroid>();
        bullets = new ArrayList<Bullet>();
        // generate random
        for (int i = 0; i < state.getLevel(); ++i) {
            asteroids.add(factory.getLargeAsteroid());
            asteroids.add(factory.getMediumAsteroid());
            asteroids.add(factory.getSmallAsteroid());
        }
        createShip(false);
    }

    private void createShip(boolean invincible) {
        ship.reset();
        if (invincible) {
            ship.setInvincible();
        }
    }

    @Override
    public void processInput(double delta) {
        if (ship.isAlive()) {
            if (keys.keyDownOnce(KeyEvent.VK_SPACE)) {
                bullets.add(ship.launchBullet());
                laser.fire();
            }
            if (keys.keyDown(KeyEvent.VK_A)) {
                ship.rotateLeft(delta);
            }
            if (keys.keyDown(KeyEvent.VK_D)) {
                ship.rotateRight(delta);
            }
            if (keys.keyDown(KeyEvent.VK_W)) {
                ship.setThrusting(true);
                if (!thrusting) {
                    thruster.fire();
                    thrusting = true;
                }
            } else {
                ship.setThrusting(false);
                if (thrusting) {
                    thruster.done();
                    thrusting = false;
                }
            }
        }
    }

    @Override
    public void updateObjects(double delta) {
        updateAsteroids(delta);
        updateBullets(delta);
        updateShip(delta);
        updateAsteroidExplosions(delta);
        updateShipExplosion(delta);
        checkForLevelWon();
    }

    private void updateShip(double delta) {
        if (shouldRespawn()) {
            processRespawnShip(delta);
        } else {
            ship.update(delta);
            ArrayList<Asteroid> ast = new ArrayList<Asteroid>(asteroids);
            for (Asteroid asteroid : ast) {
                Vector2D collision = ship.isTouching(asteroid);
                if (collision != null) {
                    if (ship.isInvincible()) {
                        explosions.add(new AsteroidExplosion(collision));
                        explosion[rand.nextInt(explosion.length)].fire();
                        remove(asteroid);
                    } else {
                        shipExplosion = new ShipExplosion(collision);
                        explosion[rand.nextInt(explosion.length)].fire();
                        ship.setAlive(false);
                        thruster.done();
                        thrusting = false;
                    }
                }
            }
        }
    }

    private void updateAsteroids(double delta) {
        for (Asteroid a : asteroids) {
            a.update(delta);
        }
    }

    private void updateBullets(double delta) {
        ArrayList<Bullet> copy = new ArrayList<Bullet>(bullets);
        for (Bullet bullet : copy) {
            updateBullet(delta, bullet);
        }
    }

    private void updateBullet(double delta, Bullet bullet) {
        bullet.update(delta);
        if (wrapper.hasLeftWorld(bullet.getPosition())) {
            bullets.remove(bullet);
        } else {
            ArrayList<Asteroid> ast = new ArrayList<Asteroid>(asteroids);
            for (Asteroid asteroid : ast) {
                if (asteroid.contains(bullet.getPosition())) {
                    remove(asteroid);
                    bullets.remove(bullet);
                    explosions.add(new AsteroidExplosion(bullet.getPosition()));
                    explosion[rand.nextInt(explosion.length)].fire();
                }
            }
        }
    }

    private void remove(Asteroid asteroid) {
        asteroids.remove(asteroid);
        state.updateScore(asteroid.getSize());
        spawnBabies(asteroid);
    }

    private void spawnBabies(Asteroid asteroid) {
        if (asteroid.getSize() == Asteroid.Size.Large) {
            asteroids.add(factory.getMediumAsteroid(asteroid.getPosition()));
            asteroids.add(factory.getMediumAsteroid(asteroid.getPosition()));
        }
        if (asteroid.getSize() == Asteroid.Size.Medium) {
            asteroids.add(factory.getSmallAsteroid(asteroid.getPosition()));
            asteroids.add(factory.getSmallAsteroid(asteroid.getPosition()));
        }
    }

    private boolean shouldRespawn() {
        return !ship.isAlive() && shipExplosion == null;
    }

    private void processRespawnShip(double delta) {
        respawn += delta;
        if (respawn > 1.0) {
            if (state.getLives() > 0) {
                respawn = 0.0;
                state.setLives(state.getLives() - 1);
                createShip(true);
            } else {
                getController().setState(new GameOver(asteroids, state));
            }
        }
    }

    private void checkForLevelWon() {
        if (asteroids.isEmpty() && explosions.isEmpty()) {
            state.setLevel(state.getLevel() + 1);
            thruster.done();
            getController().setState(new LevelStarting(state));
        }
    }

    private void updateAsteroidExplosions(double delta) {
        for (AsteroidExplosion explosion : new ArrayList<>(
                explosions)) {
            explosion.update(delta);
            if (explosion.isFinished()) {
                explosions.remove(explosion);
            }
        }
    }

    private void updateShipExplosion(double delta) {
        if (shipExplosion != null) {
            shipExplosion.update(delta);
            if (shipExplosion.isFinished()) {
                shipExplosion = null;
            }
        }
    }

    @Override
    public void render(Graphics2D g, Matrix3x3f view) {
        background.render(g, view);
        ship.draw(g, view);
        for (Asteroid a : asteroids) {
            a.draw(g, view);
        }
        for (AsteroidExplosion ex : explosions) {
            ex.render(g, view);
        }
        for (Bullet b : bullets) {
            b.draw(g, view);
        }
        if (shipExplosion != null) {
            shipExplosion.render(g, view);
        }
        acme.drawLives(g, view, state.getLives());
        acme.drawScore(g, state.getScore());
    }
}
