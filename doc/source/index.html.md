# Titan

Titan is a simple and extensible framework for building Clojure web
applications. Whether you're new to Clojure or an experienced Clojurist, Titan
can help you develop quickly and with delight.

## Introduction

Titan is a Clojure web application framework with a focus on simplicity,
extensibilty, and productivity. Titan is designed to make development easy,
straightforward, and fun.

*Titan is guided by the following core principles:*

 - Simple is better than complex
 - Clarity is better than cleverness
 - Build for extension: everything should compose.

#### A Quick Note on the Documentation

The code examples included in this documentation are oriented around the
creation of a hypothetical blog application. The complete source code for this
demonstration application can be found [here](https://github.com/venantius/titan/tree/master/example).

## Getting Started

It's easy to get started with Titan, whether you're starting
from scratch or modifying an existing application.

### Beginning From Scratch

<aside class="warning">This feature is not yet complete.</aside>

To create a new application using the Titan Leiningen template, just run `lein new titan-app $your_app_name`. This will create a version of the example blog application that uses
the provided application name instead of `example`.

### Adding Titan to an Existing App

Adding Titan to an existing application is fairly straighforward, but for
obvious reasons involves a few more steps than starting from a clean slate.

#### Configure your `project.clj`

```clojure
:dependencies [[titan "0.0.1-alpha1"]
               ; ...
```

First, add Titan to your list of dependencies.

```clojure
               [org.postgresql/postgresql "9.4-1206-jdbc42"]
               ; ...
```

If you're currently using a SQL database for your application, make sure to
include the appropriate JDBC driver in your dependencies. The example code
includes a fairly recent version of the PostgreSQL JDBC driver.

```clojure
               [compojure "1.5.0"]
               ;; etc...
               ]
```

Titan doesn't include its own routing engine, so you'll want to make sure you
have the appropriate dependency included for that as well. In our example code
you'll find a dependency for
[Compojure](https://github.com/weavejester/compojure),
the popular routing library, but you can use whatever routing library you
prefer.

```clojure
:aliases {"migrate"  ["run" "-m" "titan.db.migrations/migrate"]
          "rollback" ["run" "-m" "titan.db.migrations/rollback"]}
```

We also recommend adding some aliases to your `project.clj` to make
command-line migrations more convenient.

#### Configuring Your Application Code

```clojure
(ns example.core
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [titan.app :refer [defapp]]
            [titan.server :as server]))

(defroutes app-routes
  (GET "/" [] {:status 200 :body "<h1>Hello World!</h1>"})
  (route/not-found "Beep boop not found"))

(defapp app-routes)
```

Titan expects you to wrap your application's main handler in the `defapp` macro,
located in the `titan.app` namespace. The `defapp` macro is used to store
your primary application handler in the `titan.app/app` atom, which allows
Titan to know where to find it without having to know the details of your
your application structure. Re-loading the namespace including the `defapp`
macro will reset the `app` atom's contents.

#### Starting the Titan server

To start the Titan server, you

<aside class="warning">I haven't written this documentation yet</aside>

# Database

Titan assumes the use of a SQL-powered relational database, and provides
a drop-in solution for simple database query patterns. Titan also provides a
straightforward, pure SQL solution for migrations.

Under the hood, Titan is powered by [Korma](https://github.com/korma/korma) and
[Ragtime](https://github.com/weavejester/ragtime).

## Migrations

Database migrations for a Titan application should be written in pure SQL and
stored in individual files in your project's `resources/migrations` directory.
Each migration should have one file for the up migration and a corresponding
file for the down migration.

Up migration files should use the `.up.sql` extension, while down migration
files should use `down.sql.` Migration filenames should be unique and ordered -
in general, using the current time (down to the second) is a good practice for
generating an ordering prefix.

<aside class="notice">
On OS X, you can get the current time with <code>date -u +"%Y%m%d%H%M%S"</code>
</aside>

### Writing Migrations

```sql
/*20160601062426_initial.up.sql*/
CREATE TABLE author (
  id       SERIAL  PRIMARY KEY,
  username TEXT    NOT NULL,
  password TEXT    NOT NULL
);
-- ;;
CREATE TABLE blog (
  id    SERIAL  PRIMARY KEY,
  title TEXT    NOT NULL
);
-- ;;
CREATE TABLE post (
  id        SERIAL  PRIMARY KEY,
  author_id INTEGER NOT NULL REFERENCES author(id) ON DELETE CASCADE,
  blog_id   INTEGER NOT NULL REFERENCES blog(id) ON DELETE CASCADE,
  content   TEXT    NOT NULL
);
```

Let's consider our example blog application. The initial migration, creating the
relevant tables, might look like the code on the right. In this migration, we're
creating three tables: one for authors, one for blogs, and one for blog posts.

***

```sql
/*20160601062426_initial.down.sql*/
DROP TABLE post;
--;;
DROP TABLE blog;
--;;
DROP TABLE author;;
```

A corresponding down migration might look like the attached code sample.

### Running Migrations

<aside class="warning">
This is a feature with an actively changing API.
</aside>

#### Running Migrations at the Command Line

To apply migrations at the command line, run `lein migrate`. If
you want to roll back all prior migrations, use `lein rollback`.

Titan AOT-compiles migration classes, enabling you to run migrations using an
Uberjar as part of your deployment process as well. Running migrations this way
can be done by executing `java -cp path/to/app.jar titan.commands.db.migrate`.

#### Running Migrations at the REPL

```clojure
(require 'titan.db.migrations)
(titan.db.migrations/migrate)
```

To apply migrations from the REPL, first require the Titan database migrations
namespace, then run the `migrate` command. You can also roll migrations back
with `rollback`.

## Models

```clojure
(ns example.model
  (:require [korma.core :as k]))

(k/defentity app-user
  (k/table :app_user))
```

Titan provides a SQL query DSL that wraps [Korma](https://github.com/korma/korma)
for runtime query generation and execution. This DSL represents an explicit
subset of Korma's functionality with a specific focus on composable functions
and delayed execution.

<aside class="warning">This documentation is incomplete</aside>

The expected usage pattern for a Titan query is composition followed by
dereference. This patterns is



You should define all of your Korma entities in a single namespace
and then store per-model logic in individual namespaces. By convention,
entities should be declared in `$yourapp.model` and individual models should be
declared in `$yourapp.model.$model_name`.

```clojure
(ns example.model.app-user
  (:require [titan.model :as m]))
```

### Fetch

```clojure
;; SELECT * FROM app_user;
@(m/fetch app-user)
=> ({:id 1 :name "Venantius"}, {:id 2 :name "Test User"})

;; SELECT * FROM app_user WHERE id = 2;
@(m/fetch app-user {:id 2})
=> ({:id 2 :name "Test User"})

;; SELECT * FROM app_user LIMIT 1
@(-> (m/fetch app-user)
     (m/limit 1))
=> ({:id 1 :name "Test User"})
```

Fetch records for a model with `fetch`.

You can fetch all records where a map of parameters matches by passing that in as well.

### Fetch-one

```clojure
;; SELECT * FROM app_user WHERE id = 2 LIMIT 1;
(fetch-one-app-user {:id 2})
;; => {:id 2 :name "Test User"}
```

If you just want to fetch a single record from the database, use `fetch-one`.

<aside class="notice">
Note that you'll get a single map back from <code>fetch-one</code>, not a seq.
</aside>

### Create

```clojure
;; INSERT INTO app_user (name) VALUES ('Bear');
(create-app-user! {:name "Bear"})
;; => {:id 3 :name "Bear"}
```

Insert a new record into the database with `create`. Returns the inserted
record.

### Update

```clojure
(update-app-user! 3 {:name "Ursa Americanus Kermodei"})
;; => {:id 3 :name "Ursa Americanus Kermodei"}
```

Update the record with a given id to have the provided new fields. Returns the
updated record.

### Delete

```clojure
;; DELETE FROM app_user WHERE id = 3;
(delete-app-user! {:id 3})
;; => 1 ;; returns the number of records deleted
```

Deletes all records matching the provided parameters. Returns the number of records
deleted.


# Controllers

Controllers are defined with `titan.controller/defcontroller`.

Let's say you have a schema named `repo`:

```clojure
(def repo
  {:id s/Int
   :full_name s/Str
   :name s/Str
   :owner owner
   :private s/Bool
   :fork s/Bool})
```

Now you can define a controller in which `:params` must be coerced into the `repo`
schema as follows:

```clojure
(defcontroller create!
  "Follow a new repository."
  {:params repo}
  [{:keys [params user]}]
  (behavior ...))
```

# About

Titan and any associated documentation is Copyright © 2016 W. David Jarvis

Titan is distributed under the Eclipse Public License version 1.0.
