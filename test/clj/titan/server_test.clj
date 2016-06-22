(ns titan.server-test
  (:require [bond.james :as bond :refer [with-spy]]
            [clojure.test :refer :all]
            [titan.server :as server]))

(use-fixtures :each (fn [t] (t) (reset! server/server nil)))

(deftest -start-web-server!-destructures-properly-with-zero-args
  (with-redefs [immutant.web/run (fn [& _] nil)]
    (bond/with-spy [immutant.web/run]
      (server/-start-web-server!)
      (let [args (-> immutant.web/run bond/calls first :args second)]
        (is (= args {:host "127.0.0.1"
                     :path "/"
                     :port 8080}))))))

(deftest -start-web-server!-destructures-properly-with-passed-map
  (with-redefs [immutant.web/run (fn [& _] nil)]
    (bond/with-spy [immutant.web/run]
      (server/-start-web-server! {:host "0.0.0.0" :port 3000})
      (let [args (-> immutant.web/run bond/calls first :args second)]
        (is (= args {:host "0.0.0.0"
                     :path "/"
                     :port 3000}))))))
