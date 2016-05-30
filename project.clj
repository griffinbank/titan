(defproject venantius/titan "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.7.0"]
                 [org.clojure/tools.logging "0.3.1"]
                 [org.clojure/tools.nrepl "0.2.12"]
                 [org.slf4j/slf4j-log4j12 "1.7.21"]
                 [org.immutant/web "2.1.4"
                  :exclusions [[ch.qos.logback/logback-classic]
                               [ch.qos.logback/logback-core]]]
                 [venantius/korma "0.5.0-SNAPSHOT"
                  :exclusions [[org.clojure/java.jdbc]]]
                 [ragtime "0.6.0"]
                 [ring "1.4.0"]
                 [ring/ring-defaults "0.2.0"]
                 [ring/ring-json "0.4.0"]
                 [prismatic/schema "1.1.0"]
                 [environ "1.0.2"]
                 [clj-time "0.11.0"]

                 ;; May need add'l JDBC drivers in the future
                 [org.postgresql/postgresql "9.4-1206-jdbc42"]

                 ;; This may not stick around. I was just tinkering with having
                 ;; a richer REPL as part of Titan rather than in my
                 ;; ~/.lein/profiles.clj
                 [mvxcvi/whidbey "1.1.1"]
                 ]

  :aot [titan.commands.db.migrate]
  :source-paths ["src/clj"]
  :test-paths ["test/clj"]
  :min-lein-version "2.5.0"

  ;; Titan might need a plugin as well if there end up being multiple plugin
  ;; dependencies
  :plugins [[lein-environ "1.0.2"]]

  :profiles {:dev
             {:dependencies [[bond "0.2.5"]]
              :env {:database-url "postgres://localhost:5432/titan"}}
             :test {:env {:database-url "postgres://localhost:5432/titan_test"}}}
)