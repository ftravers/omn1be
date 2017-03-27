(ns omn1be.core
  (:require [datomic.api :as d]
            [clojure.core.async :refer [go <! timeout]]
            [taoensso.timbre :refer [debug]]))

(def db-url "datomic:free://127.0.0.1:4334/omn-dev")

(def schema
  [{:db/doc "Car make."
    :db/ident :car/make
    :db/valueType :db.type/string
    :db/cardinality :db.cardinality/one
    :db.install/_attribute :db.part/db}
   {:db/doc "Car model."
    :db/ident :model
    :db/valueType :db.type/string
    :db/cardinality :db.cardinality/one
    :db.install/_attribute :db.part/db}
   {:db/doc "Make models."
    :db/ident :make/models
    :db/valueType :db.type/ref
    :db/cardinality :db.cardinality/many
    :db.install/_attribute :db.part/db}])

(def test-data
  [{:model "Tacoma" :db/id "tac"}
   {:model "Tercel" :db/id "ter"}
   {:model "325xi" :db/id "325"}
   {:model "X5" :db/id "x5"}
   {:car/make "Toyota"
    :make/models [{:db/id "tac"}
                  {:db/id "ter"}]}
   {:car/make "BMW"
    :make/models [{:db/id "325"}
                  {:db/id "x5"}]}])

(defn reload-dbs
  ([]
   (reload-dbs db-url))
  ([db-url]
   (d/delete-database db-url)
   (d/create-database db-url)
   (d/transact (d/connect db-url) schema)
   (d/transact (d/connect db-url) test-data)))

(defn db [] (-> db-url d/connect d/db))

(defn q [make pull-pattern db]
  (first (d/q
          '[:find [(pull ?e ppat) ...]
            :in $ ?make ppat
            :where
            [?e :car/make ?make]]
          db
          make pull-pattern)))

(q "Toyota" [:car/make {:make/models [:model]}] (db))
