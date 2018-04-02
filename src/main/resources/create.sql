DROP TABLE IF EXISTS `user`;

CREATE TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_name` varchar(45) NOT NULL,
  `password` varchar(45) NOT NULL,
  `status` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_name` (`user_name`)
);

DROP TABLE IF EXISTS `transaction_code`;

CREATE TABLE `transaction_code` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `code` varchar(45) NOT NULL,
  `user_id` int(11) NOT NULL,
  `datetime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `used` tinyint(4) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_code` (`code`, `user_id`),
  CONSTRAINT `fk_transaction_code_user_id` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
);


DROP TABLE IF EXISTS `client_account`;

CREATE TABLE `client_account` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `amount` decimal(19,4) DEFAULT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_client_account_user_id` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
);


DROP TABLE IF EXISTS `client_info`;

CREATE TABLE `client_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `full_name` varchar(255) NOT NULL,
  `fin` varchar(255) NOT NULL,
  `date_of_birth` date,
  `occupation` varchar(255),
  `mobile_number` varchar(255),
  `address` varchar(255),
  `email` varchar(255) NOT NULL,
  `user_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_client_info_user_id` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
);

DROP TABLE IF EXISTS `client_transaction`;

CREATE TABLE `client_transaction` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `trans_code` varchar(45) NOT NULL,
  `status` varchar(45) DEFAULT NULL,
  `datetime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `amount` decimal(19,4) DEFAULT NULL,
  `to_account_num` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_client_transaction_trans_code` FOREIGN KEY (`trans_code`, `user_id`) REFERENCES `transaction_code` (`code`, `user_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_client_transaction_to_account_num` FOREIGN KEY (`to_account_num`) REFERENCES `client_account` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
);


DROP TABLE IF EXISTS `user_role`;

CREATE TABLE `user_role` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_name` varchar(45) NOT NULL,
  `role` varchar(45) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_name_role` (`user_name`, `role`),
  CONSTRAINT `fk_user_role_user_name` FOREIGN KEY (`user_name`) REFERENCES `user` (`user_name`) ON DELETE NO ACTION ON UPDATE NO ACTION
);

INSERT INTO `user`(`user_name`, `password`, `status`) VALUES ('staff_1','123456','APPROVED'), ('staff_2','123456','APPROVED');
INSERT INTO `user_role`(`user_name`, `role`) VALUES ('staff_1','staff'),('staff_2','staff');
