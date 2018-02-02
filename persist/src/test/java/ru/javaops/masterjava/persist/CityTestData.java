package ru.javaops.masterjava.persist;

import com.google.common.collect.ImmutableList;
import ru.javaops.masterjava.persist.dao.CityDAO;
import ru.javaops.masterjava.persist.model.City;

import java.util.List;

public class CityTestData {
    public static City mos;
    public static City spb;
    public static City nn;
    public static City kost;
    public static List<City> FIRST3_CITIES;

    public static void init() {
        mos = new City("mos", "mos");
        spb = new City("spb","spb");
        nn  = new City("nn", "nn");
        kost = new City("kost","kost");
        FIRST3_CITIES = ImmutableList.of(mos, spb, nn);
    }

    public static void setUp() {
        CityDAO dao = DBIProvider.getDao(CityDAO.class);
        dao.clean();
        DBIProvider.getDBI().useTransaction((conn, status) -> {
            FIRST3_CITIES.forEach(dao::insert);
            dao.insert(kost);
        });
    }
}
