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
    private static final float STEP=1f/60f, LAUNCH_X=52.5f, SETTLE_SPEED=.14f, SETTLE_SPIN=.14f, SETTLE_DELAY=.9f;
    private static final int MAX=5, TRAIL_POINTS=18;
    private static final Vector2[] RAMP={new Vector2(-20,15),new Vector2(-10,14),new Vector2(0,11),new Vector2(12,6),new Vector2(24,2.2f),new Vector2(32,1.4f),new Vector2(38,1.7f),new Vector2(43,2.5f),new Vector2(47,3.8f),new Vector2(50,5.2f),new Vector2(LAUNCH_X,6.8f)};
    private static final Vector2[] LAND={new Vector2(62,6.8f),new Vector2(70,5.2f),new Vector2(82,3),new Vector2(96,1.1f),new Vector2(112,0),new Vector2(1200,0)};
    private static final float[] PX={72,88,108,132,160}, PY={10,12,9,7,5};
    private enum RunState{RUNNING,FINISHED} private enum Outcome{NONE,CLEAN,CRASH}
    private World world; private Body sled; private OrthographicCamera camera; private ExtendViewport viewport; private ShapeRenderer shapes; private Preferences progress;
    private float accumulator,stationaryTime,bestDistance,touchdownDistance,finalDistance,trailTimer;
    private boolean launched,touchedLanding,touchLeft,touchRight; private int groundContacts,trailCount; private RunState state; private Outcome outcome;
    private int points,speedLevel,glideLevel,controlLevel,rampLevel,aeroLevel,bounceLevel,slideLevel,lastReward,pickupPoints;
    private final boolean[] pickupCollected=new boolean[PX.length]; private final float[] trailX=new float[TRAIL_POINTS],trailY=new float[TRAIL_POINTS];

    @Override public void create(){world=new World(new Vector2(0,-7.4f),true);camera=new OrthographicCamera();viewport=new ExtendViewport(32,18,camera);shapes=new ShapeRenderer();progress=Gdx.app.getPreferences("fly-like-an-eagle-progress");load();terrain();contacts();startRun();}
    private void load(){points=progress.getInteger("points",progress.getInteger("coins",0));speedLevel=progress.getInteger("speed",0);glideLevel=progress.getInteger("glide",0);controlLevel=progress.getInteger("control",0);rampLevel=progress.getInteger("ramp",0);aeroLevel=progress.getInteger("aero",0);bounceLevel=progress.getInteger("bounce",0);slideLevel=progress.getInteger("slide",0);bestDistance=progress.getFloat("bestLanding",progress.getFloat("best",0));}
    private void save(){progress.putInteger("points",points).putInteger("speed",speedLevel).putInteger("glide",glideLevel).putInteger("control",controlLevel).putInteger("ramp",rampLevel).putInteger("aero",aeroLevel).putInteger("bounce",bounceLevel).putInteger("slide",slideLevel).putFloat("bestLanding",bestDistance).flush();}
    private void terrain(){chain(RAMP,"ramp");chain(LAND,"landing");}
    private void chain(Vector2[] v,String tag){Body b=world.createBody(new BodyDef());ChainShape s=new ChainShape();s.createChain(v);Fixture f=b.createFixture(s,0);f.setFriction(.015f);f.setUserData(tag);s.dispose();}
    private boolean pair(Fixture a,Fixture b,String x,String y){Object au=a.getUserData(),bu=b.getUserData();return(x.equals(au)&&y.equals(bu))||(x.equals(bu)&&y.equals(au));}
    private void contacts(){world.setContactListener(new ContactListener(){public void beginContact(Contact c){Fixture a=c.getFixtureA(),b=c.getFixtureB();if(pair(a,b,"sled","landing")&&launched){groundContacts++;stationaryTime=0;if(!touchedLanding){touchedLanding=true;touchdownDistance=Math.max(0,sled.getPosition().x-LAUNCH_X);outcome=landing();}}}public void endContact(Contact c){if(pair(c.getFixtureA(),c.getFixtureB(),"sled","landing")&&launched){groundContacts=Math.max(0,groundContacts-1);stationaryTime=0;}}public void preSolve(Contact c,Manifold m){}public void postSolve(Contact c,ContactImpulse i){}});}
    private Outcome landing(){float deg=norm(sled.getAngle()*MathUtils.radiansToDegrees),err=Math.abs(norm(deg+17));return err<=38&&Math.abs(sled.getAngularVelocity())<=6.5f&&-sled.getLinearVelocity().y<=20?Outcome.CLEAN:Outcome.CRASH;}
    private float norm(float v){while(v>180)v-=360;while(v<-180)v+=360;return v;}

    private void startRun(){if(sled!=null)world.destroyBody(sled);BodyDef d=new BodyDef();d.type=BodyDef.BodyType.DynamicBody;d.position.set(-14,16.2f);d.angle=-.08f;d.angularDamping=.12f+aeroLevel*.035f;sled=world.createBody(d);PolygonShape s=new PolygonShape();s.setAsBox(1.4f,.28f);FixtureDef fd=new FixtureDef();fd.shape=s;fd.density=1.2f;fd.friction=Math.max(.0005f,.005f-slideLevel*.0009f);fd.restitution=.03f+bounceLevel*.07f;Fixture f=sled.createFixture(fd);f.setUserData("sled");s.dispose();sled.setLinearVelocity(9+speedLevel*1.2f,0);accumulator=stationaryTime=touchdownDistance=finalDistance=trailTimer=0;trailCount=groundContacts=lastReward=pickupPoints=0;launched=touchedLanding=touchLeft=touchRight=false;outcome=Outcome.NONE;state=RunState.RUNNING;for(int i=0;i<pickupCollected.length;i++)pickupCollected[i]=false;camera.position.set(-9,10,0);camera.update();}
    @Override public void render(){float dt=Math.min(Gdx.graphics.getDeltaTime(),.25f);if(state==RunState.FINISHED){shopInput();if(Gdx.input.isKeyJustPressed(Input.Keys.R)||Gdx.input.isKeyJustPressed(Input.Keys.SPACE))startRun();}else{controls();rampAssist();airAssist();step(dt);trail(dt);pickups();runState(dt);}camera();draw();title();}

    private void shopInput(){if(!Gdx.input.justTouched())return;float x=Gdx.input.getX()/(float)Gdx.graphics.getWidth(),y=Gdx.input.getY()/(float)Gdx.graphics.getHeight();if(y>.72f){startRun();return;}int col=Math.min(3,(int)(x*4));int row=y<.36f?0:1;int type=row*4+col;if(type<7)buy(type);}
    private int level(int t){switch(t){case 0:return speedLevel;case 1:return glideLevel;case 2:return controlLevel;case 3:return rampLevel;case 4:return aeroLevel;case 5:return bounceLevel;default:return slideLevel;}}
    private void buy(int t){int l=level(t);if(l>=MAX)return;int cost=cost(l);if(points<cost)return;points-=cost;switch(t){case 0:speedLevel++;break;case 1:glideLevel++;break;case 2:controlLevel++;break;case 3:rampLevel++;break;case 4:aeroLevel++;break;case 5:bounceLevel++;break;case 6:slideLevel++;break;}save();}
    private int cost(int l){return 40+l*50;}
    private void controls(){if(!launched&&sled.getPosition().x>52.1f)launched=true;touchLeft=touchRight=false;boolean left=Gdx.input.isKeyPressed(Input.Keys.LEFT)||Gdx.input.isKeyPressed(Input.Keys.A),right=Gdx.input.isKeyPressed(Input.Keys.RIGHT)||Gdx.input.isKeyPressed(Input.Keys.D);if(Gdx.input.isTouched()){touchLeft=Gdx.input.getX()<Gdx.graphics.getWidth()/2;touchRight=!touchLeft;left|=touchLeft;right|=touchRight;}float torque=launched&&groundContacts==0?48+controlLevel*6:22;if(left)sled.applyTorque(torque,true);if(right)sled.applyTorque(-torque,true);}
    private void rampAssist(){if(launched)return;float x=sled.getPosition().x;if(x>30&&x<52.4f){Vector2 v=sled.getLinearVelocity();float target=16+speedLevel*1.4f+rampLevel*1.8f;if(v.x<target)sled.setLinearVelocity(target,v.y);}}
    private void airAssist(){if(!launched||groundContacts>0)return;Vector2 v=sled.getLinearVelocity();if(glideLevel>0&&v.y<1)sled.applyForceToCenter(0,glideLevel*1.6f,true);if(aeroLevel>0){sled.applyForceToCenter(aeroLevel*.32f,Math.max(0,v.x)*aeroLevel*.055f,true);}}
    private void step(float dt){accumulator+=dt;while(accumulator>=STEP){world.step(STEP,8,3);accumulator-=STEP;}}
    private void trail(float dt){if(!launched||groundContacts>0)return;trailTimer+=dt;if(trailTimer<.08f)return;trailTimer=0;for(int i=Math.min(trailCount,TRAIL_POINTS-1);i>0;i--){trailX[i]=trailX[i-1];trailY[i]=trailY[i-1];}trailX[0]=sled.getPosition().x;trailY[0]=sled.getPosition().y;if(trailCount<TRAIL_POINTS)trailCount++;}
    private void pickups(){if(!launched||groundContacts>0)return;Vector2 p=sled.getPosition();for(int i=0;i<PX.length;i++){if(pickupCollected[i])continue;float dx=p.x-PX[i],dy=p.y-PY[i];if(dx*dx+dy*dy<3.2f){pickupCollected[i]=true;pickupPoints+=5;}}}
    private void runState(float dt){if(groundContacts<=0){stationaryTime=0;return;}float speed=sled.getLinearVelocity().len(),spin=Math.abs(sled.getAngularVelocity());if(speed<SETTLE_SPEED&&spin<SETTLE_SPIN)stationaryTime+=dt;else stationaryTime=0;if(stationaryTime>=SETTLE_DELAY)finish();}
    private void finish(){finalDistance=Math.max(0,sled.getPosition().x-LAUNCH_X);bestDistance=Math.max(bestDistance,finalDistance);lastReward=Math.round(finalDistance)+pickupPoints+(outcome==Outcome.CLEAN?20:0);points+=lastReward;save();state=RunState.FINISHED;}
    private float distance(){return launched?Math.max(0,sled.getPosition().x-LAUNCH_X):0;}
    private void camera(){Vector2 p=sled.getPosition();camera.position.x+=(p.x+5-camera.position.x)*.08f;float y=launched?Math.max(10,p.y+1.5f):Math.max(8,p.y+1);camera.position.y+=(y-camera.position.y)*.06f;camera.update();}

    private void draw(){Gdx.gl.glClearColor(.42f,.72f,.95f,1);Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);shapes.setProjectionMatrix(camera.combined);shapes.begin(ShapeRenderer.ShapeType.Filled);shapes.setColor(1,.9f,.35f,1);shapes.circle(22,22,2.2f,28);cloud(-2,18,1.2f);cloud(36,20,.9f);cloud(88,17,1.1f);cloud(140,19,1);cloud(205,16,1.15f);shapes.setColor(.23f,.55f,.25f,1);fill(RAMP);fill(LAND);shapes.setColor(.95f,.78f,.18f,1);shapes.rect(LAUNCH_X-.35f,6.6f,.7f,.4f);for(float x=72;x<=1120;x+=40)post(x,x<112?1:.3f);for(int i=0;i<PX.length;i++)if(!pickupCollected[i]){shapes.setColor(1,.78f,.12f,1);shapes.circle(PX[i],PY[i],.48f,18);shapes.setColor(1,.94f,.45f,1);shapes.circle(PX[i],PY[i],.22f,14);}if(launched)for(int i=trailCount-1;i>=0;i--){float z=.08f+(trailCount-i)*.012f;shapes.setColor(1,1,1,.18f+(trailCount-i)*.018f);shapes.circle(trailX[i],trailY[i],z,10);}Vector2 p=sled.getPosition();float a=sled.getAngle()*MathUtils.radiansToDegrees;shapes.setColor(.9f,.22f,.12f,1);shapes.rect(p.x-1.4f,p.y-.28f,1.4f,.28f,2.8f,.56f,1,1,a);shapes.setColor(Color.DARK_GRAY);shapes.circle(p.x-.8f,p.y-.42f,.22f,12);shapes.circle(p.x+.8f,p.y-.42f,.22f,12);if(state==RunState.RUNNING&&(touchLeft||touchRight)){shapes.setColor(1,1,1,.6f);shapes.circle(p.x+(touchLeft?-2.4f:2.4f),p.y+1.8f,.55f,18);}if(state==RunState.FINISHED){shapes.setColor(outcome==Outcome.CLEAN?new Color(.35f,.95f,.45f,.9f):new Color(.95f,.25f,.2f,.9f));shapes.circle(p.x,p.y+3,1.25f,24);}shapes.end();shapes.begin(ShapeRenderer.ShapeType.Line);shapes.setColor(Color.WHITE);line(RAMP);line(LAND);shapes.end();}
    private void cloud(float x,float y,float s){shapes.setColor(1,1,1,.72f);shapes.circle(x,y,1.15f*s,18);shapes.circle(x+1.1f*s,y+.18f*s,.9f*s,18);shapes.circle(x-1*s,y+.12f*s,.78f*s,18);}
    private void post(float x,float y){shapes.setColor(1,1,1,.75f);shapes.rect(x-.08f,y,.16f,1.8f);shapes.setColor(.95f,.45f,.15f,.9f);shapes.triangle(x,y+1.8f,x,y+2.6f,x+.75f,y+2.2f);}
    private void fill(Vector2[] p){for(int i=0;i<p.length-1;i++){Vector2 a=p[i],b=p[i+1];shapes.triangle(a.x,a.y,b.x,b.y,a.x,-20);shapes.triangle(b.x,b.y,b.x,-20,a.x,-20);}}
    private void line(Vector2[] p){for(int i=0;i<p.length-1;i++)shapes.line(p[i],p[i+1]);}
    private String token(int l){return l>=MAX?l+"/MAX":l+"/"+cost(l);}
    private void title(){String st=state==RunState.FINISHED?"RESULT":launched?(groundContacts>0?"GROUND":"AIR"):"RAMP";int land=state==RunState.FINISHED?Math.round(finalDistance):(touchedLanding?Math.round(touchdownDistance):-1);Gdx.graphics.setTitle("FLYEAGLE|DIST="+Math.round(distance())+"|LAND="+land+"|BEST="+Math.round(bestDistance)+"|POINTS="+points+"|REWARD="+lastReward+"|PICKUPS="+pickupPoints+"|SPEED="+token(speedLevel)+"|GLIDE="+token(glideLevel)+"|CONTROL="+token(controlLevel)+"|RAMP="+token(rampLevel)+"|AERO="+token(aeroLevel)+"|BOUNCE="+token(bounceLevel)+"|SLIDE="+token(slideLevel)+"|OUTCOME="+outcome.name()+"|STATE="+st);}
    @Override public void resize(int w,int h){viewport.update(w,h,false);}@Override public void dispose(){shapes.dispose();world.dispose();}
}
