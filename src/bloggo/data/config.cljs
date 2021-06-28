(ns bloggo.data.config
  (:refer-clojure :exclude [get])
  (:require [bloggo.data :as data]))

(defn add!
  "Adds a `key`:`value` config pair to the database."
  [key value]
  (data/run! "INSERT INTO config (`key`, `value`) VALUES (?, ?)" [key value]))

(defn get
  "Gets a value corresponding to `key` from config."
  [key]
  (data/query "SELECT * FROM config WHERE key = ?" [key]))