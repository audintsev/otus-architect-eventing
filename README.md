# Eventing

The three service collaboration styles:

* Sync API
 
  ![Sync API diagram](README.assets/sync-api.png)

* Notification-assisted

  ![Notification-assisted diagram](README.assets/notification-assisted.png)

* Event Collaboration

  ![Event Collaboration diagram](README.assets/event-collaboration.png)


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
