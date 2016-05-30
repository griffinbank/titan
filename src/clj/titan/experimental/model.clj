(ns titan.experimental.model
  "Next-generation model"
  (:require [korma.core :as korma]))

(defn where*
  [query params]
  (korma/where query params))

(defprotocol ITitanQuery
  (where [params])
  (addValues [_ vs]))

(defrecord TitanQuery [query]
  clojure.lang.IDeref
  (deref [_] (korma/exec @query))

  ITitanQuery
  (where [params] (swap! query where* params))
  ; can't call 'values' because of method name conflict
  (addValues [this vs] (swap! query korma/values vs) this))

(defn values
  [query vs]
  (.addValues query vs))

(defn create!
  [entity vs]
  (map->TitanQuery {:query (atom (korma/values
                                  (korma/insert* entity)
                                  vs))}))

(defn fetch
  ([entity]
   (map->TitanQuery {:query (atom (korma/select* entity))}))
  ([entity conditions]
   (map->TitanQuery {:query (atom (korma/select* entity))})))
