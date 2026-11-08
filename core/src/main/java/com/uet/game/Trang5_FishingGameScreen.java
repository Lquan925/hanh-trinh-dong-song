package com.uet.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import java.util.ArrayList;

public class Trang5_FishingGameScreen extends ScreenAdapter {
    private final HanhTrinhDongSongGame game;
    private final Texture bgGame, imgBongBong;
    private final ArrayList<Rectangle> danhSachBongBong = new ArrayList<>();
    private float gameTime;
    private float thoiGianSinhBong = 0.8f;

    public Trang5_FishingGameScreen(HanhTrinhDongSongGame game) {
        this.game = game;
        bgGame = new Texture("image/Nen/background1.jpg");
        Pixmap pixmap = new Pixmap(40, 40, Pixmap.Format.RGBA8888);
        pixmap.setColor(0.3f, 0.8f, 1f, 0.5f);
        pixmap.fillCircle(20, 20, 16);
        pixmap.setColor(0.1f, 0.9f, 1f, 0.9f);
        pixmap.drawCircle(20, 20, 16);
        imgBongBong = new Texture(pixmap);
        pixmap.dispose();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        float screenW = Gdx.graphics.getWidth();
        float screenH = Gdx.graphics.getHeight();
        gameTime += delta;
        if (gameTime >= 240f) {
            game.setScreen(new Trang7_ResultScreen(game));
            dispose();
            return;
        }

        float mouseX = Gdx.input.getX();
        float mouseY = screenH - Gdx.input.getY();
        Rectangle btnThoat = new Rectangle(10, screenH - 50, 100, 40);
        if (Gdx.input.justTouched() && btnThoat.contains(mouseX, mouseY)) {
            game.setScreen(new Trang1_StartScreen(game));
            dispose();
            return;
        }

        for (int i = danhSachBongBong.size() - 1; i >= 0; i--) {
            Rectangle bong = danhSachBongBong.get(i);
            bong.y -= 40f * delta;
            Rectangle hitBox = new Rectangle(bong.x - 5, bong.y - 5, 50, 50);
            if (bong.y + bong.height < 0) {
                danhSachBongBong.remove(i);
            } else if (Gdx.input.isTouched() && hitBox.contains(mouseX, mouseY)) {
                danhSachBongBong.remove(i);
                game.playerData.bongBat++;
            }
        }

        // Keep each new bubble fully visible and below the score/exit bar.
        thoiGianSinhBong += delta;
        if (thoiGianSinhBong >= 0.8f) {
            float size = Math.min(40f, Math.min(screenW, screenH));
            float x = MathUtils.random(0f, Math.max(0f, screenW - size));
            float y = MathUtils.random(0f, Math.max(0f, screenH - 60f - size));
            danhSachBongBong.add(new Rectangle(x, y, size, size));
            thoiGianSinhBong = 0f;
        }

        game.batch.begin();
        game.batch.draw(bgGame, 0, 0, screenW, screenH);
        for (Rectangle bong : danhSachBongBong) {
            game.batch.draw(imgBongBong, bong.x, bong.y, bong.width, bong.height);
        }
        game.font.draw(game.batch, "Diem: " + game.playerData.bongBat,
                screenW / 2 - 60, screenH - 30);
        game.font.draw(game.batch, "[ THOAT ]", 20, screenH - 20);
        int timeRemaining = 240 - (int) gameTime;
        game.font.draw(game.batch, String.format("Thoi gian: %02d:%02d",
                timeRemaining / 60, timeRemaining % 60), screenW - 150, screenH - 20);
        game.batch.end();
    }

    @Override
    public void dispose() {
        bgGame.dispose();
        imgBongBong.dispose();
    }
}
