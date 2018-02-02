package ru.javaops.masterjava.persist.model;


import com.bertoncelj.jdbi.entitymapper.Column;
import lombok.*;

@Data
@RequiredArgsConstructor
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class City extends BaseEntity {
    @Column("citykey")
    private @NonNull String citykey;
    @Column("name")
    private @NonNull String name;

    public City(Integer id, String citykey, String name)
    {
        this.id = id;
        this.citykey = citykey;
        this.name = name;
    }
}
