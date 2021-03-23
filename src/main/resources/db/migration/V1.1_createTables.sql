CREATE TABLE if not exists `eagle_eye`.`product` (
  `code` VARCHAR(25) NOT NULL ,
  `name` VARCHAR(45) NOT NULL,
  `use_card` TINYINT(1) NOT NULL,
  `use_account` TINYINT(1) NOT NULL,
  `callback_url` VARCHAR(200) NOT NULL,
  `status` TINYINT(1) NOT NULL,
  `created_by` VARCHAR(150) NOT NULL,
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_by` VARCHAR(150),
  `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`code`));


  CREATE TABLE if not exists `eagle_eye`.`product_dataset` (
    `product_code` VARCHAR(25) NOT NULL ,
    `field_name` VARCHAR(45) NOT NULL,
    `data_type` VARCHAR(200) NOT NULL,
    `mandatory` TINYINT(1) NOT NULL,
    `authorised` TINYINT(1) NOT NULL,
    `created_by` VARCHAR(150) NOT NULL,
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_by` VARCHAR(150),
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`product_code`, `field_name`),
    CONSTRAINT `FK_PRODUCT_CODE`
        FOREIGN KEY (`product_code`)
        REFERENCES `eagle_eye`.`product` (`code`)
        ON DELETE RESTRICT
        ON UPDATE CASCADE);


  CREATE TABLE if not exists `eagle_eye`.`card` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `card_holder_name` VARCHAR(25) NOT NULL ,
    `card_brand` VARCHAR(100),
    `card_type` VARCHAR(100),
    `card_expiry` VARCHAR(100) ,
    `card_cvv` VARCHAR(100),
    `card_bin` INT ,
    `issuer_code` VARCHAR(100),
    `issuer_name` VARCHAR(100),
    `iso_country` VARCHAR(100),
    `iso_country_code` VARCHAR(100),
    `status` TINYINT(1) NOT NULL,
    `suspicion_count` INT,
    `block_reason` VARCHAR(200),
    `created_by` VARCHAR(150) NOT NULL,
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_by` VARCHAR(150),
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE KEY UC_CARD( `card_bin`,`iso_country_code` ));

    CREATE TABLE IF NOT EXISTS `eagle_eye`.`account` (
      `id` BIGINT NOT NULL AUTO_INCREMENT,
      `account_no` INT NOT NULL,
      `account_name` VARCHAR(25) NOT NULL ,
      `bank_code` VARCHAR(45) NOT NULL,
      `bank_name` VARCHAR(45) NOT NULL,
      `status` TINYINT(1) NOT NULL,
      `suspicion_count` INT,
      `block_reason` VARCHAR(200),
      `created_by` VARCHAR(150) NOT NULL,
      `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
      `updated_by` VARCHAR(150),
      `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
      PRIMARY KEY (`id`),
      UNIQUE KEY UC_ACCOUNT(`account_no`,`bank_code`));

    CREATE TABLE if not exists `eagle_eye`.`card_product` (
              `product_code` VARCHAR(25) NOT NULL ,
              `card_id` BIGINT(20) NOT NULL,
              `status` TINYINT(1) NOT NULL,
              `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
              PRIMARY KEY (`card_id`,`product_code`),
               CONSTRAINT `FK_CARD_PRODUCT_CODE`
                      FOREIGN KEY (`product_code`)
                      REFERENCES `eagle_eye`.`product` (`code`),
    			CONSTRAINT `FK_CARD_ID`
                      FOREIGN KEY (`card_id`)
                      REFERENCES `eagle_eye`.`card` (`id`));

CREATE TABLE if not exists `eagle_eye`.`account_product` (
          `product_code` VARCHAR(25) NOT NULL ,
          `account_id` BIGINT(20) NOT NULL,
          `status` TINYINT(1) NOT NULL,
          `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
          PRIMARY KEY (`account_id`,`product_code`),
           CONSTRAINT `FK_ACCOUNT_PRODUCT_CODE`
                  FOREIGN KEY (`product_code`)
                  REFERENCES `eagle_eye`.`product` (`code`),
			CONSTRAINT `FK_ACCOUNT_PRODUCT_ID`
                  FOREIGN KEY (`account_id`)
                  REFERENCES `eagle_eye`.`account` (`id`));


CREATE TABLE if not exists `eagle_eye`.`email_group` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `group_name` VARCHAR(25) NOT NULL ,
  `email` text NOT NULL,
  `status` TINYINT(1) NOT NULL,
  `created_by` VARCHAR(150) NOT NULL,
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_by` VARCHAR(150),
  `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY UC_GROUP_NAME(`group_name`));


    CREATE TABLE if not exists `eagle_eye`.`product_rule` (
      `id` BIGINT NOT NULL AUTO_INCREMENT,
      `product_code` VARCHAR(25) NOT NULL ,
      `rule_id` BIGINT(20) NOT NULL,
      `notify_admin` TINYINT(1) NOT NULL,
      `email_group_id` bigint(20) NOT NULL,
      `notify_customer` TINYINT(1) NOT NULL,
      `status` TINYINT(1) NOT NULL,
      `authorised` TINYINT(1) NOT NULL,
      `created_by` VARCHAR(150) NOT NULL,
      `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
      `updated_by` VARCHAR(150),
      `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
      PRIMARY KEY (`id`),
      UNIQUE KEY UC_PRODUCT_RULE(`rule_id`,`product_code`),
      CONSTRAINT `FK_PRODUCT_RULE_CODE`
                        FOREIGN KEY (`product_code`)
                        REFERENCES `eagle_eye`.`product`(`code`),
  	CONSTRAINT `FK_PRODUCT_RULE_EMAIL_GROUP_ID`
                        FOREIGN KEY (`email_group_id`)
                        REFERENCES `eagle_eye`.`email_group`(`id`));


 CREATE TABLE if not exists `eagle_eye`.`transaction_log` (
      `id` BIGINT NOT NULL AUTO_INCREMENT,
      `transaction_id` VARCHAR(200) NOT NULL ,
      `source_url` VARCHAR(255) NOT NULL,
      `is_fraud` TINYINT(1) NOT NULL,
      `flagged_rule` BIGINT(20) NOT NULL,
      `product_code` VARCHAR(200) NOT NULL ,
      `use_card` TINYINT(1),
      `card_id` bigint(20) ,
      `use_account` TINYINT(1),
      `account_id` BIGINT(20) ,
      `channel` VARCHAR(50) NOT NULL,
      `amount` DECIMAL(20,2) NOT NULL,
      `request_dump` LONGTEXT NOT NULL,
      `created_by` VARCHAR(150) NOT NULL,
      `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
      `updated_by` VARCHAR(150),
      `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
      PRIMARY KEY (`id`),
      UNIQUE KEY UC_TRANSACTION_ID(`transaction_id`),
      CONSTRAINT `FK_TRANSACTION_LOG_CODE`
                        FOREIGN KEY (`product_code`)
                        REFERENCES `eagle_eye`.`product`(`code`),
  	  CONSTRAINT `FK_TRANSACTION_LOG_FLAGGED_RULE`
                        FOREIGN KEY (`flagged_rule`)
                        REFERENCES `eagle_eye`.`product_rule`(`id`),
      CONSTRAINT `FK_TRANSACTION_LOG_CARD_ID`
                        FOREIGN KEY (`card_id`)
                        REFERENCES `eagle_eye`.`card`(`id`),
      CONSTRAINT `FK_TRANSACTION_LOG_ACCOUNT_ID`
                        FOREIGN KEY (`account_id`)
                        REFERENCES `eagle_eye`.`account`(`id`));


CREATE TABLE if not exists `eagle_eye`.`report` (
  `id` INT(20) NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(150) NOT NULL ,
  `description` VARCHAR(255) NOT NULL,
  `created_by` VARCHAR(150) NOT NULL,
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_by` VARCHAR(150),
  `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`));


CREATE TABLE if not exists `eagle_eye`.`report_scheduler` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `report_id` int(20),
  `email_group_id` bigint(20),
  `interval_value` int(20),
  `interval_type` VARCHAR(45),
  `loop` TINYINT(1),
  `status` TINYINT(1),
  `created_by` VARCHAR(150) NOT NULL,
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_by` VARCHAR(150),
  `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  CONSTRAINT `FK_REPORT_SCHEDULER_REPORT_ID`
                          FOREIGN KEY (`report_id`)
                          REFERENCES `eagle_eye`.`report`(`id`),
  CONSTRAINT `FK_REPORT_SCHEDULER_EMAIL_GROUP_ID`
                          FOREIGN KEY (`email_group_id`)
                          REFERENCES `eagle_eye`.`email_group`(`id`));


CREATE TABLE if not exists `eagle_eye`.`internal_watchlist` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `bvn` int(20),
  `comments` VARCHAR(250),
  `status` TINYINT(1) NOT NULL,
  `authorised` TINYINT(1) NOT NULL,
  `created_by` VARCHAR(150) NOT NULL,
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_by` VARCHAR(150),
  `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY UC_BVN(`bvn`));

CREATE TABLE if not exists `eagle_eye`.`ofac_watchlist` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `full_name` VARCHAR(250),
  `category` VARCHAR(200) not null,
  `comments` VARCHAR(250),
  `status` TINYINT(1) NOT NULL,
  `authorised` TINYINT(1) NOT NULL,
  `created_by` VARCHAR(150) NOT NULL,
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_by` VARCHAR(150),
  `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`));