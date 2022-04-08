package com.example.securetracktraining;

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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import javax.sql.DataSource;
import java.io.IOException;
import java.net.URI;
import java.sql.SQLException;
import java.util.Base64;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;


@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BasicAuthenticationIT {

    @LocalServerPort
    private int serverPort;

    private DatabaseDataSourceConnection dataSourceConnection;

    @Autowired
    void setComponents(final DataSource dataSource) throws SQLException {
        this.dataSourceConnection = new DatabaseDataSourceConnection(dataSource,"test");
    }

    @BeforeEach
    void set_up() throws DatabaseUnitException, IOException, SQLException {
        DatabaseOperation.REFRESH.execute(this.dataSourceConnection, readDataset());
    }

    @AfterEach
    void close_connection() throws DatabaseUnitException, IOException, SQLException {
        dataSourceConnection.close();
    }

    @Test
    public void basicAuthentication_401() throws Exception {

        URI wsEndpoint = URI.create("ws://localhost:%d/jokes".formatted(serverPort));

        StandardWebSocketClient client = new StandardWebSocketClient();
        ListenableFuture<WebSocketSession> future = client.doHandshake(new TextWebSocketHandler(),
                new WebSocketHttpHeaders(),
                wsEndpoint);

        ExecutionException executionException = assertThrows(ExecutionException.class, future::get);
        assertTrue(executionException.getMessage().contains("Failed to handle HTTP response code [401]." +
                " Missing WWW-Authenticate header in response"));

    }

    @Test
    public void basicAuthentication_ok() throws Exception {

        URI wsEndpoint = URI.create("ws://localhost:%d/jokes".formatted(serverPort));

        StandardWebSocketClient client = new StandardWebSocketClient();

        String auth = "user1" + ":" + "user1";
        WebSocketHttpHeaders headers = new WebSocketHttpHeaders();
        headers.add("Authorization", "Basic " + new String(Base64.getEncoder().encode(auth.getBytes())));
        ListenableFuture<WebSocketSession> future = client.doHandshake(new TextWebSocketHandler(),
                headers, wsEndpoint);

        assertTrue(future.get().isOpen());
    }

    private IDataSet readDataset() throws DataSetException, IOException {

        return new FlatXmlDataSetBuilder().build(getClass().getClassLoader()
                .getResourceAsStream("BasicAuthenticationIT_dataset.xml"));

    }

}



