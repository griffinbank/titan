# Titan

Titan is a small, simple framework for building Clojure web applications.
It focuses on conventions around application structure, and provides tooling
to aid applications following those conventions.

 - Application Structure
 - Database Models
 - Migrations
 - Controllers

## Getting Started

#### Creating a New Titan App

To create a new application using the Titan Leiningen template, just run `lein new titan $your_app_name`.

#### Adding Titan to an Existing App

```clojure
{:dependencies [[titan "0.1.0"]
                [org.postgresql/postgresql "9.4-1206-jdbc42"]
                ...
                ]}
```

First, add Titan to your list of dependencies.

If you're currently using a SQL database for your application, you probably already
have the appropriate JDBC driver in your dependencies. If not, you'll need to
add one. The example code on the right is a fairly recent version of the
PostgreSQL JDBC driver.

## Application Structure

Titan 

# Database

Titan assumes the use of a SQL-driven relational database, and provides
drop-in solutions for simple database query patterns. Titan also provides a
straightforward, pure SQL solution for migrations.

Under the hood, Titan is powered by [Korma](https://github.com/korma/korma) and
[Ragtime](https://github.com/weavejester/ragtime).

## Models

We recommend defining all of your Korma entities in a single namespace
and then storing per-model logic in individual namespaces. By convention,
entities should be declared in `$yourapp.model` and individual models should be
declared in `$yourapp.model.$model_name`.

### Defmodel

```clojure
(korma/defentity app-user
  (korma/table :app_user))

(defmodel app-user)
```

Titan has a single model-related macro, `defmodel`. `defmodel` takes a single argument - a Korma entity, and declares the following functions in the namespaces in which it's used:

#### Fetch

```clojure
;; SELECT * FROM app_user;
(fetch-app-user)
;; => ({:id 1 :name "Venantius"}, {:id 2 :name "Test User"})

;; SELECT * FROM app_user WHERE id = 2;
(fetch-app-user {:id 2})
;; => {:id 2 :name "Test User"}
```

Fetch records for a model with `fetch`.

You can fetch all records where a map of parameters matches by passing that in as well.

#### Fetch-one

```clojure
;; SELECT * FROM app_user WHERE id = 2 LIMIT 1;
(fetch-one-app-user {:id 2})
;; => {:id 2 :name "Test User"}
```

If you just want to fetch a single record from the database, use `fetch-one`.

<aside class="notice">
Note that you'll get a single map back from <code>fetch-one</code>, not a seq.
</aside>

#### Create

```clojure
;; INSERT INTO app_user (name) VALUES ('Bear');
(create-app-user! {:name "Bear"})
;; => {:id 3 :name "Bear"}
```

Insert a new record into the database with `create`. Returns the inserted
record.

#### Update

```clojure
(update-app-user! 3 {:name "Ursa Americanus Kermodei"})
;; => {:id 3 :name "Ursa Americanus Kermodei"}
```

Update the record with a given id to have the provided new fields. Returns the
updated record.

#### Delete

```clojure
;; DELETE FROM app_user WHERE id = 3;
(delete-app-user! {:id 3})
;; => 1 ;; returns the number of records deleted
```

Deletes all records matching the provided parameters. Returns the number of records
deleted.

## Migrations

Database migrations should be individual SQL files stored in
`resources/migrations`.

As an example, your application's first migration might be stored in
`001-table_creation.up.sql`. The corresponding rollback would be stored in
`001-table_creation.down.sql`.

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

## Usage

FIXME

## License

Copyright © 2016 FIXME

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
