package com.uet.game;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;

public class Trang6_LetterGameScreen extends ScreenAdapter {
    HanhTrinhDongSongGame game;

    public Trang6_LetterGameScreen(HanhTrinhDongSongGame game) {
        this.game = game;
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.batch.begin();
        game.font.draw(game.batch, "GAME DAY CHU - Tim chu dung!", 100, 400);
        game.font.draw(game.batch, "Click de quay ve Menu", 100, 300);
        game.batch.end();

        // SỬA: Quay thẳng về Trang 1 thay vì Trang 4 không còn tồn tại
        if (Gdx.input.justTouched()) {
            game.setScreen(new Trang1_StartScreen(game));
        }
    }
}