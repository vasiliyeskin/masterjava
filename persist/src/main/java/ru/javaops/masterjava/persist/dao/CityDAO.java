package ru.javaops.masterjava.persist.dao;

import com.bertoncelj.jdbi.entitymapper.EntityMapperFactory;
import org.skife.jdbi.v2.sqlobject.*;
import org.skife.jdbi.v2.sqlobject.customizers.BatchChunkSize;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapperFactory;
import ru.javaops.masterjava.persist.DBIProvider;
import ru.javaops.masterjava.persist.model.City;

import java.util.List;

@RegisterMapperFactory(EntityMapperFactory.class)
public abstract class CityDAO implements AbstractDao {

    public City insert(City city) {
        if (city.isNew()) {
            int id = insertGeneratedId(city);
            city.setId(id);
        } else {
            insertWitId(city);
        }
        return city;
    }

    @SqlQuery("SELECT nextval('city_seq')")
    abstract int getNextVal();

    @Transaction
    public int getSeqAndSkip(int step) {
        int id = getNextVal();
        DBIProvider.getDBI().useHandle(h -> h.execute("ALTER SEQUENCE city_seq RESTART WITH " + (id + step)));
        return id;
    }

    @SqlUpdate("INSERT INTO cities (citykey, name) VALUES (:citykey, :name) ON CONFLICT DO NOTHING ")
    @GetGeneratedKeys
    abstract int insertGeneratedId(@BindBean City city);

    @SqlUpdate("INSERT INTO cities (id,citykey, name) VALUES (:id,:citykey, :name) ")
    abstract void insertWitId(@BindBean City city);

    @SqlQuery("SELECT * FROM cities ORDER BY id LIMIT :it")
    public abstract List<City> getWithLimit(@Bind int limit);

    @SqlBatch("INSERT INTO cities (citykey, name) VALUES (:citykey, :name) ON CONFLICT DO NOTHING")
    public abstract int[] insertBatch(@BindBean List<City> users, @BatchChunkSize int chunkSize);

    //   http://stackoverflow.com/questions/13223820/postgresql-delete-all-content
    @SqlUpdate("TRUNCATE cities")
    @Override
    public abstract void clean();

}
