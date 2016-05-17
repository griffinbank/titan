# Titan

Titan is a simple framework for building Clojure web applications.

Titan provides drop-in solutions for the following components:

 - [Application Structure](#application-structure)
 - [Database Models](#database-models)
 - [Migrations](#migrations)
 - [Controllers](#controllers)

## Getting Started

First, you'll need to add Titan to your list of dependencies:

```clojure
[titan "0.1.0"]
```

Next, you'll

## Application Structure

## Migrations

## Database Models

Titan assumes you're using SQL and extends [Korma](https://github.com/korma/korma) as a query DSL.

Titan has a single model-related macro, `defmodel`, which defines a number of handy utility functions for you. For example:

```clojure
(ns example.model
  (:require [korma.core :as korma]
            [titan.model :refer [defmodel]])

;; Use standard Korma `defentity` to declare a model for the `app_user` table
;; in our database
(korma/defentity app-user
  (korma/table app_user))

;; Declare our helper functions
(defmodel db/user)

;; Fetch all records for this model
(fetch-app-user)
;; => ({:id 1 :name "Venantius"}
       {:id 2 :name "Test User"})

;; Fetch all records matching the params
(fetch-app-user {:id 1})
;; => ({:id 1 :name "Venantius"})

;; Just fetch one, otherwise works like above. Note that we get a map, not a seq.
(fetch-one-app-user {:id 2})
;; => {:id 2 :name "Test User"}

(create-app-user! {:name "Bear"})
;; => {:id 3 :name "Bear"}

;; Update the record with the provided id to have the following new fields
(update-app-user! 3 {:name "Ursa Americanus Kermodei"})
;; => {:id 3 :name "Ursa Americanus Kermodei"}

;; Deletes all records matching the provided parameters
(delete-app-user! {:id 3})
;; => 1 ;; returns the number of records deleted
```

## Controllers

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
