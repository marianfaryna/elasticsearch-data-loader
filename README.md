# elasticsearch-data-loader
loads data into ES with parent-child relations

1. Start elasticsearch docker image before running application

```
docker pull docker.elastic.co/elasticsearch/elasticsearch:6.3.2
docker run -p 9200:9200 -p 9300:9300 -e "discovery.type=single-node" docker.elastic.co/elasticsearch/elasticsearch:6.3.2
```

2. Define schema for index 
```
curl -H 'Content-Type: application/json' -XPUT 'http://localhost:9200/records' -d '{
  "mappings": {
    "record": {
      "properties": {
        "date": {
          "type":   "date"
        },
        "id": {
          "type":   "keyword"
        },
        "status": {
          "type":   "keyword"
        },
        "type": {
          "type":   "keyword"
        },
        "relation": {
          "type": "join",
          "relations": {
            "parent": "child"
          }
        }
      }
    }
  }
}'
```

3. Build application with Maven
```
mvn clean install
```

4. Run application with parameters (after jar name provide number of records you want to put into your ES index)
```
java -jar elasticsearch-data-loader-1.0-SNAPSHOT.jar 1000
```
