(ns omn1be.router
  (:require
   [omn1be.core :as core]
   [om.next.server :as om]
   [taoensso.timbre :refer [debug warn]]))

(defmulti api :function)

(defmethod api :user/authenticated
  [{{user :user/name pass :user/password token :user/token} :params}]
  (debug "username" user "password" pass "db" (core/db))
  (cond
    user (core/valid-user (core/db) user pass)
    token true                          ; TODO: handle token
    :default false))

(defmulti reader om/dispatch)

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

(def parser (om/parser {:read reader}))

