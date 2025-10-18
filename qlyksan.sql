-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Máy chủ: 127.0.0.1
-- Thời gian đã tạo: Th10 18, 2025 lúc 07:34 AM
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
  `actual_pay` double NOT NULL,
  `status` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Đang đổ dữ liệu cho bảng `bill`
--

INSERT INTO `bill` (`id`, `room`, `user`, `check_in`, `check_out`, `total_time`, `total_service`, `total`, `exchange`, `discount`, `actual_pay`, `status`) VALUES
(1, '303', '001304035000', '2025-07-11 00:00:00', '2025-07-12 00:00:00', 1500000, 15000, 1515000, 0, 0, 0, 0),
(2, '303', '001304035000', '2025-07-11 00:00:00', '2025-07-12 00:00:00', 1500000, 15000, 1515000, 0, 0, 0, 0),
(3, '304', '001304035000', '2025-07-11 00:00:00', '2025-07-12 00:00:00', 2000000, 45000, 2045000, 0, 0, 0, 0),
(4, '403', '001304035000', '2025-07-11 00:00:00', '2025-07-12 00:00:00', 1500000, 50000, 1550000, 0, 0, 0, 0),
(5, '101', '001304035000', '2025-07-12 00:00:00', '2025-07-17 00:00:00', 4500000, 80000, 4580000, 0, 0, 0, 0),
(6, '202', '001304035000', '2025-10-06 00:00:00', '2025-10-07 00:00:00', 1300000, 80000, 1380000, 0, 0, 0, 0),
(7, '203', '001304035000', '2025-07-11 00:00:00', '2025-07-11 00:00:00', 900000, 65000, 965000, 0, 0, 0, 0),
(8, '302', '001304035000', '2025-09-24 00:00:00', '2025-09-25 00:00:00', 1300000, 50000, 1350000, 0, 0, 0, 0),
(9, '201', '001304035000', '2025-10-06 00:00:00', '2025-10-07 00:00:00', 900000, 30000, 930000, 0, 0, 0, 0),
(10, '101', '001304035000', '2025-10-06 00:00:00', '2025-10-08 00:00:00', 1800000, 0, 1800000, 0, 0, 0, 0),
(11, '101', '001304035000', '2025-10-08 00:00:00', '2025-10-09 00:00:00', 100000, 65000, 165000, 0, 0, 165000, 0);

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
(11, 5, 1, 15000);

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
(1, '101', 1, 1, 0, ''),
(2, '102', 1, 2, 0, ''),
(3, '103', 1, 3, 0, ''),
(4, '104', 1, 4, 0, ''),
(5, '105', 1, 5, 0, ''),
(6, '201', 2, 1, 0, ''),
(7, '202', 2, 2, 0, ''),
(8, '203', 2, 1, 0, ''),
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
(21, '501', 5, 1, 0, '');

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
(1, 'Coca', 0, 15000, ''),
(2, 'Pepsi1', 0, 15000, ''),
(3, 'RedBull', 0, 20000, ''),
(4, '7Up', 0, 15000, ''),
(5, 'Fanta', 0, 15000, ''),
(6, 'Mỳ Ly', 0, 15000, '');

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
  MODIFY `id` int(5) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=12;

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
