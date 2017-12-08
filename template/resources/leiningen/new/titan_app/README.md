# {{name}}

A new Titan application

# Development

In order to configure your database for local development, you'll need to set it up first. Note that for the moment Titan only supports Postgres as a storage backend.

```bash
createdb {{name}}
lein migrate
```

# Usage

Boot the Titan server with:

```clojure
(require '{{name}}.core)
({{name}}.core/-main)
```

