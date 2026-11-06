package com.uet.game;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class PlayerData {
    public String tenNhanVat = "Chua dat ten"; 
    public String gioiTinh = "Chua chon"; 
    public String ngayGioChoi; 

    // Thống kê game rút gọn
    public int dongVatBat = 0; // Đổi từ caBat
    public int bongBat = 0;
    public int racVot = 0;     // Thêm biến đếm rác
    public int tongDongVatXuatHien = 0; // Đổi từ tongCaXuatHien

    // Thống kê Game Dãy chữ
    public int chuDung = 0; 
    public int chuSaiTrai = 0, chuSaiPhai = 0; 

    public PlayerData() {
        capNhatThoiGian();
    }

    public void capNhatThoiGian() {
        LocalDateTime dongHo = LocalDateTime.now();
        DateTimeFormatter dinhDang = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        this.ngayGioChoi = dongHo.format(dinhDang);
    }

    public float getTyLeDongVatTrai() {
        if (tongDongVatXuatHien == 0) return 0f;
        return ((float) dongVatBat / tongDongVatXuatHien) * 100;
    }
}