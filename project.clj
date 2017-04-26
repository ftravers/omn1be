(defproject omn1be "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.9.0-alpha14"]
                 [com.datomic/datomic-free "0.9.5561" :exclusions [joda-time org.slf4j/slf4j-nop]]
                 [org.clojure/core.async "0.2.395"]
                 [com.taoensso/timbre "4.8.0"]
                 [org.omcljs/om "1.0.0-alpha48-SNAPSHOT"]
                 [fentontravers/websocket-server "0.4.6"]]
  :source-paths ["src"]
  :clean-targets ^{:protect false} ["target"]
  :target-path "target/%s"
  :plugins [[cider/cider-nrepl "0.15.0-SNAPSHOT"]
            [com.datomic/datomic-free "0.9.5344" :exclusions [joda-time org.slf4j/slf4j-nop]]
            [com.billpiel/sayid "0.0.10"]]
  
  :main ^:skip-aot omn1be.core
  :profiles
  {:dev {:dependencies [[org.clojure/tools.namespace "0.2.11"]]
         :source-paths ["src/clj"]
         :repl-options {:init (set! *print-length* 50)}}
   :uberjar {:aot :all}})
