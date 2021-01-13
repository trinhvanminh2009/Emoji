package com.example.emoji.emojiworld;

import android.util.Log;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class EmojiWorld {
    public State state;
    private final World world;
    private Disposable simulationDisposable;
    private float worldWidth;
    private float worldHeight;
    private final Listener listener;
    private static final float TARGET_FPS = 60.0F;
    private static final float TIME_STEP = 1.0F / TARGET_FPS;
    private static final long TIME_STEP_MS = 16L;
    private static final int VELOCITY_ITERATIONS = 2;
    private static final int POSITION_ITERATIONS = 2;
    private static final float DENSITY = 0.5F;
    private static final float FRICTION = 0.5F;
    private static final float RESTITUTION = 0.0F;
    private static final float INITIAL_IMPULSE_X = 0.05F;
    private static final float INITIAL_IMPULSE_Y = 0.05F;
    private static final float DEGTORAD = 0.0174532925199432957f;
    public EmojiWorld(Listener listener) {
        this.world = new World(new Vec2(0.0F, 0.0F), true);
        this.listener = listener;
        this.state = State.IDLE;
    }

    public final void create(int viewWidth, int viewHeight) {
        this.worldWidth = Metrics.pixelsToMeters((float) viewWidth);
        this.worldHeight = Metrics.pixelsToMeters((float) viewHeight);
        this.stopSimulation();
        this.destroyWorld();
        this.createWorldBoundaries();
        this.state = State.READY;
    }

    public final void createEmoji(Emoji emoji) {
        Log.e("createEmoji", emoji.getPlayerId() + "");
        this.createBody(emoji);
    }

    public void stopSimulation() {
        Log.d(EmojiWorld.class.getSimpleName(), "Stop simulation");
        Disposable disposable = this.simulationDisposable;
        if (disposable != null) {
            disposable.dispose();
        }
        this.state = State.READY;
    }

    public final void startSimulation() {
        this.stopSimulation();
        this.simulationDisposable = Observable.interval(TIME_STEP_MS, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers
                        .mainThread()).subscribe((Consumer) var1 -> {
                    EmojiWorld.this.update();
                });
        this.state = State.SIMULATING;
    }

    public void destroyWorld() {
        for (Body body = this.world.getBodyList(); body != null; body = body.getNext()) {
            this.world.destroyBody(body);
        }
    }

//    private Body createBody(Emoji emoji){
//        BodyDef bodyDef = new BodyDef();
//        bodyDef.type = BodyType.DYNAMIC;
//        bodyDef.position.set(0, 0);
//
//        CircleShape shape = new CircleShape();
//        shape.m_radius = 10.0f;
//        FixtureDef fixtureDef = new FixtureDef();
//        fixtureDef.shape = shape;
//        fixtureDef.density = 1.0f;
//        fixtureDef.restitution = 0.5f;
//
//
//        Body body = this.world.createBody(bodyDef);
//        body.createFixture(fixtureDef);
//        body.setUserData(emoji);
//        body.m_mass = 0.1f;
//        body.setTransform(new Vec2(10,20), 45 * DEGTORAD); //45 degrees clockwise.
//        body.setLinearVelocity(new Vec2(-5.0f, -5.0f));
//
//        body.applyForce(new Vec2(1, 1),body.getPosition());
//        return body;
//    }


    private Body createBody(Emoji emoji) {

        float startingX = (float) (Math.random() * ((double) this.worldWidth - 0.1));
        float startingY = (float) (Math.random() * (double) this.worldHeight);
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyType.DYNAMIC;
        Log.e("e", startingX +  " "+ startingY + " "+emoji.getPlayerId()  + " worldWidth: "+ this.worldWidth);
        if(emoji.getPlayerId() == 0){
            bodyDef.position.set(startingX, -0.01F);
        }else{
            bodyDef.position.set(startingX, 1F);
        }
        bodyDef.angle = 100;
        CircleShape shape = new CircleShape();
        shape.m_radius = Metrics.pixelsToMeters(100);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 0.9F;
        fixtureDef.friction = FRICTION;
        fixtureDef.restitution = 0F;
        Body body = this.world.createBody(bodyDef);
        body.createFixture(fixtureDef);
        body.m_mass = 0.01F;
        //body.setTransform(new Vec2(0.1F,-0.9F), 90 *DEGTORAD);

        body.setUserData(emoji);
      //  body.setLinearVelocity(new Vec2(0.1F,0.9F));
        Vec2 impulse;
        if(emoji.getPlayerId() == 0){
            impulse = new Vec2(0.01F, 0.99F);
            //body.setTransform(new Vec2(0,0), 0);
            body.applyForce(impulse, body.getPosition());
        }else{
            impulse = new Vec2(startingX, 1F);
           // body.applyForce(impulse, body.getWorldCenter());
           // body.setTransform(new Vec2(1,1), 90);
        }
        body.applyForce(impulse, body.getPosition());
        return body;
    }

    private void createWorldBoundaries() {
        BodyDef boundariesBodyDef = new BodyDef();
        boundariesBodyDef.type = BodyType.STATIC;
        Vec2 topLeft = new Vec2(0.0F, -0.3F);
        Vec2 topRight = new Vec2(this.worldWidth, 0.0F);
        Vec2 bottomLeft = new Vec2(0.0F, this.worldHeight);
        Vec2 bottomRight = new Vec2(this.worldWidth, this.worldHeight);
        PolygonShape leftShape = new PolygonShape();
        PolygonShape rightShape = new PolygonShape();
        PolygonShape topShape = new PolygonShape();
        PolygonShape bottomShape = new PolygonShape();
        leftShape.setAsEdge(topLeft, bottomLeft);
        rightShape.setAsEdge(topRight, bottomRight);
        topShape.setAsEdge(topLeft, topRight);
        bottomShape.setAsEdge(bottomLeft, bottomRight);
        FixtureDef leftFixture = new FixtureDef();
        leftFixture.shape = leftShape;
        leftFixture.restitution = RESTITUTION;
        leftFixture.density = DENSITY;
        leftFixture.friction = FRICTION;
        FixtureDef rightFixture = new FixtureDef();
        rightFixture.shape = rightShape;
        rightFixture.restitution = RESTITUTION;
        rightFixture.density = DENSITY;
        rightFixture.friction = FRICTION;
        FixtureDef topFixture = new FixtureDef();
        topFixture.shape = topShape;
        topFixture.restitution = RESTITUTION;
        topFixture.density = DENSITY;
        topFixture.friction = FRICTION;
        FixtureDef bottomFixture = new FixtureDef();
        bottomFixture.shape = bottomShape;
        bottomFixture.restitution = RESTITUTION;
        bottomFixture.density = DENSITY;
        bottomFixture.friction = FRICTION;
        Body boundariesBody = this.world.createBody(boundariesBodyDef);
        boundariesBody.createFixture(leftFixture);
        boundariesBody.createFixture(rightFixture);
        boundariesBody.createFixture(topFixture);
        boundariesBody.createFixture(bottomFixture);
    }

    /**
     * While the simulation is running, this method is called at constant intervals.
     */
    private void update() {
        this.world.step(TIME_STEP, VELOCITY_ITERATIONS, POSITION_ITERATIONS);
        for (Body body = this.world.getBodyList(); body != null; body = body.getNext()) {
            Object emoji = body.getUserData();
            if (emoji instanceof Emoji) {
                ((Emoji) emoji).setViewX(Metrics.metersToPixels(body.getPosition().x) - (float) (((Emoji) emoji).getViewSize() / 2));
                ((Emoji) emoji).setViewY(Metrics.metersToPixels(body.getPosition().y) - (float) (((Emoji) emoji).getViewSize() / 2));
                this.listener.onSimulationUpdate((Emoji) emoji);
            }
        }

    }

    public enum State {
        /**
         * The 2d world is not yet prepared. Call [create] before any other interaction with this instance.
         */
        IDLE,
        /**
         * The world is created and ready to start the simulation.
         */
        READY,

        /**
         * The world is simulating the movements of the bodies within it.
         */
        SIMULATING;
    }

    public interface Listener {
        void onSimulationUpdate( Emoji emoji);
    }

}
