# Example

An example Titan application

## Usage

First, create your Postgres database:

`createdb example`

Next, run a migration:

`lein run -m titan.db.migrations/migrate`

You can now run the application:

`lein run`

Navigate your browser to `http://localhost:5000/` and you should see a user
fetched from the database.

#### REPL Migration & Server Initialization

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

## License

Copyright © 2016 W. David Jarvis

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
