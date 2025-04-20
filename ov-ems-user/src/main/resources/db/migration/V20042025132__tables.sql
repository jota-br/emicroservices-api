CREATE TABLE IF NOT EXISTS tb_country (
    id BIGINT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    uuid VARCHAR(40) NOT NULL UNIQUE,
    name VARCHAR(255) NOT NULL UNIQUE,
    is_active BOOLEAN,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    version INTEGER
);

CREATE TABLE IF NOT EXISTS tb_state (
    id BIGINT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    uuid VARCHAR(40) NOT NULL UNIQUE,
    country_id BIGINT NOT NULL,
    name VARCHAR(255) NOT NULL UNIQUE,
    is_active BOOLEAN,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    version INTEGER,
    FOREIGN KEY (country_id) REFERENCES tb_country (id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS tb_city (
    id BIGINT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    uuid VARCHAR(40) NOT NULL UNIQUE,
    state_id BIGINT NOT NULL,
    name VARCHAR(255) NOT NULL UNIQUE,
    is_active BOOLEAN,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    version INTEGER,
    FOREIGN KEY (state_id) REFERENCES tb_state (id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS tb_role (
    id BIGINT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    uuid VARCHAR(40) NOT NULL UNIQUE,
    name VARCHAR(50) NOT NULL UNIQUE,
    description VARCHAR(255) NOT NULL UNIQUE,
    is_active BOOLEAN,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    version INTEGER
);

CREATE TABLE IF NOT EXISTS tb_user (
    id BIGINT PRIMARY KEY not null AUTO_INCREMENT,
    uuid VARCHAR(40) NOT NULL UNIQUE,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(320) NOT NULL UNIQUE,
    salt VARCHAR(255) NOT NULL,
    hash VARCHAR(255) NOT NULL,
    first_name VARCHAR(255),
    last_name VARCHAR(255),
    phone VARCHAR(20) UNIQUE,
    is_active BOOLEAN,
    role_id BIGINT NOT NULL,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    version INTEGER,
    FOREIGN KEY (role_id) REFERENCES tb_role (id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS tb_address (
    id BIGINT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    uuid VARCHAR(40) NOT NULL UNIQUE,
    user_id BIGINT NOT NULL,
    postal_code VARCHAR(50) NOT NULL,
    street VARCHAR(255) NOT NULL,
    number VARCHAR(50) NOT NULL,
    type TINYINT NOT NULL,
    city_id BIGINT NOT NULL,
    is_active BOOLEAN,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    version INTEGER,
    FOREIGN KEY (user_id) REFERENCES tb_user (id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (city_id) REFERENCES tb_city (id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS tb_user_activation_token (
    id BIGINT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    expiration TIMESTAMP NOT NULL,
    token VARCHAR(40) NOT NULL UNIQUE,
    user_uuid VARCHAR(40) NOT NULL UNIQUE,
    used BOOLEAN DEFAULT FALSE,
    version INTEGER,
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);

CREATE INDEX idx_user_uuid ON tb_user(uuid);
CREATE INDEX idx_user_email ON tb_user(email);
CREATE INDEX idx_user_username ON tb_user(username);
