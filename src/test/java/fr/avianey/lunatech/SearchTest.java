package fr.avianey.lunatech;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import fr.avianey.lunatech.entity.Airport;
import fr.avianey.lunatech.entity.Country;
import fr.avianey.lunatech.entity.Runway;
import org.apache.cxf.jaxrs.client.WebClient;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class SearchTest extends AbstractTest {

    @Test
    public void shouldFilterCountries() throws IOException {
        String body = WebClient.create("http://localhost:4204")
            .path("/api/v1/search")
            .query("q", "FR")
            .get(String.class);
        List<Runway> list = new Gson().fromJson(body, new TypeToken<List<Runway>>() {}.getType());
        list.stream().forEach(runway -> {
            final Country country = runway.getAirport().getCountry();
            Assert.assertTrue("FR".equals(country.getCode())
                || country.getName().toLowerCase(Locale.US).startsWith("fr"));
        });
    }

    @Test
    public void shouldMatchCountryNamesByPrefix() throws IOException {
        String body = WebClient.create("http://localhost:4204")
            .path("/api/v1/search")
            .query("q", "austr")
            .get(String.class);
        List<Runway> list = new Gson().fromJson(body, new TypeToken<List<Runway>>() {}.getType());
        long expectedCount = list.stream().filter(runway -> {
            final String name = runway.getAirport().getCountry().getName();
            return "Australia".equalsIgnoreCase(name) || "Austria".equalsIgnoreCase(name);
        }).count();
        Assert.assertEquals(expectedCount, list.size());
    }

}
