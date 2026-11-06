package com.uet.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.graphics.Pixmap; 
import java.util.ArrayList; 

public class Trang5_FishingGameScreen extends ScreenAdapter {
    HanhTrinhDongSongGame game;
    
    Rectangle btnDoiDungCu;
    Texture bgGame, imgBongBong;
    Animation<TextureRegion> animChim, animChimXanh;
    ArrayList<Texture> animTextures;

    float stateTime = 0; 
    float gameTime = 0; 

    class ThuyQuai {
        Rectangle rect;
        int type;
        float speedX;
        float baseY;
        public ThuyQuai(Rectangle rect, int type, float speedX, float baseY) {
            this.rect = rect; 
            this.type = type; 
            this.speedX = speedX; 
            this.baseY = baseY;
        }
    }
    
    ArrayList<ThuyQuai> dsMucTieu; 
    float thoiGianHoiChieu = 3f; 
    ArrayList<Rectangle> danhSachBongBong;
    float thoiGianThaBongBong = 0;

    public Trang5_FishingGameScreen(HanhTrinhDongSongGame game) { 
        this.game = game; 
        
        btnDoiDungCu = new Rectangle(20, 0, 140, 60); 
        dsMucTieu = new ArrayList<>();
        danhSachBongBong = new ArrayList<>(); 
        animTextures = new ArrayList<>(); 
        bgGame = new Texture("image/Nen/background1.jpg");
        
        animChim = createAnimation(0.15f, "Chim/chim1.png", "Chim/chim2.png", "Chim/chim3.png", "Chim/chim4.png", "Chim/chim5.png", "Chim/chim6.png"); 
        animChimXanh = createAnimation(0.15f, "Chim xanh/chimxanh1.png", "Chim xanh/chimxanh2.png", "Chim xanh/chimxanh3.png", "Chim xanh/chimxanh4.png", "Chim xanh/chimxanh5.png", "Chim xanh/chimxanh6.png", "Chim xanh/chimxanh7.png", "Chim xanh/chimxanh8.png");
        Pixmap pixmap = new Pixmap(40, 40, Pixmap.Format.RGBA8888);
        pixmap.setColor(0.3f, 0.8f, 1f, 0.5f); 
        pixmap.fillCircle(20, 20, 16); 
        pixmap.setColor(0.1f, 0.9f, 1f, 0.9f); 
        pixmap.drawCircle(20, 20, 16); 
        imgBongBong = new Texture(pixmap);
        pixmap.dispose(); 
        
    }

    private Animation<TextureRegion> createAnimation(float frameDuration, String... fileNames) {
        TextureRegion[] frames = new TextureRegion[fileNames.length];
        for (int i = 0; i < fileNames.length; i++) {
            Texture frameTex = new Texture(fileNames[i]);
            animTextures.add(frameTex); 
            frames[i] = new TextureRegion(frameTex);
        }
        return new Animation<>(frameDuration, frames);
    }

    private void spawnConVat(float screenW, float screenH) {
        int conVatHienTai = MathUtils.random(0, 1);
        Rectangle rect = new Rectangle(0, 0, 100, 100);
        
        float minY = screenH / 2;
        float baseY = MathUtils.random(minY, Math.max(minY, screenH - 200));
        rect.y = baseY;

        int huong = MathUtils.randomBoolean() ? 1 : -1;
        float tocDo = 45.0f * huong; 

        if (huong == 1) {
            rect.x = -rect.width; 
        } else {
            rect.x = screenW; 
        }
        
        dsMucTieu.add(new ThuyQuai(rect, conVatHienTai, tocDo, baseY));
        game.playerData.tongDongVatXuatHien++; // Cập nhật biến tổng động vật
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        float screenW = Gdx.graphics.getWidth();
        float screenH = Gdx.graphics.getHeight();
        
        btnDoiDungCu.y = screenH - 120; 
        stateTime += delta; 
        gameTime += delta;
        thoiGianHoiChieu += delta;
        if (gameTime >= 300f) {
            game.setScreen(new Trang7_ResultScreen(game));
            return;
        }

        if (dsMucTieu.isEmpty()) { spawnConVat(screenW, screenH); }
        
        for (int i = dsMucTieu.size() - 1; i >= 0; i--) {
            ThuyQuai tq = dsMucTieu.get(i);
            tq.rect.x += tq.speedX * delta;
            
            tq.rect.y = tq.baseY + MathUtils.sin(stateTime * 4f) * 60f;
            
            if (tq.speedX > 0 && tq.rect.x > screenW) {
                dsMucTieu.remove(i); 
            } else if (tq.speedX < 0 && tq.rect.x < -tq.rect.width) {
                dsMucTieu.remove(i); 
            }
        }
        
        thoiGianThaBongBong += delta;
        if (thoiGianThaBongBong >= 0.8f) { 
            for (ThuyQuai tq : dsMucTieu) {
                danhSachBongBong.add(new Rectangle(tq.rect.x + tq.rect.width / 2, tq.rect.y + tq.rect.height / 2, 40, 40));
            }
            thoiGianThaBongBong = 0; 
        }

        float mouseX = Gdx.input.getX();
        float mouseY = screenH - Gdx.input.getY();

        for (int i = danhSachBongBong.size() - 1; i >= 0; i--) {
            Rectangle bongBong = danhSachBongBong.get(i);
            bongBong.y -= 120f * delta; 
            Rectangle hitBox = new Rectangle(bongBong.x - 5, bongBong.y - 5, 50, 50);
            if (bongBong.y + bongBong.height < 0) {
                danhSachBongBong.remove(i); 
            } else if (Gdx.input.isTouched() && hitBox.contains(mouseX, mouseY)) {
                danhSachBongBong.remove(i); 
                game.playerData.bongBat++; 
            }
        }

        if (Gdx.input.justTouched()) {
            Rectangle btnThoat = new Rectangle(10, screenH - 50, 100, 40);
            if (btnThoat.contains(mouseX, mouseY)) {
                game.setScreen(new Trang1_StartScreen(game)); return;
            }
            
            if (btnDoiDungCu.contains(mouseX, mouseY)) {
                if (thoiGianHoiChieu >= 3f) {
                    spawnConVat(screenW, screenH); 
                    thoiGianHoiChieu = 0; 
                }
            } else {
                for (int i = dsMucTieu.size() - 1; i >= 0; i--) {
                    ThuyQuai tq = dsMucTieu.get(i);
                    float insetX = tq.rect.width * 0.2f; float insetY = tq.rect.height * 0.2f;
                    Rectangle chimHitbox = new Rectangle(tq.rect.x + insetX, tq.rect.y + insetY, tq.rect.width - (2 * insetX), tq.rect.height - (2 * insetY));
                    if (chimHitbox.contains(mouseX, mouseY)) {
                        game.playerData.dongVatBat++; // Cập nhật biến động vật
                        dsMucTieu.remove(i); break; 
                    }
                }
            }
        }

        game.batch.begin();
        game.batch.draw(bgGame, 0, 0, screenW, screenH); 
        
        game.font.draw(game.batch, "[ THEM CHIM ]", btnDoiDungCu.x, btnDoiDungCu.y + 50);

        int diemDongVat = game.playerData.dongVatBat * 10;
        int tongDiem = diemDongVat + game.playerData.bongBat;
        
        // Hiển thị chữ trên màn hình (đổi thành Động vật và thêm phần Rác)
        game.font.draw(game.batch, "Diem: " + tongDiem + " (Chim: " + game.playerData.dongVatBat + ", Bong: " + game.playerData.bongBat + ")", screenW / 2 - 150, screenH - 30);
        game.font.draw(game.batch, "[ THOAT ]", 20, screenH - 20);
        
        int timeRemaining = 300 - (int)gameTime;
        int minutes = timeRemaining / 60;
        int seconds = timeRemaining % 60;
        game.font.draw(game.batch, String.format("Thoi gian: %02d:%02d", minutes, seconds), screenW - 150, screenH - 20);
        
        for (Rectangle bongBong : danhSachBongBong) {
            game.batch.draw(imgBongBong, bongBong.x, bongBong.y, bongBong.width, bongBong.height);
        }

        for (ThuyQuai tq : dsMucTieu) {
            boolean flipX = (tq.speedX > 0); 
            
            TextureRegion currentFrame = (tq.type == 0 ? animChim : animChimXanh)
                    .getKeyFrame(stateTime, true);

            if (currentFrame != null) {
                if (currentFrame.isFlipX() != flipX) currentFrame.flip(true, false);
                game.batch.draw(currentFrame, tq.rect.x, tq.rect.y, tq.rect.width, tq.rect.height);
            }
        }
        
        game.batch.end();
    }

    @Override
    public void dispose() {
        bgGame.dispose();
        imgBongBong.dispose();
        for (Texture texture : animTextures) texture.dispose();
    }
}
