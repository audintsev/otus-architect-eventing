# Eventing


## Sync API

On order checkout, Order Service invokes withdrawal in Billing Service, and upon completion, invokes Notification Service - all synchronously.

![Sync API diagram](README.assets/sync-api.png)

## Notification-Assisted

On order checkout, Order Service publishes a *notification* to the `order-checked-out` topic. The message only contains ID of the order.

Billing Service listens the `order-checked-out` topic, extracts ID from the message, and triggers Order Service to get the details about the order (it's price).
Then Billing Service attempts to withdraw order price from user's account, and publishes a notification to the `billing-processed` topic.
This notification contains orderId and billing status: success or failure.

Both Order Service and Notification Service listen the `billing-processed` topic.
Billing Service updates order status in the DB.
Notification Service extracts orderId and billing status from the message, contacts Order Service to get info about the order (price, user, etc),
and then notifies the user by _sending an email_.

![Notification-assisted diagram](README.assets/notification-assisted.png)

## Event Collaboration

On order checkout, Order Service publishes a *complete order object* to the `order-checked-out` topic.

Billing Service listens the `order-checked-out` topic, extracts all necessary information about the order from the message, including its price.
Then Billing Service attempts to withdraw order price from user's account, and publishes *complete updated order object* to the `billing-processed` topic.

Both Order Service and Notification Service listen the `billing-processed` topic.
Billing Service updates order status in the DB.
Notification Service extracts order details and billing status from the message, and then notifies the user by _sending an email_.

![Event Collaboration diagram](README.assets/event-collaboration.png)


## API docs

* Order Service API: [spec](README.assets/order.oas3.yaml), [SwaggerHub](https://app.swaggerhub.com/apis/audintsev/orders-service/0.1.0)
* Billing Service API:
  * Sync API: [spec](README.assets/billing-sync.oas3.yaml), [SwaggerHub](https://app.swaggerhub.com/apis/audintsev/billing-service_sync_api/0.1.0)
  * Async API: [spec](README.assets/billing-async.oas3.yaml) - the difference is that the _async_ version doesn't provide an API method to withdraw
* Notification Service API:
  * Sync API: [spec](README.assets/notification-sync.oas3.yaml), [SwaggerHub](https://app.swaggerhub.com/apis/audintsev/notification-service_sync_api/0.1.0)
  * Async API: [spec](README.assets/notification-async.oas3.yaml) - the difference is that the _async_ version doesn't provide an API method to send notifications

# Deployment

Deploying:

```shell
git clone https://github.com/audintsev/otus-architect-eventing.git
cd otus-architect-eventing

kubectl create ns udintsev
helm install -n udintsev hw15 ./arch-eventing
```

Undeploying:

```shell
helm uninstall -n udintsev hw15
for pvc in $(kubectl get pvc -n udintsev -o jsonpath='{.items[*].metadata.name}'); do kubectl delete -n udintsev pvc $pvc; done
kubectl delete ns udintsev
```

# Invoking the postman collection

Run from the root of the cloned repo:
```shell
newman run postman_collection.json 
```


# Building and pushing

```shell
cd order
../gradlew bootBuildImage --imageName=udintsev/hw15-order:latest
docker push udintsev/hw15-order:latest

cd ../billing
../gradlew bootBuildImage --imageName=udintsev/hw15-billing:latest
docker push udintsev/hw15-billing:latest

cd ../notification
../gradlew bootBuildImage --imageName=udintsev/hw15-notification:latest
docker push udintsev/hw15-notification:latest

```

Running postman collection against my dev env:
```shell
newman run --env-var "baseUrl=http://arch.labs" postman_collection.json
```
