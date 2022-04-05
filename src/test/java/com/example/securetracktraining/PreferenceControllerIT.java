package com.example.securetracktraining;


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
import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.SQLException;
import java.util.Base64;
import java.util.concurrent.ForkJoinPool;

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
    void set_up() throws DatabaseUnitException, IOException, SQLException {
        DatabaseOperation.REFRESH.execute(this.dataSourceConnection, getActualDataset());
    }

    @AfterEach
    void close_connection() throws DatabaseUnitException, IOException, SQLException {
        dataSourceConnection.close();
    }


    @Test
    void testSetPreferences_ok() throws Exception {
        HttpClient httpClient = HttpClient.newBuilder().executor(ForkJoinPool.commonPool()).build();
        String auth = "Biba" + ":" + "bibaPassword";
        var uri = new URIBuilder().setScheme("http").setHost("localhost").setPort(serverPort)
                .setPath("/preferences")
                .setParameter("id", "1")
                .setParameter("categories", "pun")
                .setParameter("flags", "political")
                .build();
        httpClient.send(HttpRequest.newBuilder()
                .uri(uri)
                .header("Authorization", "Basic " + new String(Base64.getEncoder().encode(auth.getBytes())))
                .PUT(HttpRequest.BodyPublishers.noBody())
                .build(), HttpResponse.BodyHandlers.ofString());
    }

    private IDataSet getActualDataset() throws DataSetException, IOException {
        return new FlatXmlDataSetBuilder().build(getClass().getClassLoader()
                .getResourceAsStream("PreferenceControllerIT_dataset.xml"));
    }

    private IDataSet getExpectedDataset() throws DataSetException, IOException {
        return new FlatXmlDataSetBuilder().build(getClass().getClassLoader()
                .getResourceAsStream("PreferenceControllerIT_dataset.xml"));
    }

}
