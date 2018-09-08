package com.elasticsearch;


import com.elasticsearch.index.RecordBuilder;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

/**
 * Application Entry point
 */
@SpringBootApplication
public class Main implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @Override
    public void run(String... strings) throws Exception {

        Integer numOfRecords = Integer.valueOf(strings[0]);

        TransportClient client = Util.prepareClient();
        BulkRequestBuilder bulkRequest = client.prepareBulk();

        addData(client, bulkRequest, numOfRecords);

        BulkResponse bulkResponse = bulkRequest.get();
        if (bulkResponse.hasFailures()) {
            System.out.println("Failure Message : " + bulkResponse.buildFailureMessage());
        }

        client.close();
        Util.printRelation();
    }

    /**
     * Filling BuldRequest with data
     *
     * @param client ES client
     * @param bulkRequest Request object
     * @param numOfRecords number of records to put
     * @throws IOException
     */
    private void addData(TransportClient client, BulkRequestBuilder bulkRequest, int numOfRecords) throws IOException {
        for (int i = 1; i <= numOfRecords; i++) {
            bulkRequest.add(client.prepareIndex("records", "record", String.valueOf(i))
                    .setRouting("1")
                    .setSource(RecordBuilder.getInstance().getRecordObject(i))
            );

        }
    }


}
