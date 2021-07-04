(ns bloggo.data
  (:refer-clojure :exclude [run!])
  (:require ["better-sqlite3" :as sqlite]))

(def db (sqlite "bloggo.db"))

(defn query
  "Runs a query against the database and retrieves the result."
  ([query]
   (let [statement (.prepare db query)]
     (js->clj (.get statement) :keywordize-keys true)))
  ([query args]
   (let [statement (.prepare db query)]
     (js->clj (.get statement (clj->js args)) :keywordize-keys true))))

(defn query-all
  "Runs a query against the database and retrieves the result."
  [query args]
  (let [statement (.prepare db query)]
    (js->clj (.all statement (clj->js args)) :keywordize-keys true)))

(defn run!
  "Runs a query against the database, but doesn't retrieve a result."
  [query args]
  (let [statement (.prepare db query)]
    (.run statement (clj->js args))))