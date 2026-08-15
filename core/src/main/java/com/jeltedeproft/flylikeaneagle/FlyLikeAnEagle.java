package com.jeltedeproft.flylikeaneagle;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

public class FlyLikeAnEagle extends ApplicationAdapter {
    private static final float STEP = 1f / 60f;
    private static final float PITCH_TORQUE = 18f;

    private World world;
    private Body sled;
    private OrthographicCamera camera;
    private ExtendViewport viewport;
    private Box2DDebugRenderer debugRenderer;
    private ShapeRenderer shapes;
    private float accumulator;
    private float startX;

    @Override
    public void create() {
        world = new World(new Vector2(0f, -9.81f), true);
        camera = new OrthographicCamera();
        viewport = new ExtendViewport(32f, 18f, camera);
        debugRenderer = new Box2DDebugRenderer();
        shapes = new ShapeRenderer();

        createTerrain();
        createSled();
    }

    private void createTerrain() {
        BodyDef bodyDef = new BodyDef();
        Body terrain = world.createBody(bodyDef);

        ChainShape shape = new ChainShape();
        Vector2[] points = {
            new Vector2(-20f, 14f),
            new Vector2(-10f, 13f),
            new Vector2(0f, 10f),
            new Vector2(12f, 5f),
            new Vector2(22f, 2f),
            new Vector2(29f, 2f),
            new Vector2(34f, 4f),
            new Vector2(38f, 7f),
            new Vector2(42f, 7f),
            new Vector2(60f, 0f),
            new Vector2(200f, 0f)
        };
        shape.createChain(points);
        terrain.createFixture(shape, 0f).setFriction(0.08f);
        shape.dispose();
    }

    private void createSled() {
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

        sled.setLinearVelocity(4f, 0f);
        startX = sled.getPosition().x;
    }

    @Override
    public void render() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.R)) createSled();

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A)) {
            sled.applyTorque(PITCH_TORQUE, true);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D)) {
            sled.applyTorque(-PITCH_TORQUE, true);
        }

        accumulator += Math.min(Gdx.graphics.getDeltaTime(), 0.25f);
        while (accumulator >= STEP) {
            world.step(STEP, 8, 3);
            accumulator -= STEP;
        }

        Vector2 position = sled.getPosition();
        camera.position.x += (position.x + 5f - camera.position.x) * 0.08f;
        camera.position.y += (Math.max(7f, position.y) - camera.position.y) * 0.06f;
        camera.update();

        Gdx.gl.glClearColor(0.08f, 0.11f, 0.16f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        shapes.setProjectionMatrix(camera.combined);
        shapes.begin(ShapeRenderer.ShapeType.Line);
        shapes.line(startX, 0.5f, position.x, 0.5f);
        shapes.end();

        debugRenderer.render(world, camera.combined);

        Gdx.graphics.setTitle(String.format(
            "Fly Like an Eagle | %.0f m | %.1f m/s | R reset | A/D pitch",
            Math.max(0f, position.x - startX), sled.getLinearVelocity().len()));
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, false);
    }

    @Override
    public void dispose() {
        shapes.dispose();
        debugRenderer.dispose();
        world.dispose();
    }
}
