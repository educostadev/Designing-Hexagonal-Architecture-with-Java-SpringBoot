package dev.educosta.domain.entity;

import dev.educosta.domain.specification.EmptyCityNameSpec;
import dev.educosta.domain.vo.Id;
import dev.educosta.domain.vo.State;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class City {

    private final Id id;

    private final String name;

    private final State state;

    public City(Id id, String name, State state) {
        var spec = new EmptyCityNameSpec();
        spec.check(name);
        this.id = id;
        this.name = name;
        this.state = state;
    }
}
