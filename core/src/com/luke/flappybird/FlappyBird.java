package com.luke.flappybird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

public class FlappyBird extends ApplicationAdapter {
	SpriteBatch batch;
	Texture background;
	Texture bird;

	@Override
	public void create () {
		batch = new SpriteBatch();
		background = new Texture("bg.png");
		bird = new Texture("bird.png");
	}

	@Override
	public void render () {
		batch.begin();
		batch.draw(background, 0,0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		batch.draw(bird, Gdx.graphics.getWidth() / 2 - Gdx.graphics.getHeight() / 2,
				Gdx.graphics.getWidth() / 2 - Gdx.graphics.getHeight() / 2);
		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}
}
