(defproject example "0.0.1-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :plugins [[lein-environ "1.0.2"]]
  :profiles {:dev {:env {:database-url "postgres://localhost:5432/example"}}
             :test {:env {:database-url "postgres://localhost:5432/example_test"}}}
  :main example.core
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [compojure "1.5.0"]
                 [org.postgresql/postgresql "9.4-1206-jdbc42"]
                 [debugger "0.2.0"]
                 [ring/ring-json "0.4.0"]
                 [venantius/titan "0.0.1-alpha1"]])
