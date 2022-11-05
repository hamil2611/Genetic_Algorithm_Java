CREATE TABLE employees (
                           id bigint(20) NOT NULL AUTO_INCREMENT,
                           name varchar(100) NOT NULL,
                           role varchar(50) NOT NULL,
                           project varchar(50) DEFAULT NULL,
                           PRIMARY KEY (id),
                           UNIQUE KEY UK_name (name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;