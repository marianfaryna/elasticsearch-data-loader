package com.elasticsearch;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

/**
 * Util helper class
 */
public final class Util {

    private static final LocalDate LOCAL_DATE = LocalDate.now();
    private static final int DAYS_IN_MONTH = LOCAL_DATE.getMonth().minLength();
    private static final int IM_LAST_DAY_IN_MONTH = 1;
    private static final String DATE_FORMAT = "yyyy-MM-dd";

    private static final Random RANDOM = new Random();
    private static final Map<Integer, Set<Integer>> PARENT_CHILD_RELATION = new HashMap<>();

    private Util() {

    }

    /**
     * Creates map with randomly chosen relation type and parent id relation oif needed
     *
     * @param documentId id of document
     * @return map with key-value properties for document that will be sent to ES
     */
    public static Map<String, String> createParentChildRelation(Integer documentId) {
        Map<String, String> parentChildMap = new HashMap<>();
        if (RANDOM.nextBoolean() && !PARENT_CHILD_RELATION.isEmpty()) {
            final Integer parentId = getParentId();
            parentChildMap.put("name", "child");
            parentChildMap.put("parent", String.valueOf(parentId));

            updateChildRelation(parentId, documentId);
        } else {
            parentChildMap.put("name", "parent");
            PARENT_CHILD_RELATION.put(documentId, new HashSet<>());
        }

        return parentChildMap;
    }

    /**
     * Randomly retrieves id of parent document to bound child document
     *
     * @return id from Parent ID's map
     */
    private static Integer getParentId() {
        final List<Integer> keys = new LinkedList<>(PARENT_CHILD_RELATION.keySet());
        return keys.get(RANDOM.nextInt(keys.size()));
    }

    /**
     * Add entry to parent keys map like parentId -> [list of child ids]
     *
     * @param parentId document parent Id
     * @param childRelationId document child Id
     */
    private static void updateChildRelation(Integer parentId, Integer childRelationId) {
        PARENT_CHILD_RELATION.computeIfAbsent(parentId, empty -> new HashSet<>()).add(childRelationId);
    }

    /**
     * Prepares ES client to work with Elasticsearch instance
     *
     * @return instance of ES client
     * @throws UnknownHostException
     */
    public static TransportClient prepareClient() throws UnknownHostException {
        Settings settings = Settings.builder()
                .put("cluster.name", "docker-cluster").build();

        final TransportClient client = new PreBuiltTransportClient(settings);
        return client.addTransportAddress(new TransportAddress(InetAddress.getByName("localhost"), 9300));
    }

    /**
     * Return Date object as String with pseudorandom date
     *
     * @return  created Date as String
     */
    public static String getPseudoRandomDateAsString() {
        return getPseudoRandomDate().format(DateTimeFormatter.ofPattern(DATE_FORMAT));
    }

    /**
     * Return Date object as String with pseudorandom date
     *
     * @return pseudorandom Date object
     */
    private static LocalDate getPseudoRandomDate() {
        return LOCAL_DATE.withDayOfMonth(RANDOM.nextInt(DAYS_IN_MONTH) + IM_LAST_DAY_IN_MONTH);
    }


    /**
     * Helper method to print out parent child document Id relations
     */
    public static void printRelation() {
        PARENT_CHILD_RELATION.forEach((key, value) -> {
            System.out.println("Key : " + key);
            value.forEach(element -> System.out.println("\tValue : " + element));
        });
    }
}
