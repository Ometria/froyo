(defproject ometria.froyo "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [metosin/compojure-api "1.1.10"]]
  :ring {:handler ometria.froyo.handler/app}
  :uberjar-name "server.jar"
  :profiles {:dev {:dependencies [[javax.servlet/javax.servlet-api "3.1.0"]
                                  [cheshire "5.5.0" :exclusions [com.fasterxml.jackson.core/jackson-core]]
                                  [clj-json "0.5.3" :exclusions [com.fasterxml.jackson.core/jackson-core]]
                                  [ring/ring-mock "0.3.0"]]
                   :plugins [[lein-ring "0.10.0"]]}})
