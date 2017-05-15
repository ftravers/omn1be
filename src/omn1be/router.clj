(ns omn1be.router
  (:require
   [omn1be.core :as core]
   [om.next.server :as om]
   [taoensso.timbre :refer [debug warn]]))

(defmulti api :function)

(defmethod api :user/login
  [args]
  (let [{{user :username pass :password} :params} args]
    {:valid-user (core/valid-user (core/db) user pass)}))

(defmulti mutate om/dispatch)

(defmethod mutate :default
  [env kee params]
  (debug env)
  (let [keyz {:keys (keys params)}
        rez (api {:function (keyword kee) :params params})]
    {:value (merge keyz rez)
     :action #(debug "running action")}))

(def parser (om/parser {:mutate mutate}))

(comment
  (parser {:database (core/db)} '[(user/login {:username "fenton", :password "passwErd"})]))
