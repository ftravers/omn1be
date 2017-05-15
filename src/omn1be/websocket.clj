(ns omn1be.websocket
  (:require [websocket-server.core :refer [start-ws-server send!]]
            [clojure.edn :refer [read-string]]
            [clojure.string :as str]
            [omn1be.core :as be]
            [omn1be.router :as router]
            [taoensso.timbre :refer [debug error]]))

(defonce ws-server (atom nil))

(def sample-msg {:email "fenton.travers@gmail.com"
                 :query [:user/email :user/age #:user{:cars [:id :car/make :car/model :year]}]})
 
(defn req-hndlr-datomic [channel data]
  (debug "data: " data)
  (->> data
       read-string
       prn-str
       str/upper-case
       (send! channel)))

(defn start []
  "Demonstrate how to use the websocket server library."
  (let [port 7890]
    (reset! ws-server (start-ws-server port req-hndlr-datomic))))

(defn stop [] "Stop websocket server" (@ws-server))

(defn restart [] (stop) (start))
