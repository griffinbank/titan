(defproject {{name}} "0.1.0-snapshot"
  :description "A new Titan app"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}

  :dependencies [[org.clojure/clojure "1.9.0"]
                 [org.clojure/java.jdbc "0.6.1"]

                 [compojure "1.6.0"]

                 [org.postgresql/postgresql "9.4.1211"]
                 [cheshire "5.8.0"]
                 [clj-http "3.7.0"]
                 [clj-jwt "0.1.1"]
                 [clj-time "0.14.0"]
                 [environ "1.1.0"]
                 [com.cemerick/url "0.1.1"]

                 [venantius/titan "0.1.0"]]

  :plugins [[lein-environ "1.1.0"]]

  :aliases {"migrate"  ["run" "-m" "titan.db.migrations/migrate"]
            "rollback" ["run" "-m" "titan.db.migrations/rollback"]
            "rollback-all" ["run" "-m" "titan.db.migrations/rollback-all"]}

  :profiles {
    :dev {
      :dependencies [[ring/ring-mock "0.3.1"]]
      :env {:database-url "postgres://localhost:5432/{{name}}"}
      :repl-options {
        :prompt (fn [ns] (str "[" \u001b \[ 33 \m ns \u001b \[ 0 \m "]"
                              "(" \u001b \[ 36 \m "λ" \u001b \[ 0 \m ")=> "))
      }
    }
  }

  :main {{name}}.core
)
