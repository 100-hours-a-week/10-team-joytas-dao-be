ALTER TABLE chat_rooms
    ADD COLUMN objet_id BIGINT;

ALTER TABLE chat_rooms
    ADD FOREIGN KEY (objet_id)
        REFERENCES objets (objet_id);

