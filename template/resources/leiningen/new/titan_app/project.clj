(defproject {{name}} "0.1.0-snapshot"
  :description "A new Titan app"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}

  :dependencies [[org.clojure/clojure "1.9.0-rc1"]
                 [org.clojure/core.async "0.3.443"]
                 [org.clojure/java.jdbc "0.6.1"]
                 [org.clojure/core.match "0.3.0-alpha4"]

                 [org.postgresql/postgresql "9.4.1211"]
                 [cheshire "5.8.0"]
                 [clj-http "3.7.0"]
                 [clj-jwt "0.1.1"]
                 [clj-time "0.14.0"]
                 [environ "1.1.0"]
                 [com.cemerick/url "0.1.1"]
                 [ring/ring-mock "0.3.1"]

                 [venantius/titan "0.0.1-alpha4"]
                 ] ;; TODO: move to dev?

  :plugins [[lein-environ "1.1.0"]]

  :dev {:dependencies [[ring/ring-mock "0.3.0"]]}

  :repl-options {:prompt (fn [ns] (str ns "=> "))}
  )
