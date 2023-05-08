package com.example.telegram_bot.repository.entity;

import com.vladmihalcea.hibernate.type.array.IntArrayType;
import com.vladmihalcea.hibernate.type.array.StringArrayType;
import lombok.Data;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.List;

@Data
@Entity
@Table(name = "tg_user")
@TypeDefs({
        @TypeDef(
                name = "int-array",
                typeClass = IntArrayType.class
        )
})
public class TelegramUser {
    @Id
    @Column(name = "chat_id")
    private Long chatId;

    @Column(name = "username")
    private String username;

    @Column(name = "firstname")
    private String firstName;

    @Column(name = "lastname")
    private String lastName;

    @Column(name = "location")
    private String location;

    @Column(name = "qualification")
    private int qualification;

    @Column(name = "specialization",
            columnDefinition = "integer[]")
    @Type(type = "int-array")
    private int[] specialization;

    @Column(name = "skills",
            columnDefinition = "integer[]")
    @Type(type = "int-array")
    private List<Integer> skills;
}
