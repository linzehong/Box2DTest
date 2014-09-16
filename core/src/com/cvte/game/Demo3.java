package com.cvte.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.DistanceJoint;
import com.badlogic.gdx.physics.box2d.joints.DistanceJointDef;

public class Demo3 extends ApplicationAdapter implements InputProcessor {
	
    private static final float PXTM = 80;
	
	protected OrthographicCamera mCamera;
	private Box2DDebugRenderer mRenderer;
	
	private World mWorld;
	
	private Body mBodyA;
	private Body mBodyB;
	private DistanceJoint mDistanceJoint;
	
	@Override
	public void create() {
        float cameraWidth = Gdx.graphics.getWidth() / PXTM;
        float cameraHeight = Gdx.graphics.getHeight() / PXTM;
		mCamera = new OrthographicCamera(cameraWidth, cameraHeight);
		mCamera.position.set(cameraWidth / 2, cameraHeight / 2, 0);
		mCamera.update();
		
		mRenderer = new Box2DDebugRenderer();
		
		mWorld = new World(new Vector2(0, -10), true);
		
//		//ground
//		{
//			BodyDef bd = new BodyDef();
//			bd.type = BodyType.StaticBody;
//			bd.position.set(toWorldSize(0), toWorldSize(20));
//
//			EdgeShape edgeShape = new EdgeShape();
//			edgeShape.set(new Vector2(toWorldSize(0), toWorldSize(0)), new Vector2(toWorldSize(Gdx.graphics.getWidth()), toWorldSize(0)));
//
//			FixtureDef fd = new FixtureDef();
//			fd.shape = edgeShape;
//
//			Body ground = mWorld.createBody(bd);
//			ground.createFixture(fd);
//		}
		
		// wall
		{
			BodyDef bd = new BodyDef();
			bd.type = BodyType.StaticBody;

			EdgeShape edgeShape = new EdgeShape();

			FixtureDef fd = new FixtureDef();
			fd.shape = edgeShape;

			Body box = mWorld.createBody(bd);
			edgeShape.set(new Vector2(toWorldSize(0), toWorldSize(0)), new Vector2(toWorldSize(Gdx.graphics.getWidth()), toWorldSize(0)));
			box.createFixture(fd);
			edgeShape.set(new Vector2(toWorldSize(Gdx.graphics.getWidth()), toWorldSize(0)), new Vector2(toWorldSize(Gdx.graphics.getWidth()),
					toWorldSize(Gdx.graphics.getHeight())));
			box.createFixture(fd);
			edgeShape.set(new Vector2(toWorldSize(Gdx.graphics.getWidth()), toWorldSize(Gdx.graphics.getHeight())), new Vector2(0, toWorldSize(Gdx.graphics.getHeight())));
			box.createFixture(fd);
			edgeShape.set(new Vector2(toWorldSize(0), toWorldSize(Gdx.graphics.getHeight())), new Vector2(0, toWorldSize(0)));
			box.createFixture(fd);
		}
		
		//ball
		{
			BodyDef bd = new BodyDef();
			bd.type = BodyType.DynamicBody;
			bd.position.set(toWorldSize(400), toWorldSize(800));
			
			CircleShape circleShape = new CircleShape();
			circleShape.setRadius(toWorldSize(20));
			
			FixtureDef fd = new FixtureDef();
			fd.shape = circleShape;
			fd.density = 1;
			fd.restitution = 1;
			
			Body ball = mWorld.createBody(bd);
			ball.createFixture(fd);
		}
		
		createBodies();
		createDistanceJoint();
		
		Gdx.input.setInputProcessor(this);
	}

	@Override
	public void dispose() {
		mRenderer.dispose();
		
		mWorld.dispose();
		
		mCamera = null;
	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		mWorld.step(Gdx.app.getGraphics().getDeltaTime(), 6, 2);
		
		mRenderer.render(mWorld, mCamera.combined);
	}

	private float toWorldSize(float pos) {
		return (pos / PXTM);
	}
	
	/**
	 * 创建关节连接的两个刚体
	 */
	private void createBodies() {
		Vector2 posA = new Vector2(toWorldSize(100), toWorldSize(100));
		Vector2 posB = new Vector2(toWorldSize(200), toWorldSize(100));
		
		{
			BodyDef bd = new BodyDef();
			bd.type = BodyType.DynamicBody;
			bd.position.set(posA.x, posA.y);
			
			CircleShape circleShape = new CircleShape();
			circleShape.setRadius(toWorldSize(30));
			
			FixtureDef fd = new FixtureDef();
			fd.shape = circleShape;
			fd.density = 1;
			fd.friction = 0.5f;
			fd.restitution = 0;
			
			mBodyA = mWorld.createBody(bd);
			mBodyA.createFixture(fd);
		}
		
		{
			BodyDef bd = new BodyDef();
			bd.type = BodyType.DynamicBody;
			bd.position.set(posB.x, posB.y);
			
			CircleShape circleShape = new CircleShape();
			circleShape.setRadius(toWorldSize(15));
			
			FixtureDef fd = new FixtureDef();
			fd.shape = circleShape;
			fd.density = 1;
			fd.friction = 0.5f;
			fd.restitution = 0;
			
			mBodyB = mWorld.createBody(bd);
			mBodyB.createFixture(fd);
		}
	}
	
	/**
	 * 创建距离关节
	 */
	private void createDistanceJoint() {
		DistanceJointDef djd = new DistanceJointDef();
		djd.initialize(mBodyA, mBodyB, mBodyA.getPosition(), mBodyB.getPosition());
		djd.collideConnected = true;
		
		mDistanceJoint = (DistanceJoint)mWorld.createJoint(djd);
	}

	@Override
	public boolean keyDown(int keycode) {
		if (keycode == Keys.LEFT) {
			mBodyA.setAngularVelocity(2f);
		}
		else if (keycode == Keys.RIGHT) {
			mBodyA.setAngularVelocity(-2f);
		}
		
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}
	
}
