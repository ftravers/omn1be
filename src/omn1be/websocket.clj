(ns omn1be.websocket
  (:require [websocket-server.core :refer [start-ws-server send!]]
            [clojure.edn :refer [read-string]]
            [clojure.string :as str]
            [omn1be.core :refer [q2m]]))

(defonce ws-server (atom nil))

(def sample-msg {:email "fenton.travers@gmail.com"
                 :query [:user/email :user/age #:user{:cars [:id :car/make :car/model :year]}]})

(defn req-hndlr-datomic [channel data]
  (->> data
       read-string
       q2m
       prn-str
       (send! channel)))

(defn req-hndlr-upcase [channel data]
  (->> data
       read-string
       str/upper-case
       prn-str
       (send! channel)))

(defn start []
  "Demonstrate how to use the websocket server library."
  (let [port 7890]
    (reset! ws-server ;; (start-ws-server port req-hndlr-datomic)
            (start-ws-server port req-hndlr-upcase))))

(defn stop "Stop websocket server" [] (@ws-server))

(defn restart [] (stop) (start))
