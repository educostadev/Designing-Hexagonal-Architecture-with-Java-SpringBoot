package dev.educosta.domain.vo;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.UUID;

@Getter
@EqualsAndHashCode
public class Id {

    private final UUID uuid;

    private Id(UUID uuid){
        this.uuid = uuid;
    }

    public static Id withId(String id){
        return new Id(UUID.fromString(id));
    }

    public static Id withoutId(){
        return new Id(UUID.randomUUID());
    }

    @Override
    public String toString(){
        return uuid.toString();
    }
}
