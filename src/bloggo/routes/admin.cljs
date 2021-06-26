(ns bloggo.routes.admin
  (:require [bloggo.data :as data]))

(defn get!
  "Checks if the application has any users, because
  if there aren't any, then that means we are dealing
  with a fresh install and need to therefore go to set up.
  
  If that's not the case, then we check if the user has a session.
  If the user does not have a session, we redirect to Sign In.
  And if the user does have a session, we redirect to Posts."
  [req res]
  (cond (not (data/any-users?))
        (.redirect res "/admin/setup")

        (empty? (.-uid ^js (.-session req)))
        (.redirect res "/admin/signin")

        :else
        (.redirect res "/admin/posts")))