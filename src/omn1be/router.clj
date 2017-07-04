(ns omn1be.router
  (:require
   [omn1be.core :as core]
   [om.next.server :as om]
   [taoensso.timbre :refer [debug warn]]))

(defmulti api :function)

(defmethod api :user/login
  [args]
  (let [{{user :user/name pass :user/password} :params} args]
    (debug "username" user "password" pass "db" (core/db))
    {:novelty {:valid-user (core/valid-user (core/db) user pass)}}))

(defmethod api :user/authenticated
  [args]
  (let [{{user :user/name pass :user/password} :params} args]
    (debug "username" user "password" pass "db" (core/db))
    (if user
      (core/valid-user (core/db) user pass)
      false)))

(defmulti mutate om/dispatch)
(defmulti reader om/dispatch)

(defmethod mutate :default
  [env kee params]
  (debug env)
  (let [keyz {:keys (keys params)}
        rez (api {:function (keyword kee) :params params})]
    {:value (merge keyz rez)
     :action #(debug "running action")}))

(defmethod reader :user/authenticated
  [env kee params]
  (debug ":user/authenticated")
  (debug "ENV: " env ", KEY: " kee ", PARAMS: " params)
  (let [resp {:value (api {:function (keyword kee) :params params})}]
    (debug "RESPONSE: " resp)
    resp))

(defmethod reader :default
  [env kee params]
  (debug "ENV: " env ", KEY: " kee ", PARAMS: " params)
  
  {:value {:some "value"}})

(def parser (om/parser {:mutate mutate :read reader}))

(comment
  (parser {:database (core/db)} '[(user/login {:user/name "fenton", :user/password "passwErd"})])
  (api {:function :user/login :params {:user/name "fenton", :user/password "passwErd"}}))
