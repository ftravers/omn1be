(ns omn1be.router
  (:require
   [omn1be.core :as core]
   [om.next.server :as om]
   [taoensso.timbre :refer [debug warn]]))

(defmulti reader om/dispatch)

(defmulti mutate om/dispatch)

(defmethod mutate 'user/login
  [env key params]
  (map #(warn (% 0) (% 1))
       [["env:" env] ["key:" key] ["params:" params]])
  {:value {:keys (keys params)}
   :action #(debug "running action")})

(defmethod reader :default
  [env key params]
  (map #(debug (% 0) (% 1))
       [["env:" env] ["key:" key] ["params:" params]])
  (debug "env keys: " (keys env) )
  {:value "abc"})

(def parser (om/parser {:read reader :mutate mutate}))

(comment
  (parser {:database (core/db)} (quote [(user/login {:username "abc", :password "def"})]))

  (parser {:database (core/db)} ['(user/login)]))
