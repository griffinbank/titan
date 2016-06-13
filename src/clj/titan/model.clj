(ns titan.model
  "Next-generation model engine.

  Immutable query constructor. Dereference to execute query."
  (:refer-clojure :exclude [update])
  (:require [clojure.tools.macro :as macro]
            [korma.core :as korma]))

(defprotocol ITitanQuery
  (dry-run [this]))

(defrecord TitanQuery [alias aliases db ent fields
                       from group joins modifiers
                       order results table type where]
  clojure.lang.IDeref
  (deref [this] (korma/exec this)))

;; Declare local versions of all of these functions

(defn- declare-body
  [origin-fn args]
  (let [filtered-args (filter #(not= '& %) args)]
    `(~args (map->TitanQuery (~origin-fn ~@filtered-args)))))

(defn- declare-fn
  [doc name orig-fn args]
  (if doc
    `(defn ~name ~doc ~@(map #(declare-body orig-fn %) args))
    `(defn ~name ~@(map #(declare-body orig-fn %) args))))

(defn strip-name-of-*
  "Given a symbol, i.e. 'where*', strip it of the asterisk at the end."
  [n]
  (symbol
   (let [n (str n)]
     (if (.endsWith n "*")
       (subs n 0 (dec (count n)))
       n))))

(defmacro declare-wrapped-fn
  [f]
  `(eval
    (let [v# (var ~f)
          m# (meta (var ~f))
          n# (:name m#)
          d# (:doc m#)
          args# (:arglists m#)]
      (#'declare-fn d# (strip-name-of-* n#) v# args#))))

; Declare all of the various Korma things we might care about

(declare-wrapped-fn korma.core/add-joins)
(declare-wrapped-fn korma.core/empty-query)
(declare-wrapped-fn korma.core/from)
(declare-wrapped-fn korma.core/having)
(declare-wrapped-fn korma.core/insert*)
(declare-wrapped-fn korma.core/intersect*)
;; (declare-wrapped-fn korma.core/join) ;; special case, bad metadata (TODO)
(declare-wrapped-fn korma.core/limit)
(declare-wrapped-fn korma.core/modifier)
(declare-wrapped-fn korma.core/offset)
(declare-wrapped-fn korma.core/order)
(declare-wrapped-fn korma.core/post-query)
(declare-wrapped-fn korma.core/queries)
(declare-wrapped-fn korma.core/query-only)
(declare-wrapped-fn korma.core/raw)
(declare-wrapped-fn korma.core/select)
(declare-wrapped-fn korma.core/select*)
(declare-wrapped-fn korma.core/set-fields)
(declare-wrapped-fn korma.core/sql-only)
(declare-wrapped-fn korma.core/sqlfn)
(declare-wrapped-fn korma.core/sqlfn*)
(declare-wrapped-fn korma.core/subselect)
(declare-wrapped-fn korma.core/union*)
(declare-wrapped-fn korma.core/union-all)
(declare-wrapped-fn korma.core/update*)
(declare-wrapped-fn korma.core/values)
(declare-wrapped-fn korma.core/where)
(declare-wrapped-fn korma.core/with)

;; Variadic functions that I couldn't deal with as easily above.

(defn
  ^{:doc (:doc (meta #'korma.core/fields))}
  fields
  [query & vs]
  (map->TitanQuery (apply korma.core/fields (conj vs query))))

(defn
  ^{:doc (:doc (meta #'korma.core/group))}
  group
  [query & fields]
  (map->TitanQuery (apply korma.core/group (conj fields query))))

;; I don't know if this formulation actually works.

(defmacro
  ^{:doc (:doc (meta #'korma.core/with))}
  with
  [query ent & body]
  (map->TitanQuery `(korma.core/with ~@(conj body ent query))))

(defmacro
  ^{:doc (:doc (meta #'korma.core/with-batch))}
  with-batch
  [query ent & body]
  (map->TitanQuery `(korma.core/with-batch ~@(conj body ent query))))

;; Utility queries

(defn create!
  [entity vs]
  (let [query (-> (korma/insert* entity)
                  (korma/values vs))]
    (map->TitanQuery query)))

(defn fetch
  ([entity]
   (map->TitanQuery (korma/select* entity)))
  ([entity conditions]
   (let [query (-> (korma/select* entity)
                   (korma/where conditions))]
     (map->TitanQuery (korma/select* query)))))

(defn delete!
  ([entity]
   (map->TitanQuery (korma/delete* entity)))
  ([entity conditions]
   (let [query (-> (korma/delete* entity)
                   (korma/where conditions))]
     (map->TitanQuery query))))
