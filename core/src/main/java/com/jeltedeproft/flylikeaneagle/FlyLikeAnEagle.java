package com.jeltedeproft.flylikeaneagle;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

public class FlyLikeAnEagle extends ApplicationAdapter {
    private static final float STEP = 1f / 60f;
    private static final float GROUND_PITCH_TORQUE = 22f;
    private static final float BASE_AIR_PITCH_TORQUE = 48f;
    private static final float FINISH_SPEED = 0.7f;
    private static final float FINISH_DELAY = 1.25f;
    private static final float LANDING_SLOPE_DEGREES = -17f;
    private static final int MAX_UPGRADE_LEVEL = 5;

    private static final Vector2[] RUN_UP_AND_RAMP = {
        new Vector2(-20f, 15f), new Vector2(-10f, 14f), new Vector2(0f, 11f),
        new Vector2(12f, 6f), new Vector2(24f, 2.2f), new Vector2(32f, 1.4f),
        new Vector2(38f, 1.7f), new Vector2(43f, 2.5f), new Vector2(47f, 3.8f),
        new Vector2(50f, 5.2f), new Vector2(52.5f, 6.8f)
    };

    private static final Vector2[] LANDING = {
        new Vector2(68f, 5.7f), new Vector2(80f, 3.2f), new Vector2(94f, 1.2f),
        new Vector2(110f, 0f), new Vector2(220f, 0f)
    };

    private enum RunState { RUNNING, FINISHED }
    private enum Outcome { NONE, CLEAN, CRASH }

    private World world;
    private Body sled;
    private OrthographicCamera camera;
    private ExtendViewport viewport;
    private ShapeRenderer shapes;
    private Preferences progress;

    private float accumulator, startX, stationaryTime, runTime, bestDistance, finalDistance, landingTimer;
    private boolean launched, touchedLanding, touchLeft, touchRight;
    private RunState state;
    private Outcome outcome;

    private int coins, speedLevel, glideLevel, controlLevel, lastReward;

    @Override public void create() {
        world = new World(new Vector2(0f, -7.4f), true);
        camera = new OrthographicCamera();
        viewport = new ExtendViewport(32f, 18f, camera);
        shapes = new ShapeRenderer();
        progress = Gdx.app.getPreferences("fly-like-an-eagle-progress");
        loadProgress();
        createTerrain();
        installContactListener();
        startRun();
    }

    private void loadProgress() {
        coins = progress.getInteger("coins", 0);
        speedLevel = progress.getInteger("speed", 0);
        glideLevel = progress.getInteger("glide", 0);
        controlLevel = progress.getInteger("control", 0);
        bestDistance = progress.getFloat("best", 0f);
    }

    private void saveProgress() {
        progress.putInteger("coins", coins).putInteger("speed", speedLevel)
            .putInteger("glide", glideLevel).putInteger("control", controlLevel)
            .putFloat("best", bestDistance).flush();
    }

    private void createTerrain() {
        createTerrainChain(RUN_UP_AND_RAMP, "ramp");
        createTerrainChain(LANDING, "landing");
    }

    private void createTerrainChain(Vector2[] points, String tag) {
        Body terrain = world.createBody(new BodyDef());
        ChainShape shape = new ChainShape();
        shape.createChain(points);
        Fixture fixture = terrain.createFixture(shape, 0f);
        fixture.setFriction(0.015f);
        fixture.setUserData(tag);
        shape.dispose();
    }

    private void installContactListener() {
        world.setContactListener(new ContactListener() {
            @Override public void beginContact(Contact contact) {
                Fixture a = contact.getFixtureA(), b = contact.getFixtureB();
                if (isPair(a, b, "sled", "landing") && launched && !touchedLanding) {
                    touchedLanding = true;
                    landingTimer = 0f;
                    outcome = evaluateLanding();
                }
            }
            @Override public void endContact(Contact contact) { }
            @Override public void preSolve(Contact contact, Manifold oldManifold) { }
            @Override public void postSolve(Contact contact, ContactImpulse impulse) { }
        });
    }

    private boolean isPair(Fixture a, Fixture b, String first, String second) {
        Object au = a.getUserData(), bu = b.getUserData();
        return (first.equals(au) && second.equals(bu)) || (first.equals(bu) && second.equals(au));
    }

    private Outcome evaluateLanding() {
        float sledDegrees = normalizeDegrees(sled.getAngle() * MathUtils.radiansToDegrees);
        float angleError = Math.abs(normalizeDegrees(sledDegrees - LANDING_SLOPE_DEGREES));
        return angleError <= 38f && Math.abs(sled.getAngularVelocity()) <= 6.5f
            && -sled.getLinearVelocity().y <= 20f ? Outcome.CLEAN : Outcome.CRASH;
    }

    private float normalizeDegrees(float degrees) {
        while (degrees > 180f) degrees -= 360f;
        while (degrees < -180f) degrees += 360f;
        return degrees;
    }

    private void startRun() {
        if (sled != null) world.destroyBody(sled);
        BodyDef def = new BodyDef();
        def.type = BodyDef.BodyType.DynamicBody;
        def.position.set(-14f, 16.2f);
        def.angle = -0.08f;
        def.angularDamping = 0.12f;
        sled = world.createBody(def);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(1.4f, 0.28f);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1.2f;
        fixtureDef.friction = 0.005f;
        fixtureDef.restitution = 0.03f;
        Fixture fixture = sled.createFixture(fixtureDef);
        fixture.setUserData("sled");
        shape.dispose();

        sled.setLinearVelocity(9f + speedLevel * 1.2f, 0f);
        startX = sled.getPosition().x;
        stationaryTime = runTime = accumulator = finalDistance = landingTimer = 0f;
        lastReward = 0;
        launched = touchedLanding = touchLeft = touchRight = false;
        outcome = Outcome.NONE;
        state = RunState.RUNNING;
        camera.position.set(-9f, 10f, 0f);
        camera.update();
    }

    @Override public void render() {
        float delta = Math.min(Gdx.graphics.getDeltaTime(), 0.25f);
        if (state == RunState.FINISHED) {
            handleUpgradeInput();
            if (Gdx.input.isKeyJustPressed(Input.Keys.R) || Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) startRun();
        } else {
            updateControls();
            guaranteeRampSpeed();
            applyGlideAssist();
            stepPhysics(delta);
            updateRunState(delta);
        }
        updateCamera();
        drawWorld();
        updateTitle();
    }

    private void handleUpgradeInput() {
        if (!Gdx.input.justTouched()) return;
        float x = Gdx.input.getX() / (float) Gdx.graphics.getWidth();
        float y = Gdx.input.getY() / (float) Gdx.graphics.getHeight();
        if (y < 0.52f) {
            if (x < .333f) buyUpgrade(0); else if (x < .666f) buyUpgrade(1); else buyUpgrade(2);
        } else startRun();
    }

    private void buyUpgrade(int type) {
        int level = type == 0 ? speedLevel : type == 1 ? glideLevel : controlLevel;
        if (level >= MAX_UPGRADE_LEVEL) return;
        int cost = 20 + level * 20;
        if (coins < cost) return;
        coins -= cost;
        if (type == 0) speedLevel++; else if (type == 1) glideLevel++; else controlLevel++;
        saveProgress();
    }

    private void updateControls() {
        if (!launched && sled.getPosition().x > 52.1f) launched = true;
        touchLeft = false;
        touchRight = false;
        boolean left = Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A);
        boolean right = Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D);
        if (Gdx.input.isTouched()) {
            touchLeft = Gdx.input.getX() < Gdx.graphics.getWidth() / 2;
            touchRight = !touchLeft;
            left |= touchLeft;
            right |= touchRight;
        }
        float torque = launched && !touchedLanding
            ? BASE_AIR_PITCH_TORQUE + controlLevel * 6f
            : GROUND_PITCH_TORQUE;
        if (left) sled.applyTorque(torque, true);
        if (right) sled.applyTorque(-torque, true);
    }

    private void guaranteeRampSpeed() {
        if (launched) return;
        float x = sled.getPosition().x;
        if (x > 30f && x < 52.4f) {
            Vector2 v = sled.getLinearVelocity();
            float target = 16f + speedLevel * 1.4f;
            // Explicitly clamp forward velocity on the climb: the base sled must always reach the lip.
            if (v.x < target) sled.setLinearVelocity(target, v.y);
        }
    }

    private void applyGlideAssist() {
        if (!launched || touchedLanding) return;
        if (glideLevel > 0 && sled.getLinearVelocity().y < 1f)
            sled.applyForceToCenter(0f, glideLevel * 1.6f, true);
    }

    private void stepPhysics(float delta) {
        accumulator += delta;
        while (accumulator >= STEP) {
            world.step(STEP, 8, 3);
            accumulator -= STEP;
        }
    }

    private void updateRunState(float delta) {
        runTime += delta;
        if (touchedLanding) {
            landingTimer += delta;
            if (landingTimer >= 1.35f) finishRun();
            return;
        }
        float speed = sled.getLinearVelocity().len();
        if (runTime > 2f && speed < FINISH_SPEED) stationaryTime += delta; else stationaryTime = 0f;
        if (stationaryTime >= FINISH_DELAY || sled.getPosition().y < -12f) {
            if (outcome == Outcome.NONE) outcome = Outcome.CRASH;
            finishRun();
        }
    }

    private void finishRun() {
        finalDistance = Math.max(0f, sled.getPosition().x - startX);
        bestDistance = Math.max(bestDistance, finalDistance);
        lastReward = Math.max(5, Math.round(finalDistance * .35f)) + (outcome == Outcome.CLEAN ? 15 : 0);
        coins += lastReward;
        saveProgress();
        state = RunState.FINISHED;
    }

    private void updateCamera() {
        Vector2 p = sled.getPosition();
        camera.position.x += (p.x + 5f - camera.position.x) * .08f;
        float targetY = launched ? Math.max(10f, p.y + 1.5f) : Math.max(8f, p.y + 1f);
        camera.position.y += (targetY - camera.position.y) * .06f;
        camera.update();
    }

    private void drawWorld() {
        Gdx.gl.glClearColor(.42f, .72f, .95f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        shapes.setProjectionMatrix(camera.combined);
        shapes.begin(ShapeRenderer.ShapeType.Filled);
        shapes.setColor(.23f, .55f, .25f, 1f);
        drawTerrainFill(RUN_UP_AND_RAMP); drawTerrainFill(LANDING);

        Vector2 lip = RUN_UP_AND_RAMP[RUN_UP_AND_RAMP.length - 1];
        shapes.setColor(.95f, .78f, .18f, 1f);
        shapes.rect(lip.x - .35f, lip.y - .2f, .7f, .4f);

        Vector2 p = sled.getPosition();
        float angle = sled.getAngle() * MathUtils.radiansToDegrees;
        shapes.setColor(.9f, .22f, .12f, 1f);
        shapes.rect(p.x - 1.4f, p.y - .28f, 1.4f, .28f, 2.8f, .56f, 1f, 1f, angle);
        shapes.setColor(Color.DARK_GRAY);
        shapes.circle(p.x - .8f, p.y - .42f, .22f, 12); shapes.circle(p.x + .8f, p.y - .42f, .22f, 12);

        // Strong visual feedback while a phone side is held.
        if (state == RunState.RUNNING && (touchLeft || touchRight)) {
            shapes.setColor(1f, 1f, 1f, .55f);
            float markerX = p.x + (touchLeft ? -2.4f : 2.4f);
            shapes.circle(markerX, p.y + 1.8f, .55f, 18);
        }

        if (state == RunState.FINISHED) {
            shapes.setColor(outcome == Outcome.CLEAN ? new Color(.35f,.95f,.45f,.9f) : new Color(.95f,.25f,.2f,.9f));
            shapes.circle(p.x, p.y + 3f, 1.25f, 24);
        }
        shapes.end();

        shapes.begin(ShapeRenderer.ShapeType.Line);
        shapes.setColor(Color.WHITE);
        drawTerrainLine(RUN_UP_AND_RAMP); drawTerrainLine(LANDING);
        shapes.end();
    }

    private void drawTerrainFill(Vector2[] points) {
        for (int i = 0; i < points.length - 1; i++) {
            Vector2 a = points[i], b = points[i + 1];
            shapes.triangle(a.x,a.y,b.x,b.y,a.x,-20f); shapes.triangle(b.x,b.y,b.x,-20f,a.x,-20f);
        }
    }

    private void drawTerrainLine(Vector2[] points) {
        for (int i = 0; i < points.length - 1; i++) shapes.line(points[i], points[i + 1]);
    }

    private void updateTitle() {
        float distance = state == RunState.FINISHED ? finalDistance : Math.max(0f, sled.getPosition().x - startX);
        String status = state == RunState.FINISHED
            ? " | +" + lastReward + " coins | Coins " + coins + " | S" + speedLevel + " G" + glideLevel + " C" + controlLevel
            : (launched ? " | AIRBORNE - HOLD LEFT/RIGHT TO ROTATE" : " | BUILDING SPEED");
        Gdx.graphics.setTitle("Fly Like an Eagle | " + Math.round(distance) + " m | Best " + Math.round(bestDistance) + " m" + status);
    }

    @Override public void resize(int width, int height) { viewport.update(width, height, false); }
    @Override public void dispose() { shapes.dispose(); world.dispose(); }
}
