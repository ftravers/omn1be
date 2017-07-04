(ns omn1be.websocket
  (:require [websocket-server.core :refer [start-ws-server send!]]
            [clojure.edn :refer [read-string]]
            [clojure.string :as str]
            [omn1be.core :as be]
            [omn1be.router :as router]
            [taoensso.timbre :refer [debug error]]))

(defonce ws-server (atom nil))

(comment
  (process-data "[(user/login {:username \"fenton\", :password \"passwErd\"})]"))

(defn process-data [data]
  
  (->> data
       read-string
       (router/parser {:database (be/db)})
       prn-str
       ))

(defn req-hndlr-datomic [channel data]
  (debug "data: " data)
  (send! channel (process-data data)))

(defn start []
  "Demonstrate how to use the websocket server library."
  (let [port 7890]
    (reset! ws-server (start-ws-server port req-hndlr-datomic))))

(defn stop [] "Stop websocket server" (@ws-server))

(defn restart [] (stop) (start))
