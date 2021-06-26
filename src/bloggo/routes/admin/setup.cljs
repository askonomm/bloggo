(ns bloggo.routes.admin.setup
  (:require [bloggo.utils :as utils]
            [bloggo.data :as data]))

(defn setup!
  "Creates much-needed data in the database such as
  the first user, the blog title and description."
  [{:keys [title description email password]}]
  (data/add-user! email password)
  (data/add-config! "title" title)
  (data/add-config! "description" description))

(defn post!
  "Collects form input and does error validation on it.
  If all seems to be correct (and the site hasn't been set-up yet)
  it goes on and calls `(setup!)`."
  [req ^js res]
  (if (data/any-users?)
    (.redirect res "admin/signin")
    (let [title (.-title (.-body req))
          description (.-description (.-body req))
          email (.-email (.-body req))
          password (.-password (.-body req))
          errors (into {} (list (when (empty? title)
                                  {:error-title-required true})
                                (when (empty? description)
                                  {:error-description-required true})
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
          (.redirect res "/admin/signin"))
        (.render res "admin/setup" (clj->js errors))))))

(defn get!
  "Renders the setup view, granted that the site
  has not yet been set-up."
  [_ ^js res]
  (if (data/any-users?)
    (.redirect res "admin/signin")
    (.render res "admin/setup")))