package com.cvte.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
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

public class Demo0 extends ApplicationAdapter {
	
    private static final float PXTM = 80;
	
	protected OrthographicCamera mCamera;
	private Box2DDebugRenderer mRenderer;
	
	private World mWorld;
	
	@Override
	public void create() {
        float cameraWidth = Gdx.graphics.getWidth() / PXTM;
        float cameraHeight = Gdx.graphics.getHeight() / PXTM;
		mCamera = new OrthographicCamera(cameraWidth, cameraHeight);
		mCamera.position.set(cameraWidth / 2, cameraHeight / 2, 0);
		mCamera.update();
		
		mRenderer = new Box2DDebugRenderer();
		
		mWorld = new World(new Vector2(0, -10), true);
		
		//ground
		{
			BodyDef bd = new BodyDef();
			bd.type = BodyType.StaticBody;
			bd.position.set(toWorldSize(0), toWorldSize(100));
			
			EdgeShape edgeShape = new EdgeShape();
			edgeShape.set(new Vector2(toWorldSize(0), toWorldSize(80)), new Vector2(toWorldSize(Gdx.graphics.getWidth()), toWorldSize(80)));
			
			FixtureDef fd = new FixtureDef();
			fd.shape = edgeShape;
						
			Body ground = mWorld.createBody(bd);
			ground.createFixture(fd);
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
			fd.restitution = 0.9f;
			
			Body ball = mWorld.createBody(bd);
			ball.createFixture(fd);
		}
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
	
}
