package com.luke.flappybird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.audio.Sound;
import java.util.Random;

public class FlappyBird extends ApplicationAdapter {
	SpriteBatch batch;
	Texture background1;
	Texture background2;
	float backgroundVelocity = 3;
	Texture[] birds;
	int flapState = 0;
	float birdY;
	int gameState = 0;
	float velocity = 0;
	float gravity = 2;
	Texture topTube;
	Texture bottomTube;
	int numberOfTube = 4;
	float gap = 600;
	float[] tubeOffset = new float[numberOfTube];
	Random random = new Random();
	float[] tubeX = new float[numberOfTube];
	float tubeVelocity = 4;
	float distanceBetweenTubes;

	Texture gameOver;
	Texture tapToStart;

	int scores = 0;
	int scoringTube = 0;
	BitmapFont bitmapFont;

	ShapeRenderer shapeRenderer;
	Circle circle;

	Rectangle[] topTubeRectangle;
	Rectangle[] bottomTubeRectangle;

	short backgroundOffset;
	float screenWidth;
	float screenHeight;
	Sound flapSound;
	Sound bonkSound;
	Sound offScreenSound;
	boolean collisionSoundPlayed;
	@Override
	public void create () {
		batch = new SpriteBatch();
		screenWidth = Gdx.graphics.getWidth();
		screenHeight = Gdx.graphics.getHeight();

		background1 = new Texture("beach_background.png");
		background2 = new Texture("beach_background.png");

		birds = new Texture[2];
		birds[0] = new Texture("bird.png");
		birds[1] = new Texture("bird2.png");
		bottomTube = new Texture("grayBottomTube.png");
		topTube = new Texture("grayTopTube.png");
		gameOver = new Texture("gameOver.png");
		tapToStart = new Texture("tapToStart.png");

		bitmapFont = new BitmapFont();
		bitmapFont.setColor(Color.WHITE);
		bitmapFont.getData().scale(10);

		shapeRenderer = new ShapeRenderer();
		circle = new Circle();

		topTubeRectangle = new Rectangle[numberOfTube];
		bottomTubeRectangle = new Rectangle[numberOfTube];
		distanceBetweenTubes = screenWidth / 2.0f;

		birdY = screenHeight / 2.0f - birds[flapState].getHeight() / 2.0f;

		for (int i = 0 ; i < numberOfTube ; i++) {

			tubeOffset[i] = (random.nextFloat() - 0.5f) * (screenHeight - gap - 200);


			tubeX[i] = ((screenWidth / 2.0f) - (topTube.getWidth() / 2.0f) + screenWidth
					+ i * distanceBetweenTubes);

			topTubeRectangle[i] = new Rectangle();
			bottomTubeRectangle[i] = new Rectangle();

		}
		flapSound = Gdx.audio.newSound(Gdx.files.internal("flapsound.mp3"));
		bonkSound = Gdx.audio.newSound(Gdx.files.internal("bonksound.mp3"));
		offScreenSound = Gdx.audio.newSound(Gdx.files.internal("offscreensound.mp3"));
	}

	public void gameStart() {
		birdY = screenHeight / 2.0f - birds[flapState].getHeight() / 2.0f;

		scoringTube = 0;
		scores = 0;
		velocity = 0;

		collisionSoundPlayed = false;

		for (int i = 0 ; i < numberOfTube ; i++) {
			tubeOffset[i] = (random.nextFloat() - 0.5f) * (screenHeight - gap - 200);
			tubeX[i] = screenWidth / 2.0f - topTube.getWidth() / 2.0f + screenWidth
					+ i * distanceBetweenTubes;
			topTubeRectangle[i] = new Rectangle();
			bottomTubeRectangle[i] = new Rectangle();
		}
	}

	@Override
	public void render () {
		batch.begin();
		batch.draw(background1,backgroundOffset,0,screenWidth , screenHeight);
		batch.draw(background2, backgroundOffset + screenWidth, 0, screenWidth, screenHeight);


		if (gameState == 1) {
			if (Gdx.input.justTouched()) {
				batch.draw(tapToStart, screenWidth + tapToStart.getWidth(), screenHeight + tapToStart.getHeight());
				velocity = -25;
				flapSound.play(5.0f);
			}

			if (tubeX[scoringTube] < screenWidth / 2.0f) {
				scores++;
				if (scoringTube < numberOfTube - 1) {
					scoringTube++;
				} else {
					scoringTube = 0;
				}
			}

			for (int i = 0 ; i < numberOfTube ; i++) {
				if (tubeX[i] < -topTube.getWidth()) {
					tubeX[i] += numberOfTube * distanceBetweenTubes;
				} else {
					tubeX[i] -= tubeVelocity;
				}

				batch.draw(topTube,tubeX[i],screenHeight / 2.0f  + gap / 2 + tubeOffset[i]);
				batch.draw(bottomTube,tubeX[i],
						screenHeight / 2.0f - gap / 2 - bottomTube.getHeight() + tubeOffset[i]);

				topTubeRectangle[i] = new Rectangle(tubeX[i],
						screenHeight / 2.0f  + gap / 2 + tubeOffset[i],
						topTube.getWidth(),
						topTube.getHeight());
				bottomTubeRectangle[i] = new Rectangle(tubeX[i],
						screenHeight / 2.0f - gap / 2 - bottomTube.getHeight() + tubeOffset[i],
						bottomTube.getWidth(),
						bottomTube.getHeight());
			}

			if (birdY > -100 && birdY < screenHeight + 250) {
				velocity += gravity;
				birdY -= velocity;
			} else {
				offScreenSound.play(5.0f);
				gameState = 2;
			}

			if (flapState == 0) {
				flapState = 1;
			} else {
				flapState = 0;
			}

		} else if(gameState == 0) {
			batch.draw(tapToStart,screenWidth / 2.0f - tapToStart.getWidth() / 2.0f,
					screenHeight/ 2.0f - tapToStart.getHeight() / 0.25f);
			if (Gdx.input.justTouched()) {
				gameState = 1;
				flapSound.play(5.0f);
			}
		} else if (gameState == 2) {
			batch.draw(gameOver,screenWidth / 2.0f - gameOver.getWidth() / 2.0f,
					screenHeight/ 2.0f - gameOver.getHeight() / 2.0f);
			batch.draw(tapToStart, screenWidth / 2.0f - tapToStart.getWidth() / 2.0f,
					screenHeight / 2.3f - tapToStart.getHeight() / 2.3f);

			if (Gdx.input.justTouched()) {
				gameState = 1;
				gameStart();
			}
		}

		batch.draw(birds[flapState],screenWidth / 2.0f - birds[flapState].getWidth() / 2.0f ,
				birdY);

		bitmapFont.draw(batch,Integer.toString(scores),200,200);

		batch.end();

		backgroundOffset -= backgroundVelocity;
		if (backgroundOffset + screenWidth == 0) {
			backgroundOffset = 0;
		}

		circle.set(screenWidth / 2.0f ,birdY + birds[flapState].getWidth() / 2.0f
				,birds[flapState].getWidth() / 2.0f);

		for (int i = 0 ; i < numberOfTube ; i++)
			if (Intersector.overlaps(circle, topTubeRectangle[i])
					|| Intersector.overlaps(circle, bottomTubeRectangle[i])) {
				if (!collisionSoundPlayed) {
					bonkSound.play(5.0f);
					collisionSoundPlayed = true;
				}
				gameState = 2;
				break;
			}
		shapeRenderer.end();
	}

	@Override
	public void dispose () {
		batch.dispose();
		flapSound.dispose();
		bonkSound.dispose();
		offScreenSound.dispose();
	}
}

