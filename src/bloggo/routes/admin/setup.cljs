(ns bloggo.routes.admin.setup
  (:require [bloggo.utils :as utils]
            [bloggo.data.user :refer [add-user!]]
            [bloggo.data.config :refer [add-config! get-config]])
  (:require-macros [bloggo.macros :refer [input redirect view]]))

(defn setup!
  "Creates much-needed data in the database such as
  the first user, the blog title and description."
  [{:keys [title description email password]}]
  (add-user! email password)
  (add-config! "title" title)
  (add-config! "description" description)
  (add-config! "version" "0.1"))

(defn post!
  "Collects form input and does error validation on it.
  If all seems to be correct (and the site hasn't been set-up yet)
  it goes on and calls `(setup!)`."
  [req ^js res]
  (if (get-config "version")
    (redirect "/admin/signin")
    (let [title (input "title")
          description (input "description")
          email (input "email")
          password (input "password")
          errors (into {} (list
                           (when (empty? title)
                             {:error-title-required true})
                           (when (empty? description)
                             {:error-description-required true})
                           (when (< (count password) 8)
                             {:error "The password needs to be at least 8 characters long."})
                           (when (or (empty? email)
                                     (not (utils/valid-email? email)))
                             {:error-email-required true})
                           (when (empty? password)
                             {:error-password-required true})))]
      (if (empty? errors)
        (do
          (setup! {:title title
                   :description description
                   :email email
                   :password password})
          (redirect "/admin/signin"))
        (view "admin/setup" errors)))))

(defn get!
  "Renders the setup view, granted that the site
  has not yet been set up."
  [^js req ^js res]
  (if (get-config "version")
    (redirect "/admin/signin")
    (view "admin/setup")))
