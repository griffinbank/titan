# Example

An example Titan application

## Configuration

First, create your Postgres database:

```
createdb example
```

## Usage

### Command Line Migrations & Server Initialization

Next, run a migration:

```
lein run -m titan.db.migrations/migrate
```

You can now run the application:

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

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
