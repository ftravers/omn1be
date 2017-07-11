(ns omn1be.core
  (:require
   [om.next.server :as om]
   [taoensso.timbre :refer [debug warn]]))

(def users [{:user/name "bob" :user/password "abc123"}
            {:user/name "fenton" :user/password "passwErd"}])

(defn valid-user [userz username password]
  (->> userz
       (filter #(= username (:user/name %)))
       first
       :user/password
       (= password)))

(defn reader
  [env kee params]
  (debug "ENV: " env ", KEY: " kee ", PARAMS: " params)
  (let [userz (:state env)
        username (:user/name params)
        password (:user/password params)]
    {:value (valid-user userz username password)}))

(def parser (om/parser {:read reader}))

