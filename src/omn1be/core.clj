(ns omn1be.core
  (:require [datomic.api :as d]
            [clojure.core.async :refer [go <! timeout]]
            [taoensso.timbre :refer [debug]]))

(def db-url "datomic:free://127.0.0.1:4334/omn-dev")

(def schema
  [{:db/doc "username"
    :db/ident :user/name
    :db/valueType :db.type/string
    :db/cardinality :db.cardinality/one
    :db.install/_attribute :db.part/db}
   {:db/doc "password"
    :db/ident :user/password
    :db/valueType :db.type/string
    :db/cardinality :db.cardinality/one
    :db.install/_attribute :db.part/db}])

(def test-data [{:user/name "fenton" :user/password "passwErd"}])

(defn reload-dbs
  ([]
   (reload-dbs db-url))
  ([db-url]
   (d/delete-database db-url)
   (d/create-database db-url)
   (d/transact (d/connect db-url) schema)
   (d/transact (d/connect db-url) test-data)))

(defn db [] (-> db-url d/connect d/db))

(defn valid-user
  [db username password]
  (= 1
     (count
      (d/q
       '{:find [(pull ?e [:user/name])]
         :in [$ ?username ?password]
         :where [[?e :user/name ?username]
                 [?e :user/password ?password]]}
       db username password))))

(def person {:name "Fenton"
             :age 20})

(defn of-age [person country]
  (case country
    "USA" (>= (:age person) 21)
    "Canada" (>= (:age person) 19)))
