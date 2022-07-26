# kafka streams course

## Rock the jvm tutorial

To start the containers execute:

```shell
docker compose up -d
```

and then to go into the `broker` image cli we run:

```shell
docker exec -it broker bash
```

### Produce discounts

kafka-console-producer \
--topic discounts \
--broker-list localhost:9092 \
--property parse.key=true \
--property key.separator=,
profile1,{"profile":"profile1","amount":0.5 }
profile2,{"profile":"profile2","amount":0.25 }
profile3,{"profile":"profile3","amount":0.15 }

### Link discounts to users

kafka-console-producer \
--topic discount-profiles-by-user \
--broker-list localhost:9092 \
--property parse.key=true \
--property key.separator=,
Daniel,profile1
Riccardo,profile2

### Produce orders

kafka-console-producer \
--topic orders-by-user \
--broker-list localhost:9092 \
--property parse.key=true \
--property key.separator=,
<Hit Enter>
Daniel,{"orderId":"order1","user":"Daniel","products":[ "iPhone 13","MacBook Pro 15"],"amount":4000.0 }
Riccardo,{"orderId":"order2","user":"Riccardo","products":["iPhone 11"],"amount":800.0}

### Produce payments

kafka-console-producer \
--topic payments \
--broker-list localhost:9092 \
--property parse.key=true \
--property key.separator=,
<Hit Enter>
order1,{"orderId":"order1","status":"PAID"}
order2,{"orderId":"order2","status":"PENDING"}

### Get results

kafka-console-consumer \
--bootstrap-server localhost:9092 \
--topic paid-orders \
--from-beginning

Expected: `{"orderId":"order1","user":"Daniel","products":["iPhone 13","MacBook Pro 15"],"amount":2000.0}`