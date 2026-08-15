package com.jeltedeproft.flylikeaneagle;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

public class FlyLikeAnEagle extends ApplicationAdapter {
    private static final float STEP = 1f / 60f;
    private static final float PITCH_TORQUE = 18f;
    private static final float FINISH_SPEED = 0.7f;
    private static final float FINISH_DELAY = 1.5f;

    // One clear run-up into one oversized launch ramp. The landing is a separate
    // body so there is a real air gap after the lip instead of a downhill surface.
    private static final Vector2[] RUN_UP_AND_RAMP = {
        new Vector2(-20f, 14f),
        new Vector2(-10f, 13f),
        new Vector2(0f, 10f),
        new Vector2(12f, 5f),
        new Vector2(22f, 1.8f),
        new Vector2(28f, 1.2f),
        new Vector2(32f, 1.6f),
        new Vector2(36f, 2.8f),
        new Vector2(40f, 5.0f),
        new Vector2(43.5f, 8.2f),
        new Vector2(46.5f, 11.8f),
        new Vector2(49f, 14.2f)
    };

    private static final Vector2[] LANDING = {
        new Vector2(64f, 8.5f),
        new Vector2(72f, 5.2f),
        new Vector2(84f, 2.3f),
        new Vector2(100f, 0f),
        new Vector2(220f, 0f)
    };

    private enum RunState { RUNNING, FINISHED }

    private World world;
    private Body sled;
    private OrthographicCamera camera;
    private ExtendViewport viewport;
    private ShapeRenderer shapes;

    private float accumulator;
    private float startX;
    private float stationaryTime;
    private float runTime;
    private float bestDistance;
    private float finalDistance;
    private RunState state;

    @Override
    public void create() {
        world = new World(new Vector2(0f, -9.81f), true);
        camera = new OrthographicCamera();
        viewport = new ExtendViewport(32f, 18f, camera);
        shapes = new ShapeRenderer();
        createTerrain();
        startRun();
    }

    private void createTerrain() {
        createTerrainChain(RUN_UP_AND_RAMP);
        createTerrainChain(LANDING);
    }

    private void createTerrainChain(Vector2[] points) {
        Body terrain = world.createBody(new BodyDef());
        ChainShape shape = new ChainShape();
        shape.createChain(points);
        terrain.createFixture(shape, 0f).setFriction(0.08f);
        shape.dispose();
    }

    private void startRun() {
        if (sled != null) world.destroyBody(sled);

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(-14f, 15.2f);
        bodyDef.angle = -0.08f;
        sled = world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(1.4f, 0.28f);
        FixtureDef fixture = new FixtureDef();
        fixture.shape = shape;
        fixture.density = 1.2f;
        fixture.friction = 0.04f;
        fixture.restitution = 0.05f;
        sled.createFixture(fixture);
        shape.dispose();

        sled.setLinearVelocity(5f, 0f);
        startX = sled.getPosition().x;
        stationaryTime = 0f;
        runTime = 0f;
        accumulator = 0f;
        finalDistance = 0f;
        state = RunState.RUNNING;

        camera.position.set(-9f, 10f, 0f);
        camera.update();
    }

    @Override
    public void render() {
        float delta = Math.min(Gdx.graphics.getDeltaTime(), 0.25f);

        if (state == RunState.FINISHED) {
            if (Gdx.input.justTouched()
                || Gdx.input.isKeyJustPressed(Input.Keys.R)
                || Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
                startRun();
            }
        } else {
            updateControls();
            stepPhysics(delta);
            updateRunState(delta);
        }

        updateCamera();
        drawWorld();
        updateTitle();
    }

    private void updateControls() {
        boolean pitchLeft = Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A);
        boolean pitchRight = Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D);

        if (Gdx.input.isTouched()) {
            if (Gdx.input.getX() < Gdx.graphics.getWidth() / 2) pitchLeft = true;
            else pitchRight = true;
        }

        if (pitchLeft) sled.applyTorque(PITCH_TORQUE, true);
        if (pitchRight) sled.applyTorque(-PITCH_TORQUE, true);
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
        float speed = sled.getLinearVelocity().len();

        if (runTime > 2f && speed < FINISH_SPEED) stationaryTime += delta;
        else stationaryTime = 0f;

        if (stationaryTime >= FINISH_DELAY || sled.getPosition().y < -12f) {
            finalDistance = Math.max(0f, sled.getPosition().x - startX);
            bestDistance = Math.max(bestDistance, finalDistance);
            state = RunState.FINISHED;
        }
    }

    private void updateCamera() {
        Vector2 position = sled.getPosition();
        camera.position.x += (position.x + 5f - camera.position.x) * 0.08f;
        camera.position.y += (Math.max(8f, position.y + 1f) - camera.position.y) * 0.06f;
        camera.update();
    }

    private void drawWorld() {
        Gdx.gl.glClearColor(0.42f, 0.72f, 0.95f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        shapes.setProjectionMatrix(camera.combined);
        shapes.begin(ShapeRenderer.ShapeType.Filled);

        shapes.setColor(0.23f, 0.55f, 0.25f, 1f);
        drawTerrainFill(RUN_UP_AND_RAMP);
        drawTerrainFill(LANDING);

        // A bright lip makes the single big jump immediately readable on a phone.
        Vector2 lip = RUN_UP_AND_RAMP[RUN_UP_AND_RAMP.length - 1];
        shapes.setColor(0.95f, 0.78f, 0.18f, 1f);
        shapes.rect(lip.x - 0.35f, lip.y - 0.2f, 0.7f, 0.4f);

        Vector2 p = sled.getPosition();
        float angle = sled.getAngle() * MathUtils.radiansToDegrees;
        shapes.setColor(0.9f, 0.22f, 0.12f, 1f);
        shapes.rect(p.x - 1.4f, p.y - 0.28f, 1.4f, 0.28f,
            2.8f, 0.56f, 1f, 1f, angle);

        shapes.setColor(Color.DARK_GRAY);
        shapes.circle(p.x - 0.8f, p.y - 0.42f, 0.22f, 12);
        shapes.circle(p.x + 0.8f, p.y - 0.42f, 0.22f, 12);

        if (state == RunState.FINISHED) {
            shapes.setColor(1f, 1f, 1f, 0.8f);
            shapes.circle(p.x, p.y + 3f, 1.2f, 24);
            shapes.setColor(0.18f, 0.35f, 0.7f, 1f);
            shapes.triangle(p.x - 0.35f, p.y + 2.35f,
                p.x - 0.35f, p.y + 3.65f,
                p.x + 0.55f, p.y + 3f);
        }
        shapes.end();

        shapes.begin(ShapeRenderer.ShapeType.Line);
        shapes.setColor(Color.WHITE);
        drawTerrainLine(RUN_UP_AND_RAMP);
        drawTerrainLine(LANDING);
        shapes.end();
    }

    private void drawTerrainFill(Vector2[] points) {
        for (int i = 0; i < points.length - 1; i++) {
            Vector2 a = points[i];
            Vector2 b = points[i + 1];
            shapes.triangle(a.x, a.y, b.x, b.y, a.x, -20f);
            shapes.triangle(b.x, b.y, b.x, -20f, a.x, -20f);
        }
    }

    private void drawTerrainLine(Vector2[] points) {
        for (int i = 0; i < points.length - 1; i++) {
            shapes.line(points[i], points[i + 1]);
        }
    }

    private void updateTitle() {
        float distance = state == RunState.FINISHED
            ? finalDistance
            : Math.max(0f, sled.getPosition().x - startX);
        int speedTenths = Math.round(sled.getLinearVelocity().len() * 10f);
        String status = state == RunState.FINISHED ? " | TAP TO RETRY" : "";
        Gdx.graphics.setTitle("Fly Like an Eagle | " + Math.round(distance) + " m | "
            + (speedTenths / 10) + "." + (speedTenths % 10) + " m/s | Best "
            + Math.round(bestDistance) + " m" + status);
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, false);
    }

    @Override
    public void dispose() {
        shapes.dispose();
        world.dispose();
    }
}
