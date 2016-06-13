# Example

**THIS EXAMPLE IS INCOMPLETE AND IS UNDERGOING CHANGE**

An example Titan application

## Initial Setup & Server Initialization

First, create your Postgres database:

```
createdb example
```

Next, run the migrations:

```
lein run -m titan.db.migrations/migrate
```

Finally, we can boot the application:

```
lein run
```

Navigate your browser to `http://localhost:5000/` and you should see a JSON blob
consisting of two user objects fetched from the database.

### REPL Migrations & Server Initialization

Instead of running commands at the command line, you can also run them at the
REPL. After starting your REPL, you can run migrations with:

```clojure
(require 'titan.db.migrations)
(titan.db.migrations/migrate)
```

You can then boot the application server with:

```clojure
(require 'example.core)
(example.core/-main)
```

## Testing

To run the tests, you'll need to create a test database:

```
createdb example_test
```

Tests can then be run with `lein test`

## License

Copyright © 2016 W. David Jarvis

Distributed under the Eclipse Public License version 1.0.
