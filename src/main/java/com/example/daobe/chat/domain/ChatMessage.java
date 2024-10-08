package com.example.daobe.chat.domain;

import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.redis.core.index.Indexed;

@Getter
@Document(collection = "chat_message")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatMessage {

    @Id
    @Field(value = "_id", targetType = FieldType.OBJECT_ID)
    private ObjectId id;

    @Indexed
    @Field("room_token")
    private String roomToken;

    @Field("type")
    private String type;

    @Field("message")
    private String message;

    @Field("sender_id")
    private Long senderId;

    @Field("created_at")
    @CreatedDate
    private LocalDateTime createdAt;

    @Builder
    public ChatMessage(String roomToken, MessageType type, String message, Long senderId) {
        this.roomToken = roomToken;
        this.type = type.name();
        this.message = message;
        this.senderId = senderId;
    }
}
