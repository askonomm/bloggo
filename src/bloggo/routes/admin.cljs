(ns bloggo.routes.admin
  (:require [bloggo.data.config :refer [get-config]])
  (:require-macros [bloggo.macros :refer [redirect session]]))

(defn get!
  "Checks if the application has any users, because
  if there aren't any, then that means we are dealing
  with a fresh install and need to therefore go to set up.
  
  If that's not the case, then we check if the user has a session.
  If the user does not have a session, we redirect to Sign In.
  And if the user does have a session, we redirect to Posts."
  [^js req ^js res]
  (cond (not (get-config "version"))
        (redirect "/admin/setup")
        (session "uid")
        (redirect "/admin/signin")
        :else
        (redirect "/admin/posts")))