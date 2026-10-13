package AzureDeploy.DEPLOY.Config;

import com.azure.data.tables.TableClient;
import com.azure.data.tables.TableServiceClient;
import com.azure.data.tables.TableServiceClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AzureTableConfig {

    // Builds a TableClient bean for the configured Azure Table
    @Bean
    public TableClient employeeLogsTableClient(
            @Value("${azure.storage.connection-string}") String connectionString,
            @Value("${azure.storage.table-name}") String tableName) {

        // Creates a client for the Azure Storage account
        TableServiceClient serviceClient = new TableServiceClientBuilder()
                .connectionString(connectionString)
                .buildClient();

        // Creates the table if it does not exist, then returns its client
        return serviceClient.createTableIfNotExists(tableName);
    }
}