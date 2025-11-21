-- ================================================
-- Dữ liệu mẫu cho hệ thống Study_Space
-- ================================================

SET FOREIGN_KEY_CHECKS = 0;

TRUNCATE TABLE reservation_service;
TRUNCATE TABLE reservation_desk;
TRUNCATE TABLE reservation;
TRUNCATE TABLE service_inventory;
TRUNCATE TABLE service;
TRUNCATE TABLE desk;
TRUNCATE TABLE room;
TRUNCATE TABLE floor;
TRUNCATE TABLE customer;
TRUNCATE TABLE combo;

SET FOREIGN_KEY_CHECKS = 1;

-- ================================================
-- INSERT FLOOR (3 tầng)
-- ================================================
INSERT INTO floor (floor_id, floor_name) VALUES
                                             (1, 'Tầng 1'),
                                             (2, 'Tầng 2'),
                                             (3, 'Tầng 3');

-- ================================================
-- INSERT ROOM
-- ================================================
INSERT INTO room (room_id, floor_id, room_name, room_type, description) VALUES
                                                                            (1, 1, 'Phòng A1', 'PHONG_THUONG', 'Phòng 4 ghế'),
                                                                            (2, 1, 'Phòng A2', 'PHONG_THUONG', 'Phòng 2 ghế'),
                                                                            (3, 2, 'Phòng B1', 'PHONG_HOP', 'Phòng họp 15 ghế'),
                                                                            (4, 2, 'Phòng B2', 'PHONG_NGOI_BET', 'Ngồi bệt 1-2 người'),
                                                                            (5, 3, 'Ban Công C1', 'BAN_CONG', 'Không gian thoáng');

-- ================================================
-- INSERT DESK (bàn thuộc từng phòng)
-- ================================================
INSERT INTO desk (desk_id, room_id, desk_name, capacity, desk_type, description) VALUES
-- Phòng A1 (4 ghế)
(1, 1, 'A1-B1', 4, 'NHOM_3_4', 'Bàn 4 ghế'),
(2, 1, 'A1-B2', 4, 'NHOM_3_4', 'Bàn 4 ghế'),

-- Phòng A2 (2 ghế)
(3, 2, 'A2-B1', 2, 'CAP_DOI', 'Bàn 2 ghế'),
(4, 2, 'A2-B2', 2, 'CAP_DOI', 'Bàn 2 ghế'),

-- Phòng họp (15 ghế)
(5, 3, 'B1-Main', 15, 'NHOM_X', 'Bàn dài 15 ghế'),

-- Phòng ngồi bệt
(6, 4, 'B2-NB1', 2, 'CAP_DOI', 'Ngồi bệt 1-2 người'),
(7, 4, 'B2-NB2', 2, 'CAP_DOI', 'Ngồi bệt 1-2 người'),

-- Ban công
(8, 5, 'C1-H1', 1, 'CA_NHAN', 'Ngồi dọc lan can'),
(9, 5, 'C1-H2', 2, 'CAP_DOI', 'Ban công đôi');

-- ================================================
-- INSERT COMBO
-- ================================================
INSERT INTO combo (combo_id, combo_name, hours, price) VALUES
                                                           (1, 'Combo 4 giờ', 4, 40000),
                                                           (2, 'Combo 6 giờ', 6, 55000),
                                                           (3, 'Combo 9 giờ', 9, 75000);

-- ================================================
-- INSERT CUSTOMER (3 khách)
-- ================================================
INSERT INTO customer (customer_id, phone, name, note) VALUES
                                                          (1, '0912345678', 'Hoàng Quốc Anh', NULL),
                                                          (2, '0988776655', 'Nguyễn Văn A', NULL),
                                                          (3, '0369988123', 'Trần Thị B', 'Khách quen');

-- ================================================
-- INSERT SERVICE (dịch vụ kèm)
-- ================================================
INSERT INTO service (service_id, service_name, description, price) VALUES
                                                                       (1, 'Mượn máy chiếu', 'Máy chiếu HD', 20000),
                                                                       (2, 'Mượn bảng', 'Bảng viết + bút', 10000),
                                                                       (3, 'Ổ cắm thêm', 'Ổ cắm đa năng 6 lỗ', 5000);

-- ================================================
-- INSERT SERVICE INVENTORY (tồn kho dịch vụ)
-- ================================================
INSERT INTO service_inventory (service_inventory_id, service_id, quantity_available) VALUES
                                                                                         (1, 1, 3),   -- 3 máy chiếu
                                                                                         (2, 2, 10),  -- 10 bảng
                                                                                         (3, 3, 20);  -- 20 ổ cắm

-- ================================================
-- INSERT SAMPLE RESERVATIONS
-- ================================================
INSERT INTO reservation (reservation_id, customer_id, room_id, combo_id, startTime, endTime, peopleCount, note, createdAt)
VALUES
    (1, 1, 1, 1, '2025-11-22 00:00:00', '2025-11-22 04:00:00', 3, 'Đi nhóm 3', NOW()),
    (2, 2, 3, 3, '2025-11-22 09:00:00', '2025-11-22 18:00:00', 10, 'Họp nhóm', NOW()),
    (3, 3, 5, 2, '2025-11-22 14:00:00', '2025-11-22 20:00:00', 2, 'Học ban công', NOW());

-- ================================================
-- Thêm bàn vào reservation (ManyToMany)
-- ================================================
INSERT INTO reservation_desk (reservation_id, desk_id) VALUES
-- Reservation 1 (A1)
(1, 1),
-- Reservation 2 (Phòng họp)
(2, 5),
-- Reservation 3 (Ban công)
(3, 8);

-- ================================================
-- Dịch vụ đi kèm reservation (optional)
-- ================================================
INSERT INTO reservation_service (reservation_id, service_id, quantity) VALUES
                                                                           (1, 2, 1),   -- A1 mượn 1 bảng
                                                                           (2, 1, 1),   -- Họp mượn máy chiếu
                                                                           (3, 3, 1);   -- Ban công mượn 1 ổ cắm
