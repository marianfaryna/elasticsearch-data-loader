package com.elasticsearch.index;

import com.elasticsearch.Util;
import com.elasticsearch.domain.Type;
import com.elasticsearch.domain.Status;
import org.elasticsearch.common.xcontent.XContentBuilder;

import java.io.IOException;
import java.util.Random;

import static org.elasticsearch.common.xcontent.XContentFactory.*;


/**
 * Helper object to create Json object to put into request
 */
public class RecordBuilder {

    private static final RecordBuilder INSTANCE = new RecordBuilder();

    private RecordBuilder() {}

    private static final int STATUS_LENGTH = Status.values().length;
    private static final int TYPE_LENGTH = Type.values().length;

    /**
     * Create Json object
     *
     * @param recordId id of ES document object
     * @return JSON object
     * @throws IOException
     */
    public XContentBuilder getRecordObject(int  recordId) throws IOException {
        return jsonBuilder()
                .startObject()
                .field("id", String.valueOf(recordId))
                .field("type", Type.values()[new Random().nextInt(TYPE_LENGTH)])
                .field("status", Status.values()[new Random().nextInt(STATUS_LENGTH)])
                .field("date", Util.getPseudoRandomDateAsString())
                .field("relation", Util.createParentChildRelation(recordId))

                .endObject();
    }

    /**
     * Singleton getter
     *
     * @return instance of RecordBuilder
     */
    public static RecordBuilder getInstance() {
        return INSTANCE;
    }


}
