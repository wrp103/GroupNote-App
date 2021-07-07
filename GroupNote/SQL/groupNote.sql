SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `groupNote`
--
CREATE DATABASE IF NOT EXISTS `groupNote` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE `groupNote`;

-- --------------------------------------------------------

--
-- Table structure for table `group`
--

DROP TABLE IF EXISTS `group`;
CREATE TABLE `group` (
  `group_id` int(11) NOT NULL,
  `groupName` varchar(45) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `group`
--

INSERT INTO `group` (`group_id`, `groupName`) VALUES
(2, 'test group 1'),
(26, 'testgroup1'),
(27, 'testgroup1'),
(28, 'testgroup1'),
(29, 'test'),
(30, 'testgroup1'),
(31, 'testgroup1'),
(32, 'testgroup1'),
(33, 'testgroup1'),
(34, 'testgroup1');

-- --------------------------------------------------------

--
-- Table structure for table `group_note`
--

DROP TABLE IF EXISTS `group_note`;
CREATE TABLE `group_note` (
  `group_note_id` int(11) NOT NULL,
  `note_id_fk` int(11) DEFAULT NULL,
  `group_id_fk` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `group_note`
--

INSERT INTO `group_note` (`group_note_id`, `note_id_fk`, `group_id_fk`) VALUES
(74, 60, 2),
(76, 62, 2),
(77, 63, 2),
(78, 64, 2);

-- --------------------------------------------------------

--
-- Table structure for table `group_user`
--

DROP TABLE IF EXISTS `group_user`;
CREATE TABLE `group_user` (
  `group_user_id` int(11) NOT NULL,
  `user_id_fk` int(11) DEFAULT NULL,
  `group_id_fk` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `group_user`
--

INSERT INTO `group_user` (`group_user_id`, `user_id_fk`, `group_id_fk`) VALUES
(1, 30, 2),
(39, 30, 26),
(40, 22, 26),
(41, 30, 27),
(42, 22, 27),
(43, 30, 28),
(44, 22, 28),
(45, 30, 29),
(46, 22, 29),
(47, 30, 30),
(48, 22, 30),
(49, 30, 31),
(50, 22, 31),
(51, 19, 2),
(52, 17, 2),
(53, 30, 32),
(54, 22, 32),
(55, 30, 33),
(56, 22, 33),
(57, 21, 2),
(58, 30, 34),
(59, 22, 34);

-- --------------------------------------------------------

--
-- Table structure for table `note`
--

DROP TABLE IF EXISTS `note`;
CREATE TABLE `note` (
  `note_id` int(11) NOT NULL,
  `noteTitle` varchar(45) NOT NULL,
  `noteText` longtext NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `note`
--

INSERT INTO `note` (`note_id`, `noteTitle`, `noteText`) VALUES
(60, 'ye', 'ye'),
(62, 'wyu', 'asda'),
(63, 'gt', 'gt'),
(64, 'gt', 'gt');

-- --------------------------------------------------------

--
-- Table structure for table `profile`
--

DROP TABLE IF EXISTS `profile`;
CREATE TABLE `profile` (
  `profile_id` int(11) NOT NULL,
  `user_id_fk` int(11) NOT NULL,
  `firstname` varchar(50) NOT NULL,
  `lastname` varchar(50) NOT NULL,
  `telephone` varchar(45) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `profile`
--

INSERT INTO `profile` (`profile_id`, `user_id_fk`, `firstname`, `lastname`, `telephone`) VALUES
(1, 30, 'Bill', 'Potter', '01376326326'),
(5, 29, 'name1', 'add', '022'),
(6, 28, 'prep', 'step', '1235555'),
(10, 27, 'cok', 'step', '1235555'),
(11, 15, 'prep', 'step', '1235555'),
(12, 26, 'cok', 'step', '1235555'),
(13, 25, 'cok', 'step', '1235555'),
(14, 22, 'mask', 'man', '123'),
(15, 37, 'grat', 'grattt', '12312'),
(16, 42, 'trip', 'tripier', '123'),
(17, 43, 'asdasd', 'asdsad', '273723');

-- --------------------------------------------------------

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `user_id` int(11) NOT NULL,
  `username` varchar(50) NOT NULL,
  `password` varchar(50) NOT NULL,
  `email` varchar(45) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `user`
--

INSERT INTO `user` (`user_id`, `username`, `password`, `email`) VALUES
(15, 'test', 'test', 'test'),
(16, 'a', 'a', 'a'),
(17, 'b', 'b', 'b'),
(18, 'c', 'c', 'c'),
(19, 'd', 'd', 'd'),
(20, 'e', 'e', 'e'),
(21, 'f', 'f', 'f'),
(22, 'g', 'g', 'g'),
(23, 'h', 'h', 'h'),
(25, 'i', 'i', 'i'),
(26, 'j', 'j', 'j'),
(27, 'k', 'k', 'k'),
(28, 'l', 'l', 'l'),
(29, 'gab', 'gab', 'gab'),
(30, 'bill', 'bill123', 'bill@bill.com'),
(31, 'grate', '123', 'grat@grate.com'),
(32, 'general', '1234', 'general@outlook.com'),
(33, 'greatdad', '1234', 'general@outlook.com'),
(34, 'grabby', '1234', 'general@outlook.com'),
(35, 'grite', '1234', 'general@outlook.com'),
(36, 'billt', '1234', 'general@outlook.com'),
(37, 'billasdasd', 'adas', 'adasd'),
(38, 'billtt', 'ha', 'go'),
(39, 'username', 'password', 'email'),
(40, 'billtkot', '1234', 'ha'),
(41, 'toptop', 'toptop', 'toptop'),
(42, 'trailer', 'trap', 'trap'),
(43, 'simom', '123', 'asdasd'),
(44, 'gettty', 'gettttyyy', 'alas..asda@lko.cum');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `group`
--
ALTER TABLE `group`
  ADD PRIMARY KEY (`group_id`);

--
-- Indexes for table `group_note`
--
ALTER TABLE `group_note`
  ADD PRIMARY KEY (`group_note_id`),
  ADD KEY `gn_group_id_fk` (`group_id_fk`),
  ADD KEY `gn_note_id_fk` (`note_id_fk`);

--
-- Indexes for table `group_user`
--
ALTER TABLE `group_user`
  ADD PRIMARY KEY (`group_user_id`),
  ADD KEY `group_user_user_id_fk` (`user_id_fk`),
  ADD KEY `group_user_group_id_fk` (`group_id_fk`);

--
-- Indexes for table `note`
--
ALTER TABLE `note`
  ADD PRIMARY KEY (`note_id`);

--
-- Indexes for table `profile`
--
ALTER TABLE `profile`
  ADD PRIMARY KEY (`profile_id`),
  ADD KEY `profile_user_id_fk` (`user_id_fk`);

--
-- Indexes for table `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`user_id`),
  ADD UNIQUE KEY `USERNAME` (`username`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `group`
--
ALTER TABLE `group`
  MODIFY `group_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=35;
--
-- AUTO_INCREMENT for table `group_note`
--
ALTER TABLE `group_note`
  MODIFY `group_note_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=79;
--
-- AUTO_INCREMENT for table `group_user`
--
ALTER TABLE `group_user`
  MODIFY `group_user_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=60;
--
-- AUTO_INCREMENT for table `note`
--
ALTER TABLE `note`
  MODIFY `note_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=65;
--
-- AUTO_INCREMENT for table `profile`
--
ALTER TABLE `profile`
  MODIFY `profile_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=18;
--
-- AUTO_INCREMENT for table `user`
--
ALTER TABLE `user`
  MODIFY `user_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=45;
--
-- Constraints for dumped tables
--

--
-- Constraints for table `group_note`
--
ALTER TABLE `group_note`
  ADD CONSTRAINT `gn_group_id_fk` FOREIGN KEY (`group_id_fk`) REFERENCES `group` (`group_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `gn_note_id_fk` FOREIGN KEY (`note_id_fk`) REFERENCES `note` (`note_id`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Constraints for table `group_user`
--
ALTER TABLE `group_user`
  ADD CONSTRAINT `group_user_group_id_fk` FOREIGN KEY (`group_id_fk`) REFERENCES `group` (`group_id`),
  ADD CONSTRAINT `group_user_user_id_fk` FOREIGN KEY (`user_id_fk`) REFERENCES `user` (`user_id`);

--
-- Constraints for table `profile`
--
ALTER TABLE `profile`
  ADD CONSTRAINT `profile_user_id_fk` FOREIGN KEY (`user_id_fk`) REFERENCES `user` (`user_id`);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
