package com.uet.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.files.FileHandle;

public class Trang7_ResultScreen extends ScreenAdapter {
    HanhTrinhDongSongGame game;
    boolean isSaved = false; 
    String ngay = "";
    String gio = "";

    public Trang7_ResultScreen(HanhTrinhDongSongGame game) { 
        this.game = game; 
        
        game.playerData.capNhatThoiGian();
        
        String[] parts = game.playerData.ngayGioChoi.split(" ");
        ngay = parts[0];
        gio = parts.length > 1 ? parts[1] : "00:00";
    }

    // Cập nhật tham số luuLichSuChoi
    private void luuLichSuChoi(String ngay, String gio, String gioiTinh, int dongVat, int bong, int rac) {
        FileHandle file = Gdx.files.local("LichSuChoi.txt"); 
        String duLieu = "--- LAN CHOI MOI ---\n"
                      + "Ngay: " + ngay + "\n"
                      + "Gio: " + gio + "\n"
                      + "Gioi tinh: " + gioiTinh + "\n"
                      + "Dong vat: " + dongVat + "\n"
                      + "Bong: " + bong + "\n"
                      + "Rac: " + rac + "\n\n";
        
        file.writeString(duLieu, true); 
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.2f, 0.3f, 0.3f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        String gioiTinh = game.playerData.gioiTinh;
        int dongVat = game.playerData.dongVatBat; // Cập nhật biến
        int bong = game.playerData.bongBat;
        int rac = game.playerData.racVot;         // Cập nhật biến rác

        if (!isSaved) {
            luuLichSuChoi(ngay, gio, gioiTinh, dongVat, bong, rac);
            isSaved = true;
        }

        game.batch.begin();
        game.font.draw(game.batch, "===== BANG KET QUA =====", 250, 800);
        game.font.draw(game.batch, "Ngay choi: " + ngay, 250, 750);
        game.font.draw(game.batch, "Gio choi: " + gio, 250, 710);
        game.font.draw(game.batch, "Gioi tinh: " + gioiTinh, 250, 670); 
        
        game.font.draw(game.batch, "--- THONG KE DU LIEU ---", 250, 600);
        game.font.draw(game.batch, "Dong vat: " + dongVat, 250, 550); // Cập nhật text hiển thị
        game.font.draw(game.batch, "Bong: " + bong, 250, 500);
        game.font.draw(game.batch, "Rac: " + rac, 250, 450);          // Hiển thị thêm dòng điểm rác
        
        game.font.draw(game.batch, "Da luu lich su vao file LichSuChoi.txt!", 250, 380); // Đẩy nhẹ text xuống dưới
        game.font.draw(game.batch, "Click de quay ve Trang Chu...", 300, 300);
        game.batch.end();

        if (Gdx.input.justTouched()) {
            game.playerData.dongVatBat = 0; // Reset biến
            game.playerData.bongBat = 0;
            game.playerData.racVot = 0;     // Reset rác
            game.playerData.tongDongVatXuatHien = 0;

            game.setScreen(new Trang1_StartScreen(game));
        }
    }
}