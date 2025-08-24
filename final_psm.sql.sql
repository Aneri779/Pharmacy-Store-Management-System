-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Aug 22, 2025 at 08:06 PM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `pharmacy_store_management`
--

DELIMITER $$
--
-- Procedures
--
CREATE DEFINER=`root`@`localhost` PROCEDURE `ADDCUSTOMER` (IN `c_name` VARCHAR(100), IN `c_email` VARCHAR(255), IN `c_phone` VARCHAR(50), IN `c_address` VARCHAR(255))   BEGIN
INSERT INTO customers (name,email,phone,address) values 
(c_name,c_email,c_phone,c_address);
END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `DELETECUSTOMER` (IN `c_id` INT)   BEGIN
DELETE FROM customers WHERE customer_id=c_id;
END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `UPDATECUSTOMER` (IN `c_id` INT, IN `c_name` VARCHAR(100), IN `c_email` VARCHAR(100), IN `c_phone` VARCHAR(20), IN `c_address` VARCHAR(255))   BEGIN
UPDATE customers SET name=c_name,email=c_email,phone=c_phone,address=
c_address WHERE customer_id=c_id;
END$$

CREATE DEFINER=`root`@`localhost` PROCEDURE `UPDATEPRODUCT` (IN `p_id` INT, IN `p_name` VARCHAR(100), IN `p_category` VARCHAR(50), IN `p_description` VARCHAR(255), IN `p_price` DECIMAL(10,2), IN `p_quantity_in_stock` INT, IN `p_reorder_level` INT, IN `p_expiry_date` DATE)   BEGIN
UPDATE products set name=p_name,category=p_category,
description=p_description,price=p_price,quantity_in_stock=p_quantity_in_stock,
reorder_level=p_reorder_level,expiry_date=p_expiry_date where product_id=p_id;
END$$

DELIMITER ;

-- --------------------------------------------------------

--
-- Table structure for table `customerfiles`
--

CREATE TABLE `customerfiles` (
  `file_id` int(11) NOT NULL,
  `file_name` varchar(255) DEFAULT NULL,
  `file_data` longtext DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `customerfiles`
--

INSERT INTO `customerfiles` (`file_id`, `file_name`, `file_data`, `created_at`) VALUES
(8, 'customers_export.csv', '+----+------------------------------+------------------------------+-------------+--------\n| ID | Name                         | Email                        | Phone       | Address\n+----+------------------------------+------------------------------+-------------+--------\n| 8  | a                            | A@a                          | 1222222221  | aaa                          \n| 1  | Alice Johnson                | alice.johnson@example.com    | 9876543210  | 123 Elm Street               \n| 2  | Bob Smith                    | bob.smith@example.com        | 9123456789  | 456 Oak Avenue               \n| 3  | Charlie Brown                | charlie.brown@example.com    | 9012345678  | 789 Pine Lane                \n| 4  | Diana Prince                 | diana.prince@example.com     | 9988776655  | 321 Maple Drive              \n| 5  | Ethan Hunt                   | ethan.hunt@example.com       | 9090909090  | 654 Cedar Road               \n+----+------------------------------+------------------------------+-------------+--------\n', '2025-08-22 10:40:37'),
(9, 'customers_export.txt', '+----+------------------------------+------------------------------+-------------+--------\n| ID | Name                         | Email                        | Phone       | Address\n+----+------------------------------+------------------------------+-------------+--------\n| 8  | a                            | A@a                          | 1222222221  | aaa                          \n| 1  | Alice Johnson                | alice.johnson@example.com    | 9876543210  | 123 Elm Street               \n| 2  | Bob Smith                    | bob.smith@example.com        | 9123456789  | 456 Oak Avenue               \n| 3  | Charlie Brown                | charlie.brown@example.com    | 9012345678  | 789 Pine Lane                \n| 4  | Diana Prince                 | diana.prince@example.com     | 9988776655  | 321 Maple Drive              \n| 5  | Ethan Hunt                   | ethan.hunt@example.com       | 9090909090  | 654 Cedar Road               \n+----+------------------------------+------------------------------+-------------+--------\n', '2025-08-22 10:41:19'),
(10, 'customers_export.txt', '+----+------------------------------+------------------------------+-------------+--------| ID | Name                         | Email                        | Phone       | Address+----+------------------------------+------------------------------+-------------+--------| 8  | a                            | A@a                          | 1222222221  | aaa                          | 1  | Alice Johnson                | alice.johnson@example.com    | 9876543210  | 123 Elm Street               | 2  | Bob Smith                    | bob.smith@example.com        | 9123456789  | 456 Oak Avenue               | 3  | Charlie Brown                | charlie.brown@example.com    | 9012345678  | 789 Pine Lane                | 4  | Diana Prince                 | diana.prince@example.com     | 9988776655  | 321 Maple Drive              | 5  | Ethan Hunt                   | ethan.hunt@example.com       | 9090909090  | 654 Cedar Road               +----+------------------------------+------------------------------+-------------+--------', '2025-08-22 10:52:07'),
(11, 'customers_export.txt', '+----+------------------------------+------------------------------+-------------+--------| ID | Name                         | Email                        | Phone       | Address+----+------------------------------+------------------------------+-------------+--------| 8  | a                            | A@a                          | 1222222221  | aaa                          | 1  | Alice Johnson                | alice.johnson@example.com    | 9876543210  | 123 Elm Street               | 2  | Bob Smith                    | bob.smith@example.com        | 9123456789  | 456 Oak Avenue               | 3  | Charlie Brown                | charlie.brown@example.com    | 9012345678  | 789 Pine Lane                | 4  | Diana Prince                 | diana.prince@example.com     | 9988776655  | 321 Maple Drive              | 5  | Ethan Hunt                   | ethan.hunt@example.com       | 9090909090  | 654 Cedar Road               +----+------------------------------+------------------------------+-------------+--------', '2025-08-22 10:59:55'),
(12, 'customers_export.txt', '+----+------------------------------+------------------------------+-------------+--------| ID | Name                         | Email                        | Phone       | Address+----+------------------------------+------------------------------+-------------+--------| 1  | Alice Johnson                | alice.johnson@example.com    | 9876543210  | 123 Elm Street               | 2  | Bob Smith                    | bob.smith@example.com        | 9123456789  | 456 Oak Avenue               | 3  | Charlie Brown                | charlie.brown@example.com    | 9012345678  | 789 Pine Lane                | 4  | Diana Prince                 | diana.prince@example.com     | 9988776655  | 321 Maple Drive              | 5  | Ethan Hunt                   | ethan.hunt@example.com       | 9090909090  | 654 Cedar Road               +----+------------------------------+------------------------------+-------------+--------', '2025-08-22 11:40:53'),
(13, 'customers_export.csv', '+----+------------------------------+------------------------------+-------------+--------\r\n| ID | Name                         | Email                        | Phone       | Address\r\n+----+------------------------------+------------------------------+-------------+--------\r\n| 10 | a                            | b                            | 123         | aa                           \r\n| 11 | a                            | A@a                          | 123456789m  | a                            \r\n| 1  | Alice Johnson                | alice.johnson@example.com    | 9876543210  | 123 Elm Street               \r\n| 2  | Bob Smith                    | bob.smith@example.com        | 9123456789  | 456 Oak Avenue               \r\n| 3  | Charlie Brown                | charlie.brown@example.com    | 9012345678  | 789 Pine Lane                \r\n| 4  | Diana Prince                 | diana.prince@example.com     | 9988776655  | 321 Maple Drive              \r\n| 5  | Ethan Hunt                   | ethan.hunt@example.com       | 9090909090  | 654 Cedar Road               \r\n+----+------------------------------+------------------------------+-------------+--------\r\n', '2025-08-22 17:16:18');

-- --------------------------------------------------------

--
-- Table structure for table `customers`
--

CREATE TABLE `customers` (
  `customer_id` int(11) NOT NULL,
  `name` varchar(100) NOT NULL,
  `email` varchar(100) DEFAULT NULL,
  `phone` varchar(20) DEFAULT NULL,
  `address` varchar(255) DEFAULT NULL,
  `loyalty_points` int(11) DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `customers`
--

INSERT INTO `customers` (`customer_id`, `name`, `email`, `phone`, `address`, `loyalty_points`) VALUES
(1, 'Alice Johnson', 'alice.johnson@example.com', '9876543210', '123 Elm Street', 66),
(2, 'Bob Smith', 'bob.smith@example.com', '9123456789', '456 Oak Avenue', 30),
(3, 'Charlie Brown', 'charlie.brown@example.com', '9012345678', '789 Pine Lane', 70),
(4, 'Diana Prince', 'diana.prince@example.com', '9988776655', '321 Maple Drive', 90),
(5, 'Ethan Hunt', 'ethan.hunt@example.com', '9090909090', '654 Cedar Road', 110),
(10, 'a', 'b', '123', 'aa', 0),
(11, 'a', 'A@a', '123456789m', 'a', 0);

--
-- Triggers `customers`
--
DELIMITER $$
CREATE TRIGGER `after_customer_delete` AFTER DELETE ON `customers` FOR EACH ROW INSERT INTO TriggerLog (table_name, operation, record_id, details)
VALUES (
    'Customers',
    'DELETE',
    OLD.customer_id,
    CONCAT('Customer Deleted: ', OLD.name, ', Email: ', COALESCE(OLD.email, 'N/A'))
)
$$
DELIMITER ;
DELIMITER $$
CREATE TRIGGER `after_customer_insert` AFTER INSERT ON `customers` FOR EACH ROW INSERT INTO TriggerLog (table_name, operation, record_id, details)
VALUES (
    'Customers',
    'INSERT',
    NEW.customer_id,
    CONCAT('Customer Added: ', NEW.name, ', Email: ', COALESCE(NEW.email, 'N/A'))
)
$$
DELIMITER ;
DELIMITER $$
CREATE TRIGGER `after_customer_update` AFTER UPDATE ON `customers` FOR EACH ROW INSERT INTO TriggerLog (table_name, operation, record_id, details)
VALUES (
    'Customers',
    'UPDATE',
    NEW.customer_id,
    CONCAT('Customer Updated: ', NEW.name, ', New Loyalty Points: ', NEW.loyalty_points)
)
$$
DELIMITER ;

-- --------------------------------------------------------

--
-- Table structure for table `prescriptions`
--

CREATE TABLE `prescriptions` (
  `prescription_id` int(11) NOT NULL,
  `customer_id` int(11) DEFAULT NULL,
  `doctor_name` varchar(100) DEFAULT NULL,
  `issue_date` date DEFAULT NULL,
  `expiry_date` date DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `prescriptions`
--

INSERT INTO `prescriptions` (`prescription_id`, `customer_id`, `doctor_name`, `issue_date`, `expiry_date`) VALUES
(101, 1, 'Dr. Rahul Sharma', '2025-08-01', '2025-08-31'),
(102, 2, 'Dr. Anjali Mehta', '2025-07-15', '2025-08-15'),
(103, 3, 'Dr. Vivek Patel', '2025-06-10', '2025-07-10'),
(104, 4, 'Dr. Sneha Roy', '2025-08-10', '2025-09-10'),
(105, 5, 'Dr. Arjun Nair', '2025-07-25', '2025-08-25');

-- --------------------------------------------------------

--
-- Table structure for table `products`
--

CREATE TABLE `products` (
  `product_id` int(11) NOT NULL,
  `name` varchar(100) NOT NULL,
  `category` varchar(50) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `price` decimal(10,2) NOT NULL,
  `quantity_in_stock` int(11) NOT NULL,
  `reorder_level` int(11) DEFAULT NULL,
  `expiry_date` date DEFAULT NULL,
  `supplier_id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `products`
--

INSERT INTO `products` (`product_id`, `name`, `category`, `description`, `price`, `quantity_in_stock`, `reorder_level`, `expiry_date`, `supplier_id`) VALUES
(1, 'Paracetamol 500mg', 'Pain Relief', 'Used to reduce fever and treat mild pain', 1.50, 88, 50, '2026-01-31', 1),
(2, 'Amoxicillin 250mg', 'Antibiotics', 'Broad-spectrum antibiotic for bacterial infections', 3.20, 150, 30, '2025-12-15', 2),
(3, 'Cough Syrup', 'Cold & Cough', 'Relieves cough and throat irritation', 2.75, 100, 20, '2025-10-10', 3),
(4, 'Vitamin C Tablets', 'Supplements', 'Boosts immune system and overall health', 4.00, 300, 40, NULL, NULL),
(5, 'Antacid Gel', 'Digestive Health', 'Neutralizes stomach acid and relieves heartburn', 2.00, 79, 25, '2026-03-01', 4),
(101, 'Test Product', NULL, NULL, 0.00, 3, 5, NULL, NULL),
(102, 'Aspirin 100mg', NULL, NULL, 2.50, 3, 10, NULL, NULL),
(103, 'Paracetamol 500mg', NULL, NULL, 1.80, 8, 8, NULL, NULL),
(104, 'Vitamin C Tablets', NULL, NULL, 3.00, 20, 15, NULL, NULL),
(110, 'pera', 'medi', 'pain', 3.00, 0, 5, NULL, NULL),
(111, 'a1', 'b1', 'b2', 3.00, 5, 5, NULL, NULL);

--
-- Triggers `products`
--
DELIMITER $$
CREATE TRIGGER `after_product_delete` AFTER DELETE ON `products` FOR EACH ROW INSERT INTO TriggerLog (table_name, operation, record_id, details)
VALUES (
    'Products',
    'DELETE',
    OLD.product_id,
    CONCAT('Product Deleted: ', OLD.name, ', Category: ', OLD.category)
)
$$
DELIMITER ;
DELIMITER $$
CREATE TRIGGER `after_product_insert` AFTER INSERT ON `products` FOR EACH ROW INSERT INTO TriggerLog (table_name, operation, record_id, details)
VALUES (
    'Products',
    'INSERT',
    NEW.product_id,
    CONCAT('Product Added: ', NEW.name, ', Category: ', NEW.category, ', Price: ', NEW.price)
)
$$
DELIMITER ;
DELIMITER $$
CREATE TRIGGER `after_product_update` AFTER UPDATE ON `products` FOR EACH ROW INSERT INTO TriggerLog (table_name, operation, record_id, details)
VALUES (
    'Products',
    'UPDATE',
    NEW.product_id,
    CONCAT('Product Updated: ', NEW.name, ', New Stock: ', NEW.quantity_in_stock)
)
$$
DELIMITER ;

-- --------------------------------------------------------

--
-- Table structure for table `saleitems`
--

CREATE TABLE `saleitems` (
  `sale_item_id` int(11) NOT NULL,
  `sale_id` int(11) DEFAULT NULL,
  `product_id` int(11) DEFAULT NULL,
  `quantity` int(11) NOT NULL,
  `unit_price` decimal(10,2) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `saleitems`
--

INSERT INTO `saleitems` (`sale_item_id`, `sale_id`, `product_id`, `quantity`, `unit_price`) VALUES
(1, 201, 1, 2, 75.00),
(2, 202, 2, 1, 230.50),
(3, 203, 3, 3, 33.33),
(4, 204, 4, 5, 35.05),
(5, 205, 5, 2, 60.00),
(6, 206, 1, 112, 1.50),
(7, 207, 5, 1, 2.00),
(8, 208, 110, 10, 3.00),
(9, 208, 110, 6, 3.00),
(10, 209, 111, 15, 3.00),
(11, 209, 110, 4, 3.00);

-- --------------------------------------------------------

--
-- Table structure for table `sales`
--

CREATE TABLE `sales` (
  `sale_id` int(11) NOT NULL,
  `customer_id` int(11) DEFAULT NULL,
  `sale_date` date NOT NULL,
  `total_amount` decimal(10,2) NOT NULL,
  `payment_method` varchar(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `sales`
--

INSERT INTO `sales` (`sale_id`, `customer_id`, `sale_date`, `total_amount`, `payment_method`) VALUES
(201, 1, '2025-08-10', 150.00, 'Cash'),
(202, 2, '2025-08-11', 230.50, 'Card'),
(203, 3, '2025-08-12', 99.99, 'Cash'),
(204, 4, '2025-08-13', 175.25, 'Card'),
(205, 5, '2025-08-14', 120.00, 'Cash'),
(206, 1, '2025-08-16', 168.00, 'Card'),
(207, 4, '2025-08-16', 2.00, 'Card'),
(208, NULL, '2025-08-22', 48.00, 'Card'),
(209, NULL, '2025-08-22', 57.00, 'Cash');

-- --------------------------------------------------------

--
-- Table structure for table `suppliers`
--

CREATE TABLE `suppliers` (
  `supplier_id` int(11) NOT NULL,
  `name` varchar(100) NOT NULL,
  `contact_person` varchar(100) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  `phone` varchar(20) DEFAULT NULL,
  `address` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `suppliers`
--

INSERT INTO `suppliers` (`supplier_id`, `name`, `contact_person`, `email`, `phone`, `address`) VALUES
(1, 'MediLife Pharma', 'Amit Verma', 'contact@medilife.com', '9876543210', '12 Health Park, Mumbai'),
(2, 'HealthFirst Supplies', 'Neha Kapoor', 'sales@healthfirst.com', '9123456789', '88 Wellness St, Delhi'),
(3, 'CurePlus Distributors', 'Ravi Sharma', 'info@cureplus.com', '9988776655', '56 Cure Lane, Bengaluru'),
(4, 'PharmaCare India', 'Pooja Desai', 'support@pharmacare.in', '9090909090', '104 Med Road, Hyderabad'),
(5, 'LifeLine Pharma', 'Rajeev Nair', 'raj@lifelinepharma.com', '9012345678', '78 Relief Ave, Chennai');

-- --------------------------------------------------------

--
-- Table structure for table `triggerlog`
--

CREATE TABLE `triggerlog` (
  `log_id` int(11) NOT NULL,
  `table_name` varchar(50) DEFAULT NULL,
  `operation` varchar(10) DEFAULT NULL,
  `record_id` int(11) DEFAULT NULL,
  `details` text DEFAULT NULL,
  `log_time` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `triggerlog`
--

INSERT INTO `triggerlog` (`log_id`, `table_name`, `operation`, `record_id`, `details`, `log_time`) VALUES
(10, 'Customers', 'INSERT', 1, 'Customer Added: Alice Johnson, Email: alice.johnson@example.com', '2025-08-15 13:47:26'),
(11, 'Customers', 'INSERT', 2, 'Customer Added: Bob Smith, Email: bob.smith@example.com', '2025-08-15 13:47:26'),
(12, 'Customers', 'INSERT', 3, 'Customer Added: Charlie Brown, Email: charlie.brown@example.com', '2025-08-15 13:47:26'),
(13, 'Customers', 'INSERT', 4, 'Customer Added: Diana Prince, Email: diana.prince@example.com', '2025-08-15 13:47:26'),
(14, 'Customers', 'INSERT', 5, 'Customer Added: Ethan Hunt, Email: ethan.hunt@example.com', '2025-08-15 13:47:26'),
(15, 'Products', 'INSERT', 1, 'Product Added: Paracetamol 500mg, Category: Pain Relief, Price: 1.50', '2025-08-15 13:50:36'),
(16, 'Products', 'INSERT', 2, 'Product Added: Amoxicillin 250mg, Category: Antibiotics, Price: 3.20', '2025-08-15 13:50:36'),
(17, 'Products', 'INSERT', 3, 'Product Added: Cough Syrup, Category: Cold & Cough, Price: 2.75', '2025-08-15 13:50:36'),
(18, 'Products', 'INSERT', 4, 'Product Added: Vitamin C Tablets, Category: Supplements, Price: 4.00', '2025-08-15 13:50:36'),
(19, 'Products', 'INSERT', 5, 'Product Added: Antacid Gel, Category: Digestive Health, Price: 2.00', '2025-08-15 13:50:36'),
(20, 'Products', 'INSERT', 101, NULL, '2025-08-16 08:06:26'),
(21, 'Products', 'UPDATE', 1, 'Product Updated: Paracetamol 500mg, New Stock: 88', '2025-08-16 08:36:44'),
(22, 'Customers', 'UPDATE', 1, 'Customer Updated: Alice Johnson, New Loyalty Points: 66', '2025-08-16 08:36:44'),
(23, 'Products', 'UPDATE', 5, 'Product Updated: Antacid Gel, New Stock: 79', '2025-08-16 09:08:23'),
(24, 'Products', 'INSERT', 102, NULL, '2025-08-16 09:38:14'),
(25, 'Products', 'INSERT', 103, NULL, '2025-08-16 09:38:14'),
(26, 'Products', 'INSERT', 104, NULL, '2025-08-16 09:38:14'),
(27, 'Products', 'INSERT', 105, 'Product Added: a, Category: ss, Price: 12.00', '2025-08-22 07:36:12'),
(28, 'Products', 'DELETE', 105, 'Product Deleted: a, Category: ss', '2025-08-22 07:37:21'),
(29, 'Products', 'INSERT', 106, 'Product Added: a, Category: b, Price: 12.00', '2025-08-22 07:37:55'),
(30, 'Products', 'DELETE', 106, 'Product Deleted: a, Category: b', '2025-08-22 07:38:33'),
(31, 'Products', 'INSERT', 107, 'Product Added: q, Category: b, Price: 12.00', '2025-08-22 09:09:22'),
(32, 'Products', 'UPDATE', 107, 'Product Updated: , New Stock: 0', '2025-08-22 09:17:52'),
(33, 'Products', 'UPDATE', 107, 'Product Updated: a1, New Stock: 0', '2025-08-22 09:18:05'),
(34, 'Products', 'UPDATE', 107, 'Product Updated: a1, New Stock: 21', '2025-08-22 09:24:47'),
(35, 'Products', 'UPDATE', 107, 'Product Updated: a12, New Stock: 20', '2025-08-22 09:36:07'),
(36, 'Products', 'UPDATE', 107, 'Product Updated: a12, New Stock: 20', '2025-08-22 09:37:23'),
(37, 'Products', 'UPDATE', 107, 'Product Updated: a123, New Stock: 11', '2025-08-22 09:53:58'),
(38, 'Products', 'UPDATE', 107, 'Product Updated: a13, New Stock: 12', '2025-08-22 09:57:31'),
(39, 'Customers', 'INSERT', 6, 'Customer Added: a, Email: ', '2025-08-22 10:18:23'),
(40, 'Customers', 'DELETE', 6, 'Customer Deleted: a, Email: ', '2025-08-22 10:18:31'),
(41, 'Customers', 'INSERT', 7, 'Customer Added: a, Email: a', '2025-08-22 10:19:03'),
(42, 'Customers', 'DELETE', 7, 'Customer Deleted: a, Email: a', '2025-08-22 10:19:29'),
(43, 'Customers', 'INSERT', 8, 'Customer Added: a, Email: A@a', '2025-08-22 10:21:01'),
(44, 'Customers', 'DELETE', 8, 'Customer Deleted: a, Email: A@a', '2025-08-22 11:29:06'),
(45, 'Customers', 'INSERT', 9, 'Customer Added: abc, Email: a@2', '2025-08-22 11:30:30'),
(46, 'Customers', 'DELETE', 9, 'Customer Deleted: abc, Email: a@2', '2025-08-22 11:31:52'),
(47, 'Customers', 'INSERT', 10, 'Customer Added: abc, Email: a@a', '2025-08-22 12:13:57'),
(48, 'Customers', 'UPDATE', 10, 'Customer Updated: 0, New Loyalty Points: 0', '2025-08-22 12:14:52'),
(49, 'Customers', 'UPDATE', 10, 'Customer Updated: abcd, New Loyalty Points: 0', '2025-08-22 12:16:28'),
(50, 'Customers', 'UPDATE', 10, 'Customer Updated: abcd, New Loyalty Points: 0', '2025-08-22 12:22:30'),
(51, 'Customers', 'UPDATE', 10, 'Customer Updated: a, New Loyalty Points: 0', '2025-08-22 12:26:47'),
(52, 'Products', 'INSERT', 108, 'Product Added: abcd, Category: abcd, Price: 12.00', '2025-08-22 13:17:45'),
(53, 'Products', 'UPDATE', 108, 'Product Updated: ab, New Stock: 11', '2025-08-22 13:18:28'),
(54, 'Products', 'DELETE', 108, 'Product Deleted: ab, Category: ab', '2025-08-22 13:18:43'),
(55, 'Customers', 'INSERT', 11, 'Customer Added: a, Email: A@a', '2025-08-22 13:34:55'),
(56, 'Products', 'INSERT', 109, 'Product Added: pera, Category: medicine, Price: 2.00', '2025-08-22 16:51:05'),
(57, 'Products', 'UPDATE', 107, 'Product Updated: a123, New Stock: 50', '2025-08-22 16:51:58'),
(58, 'Products', 'DELETE', 107, 'Product Deleted: a123, Category: medicine', '2025-08-22 16:54:59'),
(59, 'Products', 'UPDATE', 109, 'Product Updated: pera, New Stock: 80', '2025-08-22 16:55:51'),
(60, 'Customers', 'INSERT', 12, 'Customer Added: yash, Email: N/A', '2025-08-22 17:06:32'),
(61, 'Customers', 'UPDATE', 12, 'Customer Updated: Yash, New Loyalty Points: 0', '2025-08-22 17:15:41'),
(62, 'Customers', 'DELETE', 12, 'Customer Deleted: Yash, Email: ', '2025-08-22 17:16:04'),
(63, 'Products', 'INSERT', 110, 'Product Added: pera, Category: medi, Price: 3.00', '2025-08-22 17:21:11'),
(64, 'Products', 'DELETE', 109, 'Product Deleted: pera, Category: medicine', '2025-08-22 17:21:45'),
(65, 'Products', 'UPDATE', 110, 'Product Updated: pera, New Stock: 10', '2025-08-22 17:22:45'),
(66, 'Products', 'UPDATE', 110, 'Product Updated: pera, New Stock: 4', '2025-08-22 17:22:45'),
(67, 'Products', 'INSERT', 111, 'Product Added: a1, Category: b1, Price: 3.00', '2025-08-22 17:40:32'),
(68, 'Products', 'UPDATE', 111, 'Product Updated: a1, New Stock: 5', '2025-08-22 17:41:04'),
(69, 'Products', 'UPDATE', 110, 'Product Updated: pera, New Stock: 0', '2025-08-22 17:41:04');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `customerfiles`
--
ALTER TABLE `customerfiles`
  ADD PRIMARY KEY (`file_id`);

--
-- Indexes for table `customers`
--
ALTER TABLE `customers`
  ADD PRIMARY KEY (`customer_id`);

--
-- Indexes for table `prescriptions`
--
ALTER TABLE `prescriptions`
  ADD PRIMARY KEY (`prescription_id`),
  ADD KEY `customer_id` (`customer_id`);

--
-- Indexes for table `products`
--
ALTER TABLE `products`
  ADD PRIMARY KEY (`product_id`);

--
-- Indexes for table `saleitems`
--
ALTER TABLE `saleitems`
  ADD PRIMARY KEY (`sale_item_id`),
  ADD KEY `sale_id` (`sale_id`),
  ADD KEY `product_id` (`product_id`);

--
-- Indexes for table `sales`
--
ALTER TABLE `sales`
  ADD PRIMARY KEY (`sale_id`),
  ADD KEY `customer_id` (`customer_id`);

--
-- Indexes for table `suppliers`
--
ALTER TABLE `suppliers`
  ADD PRIMARY KEY (`supplier_id`);

--
-- Indexes for table `triggerlog`
--
ALTER TABLE `triggerlog`
  ADD PRIMARY KEY (`log_id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `customerfiles`
--
ALTER TABLE `customerfiles`
  MODIFY `file_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=14;

--
-- AUTO_INCREMENT for table `customers`
--
ALTER TABLE `customers`
  MODIFY `customer_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=13;

--
-- AUTO_INCREMENT for table `prescriptions`
--
ALTER TABLE `prescriptions`
  MODIFY `prescription_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=106;

--
-- AUTO_INCREMENT for table `products`
--
ALTER TABLE `products`
  MODIFY `product_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=112;

--
-- AUTO_INCREMENT for table `saleitems`
--
ALTER TABLE `saleitems`
  MODIFY `sale_item_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=12;

--
-- AUTO_INCREMENT for table `sales`
--
ALTER TABLE `sales`
  MODIFY `sale_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=210;

--
-- AUTO_INCREMENT for table `suppliers`
--
ALTER TABLE `suppliers`
  MODIFY `supplier_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT for table `triggerlog`
--
ALTER TABLE `triggerlog`
  MODIFY `log_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=70;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `prescriptions`
--
ALTER TABLE `prescriptions`
  ADD CONSTRAINT `prescriptions_ibfk_1` FOREIGN KEY (`customer_id`) REFERENCES `customers` (`customer_id`);

--
-- Constraints for table `saleitems`
--
ALTER TABLE `saleitems`
  ADD CONSTRAINT `saleitems_ibfk_1` FOREIGN KEY (`sale_id`) REFERENCES `sales` (`sale_id`),
  ADD CONSTRAINT `saleitems_ibfk_2` FOREIGN KEY (`product_id`) REFERENCES `products` (`product_id`);

--
-- Constraints for table `sales`
--
ALTER TABLE `sales`
  ADD CONSTRAINT `sales_ibfk_1` FOREIGN KEY (`customer_id`) REFERENCES `customers` (`customer_id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
