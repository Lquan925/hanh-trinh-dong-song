package com.uet.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx; // Khai báo thư viện Gdx
import com.badlogic.gdx.audio.Music; // Khai báo thư viện Music
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class HanhTrinhDongSongGame extends Game {
    public SpriteBatch batch;
    public BitmapFont font;
    public PlayerData playerData;
    
    // 1. KHAI BÁO BIẾN NHẠC NỀN
    public Music bgMusic; 

    @Override
    public void create() {
        batch = new SpriteBatch();
        font = new BitmapFont(); 
        playerData = new PlayerData();
        
        // 2. KHỞI TẠO VÀ CẤU HÌNH NHẠC
        // Đảm bảo tên file "nhac_nen.mp3" khớp chính xác với file trong thư mục assets
        bgMusic = Gdx.audio.newMusic(Gdx.files.internal("nhacnen.mp3")); 
        
        bgMusic.setLooping(true); // Đặt true để nhạc tự động lặp lại khi hết bài
        bgMusic.setVolume(0.5f);  // Điều chỉnh âm lượng từ 0.0 (im lặng) đến 1.0 (to nhất)
        bgMusic.play();           // Bắt đầu phát nhạc

        // Gọi màn hình Hub
        setScreen(new Trang1_StartScreen(this));
    }
    
    @Override
    public void dispose() {
        batch.dispose();
        font.dispose();
        
        // 3. GIẢI PHÓNG BỘ NHỚ
        // Bắt buộc phải dispose nhạc khi tắt game để tránh rò rỉ bộ nhớ (memory leak)
        if (bgMusic != null) {
            bgMusic.dispose(); 
        }
    }
}