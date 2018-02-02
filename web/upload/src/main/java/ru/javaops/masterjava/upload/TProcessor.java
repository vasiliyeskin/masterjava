package ru.javaops.masterjava.upload;

import ru.javaops.masterjava.persist.DBIProvider;
import ru.javaops.masterjava.persist.dao.CityDAO;
import ru.javaops.masterjava.persist.model.BaseEntity;
import ru.javaops.masterjava.persist.model.City;
import ru.javaops.masterjava.xml.schema.ObjectFactory;
import ru.javaops.masterjava.xml.util.JaxbParser;
import ru.javaops.masterjava.xml.util.JaxbUnmarshaller;
import ru.javaops.masterjava.xml.util.StaxStreamProcessor;

import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class TProcessor {

        private static final JaxbParser jaxbParser = new JaxbParser(ObjectFactory.class);
        private static CityDAO cityDAO = DBIProvider.getDao(CityDAO.class);

        public List<City> process(final InputStream is, int chunkSize) throws XMLStreamException, JAXBException {
            final StaxStreamProcessor processor = new StaxStreamProcessor(is);
            List<City> cities = new ArrayList<>();

            JaxbUnmarshaller unmarshaller = jaxbParser.createUnmarshaller();
            while (processor.doUntil(XMLEvent.START_ELEMENT, "City")) {
                ru.javaops.masterjava.xml.schema.CityType xmlCity = unmarshaller.unmarshal(processor.getReader(), ru.javaops.masterjava.xml.schema.CityType.class);
                final City city = new City(xmlCity.getValue(), xmlCity.getValue());
                cities.add(city);
            }
            cityDAO.insertBatch(cities, 5);
            return cities;
        }




}
