package com.luke.flappybird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

public class FlappyBird extends ApplicationAdapter {
	SpriteBatch batch;
	Texture background;
	Texture[] birds;
	int flapState = 0;
	float birdY;
	int gameState = 0;
	float velocity = 0;
	float gravity = 2;
	Texture topTube;
	Texture bottomTube;
	float gap = 400;

	@Override
	public void create () {
		batch = new SpriteBatch();
		background = new Texture("bg.png");
		birds = new Texture[2];
		birds[0] = new Texture("bird.png");
		birds[1] = new Texture("bird2.png");
		bottomTube = new Texture("bottomtube.png");
		topTube = new Texture("toptube.png");
		birdY = Gdx.graphics.getHeight()/2 - birds[flapState].getHeight()/2;

	}

	@Override
	public void render () {

		if (gameState != 0){

			if (Gdx.input.justTouched()) {
				velocity = -30;
			}

			if (birdY > 0 || velocity < 0) {
				velocity += gravity;
				birdY -= velocity;
			}

			if (flapState == 0) {
				flapState = 1;
			} else {
				flapState = 0;
			}
		} else {
			if (Gdx.input.justTouched()) {
				gameState = 1;
			}
		}

		batch.begin();
		batch.draw(background, 0,0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		batch.draw(topTube, Gdx.graphics.getWidth()/2 - topTube.getWidth()/2,
				Gdx.graphics.getHeight()/2 + gap/2);
		batch.draw(bottomTube, Gdx.graphics.getWidth()/2 - bottomTube.getWidth()/2,
				Gdx.graphics.getHeight()/2 - gap/2 - bottomTube.getHeight());

		batch.draw(birds[flapState], Gdx.graphics.getWidth()/2 - birds[flapState].getWidth()/2,
				birdY);
		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}
}
