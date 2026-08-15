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
    private static final float LAUNCH_X = 52.5f;
    private static final float GROUND_PITCH_TORQUE = 22f;
    private static final float BASE_AIR_PITCH_TORQUE = 48f;
    private static final float SETTLE_SPEED = 0.14f;
    private static final float SETTLE_SPIN = 0.14f;
    private static final float SETTLE_DELAY = 0.9f;
    private static final float LANDING_SLOPE_DEGREES = -17f;
    private static final int MAX_UPGRADE_LEVEL = 5;
    private static final int TRAIL_POINTS = 18;

    private static final Vector2[] RUN_UP_AND_RAMP = {
        new Vector2(-20f,15f), new Vector2(-10f,14f), new Vector2(0f,11f),
        new Vector2(12f,6f), new Vector2(24f,2.2f), new Vector2(32f,1.4f),
        new Vector2(38f,1.7f), new Vector2(43f,2.5f), new Vector2(47f,3.8f),
        new Vector2(50f,5.2f), new Vector2(LAUNCH_X,6.8f)
    };

    private static final Vector2[] LANDING = {
        new Vector2(62f,6.8f), new Vector2(70f,5.2f), new Vector2(82f,3f),
        new Vector2(96f,1.1f), new Vector2(112f,0f), new Vector2(360f,0f)
    };

    private static final float[] PICKUP_X = {72f,88f,108f,132f,160f};
    private static final float[] PICKUP_Y = {10f,12f,9f,7f,5f};

    private enum RunState { RUNNING, FINISHED }
    private enum Outcome { NONE, CLEAN, CRASH }

    private World world;
    private Body sled;
    private OrthographicCamera camera;
    private ExtendViewport viewport;
    private ShapeRenderer shapes;
    private Preferences progress;

    private float accumulator;
    private float stationaryTime;
    private float bestDistance;
    private float touchdownDistance;
    private boolean launched;
    private boolean touchedLanding;
    private boolean touchLeft;
    private boolean touchRight;
    private int groundContacts;
    private RunState state;
    private Outcome outcome;

    private int points;
    private int speedLevel;
    private int glideLevel;
    private int controlLevel;
    private int lastReward;
    private int pickupPoints;
    private final boolean[] pickupCollected = new boolean[PICKUP_X.length];

    private final float[] trailX = new float[TRAIL_POINTS];
    private final float[] trailY = new float[TRAIL_POINTS];
    private int trailCount;
    private float trailTimer;

    @Override public void create() {
        world = new World(new Vector2(0f,-7.4f), true);
        camera = new OrthographicCamera();
        viewport = new ExtendViewport(32f,18f,camera);
        shapes = new ShapeRenderer();
        progress = Gdx.app.getPreferences("fly-like-an-eagle-progress");
        loadProgress();
        createTerrain();
        installContactListener();
        startRun();
    }

    private void loadProgress() {
        // Migrate the old coin value into points automatically.
        points = progress.getInteger("points", progress.getInteger("coins", 0));
        speedLevel = progress.getInteger("speed",0);
        glideLevel = progress.getInteger("glide",0);
        controlLevel = progress.getInteger("control",0);
        bestDistance = progress.getFloat("bestLanding", progress.getFloat("best",0f));
    }

    private void saveProgress() {
        progress.putInteger("points",points)
            .putInteger("speed",speedLevel)
            .putInteger("glide",glideLevel)
            .putInteger("control",controlLevel)
            .putFloat("bestLanding",bestDistance)
            .flush();
    }

    private void createTerrain() {
        createTerrainChain(RUN_UP_AND_RAMP,"ramp");
        createTerrainChain(LANDING,"landing");
    }

    private void createTerrainChain(Vector2[] vertices, String tag) {
        Body body = world.createBody(new BodyDef());
        ChainShape shape = new ChainShape();
        shape.createChain(vertices);
        Fixture fixture = body.createFixture(shape,0f);
        fixture.setFriction(0.015f);
        fixture.setUserData(tag);
        shape.dispose();
    }

    private void installContactListener() {
        world.setContactListener(new ContactListener() {
            @Override public void beginContact(Contact contact) {
                Fixture a = contact.getFixtureA(), b = contact.getFixtureB();
                if (isPair(a,b,"sled","landing") && launched) {
                    groundContacts++;
                    stationaryTime = 0f;
                    if (!touchedLanding) {
                        touchedLanding = true;
                        touchdownDistance = Math.max(0f, sled.getPosition().x - LAUNCH_X);
                        bestDistance = Math.max(bestDistance, touchdownDistance);
                        outcome = evaluateLanding();
                    }
                }
            }
            @Override public void endContact(Contact contact) {
                Fixture a = contact.getFixtureA(), b = contact.getFixtureB();
                if (isPair(a,b,"sled","landing") && launched) {
                    groundContacts = Math.max(0,groundContacts - 1);
                    stationaryTime = 0f;
                }
            }
            @Override public void preSolve(Contact c, Manifold m) { }
            @Override public void postSolve(Contact c, ContactImpulse i) { }
        });
    }

    private boolean isPair(Fixture a, Fixture b, String one, String two) {
        Object au = a.getUserData(), bu = b.getUserData();
        return (one.equals(au) && two.equals(bu)) || (one.equals(bu) && two.equals(au));
    }

    private Outcome evaluateLanding() {
        float degrees = normalizeDegrees(sled.getAngle() * MathUtils.radiansToDegrees);
        float error = Math.abs(normalizeDegrees(degrees - LANDING_SLOPE_DEGREES));
        return error <= 38f && Math.abs(sled.getAngularVelocity()) <= 6.5f
            && -sled.getLinearVelocity().y <= 20f ? Outcome.CLEAN : Outcome.CRASH;
    }

    private float normalizeDegrees(float value) {
        while (value > 180f) value -= 360f;
        while (value < -180f) value += 360f;
        return value;
    }

    private void startRun() {
        if (sled != null) world.destroyBody(sled);
        BodyDef def = new BodyDef();
        def.type = BodyDef.BodyType.DynamicBody;
        def.position.set(-14f,16.2f);
        def.angle = -0.08f;
        def.angularDamping = 0.12f;
        sled = world.createBody(def);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(1.4f,0.28f);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1.2f;
        fixtureDef.friction = 0.005f;
        fixtureDef.restitution = 0.03f;
        Fixture fixture = sled.createFixture(fixtureDef);
        fixture.setUserData("sled");
        shape.dispose();

        sled.setLinearVelocity(9f + speedLevel * 1.2f,0f);
        accumulator = stationaryTime = touchdownDistance = trailTimer = 0f;
        trailCount = groundContacts = lastReward = pickupPoints = 0;
        launched = touchedLanding = touchLeft = touchRight = false;
        outcome = Outcome.NONE;
        state = RunState.RUNNING;
        for (int i=0;i<pickupCollected.length;i++) pickupCollected[i] = false;
        camera.position.set(-9f,10f,0f);
        camera.update();
    }

    @Override public void render() {
        float delta = Math.min(Gdx.graphics.getDeltaTime(),0.25f);
        if (state == RunState.FINISHED) {
            handleShopInput();
            if (Gdx.input.isKeyJustPressed(Input.Keys.R) || Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) startRun();
        } else {
            updateControls();
            guaranteeRampSpeed();
            applyGlideAssist();
            stepPhysics(delta);
            updateTrail(delta);
            updatePickups();
            updateRunState(delta);
        }
        updateCamera();
        drawWorld();
        updateTitle();
    }

    private void handleShopInput() {
        if (!Gdx.input.justTouched()) return;
        float x = Gdx.input.getX() / (float)Gdx.graphics.getWidth();
        float y = Gdx.input.getY() / (float)Gdx.graphics.getHeight();
        if (y < 0.52f) {
            if (x < .333f) buyUpgrade(0);
            else if (x < .666f) buyUpgrade(1);
            else buyUpgrade(2);
        } else startRun();
    }

    private void buyUpgrade(int type) {
        int level = type == 0 ? speedLevel : type == 1 ? glideLevel : controlLevel;
        if (level >= MAX_UPGRADE_LEVEL) return;
        int cost = upgradeCost(level);
        if (points < cost) return;
        points -= cost;
        if (type == 0) speedLevel++;
        else if (type == 1) glideLevel++;
        else controlLevel++;
        saveProgress();
    }

    private int upgradeCost(int level) {
        return 40 + level * 50;
    }

    private void updateControls() {
        if (!launched && sled.getPosition().x > 52.1f) launched = true;
        touchLeft = touchRight = false;
        boolean left = Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A);
        boolean right = Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D);
        if (Gdx.input.isTouched()) {
            touchLeft = Gdx.input.getX() < Gdx.graphics.getWidth()/2;
            touchRight = !touchLeft;
            left |= touchLeft;
            right |= touchRight;
        }
        float torque = launched && groundContacts == 0
            ? BASE_AIR_PITCH_TORQUE + controlLevel * 6f : GROUND_PITCH_TORQUE;
        if (left) sled.applyTorque(torque,true);
        if (right) sled.applyTorque(-torque,true);
    }

    private void guaranteeRampSpeed() {
        if (launched) return;
        float x = sled.getPosition().x;
        if (x > 30f && x < 52.4f) {
            Vector2 velocity = sled.getLinearVelocity();
            float target = 16f + speedLevel * 1.4f;
            if (velocity.x < target) sled.setLinearVelocity(target,velocity.y);
        }
    }

    private void applyGlideAssist() {
        if (!launched || groundContacts > 0) return;
        if (glideLevel > 0 && sled.getLinearVelocity().y < 1f)
            sled.applyForceToCenter(0f,glideLevel * 1.6f,true);
    }

    private void stepPhysics(float delta) {
        accumulator += delta;
        while (accumulator >= STEP) {
            world.step(STEP,8,3);
            accumulator -= STEP;
        }
    }

    private void updateTrail(float delta) {
        if (!launched || groundContacts > 0) return;
        trailTimer += delta;
        if (trailTimer < .08f) return;
        trailTimer = 0f;
        for (int i=Math.min(trailCount,TRAIL_POINTS-1);i>0;i--) {
            trailX[i] = trailX[i-1]; trailY[i] = trailY[i-1];
        }
        trailX[0] = sled.getPosition().x; trailY[0] = sled.getPosition().y;
        if (trailCount < TRAIL_POINTS) trailCount++;
    }

    private void updatePickups() {
        if (!launched || groundContacts > 0) return;
        Vector2 p = sled.getPosition();
        for (int i=0;i<PICKUP_X.length;i++) {
            if (pickupCollected[i]) continue;
            float dx = p.x-PICKUP_X[i], dy = p.y-PICKUP_Y[i];
            if (dx*dx + dy*dy < 3.2f) {
                pickupCollected[i] = true;
                pickupPoints += 5;
            }
        }
    }

    private void updateRunState(float delta) {
        if (groundContacts <= 0) {
            stationaryTime = 0f;
            return;
        }
        float speed = sled.getLinearVelocity().len();
        float spin = Math.abs(sled.getAngularVelocity());
        if (speed < SETTLE_SPEED && spin < SETTLE_SPIN) stationaryTime += delta;
        else stationaryTime = 0f;
        if (stationaryTime >= SETTLE_DELAY) finishRun();
    }

    private void finishRun() {
        int distancePoints = Math.round(touchdownDistance);
        lastReward = distancePoints + pickupPoints + (outcome == Outcome.CLEAN ? 20 : 0);
        points += lastReward;
        saveProgress();
        state = RunState.FINISHED;
    }

    private float liveDistance() {
        if (!launched) return 0f;
        if (touchedLanding) return touchdownDistance;
        return Math.max(0f,sled.getPosition().x - LAUNCH_X);
    }

    private void updateCamera() {
        Vector2 p = sled.getPosition();
        camera.position.x += (p.x + 5f - camera.position.x) * .08f;
        float targetY = launched ? Math.max(10f,p.y+1.5f) : Math.max(8f,p.y+1f);
        camera.position.y += (targetY-camera.position.y) * .06f;
        camera.update();
    }

    private void drawWorld() {
        Gdx.gl.glClearColor(.42f,.72f,.95f,1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        shapes.setProjectionMatrix(camera.combined);
        shapes.begin(ShapeRenderer.ShapeType.Filled);

        shapes.setColor(1f,.9f,.35f,1f); shapes.circle(22f,22f,2.2f,28);
        drawCloud(-2f,18f,1.2f); drawCloud(36f,20f,.9f); drawCloud(88f,17f,1.1f);
        drawCloud(140f,19f,1f); drawCloud(205f,16f,1.15f);

        shapes.setColor(.23f,.55f,.25f,1f);
        drawTerrainFill(RUN_UP_AND_RAMP); drawTerrainFill(LANDING);

        shapes.setColor(.95f,.78f,.18f,1f);
        shapes.rect(LAUNCH_X-.35f,6.6f,.7f,.4f);

        for (float x=72f;x<=320f;x+=20f) drawDistancePost(x, x < 112f ? 1f : .3f);

        for (int i=0;i<PICKUP_X.length;i++) {
            if (pickupCollected[i]) continue;
            shapes.setColor(1f,.78f,.12f,1f); shapes.circle(PICKUP_X[i],PICKUP_Y[i],.48f,18);
            shapes.setColor(1f,.94f,.45f,1f); shapes.circle(PICKUP_X[i],PICKUP_Y[i],.22f,14);
        }

        if (launched) {
            for (int i=trailCount-1;i>=0;i--) {
                float size=.08f+(trailCount-i)*.012f;
                shapes.setColor(1f,1f,1f,.18f+(trailCount-i)*.018f);
                shapes.circle(trailX[i],trailY[i],size,10);
            }
        }

        Vector2 p = sled.getPosition();
        float angle = sled.getAngle()*MathUtils.radiansToDegrees;
        shapes.setColor(.9f,.22f,.12f,1f);
        shapes.rect(p.x-1.4f,p.y-.28f,1.4f,.28f,2.8f,.56f,1f,1f,angle);
        shapes.setColor(Color.DARK_GRAY);
        shapes.circle(p.x-.8f,p.y-.42f,.22f,12); shapes.circle(p.x+.8f,p.y-.42f,.22f,12);

        if (state==RunState.RUNNING && (touchLeft||touchRight)) {
            shapes.setColor(1f,1f,1f,.6f);
            float mx=p.x+(touchLeft?-2.4f:2.4f);
            shapes.circle(mx,p.y+1.8f,.55f,18);
        }
        if (state==RunState.FINISHED) {
            shapes.setColor(outcome==Outcome.CLEAN ? new Color(.35f,.95f,.45f,.9f) : new Color(.95f,.25f,.2f,.9f));
            shapes.circle(p.x,p.y+3f,1.25f,24);
        }
        shapes.end();

        shapes.begin(ShapeRenderer.ShapeType.Line);
        shapes.setColor(Color.WHITE);
        drawTerrainLine(RUN_UP_AND_RAMP); drawTerrainLine(LANDING);
        shapes.end();
    }

    private void drawCloud(float x,float y,float scale) {
        shapes.setColor(1f,1f,1f,.72f);
        shapes.circle(x,y,1.15f*scale,18); shapes.circle(x+1.1f*scale,y+.18f*scale,.9f*scale,18);
        shapes.circle(x-1f*scale,y+.12f*scale,.78f*scale,18);
    }

    private void drawDistancePost(float x,float y) {
        shapes.setColor(1f,1f,1f,.75f); shapes.rect(x-.08f,y,.16f,1.8f);
        shapes.setColor(.95f,.45f,.15f,.9f); shapes.triangle(x,y+1.8f,x,y+2.6f,x+.75f,y+2.2f);
    }

    private void drawTerrainFill(Vector2[] points) {
        for (int i=0;i<points.length-1;i++) {
            Vector2 a=points[i],b=points[i+1];
            shapes.triangle(a.x,a.y,b.x,b.y,a.x,-20f); shapes.triangle(b.x,b.y,b.x,-20f,a.x,-20f);
        }
    }

    private void drawTerrainLine(Vector2[] points) {
        for (int i=0;i<points.length-1;i++) shapes.line(points[i],points[i+1]);
    }

    private String upgradeToken(int level) {
        return level >= MAX_UPGRADE_LEVEL ? level + "/MAX" : level + "/" + upgradeCost(level);
    }

    private void updateTitle() {
        String stateToken = state==RunState.FINISHED ? "RESULT" : launched ? (groundContacts>0 ? "GROUND" : "AIR") : "RAMP";
        int land = touchedLanding ? Math.round(touchdownDistance) : -1;
        Gdx.graphics.setTitle("FLYEAGLE"
            + "|DIST=" + Math.round(liveDistance())
            + "|LAND=" + land
            + "|BEST=" + Math.round(bestDistance)
            + "|POINTS=" + points
            + "|REWARD=" + lastReward
            + "|PICKUPS=" + pickupPoints
            + "|SPEED=" + upgradeToken(speedLevel)
            + "|GLIDE=" + upgradeToken(glideLevel)
            + "|CONTROL=" + upgradeToken(controlLevel)
            + "|OUTCOME=" + outcome.name()
            + "|STATE=" + stateToken);
    }

    @Override public void resize(int width,int height) { viewport.update(width,height,false); }
    @Override public void dispose() { shapes.dispose(); world.dispose(); }
}
