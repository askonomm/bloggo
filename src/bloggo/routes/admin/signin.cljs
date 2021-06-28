(ns bloggo.routes.admin.signin
  (:require [bloggo.data.user :as user]
            [bloggo.data.config :as config]))

(defn post!
  "Attempts to authenticate the user and redirects to the 
  Posts view upon success. Otherwise does error validation and
  shows the Sign In view again."
  [^js req ^js res]
  (let [email (.-email (.-body req))
        password (.-password (.-body req))
        user (user/get-by-email email)
        errors (into {} (list (when (empty? email)
                                {:error-email-required true})
                              (when (empty? password)
                                {:error-password-required true})
                              (when (not user)
                                {:error "User with the provided e-mail does not exist."})
                              (when (not (user/authenticates? email password))
                                {:error "The provided e-mail or password are incorrect."})))]
    (if (empty? errors)
      (do
        (set! (.-uid (.-session req)) (get user :id))
        (.redirect res "/admin/posts"))
      (.render res "admin/signin" (clj->js errors)))))

(defn get!
  "Renders the Sign In view, unless the site hasn't been 
  set up yet, in which case it redirects to the set up view."
  [_ ^js res]
  (if (not (config/get "version"))
    (.redirect res "/admin/setup")
    (.render res "admin/signin")))