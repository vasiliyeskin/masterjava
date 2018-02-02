package ru.javaops.masterjava.persist.dao;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.javaops.masterjava.persist.CityTestData;
import ru.javaops.masterjava.persist.model.City;


import static ru.javaops.masterjava.persist.CityTestData.FIRST3_CITIES;

import java.util.List;

public class CityDAOTest extends AbstractDaoTest<CityDAO> {

    public CityDAOTest() {
        super(CityDAO.class);
    }


    @BeforeClass
    public static void init() throws Exception {
        CityTestData.init();
    }

    @Before
    public void setUp() throws Exception {
        CityTestData.setUp();
    }

    @Test
    public void getWithLimit() throws Exception {
        List<City> cityList= dao.getWithLimit(3);
        Assert.assertEquals(FIRST3_CITIES, cityList);
    }

}