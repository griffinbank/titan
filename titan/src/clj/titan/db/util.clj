(ns titan.db.util
  (:import java.net.URI)
  (:require [clj-time.coerce :as c]
            [clj-time.core :as t]
            [clj-time.format :as f]
            [clojure.string :as str]))

(def default-subproto-map {"postgres" "postgresql"})
(def default-classname-map
  {"postgres" "org.postgresql.Driver"
   "mysql" "com.mysql.jdbc.Driver"
   "sqlite" "org.sqlite.JDBC"
   "h2" "org.h2.Driver"
   "oracle:thin" "oracle.jdbc.driver.OracleDriver"})

(defn parse-url
  "Given a String or a URI instance, and optional subproto-map and
  classname-map for conversion return a map of args suitable for use
  with `make-datasouce`."
  ([url subproto-map classname-map]
   {:pre [(map? subproto-map) (map? classname-map)]}
   (cond
      ;; URI
     (instance? URI url)
     (let [host (.getHost ^URI url)
           port (let [p (.getPort ^URI url)]
                  (and (pos? p) p))
           path (.getPath ^URI url)
           query (.getRawQuery ^URI url)
           scheme  (.getScheme ^URI url)
           adapter (subproto-map scheme scheme)
           classname (classname-map scheme)]
       (merge {:classname classname
               :host host
               :port port
               :path path
               :adapter  (keyword adapter)
               :jdbc-url (str "jdbc:" adapter "://"
                              host
                              (when port ":") (or port "") path
                              (when query "?") (or query ""))}
              (if-let [user-info (.getUserInfo ^URI url)]
                (let [[un pw] (str/split user-info #":")]
                  {:user un
                   :password pw
                   :jdbc-url (str "jdbc:" adapter "://"
                                  host
                                  (when port ":") (or port "") path
                                  (when (or un pw query) "?") (or query "")
                                  (when (and query (or un pw)) "&")
                                  (when un (str "user=" un))
                                  (when (and pw (or query un)) "&")
                                  (when pw (str "password=" pw)))}))))
      ;; String
     (string? url)
     (parse-url (if (.startsWith ^String url "jdbc:")
                  (URI. (subs url 5))
                  (URI. url))
                subproto-map classname-map)
      ;; default
     :otherwise
     (throw (IllegalArgumentException.
             (str "Expected `url` to be java.net.URI or String,"
                  " but found (" (pr-str (type url)) ") "
                  (pr-str url))))))
  ([url]
   (parse-url url default-subproto-map default-classname-map)))

(defn build-db-subname
  "Given a database URI, assembles the :subname that JDBC requires"
  [dburi]
  (let [uri-map (parse-url dburi)
        host (if (= "127.0.0.1" (:host uri-map))
               "localhost"
               (:host uri-map))]
    (clojure.string/join ["//" host ":" (:port uri-map) (:path uri-map)])))

(defn sql-datetime->date-str
  "Take a SQL DateTime/Timestamp object and convert it to a Date string"
  [datetime]
  (f/unparse
   (f/formatters :date)
   (c/from-sql-date datetime)))

