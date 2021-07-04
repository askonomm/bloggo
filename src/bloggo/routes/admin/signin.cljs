(ns bloggo.routes.admin.signin
  (:require [bloggo.data.user :refer [get-user-by-email user-authenticates?]]
            [bloggo.data.config :refer [get-config]])
  (:require-macros [bloggo.macros :refer [input session! redirect view]]))

(defn post!
  "Attempts to authenticate the user and redirects to the 
  Posts view upon success. Otherwise does error validation and
  shows the Sign In view again."
  [^js req ^js res]
  (let [email (input "email")
        password (input "password")
        user (get-user-by-email email)
        errors (into {} (list (when (empty? email)
                                {:error-email-required true})
                              (when (empty? password)
                                {:error-password-required true})
                              (when (not user)
                                {:error "User with the provided e-mail does not exist."})
                              (when (not (user-authenticates? email password))
                                {:error "The provided e-mail or password are incorrect."})))]
    (if (empty? errors)
      (do
        (session! "uid" (get user :id))
        (redirect "/admin/posts"))
      (view "admin/signin" (merge errors {:layout false})))))

(defn get!
  "Renders the Sign In view, unless the site hasn't been 
  set up yet, in which case it redirects to the set up view."
  [^js req ^js res]
  (if (not (get-config "version"))
    (redirect "/admin/setup")
    (view "admin/signin" {:layout false})))
