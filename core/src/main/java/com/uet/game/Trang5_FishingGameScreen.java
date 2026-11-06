package com.uet.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
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
    
    Rectangle thuyen, btnDoiDungCu;
    float boatSpeed = 300f; 
    boolean isReturning = false; 
    
    Texture bgGame, imgThuyen, imgBongBong; 
    Texture[] dungCu; 
    
    Animation<TextureRegion> animChim, animChimXanh, animOng;
    Texture[] dsCa; 
    ArrayList<Texture> animTextures;
    
    Texture[] texRac;
    Texture imgLuoiCau;
    Texture imgDayCau;
    float thoiGianSinhRac = 0;
    
    class Rac {
        Rectangle rect;
        int type; 
        public Rac(Rectangle rect, int type) {
            this.rect = rect;
            this.type = type;
        }
    }
    ArrayList<Rac> dsRac;
    
    Rectangle luoiCauRect;
    boolean isHookActive = false;
    float hookSpeed = 250f; 

    int congCuHienTai = 0;
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
        
        thuyen = new Rectangle(0, 100, 360, 280); 
        btnDoiDungCu = new Rectangle(20, 0, 80, 80); 
        dsMucTieu = new ArrayList<>();
        danhSachBongBong = new ArrayList<>(); 
        dsRac = new ArrayList<>(); 
        animTextures = new ArrayList<>(); 
        bgGame = new Texture("image/Nen/background1.jpg");
        
        if (game.playerData.gioiTinh.equals("Nu")) {
            imgThuyen = new Texture("image/Thuyen/thuyennu.png");
        } else if (game.playerData.gioiTinh.equals("Nam")) {
            imgThuyen = new Texture("image/Thuyen/thuyennam.png");
        } else {
            imgThuyen = new Texture("image/Thuyen/thuyen.png"); 
        }
        
        animChim = createAnimation(0.15f, "Chim/chim1.png", "Chim/chim2.png", "Chim/chim3.png", "Chim/chim4.png", "Chim/chim5.png", "Chim/chim6.png"); 
        animChimXanh = createAnimation(0.15f, "Chim xanh/chimxanh1.png", "Chim xanh/chimxanh2.png", "Chim xanh/chimxanh3.png", "Chim xanh/chimxanh4.png", "Chim xanh/chimxanh5.png", "Chim xanh/chimxanh6.png", "Chim xanh/chimxanh7.png", "Chim xanh/chimxanh8.png");
        animOng = createAnimation(0.1f, "Ong/ong1.png", "Ong/ong2.png", "Ong/ong3.png", "Ong/ong4.png", "Ong/ong5.png", "Ong/ong6.png", "Ong/ong7.png", "Ong/ong8.png");

        dsCa = new Texture[3];
        dsCa[0] = new Texture("Ca/ca1.png");   
        dsCa[1] = new Texture("Ca/rua.png");   
        dsCa[2] = new Texture("Ca/tom.png");   
        
        texRac = new Texture[5];
        texRac[0] = new Texture("image/Thuyen/Racbien/tat.png");
        texRac[1] = new Texture("image/Thuyen/Racbien/non.png");
        texRac[2] = new Texture("image/Thuyen/Racbien/linon.png");
        texRac[3] = new Texture("image/Thuyen/Racbien/thuocla.png");
        texRac[4] = new Texture("image/Thuyen/Racbien/go.png");
        
        imgLuoiCau = new Texture("image/Thuyen/luoi.png"); 
        luoiCauRect = new Rectangle(0, 0, 40, 60); 

        Pixmap linePixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        linePixmap.setColor(1f, 1f, 1f, 0.8f); 
        linePixmap.fill();
        imgDayCau = new Texture(linePixmap);
        linePixmap.dispose();

        Pixmap pixmap = new Pixmap(40, 40, Pixmap.Format.RGBA8888);
        pixmap.setColor(0.3f, 0.8f, 1f, 0.5f); 
        pixmap.fillCircle(20, 20, 16); 
        pixmap.setColor(0.1f, 0.9f, 1f, 0.9f); 
        pixmap.drawCircle(20, 20, 16); 
        imgBongBong = new Texture(pixmap);
        pixmap.dispose(); 
        
        dungCu = new Texture[1];
        dungCu[0] = new Texture("image/Vot/vot.png");
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
        int conVatHienTai = MathUtils.random(0, 5);
        Rectangle rect = new Rectangle(0, 0, 100, 100);
        
        if (conVatHienTai == 5) { 
            rect.width = 250f; rect.height = 250f;
        } else if (conVatHienTai == 3) { 
            rect.width = 400f; rect.height = 400f;
        } 
        
        float baseY;
        if (conVatHienTai <= 2) { 
            baseY = MathUtils.random(screenH / 2, screenH - 200); 
        } else {
            baseY = screenH / 30; 
        }
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

    private void spawnRac(float screenW) {
        int type = MathUtils.random(0, 4);
        Rectangle rect = new Rectangle(0, 0, 60, 60); 
        
        float minX = 180f;
        float maxX = screenW - 190f - rect.width;
        if (maxX < minX) maxX = minX;
        
        rect.x = MathUtils.random(minX, maxX);
        
        if (type == 2) { 
            rect.y = thuyen.y; 
        } else { 
            rect.y = MathUtils.random(20, thuyen.y - 80);
        }
        dsRac.add(new Rac(rect, type));
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
        thoiGianSinhRac += delta;

        if (thoiGianSinhRac >= 10f) {
            spawnRac(screenW);
            thoiGianSinhRac = 0f;
        }

        if (gameTime >= 300f) {
            game.setScreen(new Trang7_ResultScreen(game));
            return;
        }

        if (Gdx.input.isKeyPressed(Keys.LEFT)) {
            thuyen.x -= boatSpeed * delta;
            isReturning = true; 
        }
        if (Gdx.input.isKeyPressed(Keys.RIGHT)) {
            thuyen.x += boatSpeed * delta;
            isReturning = false; 
        }

        if (thuyen.x < 0) thuyen.x = 0;
        if (thuyen.x > screenW - thuyen.width) thuyen.x = screenW - thuyen.width;
        
        float dauCanCauX = 0;
        float dauCanCauY = 0;
        
        if (game.playerData.gioiTinh.equals("Nam")) {
            dauCanCauX = isReturning ? (thuyen.x + thuyen.width * 0.53f) : (thuyen.x + thuyen.width * 0.51f);
            dauCanCauY = thuyen.y + thuyen.height * 0.94f;
        } else if (game.playerData.gioiTinh.equals("Nu")) {
            dauCanCauX = isReturning ? (thuyen.x + thuyen.width * 0.52f) : (thuyen.x + thuyen.width * 0.46f);
            dauCanCauY = thuyen.y + thuyen.height * 0.78f;
        } else {
            dauCanCauX = isReturning ? (thuyen.x + thuyen.width * 0.55f) : (thuyen.x + thuyen.width * 0.45f);
            dauCanCauY = thuyen.y + thuyen.height * 0.85f;
        }
        
        if (dsMucTieu.isEmpty()) { spawnConVat(screenW, screenH); }
        
        for (int i = dsMucTieu.size() - 1; i >= 0; i--) {
            ThuyQuai tq = dsMucTieu.get(i);
            tq.rect.x += tq.speedX * delta;
            
            if (tq.type <= 2) { 
                tq.rect.y = tq.baseY + MathUtils.sin(stateTime * 4f) * 60f; 
            } else {
                tq.rect.y = tq.baseY + MathUtils.sin(stateTime * 3f) * 20f; 
            }
            
            if (tq.speedX > 0 && tq.rect.x > screenW) {
                dsMucTieu.remove(i); 
            } else if (tq.speedX < 0 && tq.rect.x < -tq.rect.width) {
                dsMucTieu.remove(i); 
            }
        }
        
        for (int i = dsRac.size() - 1; i >= 0; i--) {
            Rac rac = dsRac.get(i);
            if (rac.type == 2) { 
                Rectangle thuyenHitbox = new Rectangle(thuyen.x + 50, thuyen.y, thuyen.width - 100, thuyen.height / 2);
                if (thuyenHitbox.overlaps(rac.rect)) {
                    dsRac.remove(i);
                }
            }
        }

        if (Gdx.input.isKeyJustPressed(Keys.DOWN) && !isHookActive) {
            isHookActive = true;
            luoiCauRect.x = dauCanCauX - (luoiCauRect.width / 2f); 
            luoiCauRect.y = dauCanCauY;
        }

        if (isHookActive) {
            luoiCauRect.x = dauCanCauX - (luoiCauRect.width / 2f); 
            luoiCauRect.y -= hookSpeed * delta; 
            
            boolean hitSomething = false;
            for (int i = dsRac.size() - 1; i >= 0; i--) {
                Rac rac = dsRac.get(i);
                if (rac.type != 2 && luoiCauRect.overlaps(rac.rect)) {
                    game.playerData.racVot++; // Cộng điểm rác
                    dsRac.remove(i); 
                    hitSomething = true;
                    break;
                }
            }
            
            if (hitSomething || luoiCauRect.y < 0) {
                isHookActive = false; 
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
        
        for (Rac rac : dsRac) {
            game.batch.draw(texRac[rac.type], rac.rect.x, rac.rect.y, rac.rect.width, rac.rect.height);
        }

        if (isHookActive) {
            float dayCauX = dauCanCauX; 
            float dayCauStartY = dauCanCauY; 
            float dayCauEndY = luoiCauRect.y + luoiCauRect.height; 
            float chieuDaiDay = dayCauStartY - dayCauEndY;
            
            if (chieuDaiDay > 0) {
                game.batch.draw(imgDayCau, dayCauX - 1f, dayCauEndY, 2f, chieuDaiDay);
            }
            
            game.batch.draw(imgLuoiCau, luoiCauRect.x, luoiCauRect.y, luoiCauRect.width, luoiCauRect.height);
        }

        game.batch.draw(imgThuyen, thuyen.x, thuyen.y, thuyen.width, thuyen.height, 0, 0, imgThuyen.getWidth(), imgThuyen.getHeight(), isReturning, false);
        game.batch.draw(dungCu[0], btnDoiDungCu.x, btnDoiDungCu.y, btnDoiDungCu.width, btnDoiDungCu.height);
        
        // Tính tổng điểm mới bao gồm Rác
        int diemDongVat = game.playerData.dongVatBat * 10;
        int diemRac = game.playerData.racVot * 5; // Giả sử mỗi rác 5 điểm
        int tongDiem = diemDongVat + diemRac + game.playerData.bongBat;
        
        // Hiển thị chữ trên màn hình (đổi thành Động vật và thêm phần Rác)
        game.font.draw(game.batch, "Diem: " + tongDiem + " (Dong vat: " + game.playerData.dongVatBat + ", Bong: " + game.playerData.bongBat + ", Rac: " + game.playerData.racVot + ")", screenW / 2 - 150, screenH - 30);
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
            
            if (tq.type == 2 || tq.type == 4) {
                flipX = !flipX; 
            }

            TextureRegion currentFrame = null;
            Texture staticTex = null;

            switch (tq.type) {
                case 0: currentFrame = animChim.getKeyFrame(stateTime, true); break;
                case 1: currentFrame = animChimXanh.getKeyFrame(stateTime, true); break;
                case 2: currentFrame = animOng.getKeyFrame(stateTime, true); break;
                case 3: staticTex = dsCa[0]; break;
                case 4: staticTex = dsCa[1]; break;
                case 5: staticTex = dsCa[2]; break;
            }

            if (currentFrame != null) {
                if (currentFrame.isFlipX() != flipX) currentFrame.flip(true, false);
                game.batch.draw(currentFrame, tq.rect.x, tq.rect.y, tq.rect.width, tq.rect.height);
            } else if (staticTex != null) {
                game.batch.draw(staticTex, tq.rect.x, tq.rect.y, tq.rect.width, tq.rect.height, 
                                0, 0, staticTex.getWidth(), staticTex.getHeight(), flipX, false);
            }
        }
        
        game.batch.end();
    }

    @Override
    public void dispose() {
        bgGame.dispose();
        imgThuyen.dispose();
        imgBongBong.dispose(); 
        for(Texture t : dungCu) t.dispose();
        for(Texture t : dsCa) t.dispose();
        for(Texture t : animTextures) t.dispose();
        for(Texture t : texRac) t.dispose();
        imgLuoiCau.dispose();
        imgDayCau.dispose(); 
    }
}