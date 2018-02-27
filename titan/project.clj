(defproject venantius/titan "0.1.3"
  :description "An extensible web application framework for Clojure."
  :url "https://github.com/venantius/titan"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/tools.logging "0.3.1"]
                 [org.slf4j/slf4j-log4j12 "1.7.21"]
                 [org.immutant/web "2.1.5"
                  :exclusions [[ch.qos.logback/logback-classic]
                               [ch.qos.logback/logback-core]]]
                 [korma "0.4.3"]
                 [ragtime "0.7.2"]
                 [ring "1.5.0"]
                 [ring/ring-defaults "0.2.0"]
                 [ring/ring-json "0.4.0"]
                 [prismatic/schema "1.1.2"]
                 [environ "1.0.2"]
                 [clj-time "0.11.0"]
                 ]

  :aot [titan.commands.db.migrate]
  :source-paths ["src/clj"]
  :java-source-paths ["src/java"]
  :test-paths ["test/clj"]
  :min-lein-version "2.5.0"

  ;; Titan might need a plugin as well if there end up being multiple plugin
  ;; dependencies
  :plugins [[lein-environ "1.0.2"]]

  :aliases {"migrate"  ["run" "-m" "titan.db.migrations/migrate"]
            "rollback" ["run" "-m" "titan.db.migrations/rollback"]}
  :profiles {:dev
             {:dependencies [[bond "0.2.5"]
                             [org.postgresql/postgresql "9.4-1206-jdbc42"]]
              :env {:database-url "postgres://localhost:5432/titan"}}
             :test
             {:dependencies [[org.postgresql/postgresql "9.4-1206-jdbc42"]]
              :env {:database-url "postgres://localhost:5432/titan_test"}}}
)
