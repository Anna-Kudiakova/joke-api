package com.example.securetracktraining;


import com.example.securetracktraining.config.CustomWebSecurityConfigurerAdapter;
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
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PreferenceControllerIT {


    private DatabaseDataSourceConnection dataSourceConnection;

    @Autowired
    void setComponents(final MockMvc mockMvc, final DataSource dataSource) throws SQLException {
        this.dataSourceConnection = new DatabaseDataSourceConnection(dataSource,"test");
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
