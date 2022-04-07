package com.joke_api.client;


import org.apache.hc.core5.net.URIBuilder;
import org.dbunit.DatabaseUnitException;
import org.dbunit.database.DatabaseDataSourceConnection;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import javax.sql.DataSource;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.SQLException;
import java.util.Base64;
import java.util.concurrent.ForkJoinPool;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PreferenceControllerIT {


    @LocalServerPort
    private int serverPort;

    private DatabaseDataSourceConnection dataSourceConnection;

    @Autowired
    void setComponents(final DataSource dataSource) throws SQLException {
        this.dataSourceConnection = new DatabaseDataSourceConnection(dataSource, "test");
    }


    @BeforeEach
    void setUp() throws DatabaseUnitException, SQLException {
        DatabaseOperation.REFRESH.execute(this.dataSourceConnection, getActualDataset());
    }

    @AfterEach
    void closeConnection() throws SQLException {
        dataSourceConnection.close();
    }


    @Test
    void testSetPreferences_ok() throws Exception {
        HttpClient httpClient = HttpClient.newBuilder().executor(ForkJoinPool.commonPool()).build();
        String auth = "user1" + ":" + "user1";
        var uri = new URIBuilder().setScheme("http").setHost("localhost").setPort(serverPort)
                .setPath("/preferences")
                .setParameter("id", "1")
                .setParameter("categories", "dark")
                .setParameter("flags", "political")
                .build();
        HttpResponse<String> response = httpClient.send(HttpRequest.newBuilder()
                .uri(uri)
                .header("Authorization", "Basic " + new String(Base64.getEncoder().encode(auth.getBytes())))
                .PUT(HttpRequest.BodyPublishers.noBody())
                .build(), HttpResponse.BodyHandlers.ofString());

        assertEquals(response.statusCode(), 200);

        IDataSet actualDataset = dataSourceConnection.createDataSet();
        boolean actualCategoryValue = (boolean) actualDataset.getTable("categories").getValue(0, "dark");
        boolean actualFlagValue = (boolean) actualDataset.getTable("flags").getValue(0, "political");

        assertTrue(actualCategoryValue);
        assertTrue(actualFlagValue);
    }

    private IDataSet getActualDataset() throws DataSetException {
        return new FlatXmlDataSetBuilder().build(getClass().getClassLoader()
                .getResourceAsStream("PreferenceControllerIT_dataset.xml"));
    }

}
