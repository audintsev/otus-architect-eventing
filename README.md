# Eventing

The three service collaboration styles:

* Sync API
 
  ![Sync API diagram](README.assets/sync-api.png)

* Notification-assisted

  ![Notification-assisted diagram](README.assets/notification-assisted.png)

* Event Collaboration

  ![Event Collaboration diagram](README.assets/event-collaboration.png)

# Building and pushing

```
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

# REST API

```shell
# Create account in billing and deposit money
curl -H "Content-Type: application/json" -H "X-User-Id: john.doe@example.com" localhost:8081/api/v1/billing/deposit -X POST -v -d '{"amount": 100}'

# Create order
curl -H "Content-Type: application/json" -H "X-User-Id: john.doe@example.com" localhost:8080/api/v1/orders -d '{"items": [{"itemId": 10, "quantity": 1}, {"itemId": 22, "quantity": 3}]}'

# Checkout order
curl -H "Content-Type: application/json" localhost:8080/api/v1/orders/1/checkout -X POST

# List notifications
curl localhost:8082/api/v1/notification
```

Same with host arch.labs:
```shell
curl -H "Content-Type: application/json" -H "X-User-Id: john.doe@example.com" arch.labs/otusapp/audintsev/billing/api/v1/billing/deposit -X POST -v -d '{"amount": 100}'

curl -H "Content-Type: application/json" -H "X-User-Id: john.doe@example.com" arch.labs/otusapp/audintsev/order/api/v1/orders -d '{"items": [{"itemId": 10, "quantity": 1}, {"itemId": 22, "quantity": 3}]}'
curl arch.labs/otusapp/audintsev/order/api/v1/orders

curl -H "Content-Type: application/json" arch.labs/otusapp/audintsev/order/api/v1/orders/1/checkout -X POST

curl arch.labs/otusapp/audintsev/notification/api/v1/notification
```

Running postman collection against my dev env:

```shell
newman run --env-var "baseUrl=http://arch.labs" postman_collection.json
```

# Putting OpenAPI specs to use

## Mock server and validating proxy

Using [Prism](https://meta.stoplight.io/docs/prism/docs/getting-started/01-installation.md), we can
mock a server or run it as a proxy in front of a real server to validate
requests and responses going through, making sure our OpenAPI spec is up to date.

### TL;DR

```shell
# Install
npm install -g @stoplight/prism-cli

# Mocking based on examples
prism mock order-openapi.yaml

# Dynamic mocking based on schema
prism mock -d order-openapi.yaml

# Proxying
prism proxy order-openapi.yaml http://localhost:8080
```

## Contract Testing

[dredd](https://dredd.org/en/latest/index.html)

```shell
npm install -g dredd
```
