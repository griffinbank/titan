# titan

A Clojure library designed to ... well, that part is up to you.

## Models

Models are defined with `titan.model/defmodel`.

If you have a Korma entity defined at `db/user`, a model for that user might
look like:

```clojure
(defmodel db/user)
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
