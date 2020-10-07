CREATE TABLE `t_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `created_at` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  `updated_at` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  `telphone` varchar(40) COLLATE utf8_unicode_ci NOT NULL DEFAULT '',
  `password` varchar(200) COLLATE utf8_unicode_ci NOT NULL DEFAULT '',
  `nick_name` varchar(40) COLLATE utf8_unicode_ci NOT NULL DEFAULT '',
  `gender` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `telphone_unique_index` (`telphone`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

CREATE TABLE `t_seller` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(80) COLLATE utf8_unicode_ci NOT NULL DEFAULT '',
  `created_at` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  `updated_at` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  `remark_score` decimal(2,1) NOT NULL DEFAULT '0.0',
  `disabled_flag` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

CREATE TABLE `t_category` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `created_at` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  `updated_at` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  `name` varchar(20) COLLATE utf8_unicode_ci NOT NULL DEFAULT '',
  `icon_url` varchar(200) COLLATE utf8_unicode_ci NOT NULL DEFAULT '',
  `sort` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `name_unique_in` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

CREATE TABLE `t_shop` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `created_at` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  `updated_at` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  `name` varchar(80) COLLATE utf8_unicode_ci NOT NULL DEFAULT '',
  `remark_score` decimal(2,1) NOT NULL DEFAULT '0.0',
  `price_per_man` int(11) NOT NULL DEFAULT '0',
  `latitude` decimal(10,6) NOT NULL DEFAULT '0.000000',
  `longitude` decimal(10,6) NOT NULL DEFAULT '0.000000',
  `category_id` int(11) NOT NULL DEFAULT '0',
  `tags` varchar(2000) COLLATE utf8_unicode_ci NOT NULL DEFAULT '',
  `start_time` varchar(200) COLLATE utf8_unicode_ci NOT NULL DEFAULT '',
  `end_time` varchar(200) COLLATE utf8_unicode_ci NOT NULL DEFAULT '',
  `address` varchar(200) COLLATE utf8_unicode_ci NOT NULL DEFAULT '',
  `seller_id` int(11) NOT NULL DEFAULT '0',
  `icon_url` varchar(100) COLLATE utf8_unicode_ci NOT NULL DEFAULT '',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;