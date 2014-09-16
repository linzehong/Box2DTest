package com.cvte.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

public class Demo2 extends ApplicationAdapter implements InputProcessor {
	
    private static final float PXTM = 80;
	
	protected OrthographicCamera mCamera;
	private Box2DDebugRenderer mRenderer;
	
	private World mWorld;
	
	private SpriteBatch mBatch;
	
	private Texture mTextureBall;
	
	private float mBallRadius;
	
	@Override
	public void create() {
        float cameraWidth = Gdx.graphics.getWidth() / PXTM;
        float cameraHeight = Gdx.graphics.getHeight() / PXTM;
		mCamera = new OrthographicCamera(cameraWidth, cameraHeight);
		mCamera.position.set(cameraWidth / 2, cameraHeight / 2, 0);
		mCamera.update();
		
		mRenderer = new Box2DDebugRenderer();
		
		mWorld = new World(new Vector2(0, 0), true);
		
		//wall
		{
			BodyDef bd = new BodyDef();
			bd.type = BodyType.StaticBody;
			
			EdgeShape edgeShape = new EdgeShape();
			
			FixtureDef fd = new FixtureDef();
			fd.shape = edgeShape;
			
			Body box = mWorld.createBody(bd);
			edgeShape.set(new Vector2(toWorldSize(0), toWorldSize(0)), new Vector2(toWorldSize(Gdx.graphics.getWidth()), toWorldSize(0)));
			box.createFixture(fd);
			edgeShape.set(new Vector2(toWorldSize(Gdx.graphics.getWidth()), toWorldSize(0)), new Vector2(toWorldSize(Gdx.graphics.getWidth()), toWorldSize(Gdx.graphics.getHeight())));
			box.createFixture(fd);
			edgeShape.set(new Vector2(toWorldSize(Gdx.graphics.getWidth()), toWorldSize(Gdx.graphics.getHeight())), new Vector2(0, toWorldSize(Gdx.graphics.getHeight())));
			box.createFixture(fd);
			edgeShape.set(new Vector2(toWorldSize(0), toWorldSize(Gdx.graphics.getHeight())), new Vector2(0, toWorldSize(0)));
			box.createFixture(fd);
		}
		
		mTextureBall = new Texture(Gdx.files.internal("coin1.png"));
		
		mBatch = new SpriteBatch();
		
		Gdx.input.setInputProcessor(this);
	}

	@Override
	public void dispose() {
		mCamera = null;
		
		mRenderer.dispose();
		mRenderer = null;
		
		mWorld.dispose();
		mWorld = null;
		
		mBatch = null;
		
		mTextureBall.dispose();
		mTextureBall = null;
	}
	
	@Override
	public void render() {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		mWorld.step(Gdx.app.getGraphics().getDeltaTime(), 6, 2);
		
		mBatch.setProjectionMatrix(mCamera.combined);
		mBatch.begin();
		
		Array<Body> bodies = new Array<Body>();
		mWorld.getBodies(bodies);
		for (Body b : bodies) {
			Ball ball = (Ball)b.getUserData();
			if (ball != null) {
				ball.setPosition(b.getPosition().x, b.getPosition().y);
				ball.setRotation(MathUtils.radiansToDegrees * b.getAngle());
				
				ball.draw(mBatch);
			}
		}
		
		mBatch.end();
	}

	private float toWorldSize(float pos) {
		return (pos / PXTM);
	}
	
	private void addBall() {
		BodyDef bd = new BodyDef();
		bd.type = BodyType.DynamicBody;
		bd.position.set(toWorldSize(400), toWorldSize(800));
		
		mBallRadius = toWorldSize(20);
		CircleShape circleShape = new CircleShape();
		circleShape.setRadius(mBallRadius);
		
		FixtureDef fd = new FixtureDef();
		fd.shape = circleShape;
		fd.density = 1;
		fd.friction = 0;
		fd.restitution = 1;
		
		Body b = mWorld.createBody(bd);
		b.createFixture(fd);
		
		Vector2 force = new Vector2(100, 100);
		b.applyForce(force, bd.position, true);
		b.applyLinearImpulse(force, bd.position, true);

		Ball ball = new Ball(mTextureBall, mBallRadius);
		b.setUserData(ball);
	}

	@Override
	public boolean keyDown(int keycode) {
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
		addBall();
		
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
	
	
	
	
	public class Ball extends Sprite {

		public Ball(Texture texture, float radius) {
			super(texture);
			
			setSize(radius * 2, radius * 2);
		}
		
	}
	
}
