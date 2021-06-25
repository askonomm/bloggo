(ns bloggo.routes.admin
  (:require [bloggo.data :as data]))

(defn get [req res]
  (cond (and (not (data/any-posts?))
             (not (data/any-users?)))
        (.redirect res "/admin/setup")
        
        (nil? (.-uid (.-session req)))
        (.redirect res "/admin/signin")
        
        :else
        (.redirect res "/admin/posts")))