package com.uet.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

public class Trang2_SelectGenderScreen extends ScreenAdapter {
    HanhTrinhDongSongGame game;
    Texture bgMenu; // Chỉ cần 1 ảnh nền chứa toàn bộ hình ảnh
    Rectangle rectNam, rectNu;

    public Trang2_SelectGenderScreen(HanhTrinhDongSongGame game) {
        this.game = game;
        // Đổi đường dẫn file này cho khớp với tên ảnh mới bạn lưu trong thư mục assets
        bgMenu = new Texture("image/Page2/background2.jpg"); 
        
        rectNam = new Rectangle();
        rectNu = new Rectangle();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        float screenW = Gdx.graphics.getWidth();
        float screenH = Gdx.graphics.getHeight();

        // Chia đôi màn hình để làm hitbox: nửa trái là Nam, nửa phải là Nữ
        rectNam.set(0, 0, screenW / 2f, screenH);
        rectNu.set(screenW / 2f, 0, screenW / 2f, screenH);

        game.batch.begin();
        // Vẽ ảnh nền lấp đầy màn hình
        game.batch.draw(bgMenu, 0, 0, screenW, screenH);
        game.batch.end();

        // Xử lý click chuột
        if (Gdx.input.justTouched()) {
            float touchX = Gdx.input.getX();
            float touchY = screenH - Gdx.input.getY();

            if (rectNam.contains(touchX, touchY)) {
                game.playerData.gioiTinh = "Nam"; 
                game.setScreen(new Trang5_FishingGameScreen(game));
            } else if (rectNu.contains(touchX, touchY)) {
                game.playerData.gioiTinh = "Nu";  
                game.setScreen(new Trang5_FishingGameScreen(game));
            }
        }
    }

    @Override
    public void dispose() {
        bgMenu.dispose();
    }
}