package com.uet.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

public class Trang1_StartScreen extends ScreenAdapter {
    HanhTrinhDongSongGame game;
    Rectangle btnStart;
    Texture bgMenu;

    public Trang1_StartScreen(HanhTrinhDongSongGame game) {
        this.game = game;
        bgMenu = new Texture("image/Start/background2.jpg"); 
        
        // Khởi tạo nút với kích thước 200x50, vị trí sẽ được tự động căn chỉnh trong hàm render
        btnStart = new Rectangle(0, 0, 200, 50); 
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Lấy kích thước thật của cửa sổ game hiện tại
        float screenW = Gdx.graphics.getWidth();
        float screenH = Gdx.graphics.getHeight();

        // Tự động căn giữa nút Start theo chiều ngang, và đặt ở độ cao 30% tính từ dưới lên
        btnStart.x = (screenW - btnStart.width) / 2f;
        btnStart.y = screenH * 0.3f; 

        game.batch.begin();
        
        // VẼ ẢNH NỀN LẤP ĐẦY 100% CỬA SỔ
        game.batch.draw(bgMenu, 0, 0, screenW, screenH);
        
        // Đã xóa dòng chữ hiển thị tên trò chơi ở đây
        
        // Vẽ chữ lên trên nút Start
        game.font.draw(game.batch, ">>> NHAN DE BAT DAU <<<", btnStart.x + 10, btnStart.y + 30);
        
        game.batch.end();

        // Xử lý sự kiện click chuột
        if (Gdx.input.justTouched()) {
            float touchX = Gdx.input.getX();
            float touchY = screenH - Gdx.input.getY(); 
            
            if (btnStart.contains(touchX, touchY)) {
                // Chuyển sang màn hình chọn giới tính thay vì Hub
                game.setScreen(new Trang2_SelectGenderScreen(game));
            }
        }
    }

    @Override
    public void dispose() {
        bgMenu.dispose();
    }
}