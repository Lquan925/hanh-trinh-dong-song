package com.uet.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import java.util.ArrayList;

public class Trang5_FishingGameScreen extends ScreenAdapter {
    private final HanhTrinhDongSongGame game;
    private final Texture bgGame, imgThuyen, imgLuoiCau, imgDayCau;
    private final Texture[] texRac = new Texture[5];
    private final Rectangle thuyen = new Rectangle(0, 100, 360, 280);
    private final Rectangle luoiCau = new Rectangle(0, 0, 40, 60);
    private final ArrayList<Rac> dsRac = new ArrayList<>();
    private boolean isReturning, isHookActive;
    private float gameTime, thoiGianSinhRac;

    private static class Rac {
        final Rectangle rect;
        final int type;
        Rac(Rectangle rect, int type) {
            this.rect = rect;
            this.type = type;
        }
    }

    public Trang5_FishingGameScreen(HanhTrinhDongSongGame game) {
        this.game = game;
        bgGame = new Texture("image/Nen/background1.jpg");
        String boatFile = "Nu".equals(game.playerData.gioiTinh) ? "thuyennu.png"
                : "Nam".equals(game.playerData.gioiTinh) ? "thuyennam.png" : "thuyen.png";
        imgThuyen = new Texture("image/Thuyen/" + boatFile);
        imgLuoiCau = new Texture("image/Thuyen/luoi.png");
        String[] racFiles = {"tat.png", "non.png", "linon.png", "thuocla.png", "go.png"};
        for (int i = 0; i < racFiles.length; i++) {
            texRac[i] = new Texture("image/Thuyen/RacBien/" + racFiles[i]);
        }
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(1f, 1f, 1f, 0.8f);
        pixmap.fill();
        imgDayCau = new Texture(pixmap);
        pixmap.dispose();
        for (int i = 0; i < 5; i++) spawnRac(Gdx.graphics.getWidth());
    }

    private void spawnRac(float screenW) {
        float size = Math.min(60f, screenW);
        // Spawn within the horizontal range the boat's fishing line can reach.
        float minX = Math.min(180f, Math.max(0f, (screenW - size) / 2f));
        float maxX = Math.max(minX, screenW - 190f - size);
        float x = MathUtils.random(minX, maxX);
        float y = MathUtils.random(0f, Math.max(0f, thuyen.y - size));
        dsRac.add(new Rac(new Rectangle(x, y, size, size), MathUtils.random(0, 4)));
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        float screenW = Gdx.graphics.getWidth();
        float screenH = Gdx.graphics.getHeight();
        gameTime += delta;
        if (gameTime >= 300f) {
            game.setScreen(new Trang7_ResultScreen(game));
            dispose();
            return;
        }
        Rectangle btnThoat = new Rectangle(10, screenH - 50, 100, 40);
        if (Gdx.input.justTouched() && btnThoat.contains(Gdx.input.getX(), screenH - Gdx.input.getY())) {
            game.setScreen(new Trang1_StartScreen(game));
            dispose();
            return;
        }

        if (Gdx.input.isKeyPressed(Keys.LEFT)) {
            thuyen.x -= 300f * delta;
            isReturning = true;
        }
        if (Gdx.input.isKeyPressed(Keys.RIGHT)) {
            thuyen.x += 300f * delta;
            isReturning = false;
        }
        thuyen.x = MathUtils.clamp(thuyen.x, 0f, Math.max(0f, screenW - thuyen.width));

        float rodX, rodY;
        if ("Nam".equals(game.playerData.gioiTinh)) {
            rodX = thuyen.x + thuyen.width * (isReturning ? 0.53f : 0.51f);
            rodY = thuyen.y + thuyen.height * 0.94f;
        } else if ("Nu".equals(game.playerData.gioiTinh)) {
            rodX = thuyen.x + thuyen.width * (isReturning ? 0.52f : 0.46f);
            rodY = thuyen.y + thuyen.height * 0.78f;
        } else {
            rodX = thuyen.x + thuyen.width * (isReturning ? 0.55f : 0.45f);
            rodY = thuyen.y + thuyen.height * 0.85f;
        }
        if (Gdx.input.isKeyJustPressed(Keys.DOWN) && !isHookActive) {
            isHookActive = true;
            luoiCau.y = rodY;
        }
        if (isHookActive) {
            luoiCau.x = rodX - luoiCau.width / 2f;
            luoiCau.y -= 250f * delta;
            for (int i = dsRac.size() - 1; i >= 0; i--) {
                if (luoiCau.overlaps(dsRac.get(i).rect)) {
                    dsRac.remove(i);
                    game.playerData.racVot++;
                    isHookActive = false;
                    break;
                }
            }
            if (luoiCau.y < 0) isHookActive = false;
        }

        thoiGianSinhRac += delta;
        if (thoiGianSinhRac >= 10f) {
            spawnRac(screenW);
            thoiGianSinhRac = 0f;
        }
        if (dsRac.isEmpty()) spawnRac(screenW);

        game.batch.begin();
        game.batch.draw(bgGame, 0, 0, screenW, screenH);
        for (Rac rac : dsRac) {
            game.batch.draw(texRac[rac.type], rac.rect.x, rac.rect.y, rac.rect.width, rac.rect.height);
        }
        game.batch.draw(imgThuyen, thuyen.x, thuyen.y, thuyen.width, thuyen.height,
                0, 0, imgThuyen.getWidth(), imgThuyen.getHeight(), isReturning, false);
        if (isHookActive) {
            float lineBottom = luoiCau.y + luoiCau.height;
            if (rodY > lineBottom) game.batch.draw(imgDayCau, rodX - 1f, lineBottom, 2f, rodY - lineBottom);
            game.batch.draw(imgLuoiCau, luoiCau.x, luoiCau.y, luoiCau.width, luoiCau.height);
        }
        game.font.draw(game.batch, "Diem: " + game.playerData.racVot * 5 + " (Rac: " + game.playerData.racVot + ")",
                screenW / 2 - 100, screenH - 30);
        game.font.draw(game.batch, "[ THOAT ]", 20, screenH - 20);
        game.font.draw(game.batch, "Mui ten trai/phai: di chuyen | Mui ten xuong: cau rac", 20, screenH - 65);
        int timeRemaining = 300 - (int) gameTime;
        game.font.draw(game.batch, String.format("Thoi gian: %02d:%02d",
                timeRemaining / 60, timeRemaining % 60), screenW - 150, screenH - 20);
        game.batch.end();
    }

    @Override
    public void dispose() {
        bgGame.dispose();
        imgThuyen.dispose();
        imgLuoiCau.dispose();
        imgDayCau.dispose();
        for (Texture texture : texRac) texture.dispose();
    }
}
