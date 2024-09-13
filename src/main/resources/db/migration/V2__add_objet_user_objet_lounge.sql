CREATE TABLE IF NOT EXISTS objet_users(
    user_id BIGINT NOT NULL AUTO_INCREMENT,
    nickname VARCHAR(255),
    PRIMARY KEY (user_id)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS objet_lounges(
    lounge_id BIGINT NOT NULL AUTO_INCREMENT,
    user_id BIGINT,
    PRIMARY KEY (lounge_id),
    FOREIGN KEY (user_id) REFERENCES objet_users (user_id)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS chat_rooms (
    chat_room_id BIGINT NOT NULL AUTO_INCREMENT,
    PRIMARY KEY (chat_room_id)
) ENGINE=InnoDB;
