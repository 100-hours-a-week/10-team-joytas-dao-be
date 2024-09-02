CREATE TABLE IF NOT EXISTS users (
    user_id BIGINT NOT NULL AUTO_INCREMENT,
    created_at DATETIME(6) NOT NULL,
    deleted_at DATETIME(6),
    updated_at DATETIME(6) NOT NULL,
    kakao_id VARCHAR(255),
    nickname VARCHAR(255),
    profile_url VARCHAR(255),
    reason_detail TEXT,
    reasons VARCHAR(255),
    status VARCHAR(255),
    PRIMARY KEY (user_id)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS chat_rooms (
    chat_room_id BIGINT NOT NULL AUTO_INCREMENT,
    created_at DATETIME(6) NOT NULL,
    deleted_at DATETIME(6),
    updated_at DATETIME(6) NOT NULL,
    room_token VARCHAR(255) NOT NULL,
    PRIMARY KEY (chat_room_id),
    UNIQUE (room_token)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS lounges (
    lounge_id BIGINT NOT NULL AUTO_INCREMENT,
    created_at DATETIME(6) NOT NULL,
    deleted_at DATETIME(6),
    updated_at DATETIME(6) NOT NULL,
    name VARCHAR(255),
    reason VARCHAR(255),
    reason_detail TEXT,
    status VARCHAR(255),
    type VARCHAR(255),
    user_id BIGINT,
    PRIMARY KEY (lounge_id),
    FOREIGN KEY (user_id) REFERENCES users (user_id)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS lounges_sharers (
    lng_shr_id BIGINT NOT NULL AUTO_INCREMENT,
    lounge_id BIGINT,
    user_id BIGINT,
    PRIMARY KEY (lng_shr_id),
    FOREIGN KEY (user_id) REFERENCES users (user_id),
    FOREIGN KEY (lounge_id) REFERENCES lounges (lounge_id)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS my_rooms (
    my_room_id BIGINT NOT NULL AUTO_INCREMENT,
    created_at DATETIME(6) NOT NULL,
    deleted_at DATETIME(6),
    updated_at DATETIME(6) NOT NULL,
    name VARCHAR(255),
    type VARCHAR(255),
    user_user_id BIGINT,
    PRIMARY KEY (my_room_id),
    UNIQUE (user_user_id),
    FOREIGN KEY (user_user_id) REFERENCES users (user_id)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS notifications (
    noti_id BIGINT NOT NULL AUTO_INCREMENT,
    created_at DATETIME(6) NOT NULL,
    deleted_at DATETIME(6),
    updated_at DATETIME(6) NOT NULL,
    domain_id BIGINT,
    is_read BIT,
    type VARCHAR(255),
    receive_user_id BIGINT,
    send_user_id BIGINT,
    PRIMARY KEY (noti_id),
    FOREIGN KEY (send_user_id) REFERENCES users (user_id),
    FOREIGN KEY (receive_user_id) REFERENCES users (user_id)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS objets (
    objet_id BIGINT NOT NULL AUTO_INCREMENT,
    created_at DATETIME(6) NOT NULL,
    deleted_at DATETIME(6),
    updated_at DATETIME(6) NOT NULL,
    explanation VARCHAR(255),
    image_url VARCHAR(255),
    name VARCHAR(255),
    reason VARCHAR(255),
    reason_detail TEXT,
    status VARCHAR(255),
    type VARCHAR(255),
    chat_room_id BIGINT,
    lounge_id BIGINT,
    user_id BIGINT,
    PRIMARY KEY (objet_id),
    UNIQUE (chat_room_id),
    FOREIGN KEY (chat_room_id) REFERENCES chat_rooms (chat_room_id),
    FOREIGN KEY (lounge_id) REFERENCES lounges (lounge_id),
    FOREIGN KEY (user_id) REFERENCES users (user_id)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS objets_sharers (
    obj_shr_id BIGINT NOT NULL AUTO_INCREMENT,
    objet_id BIGINT,
    user_id BIGINT,
    PRIMARY KEY (obj_shr_id),
    FOREIGN KEY (objet_id) REFERENCES objets (objet_id),
    FOREIGN KEY (user_id) REFERENCES users (user_id)
) ENGINE=InnoDB;
