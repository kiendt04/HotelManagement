-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Máy chủ: 127.0.0.1
-- Thời gian đã tạo: Th10 22, 2025 lúc 06:27 PM
-- Phiên bản máy phục vụ: 10.4.32-MariaDB
-- Phiên bản PHP: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Cơ sở dữ liệu: `qlyksan`
--

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `bill`
--

CREATE TABLE `bill` (
  `id` int(5) NOT NULL,
  `room` varchar(5) NOT NULL,
  `user` varchar(13) NOT NULL,
  `check_in` datetime NOT NULL,
  `check_out` datetime NOT NULL,
  `total_time` double NOT NULL,
  `total_service` int(11) NOT NULL,
  `total` double NOT NULL,
  `exchange` double NOT NULL,
  `discount` double NOT NULL,
  `deposit` double NOT NULL,
  `actual_pay` double NOT NULL,
  `status` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Đang đổ dữ liệu cho bảng `bill`
--

INSERT INTO `bill` (`id`, `room`, `user`, `check_in`, `check_out`, `total_time`, `total_service`, `total`, `exchange`, `discount`, `deposit`, `actual_pay`, `status`) VALUES
(1, '303', '001304035000', '2025-07-11 00:00:00', '2025-07-12 00:00:00', 1500000, 15000, 1515000, 0, 0, 0, 0, 0),
(2, '303', '001304035000', '2025-07-11 00:00:00', '2025-07-12 00:00:00', 1500000, 15000, 1515000, 0, 0, 0, 0, 0),
(3, '304', '001304035000', '2025-07-11 00:00:00', '2025-07-12 00:00:00', 2000000, 45000, 2045000, 0, 0, 0, 0, 0),
(4, '403', '001304035000', '2025-07-11 00:00:00', '2025-07-12 00:00:00', 1500000, 50000, 1550000, 0, 0, 0, 0, 0),
(5, '101', '001304035000', '2025-07-12 00:00:00', '2025-07-17 00:00:00', 4500000, 80000, 4580000, 0, 0, 0, 0, 0),
(6, '202', '001304035000', '2025-10-06 00:00:00', '2025-10-07 00:00:00', 1300000, 80000, 1380000, 0, 0, 0, 0, 0),
(7, '203', '001304035000', '2025-07-11 00:00:00', '2025-07-11 00:00:00', 900000, 65000, 965000, 0, 0, 0, 0, 0),
(8, '302', '001304035000', '2025-09-24 00:00:00', '2025-09-25 00:00:00', 1300000, 50000, 1350000, 0, 0, 0, 0, 0),
(9, '201', '001304035000', '2025-10-06 00:00:00', '2025-10-07 00:00:00', 900000, 30000, 930000, 0, 0, 0, 0, 0),
(10, '101', '001304035000', '2025-10-06 00:00:00', '2025-10-08 00:00:00', 1800000, 0, 1800000, 0, 0, 0, 0, 0),
(11, '101', '001304035000', '2025-10-08 00:00:00', '2025-10-09 00:00:00', 100000, 65000, 165000, 0, 0, 0, 165000, 0),
(12, '501', '026204004100', '2025-10-22 14:14:22', '2025-10-23 14:14:24', 500000, 255000, 755000, 0, 0, 0, 755000, 1),
(13, '101', '001304035000', '2025-10-22 14:27:22', '2025-10-23 14:27:24', 500000, 45000, 545000, 0, 0, 0, 545000, 1);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `billgroupbooking`
--

CREATE TABLE `billgroupbooking` (
  `id` int(11) NOT NULL,
  `customer` varchar(13) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `check_in` datetime NOT NULL,
  `check_out` datetime NOT NULL,
  `total_room` double NOT NULL,
  `total_service` double NOT NULL,
  `total` double NOT NULL,
  `extra_charge` double NOT NULL,
  `discount` double NOT NULL,
  `deposit` double NOT NULL,
  `actual_pay` double NOT NULL,
  `status` int(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Đang đổ dữ liệu cho bảng `billgroupbooking`
--

INSERT INTO `billgroupbooking` (`id`, `customer`, `check_in`, `check_out`, `total_room`, `total_service`, `total`, `extra_charge`, `discount`, `deposit`, `actual_pay`, `status`) VALUES
(1, '001304035643', '2025-10-27 11:07:12', '2025-11-03 11:07:12', 17500000, 150000, 17650000, 0, 353000, 8648500, 17297000, -1),
(2, '026204004100', '2025-10-27 11:18:57', '2025-11-03 11:18:57', 26250000, 180000, 26430000, 0, 528600, 12950700, 25901400, -1),
(3, '026204004957', '2025-10-27 11:20:10', '2025-11-03 11:20:10', 15750000, 0, 15750000, 0, 315000, 7717500, 15435000, -1),
(4, '001304035964', '2025-10-27 11:23:04', '2025-11-03 11:23:04', 24500000, 0, 24500000, 0, 490000, 12005000, 24010000, -1),
(5, '026204004635', '2025-10-27 11:24:31', '2025-11-03 11:24:31', 28000000, 150000, 28150000, 0, 563000, 13793500, 27587000, -1),
(6, '026204004957', '2025-10-27 11:38:57', '2025-11-03 11:38:57', 26250000, 290000, 26540000, 0, 530800, 13004600, 26009200, -1),
(7, '001304035964', '2025-10-29 01:28:40', '2025-11-05 01:28:40', 29750000, 665000, 30415000, 0, 608300, 14903350, 29806700, -1),
(8, '026204004957', '2025-10-29 02:04:44', '2025-11-05 02:04:44', 38500000, 275000, 38775000, 0, 775500, 18999750, 18999750, -2),
(9, '026204004100', '2025-10-29 01:39:44', '2025-11-05 01:39:44', 21000000, 510000, 21510000, 0, 430200, 10539900, 21079800, -1);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `billgroupbookingdetail`
--

CREATE TABLE `billgroupbookingdetail` (
  `id` int(11) NOT NULL,
  `room` varchar(5) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `service` int(5) NOT NULL,
  `quantity` int(3) NOT NULL,
  `total` double NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Đang đổ dữ liệu cho bảng `billgroupbookingdetail`
--

INSERT INTO `billgroupbookingdetail` (`id`, `room`, `service`, `quantity`, `total`) VALUES
(8, '101', 1, 10, 150000),
(8, '202', 4, 2, 30000),
(8, '302', 3, 1, 20000),
(8, '302', 4, 2, 30000),
(8, '304', 2, 3, 45000),
(9, '101', 5, 5, 75000),
(9, '201', 1, 5, 75000),
(9, '301', 2, 5, 75000),
(9, '401', 4, 10, 150000),
(9, '402', 3, 3, 60000),
(9, '402', 6, 5, 75000);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `billgroupbookingdetail_room`
--

CREATE TABLE `billgroupbookingdetail_room` (
  `id` int(11) NOT NULL,
  `room` varchar(5) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `time` int(1) NOT NULL,
  `total` double NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Đang đổ dữ liệu cho bảng `billgroupbookingdetail_room`
--

INSERT INTO `billgroupbookingdetail_room` (`id`, `room`, `time`, `total`) VALUES
(5, '201', 7, 3500000),
(5, '203', 7, 5250000),
(5, '301', 7, 3500000),
(5, '303', 7, 5250000),
(6, '201', 7, 3500000),
(6, '202', 7, 7000000),
(6, '203', 7, 5250000),
(6, '204', 7, 10500000),
(8, '101', 7, 3500000),
(8, '202', 7, 7000000),
(8, '204', 7, 10500000),
(8, '302', 7, 7000000),
(8, '304', 7, 10500000),
(9, '101', 7, 3500000),
(9, '201', 7, 3500000),
(9, '301', 7, 3500000),
(9, '401', 7, 3500000),
(9, '402', 7, 7000000);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `bill_detail`
--

CREATE TABLE `bill_detail` (
  `id_bill` int(11) NOT NULL,
  `service` int(11) NOT NULL,
  `quantity` int(11) NOT NULL,
  `total_service` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Đang đổ dữ liệu cho bảng `bill_detail`
--

INSERT INTO `bill_detail` (`id_bill`, `service`, `quantity`, `total_service`) VALUES
(1, 4, 1, 15000),
(2, 4, 1, 15000),
(3, 4, 3, 45000),
(4, 3, 1, 20000),
(4, 5, 1, 15000),
(4, 6, 1, 15000),
(5, 1, 1, 15000),
(5, 2, 1, 15000),
(5, 3, 1, 20000),
(5, 4, 2, 30000),
(6, 3, 1, 20000),
(6, 5, 1, 15000),
(7, 3, 1, 20000),
(7, 4, 2, 30000),
(7, 5, 1, 15000),
(8, 2, 1, 15000),
(8, 3, 1, 20000),
(8, 5, 1, 15000),
(9, 4, 1, 15000),
(9, 5, 1, 15000),
(11, 2, 1, 15000),
(11, 3, 1, 20000),
(11, 4, 1, 15000),
(11, 5, 1, 15000),
(12, 2, 10, 150000),
(12, 4, 4, 60000),
(12, 5, 3, 45000),
(13, 6, 3, 45000);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `customer`
--

CREATE TABLE `customer` (
  `cccd` varchar(13) NOT NULL,
  `name` varchar(30) NOT NULL,
  `gt` int(11) NOT NULL,
  `sdt` varchar(12) NOT NULL,
  `region` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Đang đổ dữ liệu cho bảng `customer`
--

INSERT INTO `customer` (`cccd`, `name`, `gt`, `sdt`, `region`) VALUES
('001304035000', 'Nguyen Thu Trang', 0, '0382572004', 'Vietnam'),
('001304035643', 'Doan Van Huan', 1, '0349980138', 'VietNam'),
('001304035964', 'Nguyen Phuong Anh', 0, '0966976033', 'VietNam'),
('026204004100', 'Tran Thuy Linh', 0, '0358002157', 'VietNam'),
('026204004635', 'Duong Duc Tu', 1, '0355158167', 'VietNam'),
('026204004957', 'Do Trung Kien', 1, '0358002157', 'VietNam');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `discount`
--

CREATE TABLE `discount` (
  `code` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `percent` int(2) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `floor`
--

CREATE TABLE `floor` (
  `id` int(11) NOT NULL,
  `name` varchar(30) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Đang đổ dữ liệu cho bảng `floor`
--

INSERT INTO `floor` (`id`, `name`) VALUES
(1, 'Tầng 1'),
(2, 'Tầng 2'),
(3, 'Tầng 3'),
(4, 'Tầng 4'),
(5, 'Tầng 5');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `room`
--

CREATE TABLE `room` (
  `Id` int(5) NOT NULL,
  `Number` varchar(5) NOT NULL,
  `floor` int(11) NOT NULL,
  `Type` int(2) NOT NULL,
  `Status` int(1) NOT NULL,
  `Note` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Đang đổ dữ liệu cho bảng `room`
--

INSERT INTO `room` (`Id`, `Number`, `floor`, `Type`, `Status`, `Note`) VALUES
(1, '101', 1, 1, 1, ''),
(2, '102', 1, 2, 0, ''),
(3, '103', 1, 3, 0, ''),
(4, '104', 1, 4, 0, ''),
(5, '105', 1, 5, 0, ''),
(6, '201', 2, 1, 0, ''),
(7, '202', 2, 2, 0, ''),
(8, '203', 2, 3, 0, ''),
(9, '204', 2, 4, 0, ''),
(10, '205', 2, 5, 0, ''),
(11, '301', 3, 1, 0, ''),
(12, '302', 3, 2, 0, ''),
(13, '303', 3, 3, 0, ''),
(14, '304', 3, 4, 0, ''),
(15, '305', 3, 5, 0, ''),
(16, '401', 4, 1, 0, ''),
(17, '402', 4, 2, 0, ''),
(18, '403', 4, 3, 0, ''),
(19, '404', 4, 4, 0, ''),
(20, '405', 4, 5, 0, ''),
(21, '501', 5, 1, 1, '');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `room_type`
--

CREATE TABLE `room_type` (
  `id` int(11) NOT NULL,
  `name` varchar(255) NOT NULL,
  `bed` int(11) NOT NULL,
  `price_per_hour` double NOT NULL,
  `price_per_night` double NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Đang đổ dữ liệu cho bảng `room_type`
--

INSERT INTO `room_type` (`id`, `name`, `bed`, `price_per_hour`, `price_per_night`) VALUES
(1, 'single_normal', 1, 100000, 500000),
(2, 'single_vip', 1, 200000, 1000000),
(3, 'double_normal', 2, 150000, 750000),
(4, 'double_vip', 2, 300000, 1500000),
(5, 'Vvip', 2, 500000, 2500000);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `service`
--

CREATE TABLE `service` (
  `id` int(5) NOT NULL,
  `name` varchar(255) NOT NULL,
  `quantity` int(11) NOT NULL,
  `price` double NOT NULL,
  `unit` varchar(10) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Đang đổ dữ liệu cho bảng `service`
--

INSERT INTO `service` (`id`, `name`, `quantity`, `price`, `unit`) VALUES
(1, 'Coca', 0, 15000, 'Lon'),
(2, 'Pepsi1', 0, 15000, 'Lon'),
(3, 'RedBull', 0, 20000, 'Lon'),
(4, '7Up', 0, 15000, 'Lon'),
(5, 'Fanta', 0, 15000, 'Lon'),
(6, 'Mỳ Ly', 0, 15000, 'Hop');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `user`
--

CREATE TABLE `user` (
  `id` int(11) NOT NULL,
  `name` varchar(30) NOT NULL,
  `pass` varchar(255) NOT NULL,
  `role` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Đang đổ dữ liệu cho bảng `user`
--

INSERT INTO `user` (`id`, `name`, `pass`, `role`) VALUES
(1, 'ADMIN', '$2a$10$RYMbGSaXKsTCIrHbyJ1R.ePE/F.CrP8c.66iblrOe7mDE6iPfAJ/m', 1),
(2, 'STAFF', '$2a$10$eQRk0uedp83Qx9f.lBeu/.vozQeIdC/8jxnHEy2rEyQ6NxrMMERsy', 0);

--
-- Chỉ mục cho các bảng đã đổ
--

--
-- Chỉ mục cho bảng `bill`
--
ALTER TABLE `bill`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FK_billroom` (`room`);

--
-- Chỉ mục cho bảng `billgroupbooking`
--
ALTER TABLE `billgroupbooking`
  ADD PRIMARY KEY (`id`),
  ADD KEY `FK_CustomerGroup` (`customer`);

--
-- Chỉ mục cho bảng `billgroupbookingdetail`
--
ALTER TABLE `billgroupbookingdetail`
  ADD PRIMARY KEY (`id`,`room`,`service`),
  ADD KEY `FK_groupService` (`service`),
  ADD KEY `FK_groupRoom` (`room`);

--
-- Chỉ mục cho bảng `billgroupbookingdetail_room`
--
ALTER TABLE `billgroupbookingdetail_room`
  ADD PRIMARY KEY (`id`,`room`),
  ADD KEY `FK_detailRoomgroup` (`room`);

--
-- Chỉ mục cho bảng `bill_detail`
--
ALTER TABLE `bill_detail`
  ADD PRIMARY KEY (`id_bill`,`service`),
  ADD KEY `service` (`service`);

--
-- Chỉ mục cho bảng `customer`
--
ALTER TABLE `customer`
  ADD PRIMARY KEY (`cccd`);

--
-- Chỉ mục cho bảng `discount`
--
ALTER TABLE `discount`
  ADD PRIMARY KEY (`code`);

--
-- Chỉ mục cho bảng `floor`
--
ALTER TABLE `floor`
  ADD PRIMARY KEY (`id`);

--
-- Chỉ mục cho bảng `room`
--
ALTER TABLE `room`
  ADD PRIMARY KEY (`Id`),
  ADD UNIQUE KEY `Number` (`Number`),
  ADD KEY `FK_roomType` (`Type`),
  ADD KEY `FK_floor` (`floor`);

--
-- Chỉ mục cho bảng `room_type`
--
ALTER TABLE `room_type`
  ADD PRIMARY KEY (`id`);

--
-- Chỉ mục cho bảng `service`
--
ALTER TABLE `service`
  ADD PRIMARY KEY (`id`);

--
-- Chỉ mục cho bảng `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT cho các bảng đã đổ
--

--
-- AUTO_INCREMENT cho bảng `bill`
--
ALTER TABLE `bill`
  MODIFY `id` int(5) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=14;

--
-- AUTO_INCREMENT cho bảng `billgroupbooking`
--
ALTER TABLE `billgroupbooking`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=10;

--
-- AUTO_INCREMENT cho bảng `floor`
--
ALTER TABLE `floor`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- AUTO_INCREMENT cho bảng `room`
--
ALTER TABLE `room`
  MODIFY `Id` int(5) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=22;

--
-- AUTO_INCREMENT cho bảng `room_type`
--
ALTER TABLE `room_type`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT cho bảng `service`
--
ALTER TABLE `service`
  MODIFY `id` int(5) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- AUTO_INCREMENT cho bảng `user`
--
ALTER TABLE `user`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- Các ràng buộc cho các bảng đã đổ
--

--
-- Các ràng buộc cho bảng `bill`
--
ALTER TABLE `bill`
  ADD CONSTRAINT `FK_billroom` FOREIGN KEY (`room`) REFERENCES `room` (`Number`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Các ràng buộc cho bảng `billgroupbooking`
--
ALTER TABLE `billgroupbooking`
  ADD CONSTRAINT `FK_CustomerGroup` FOREIGN KEY (`customer`) REFERENCES `customer` (`cccd`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Các ràng buộc cho bảng `billgroupbookingdetail`
--
ALTER TABLE `billgroupbookingdetail`
  ADD CONSTRAINT `FK_groupRoom` FOREIGN KEY (`room`) REFERENCES `room` (`Number`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `FK_groupService` FOREIGN KEY (`service`) REFERENCES `service` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `FK_idserGroup` FOREIGN KEY (`id`) REFERENCES `billgroupbooking` (`id`);

--
-- Các ràng buộc cho bảng `billgroupbookingdetail_room`
--
ALTER TABLE `billgroupbookingdetail_room`
  ADD CONSTRAINT `FK_detailRoomgroup` FOREIGN KEY (`room`) REFERENCES `room` (`Number`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `FK_idroomGroup` FOREIGN KEY (`id`) REFERENCES `billgroupbooking` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Các ràng buộc cho bảng `bill_detail`
--
ALTER TABLE `bill_detail`
  ADD CONSTRAINT `billdetail` FOREIGN KEY (`id_bill`) REFERENCES `bill` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `service` FOREIGN KEY (`service`) REFERENCES `service` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Các ràng buộc cho bảng `room`
--
ALTER TABLE `room`
  ADD CONSTRAINT `FK_floor` FOREIGN KEY (`floor`) REFERENCES `floor` (`id`),
  ADD CONSTRAINT `FK_roomType` FOREIGN KEY (`Type`) REFERENCES `room_type` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
