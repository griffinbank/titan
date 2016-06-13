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

***

```clojure
(defn -main
  []
  (server/start-server!))
```

In addition to defining your application's primary handler, you'll also need to
include a call to `titan.server/start-server!` in your application's main method.

Calling `start-server!` will configure a database connection pool based on the
database connection string set by the environment variable `DATABASE_URL` (or as
configured by [environ](https://github.com/weavejester/environ)) and will then
boot an Undertow server to serve requests on localhost at port 5000. You can
override both the host and port options by setting the `HOST` and `PORT`
environment variables.

#### Starting the Titan server

To boot the Titan server from the command line, just use `lein run`.

```clojure
(example.core/-main)
```

If you're focused on a more REPL-based development flow, you can also boot the
application server from the REPL. Just invoke your main method and go!

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
  name     TEXT    NOT NULL,
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

## Declaring Models

```clojure
(ns example.model
  (:require [korma.core :as k]))

(k/defentity author)
(k/defentity blog)
(k/defentity post
  (k/belongs-to author)
  (k/belongs-to blog))
```

At the moment, models are defined directly using [Korma](https://github.com/korma/korma).
Refer to the Korma [documentation](http://sqlkorma.com/docs#entities) for details
on how to declare model entities.

<aside class="notice">
The recommended pattern for model declaration is to declare all of your models
in a single namespace, typically <code>$yourapp.model</code>
</aside>

## Interacting With Models

```clojure
(ns example.model.author
  (:require [example.model :as db]
            [titan.model :as m]))
```

Titan provides a SQL query DSL that wraps [Korma](https://github.com/korma/korma)
for runtime query generation and execution. This DSL represents an explicit
subset of Korma's functionality with a specific focus on composable functions
and delayed execution.

<aside class="notice">
Per-model logic should be stored in individual namespaces. By convention,
individual models should be declared in <code>$yourapp.model.$modelname</code>
</aside>

### Fetch

```clojure
;; SELECT * FROM author;
=> @(m/fetch db/author)
({:id 1
  :name "Venantius"
  :password "$2a$10$hjTKyciiHPHutU4YAL8TK.wG6LD8L7Z0H.7jQmsXCmMK/A0/8XqqO"}
 {:id 2
  :name "Test User"
  :password "$2a$10$xNi.5prsrvR/c6Tk0BvTa.KDZxeMaCc.OhZYGFvziNbmdcS21rzRe"})

;; SELECT * FROM author WHERE id = 2;
=> @(m/fetch db/author {:id 2})
({:id 2,
  :name "Test User",
  :password "$2a$10$xNi.5prsrvR/c6Tk0BvTa.KDZxeMaCc.OhZYGFvziNbmdcS21rzRe"})

;; SELECT * FROM author LIMIT 1
=> @(-> (m/fetch db/author)
        (m/limit 1))
({:id 1,
  :name "Venantius",
  :password "$2a$10$hjTKyciiHPHutU4YAL8TK.wG6LD8L7Z0H.7jQmsXCmMK/A0/8XqqO"})
```

Titan's `fetch` method is a convenient wrapper around a SQL `SELECT` statement.
With one argument, `fetch` will default to creating a simple `SELECT * FROM $table`
query. With two arguments, `fetch` will defer to Korma's query construction engine
to filter the query using a SQL `WHERE` condition.

### Fetch-one

```clojure
;; SELECT * FROM author WHERE id = 2 LIMIT 1;
=> @(m/fetch-one author {:id 2})
{:id 2 :name "Test User"}
```

If you just want to fetch a single record from the database, use `fetch-one`.
Returns a single map, not a seq.

### Create

```clojure
;; INSERT INTO author (name) VALUES ('Bear');
=> @(m/create! author {:name "Bear" :password "youshouldhashthis"})
{:id 3 :name "Bear" :password "youshouldhashthis"}
```

Insert a new record into the database with `create!`. Returns the inserted
record.

### Update

```clojure
;; UPDATE author SET name = 'Ursa Americanus Kermodei' WHERE (author.id = 3);
=> (update author! {:id 3} {:name "Ursa Americanus Kermodei"})
1
```

Update records with `update!`. Returns the number of updated records.

### Delete

```clojure
;; DELETE FROM author WHERE id = 3;
=> (delete author! {:id 3})
1
```

Deletes all records matching the provided parameters. Returns the number of records
deleted.

# About

Titan and any associated documentation is Copyright © 2016 W. David Jarvis

Titan is distributed under the Eclipse Public License version 1.0.
