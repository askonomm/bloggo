(ns bloggo.core
  (:require ["express" :as express]
            ["express-session" :as session]
            ["express-handlebars" :as handlebars]
            ["body-parser" :as body-parser]
            [bloggo.hbs-helpers :as hbs-helpers]
            [bloggo.routes.blog :as routes.blog]
            [bloggo.routes.blog-post :as routes.blog-post]
            [bloggo.routes.admin :as routes.admin]
            [bloggo.routes.admin.setup :as routes.admin.setup]
            [bloggo.routes.admin.signin :as routes.admin.signin]
            [bloggo.routes.admin.signout :as routes.admin.signout]
            [bloggo.routes.admin.posts :as routes.admin.posts]
            [bloggo.routes.admin.new-post :as routes.admin.new-post]
            [bloggo.routes.admin.edit-post :as routes.admin.edit-post])
  (:require-macros [bloggo.macros :refer [route route!]]))

; This atom will hold our server instance, for a simple
; reason of being able to destroy it later.
(defonce server (atom nil))

; Session configuration, for more details consult the 
; express-session docs at https://www.npmjs.com/package/express-session
(def session-configuration {:secret "doesthisreallymatter?"
                            :resave false
                            :saveUninitialized true
                            :cookie {:secure false
                                     :maxAge (* 24 3600000)}})

(def hbs (.create handlebars (clj->js {:helpers {:config #(hbs-helpers/config %)}})))

(defn routes
  "Pretty self-explanatory, but for more details I would
  consult the express documentation that you can find 
  from https://expressjs.com/en/guide/routing.html."
  [^js app]
  (route "/" routes.blog/get!)
  (route "/admin" routes.admin/get!)
  (route "/admin/setup" routes.admin.setup/get!)
  (route! "/admin/setup" routes.admin.setup/post!)
  (route "/admin/posts" routes.admin.posts/get!)
  (route "/admin/new-post" routes.admin.new-post/get!)
  (route "/admin/edit-post/:id" routes.admin.edit-post/get!)
  (route! "/admin/edit-post/:id" routes.admin.edit-post/post!)
  #_(route "/admin/delete-post/:id" routes.admin.delete-post/get!)
  (route "/admin/signin" routes.admin.signin/get!)
  (route! "/admin/signin" routes.admin.signin/post!)
  (route "/admin/signout" routes.admin.signout/get!)
  (route "/:slug" routes.blog-post/get!))

(defn start-server
  "Configures the use of sessions as well as routes and 
  then starts the server on the port specified in ENV or 
  if not found, then it tries the port 3000."
  []
  (let [app (express)
        prod? (= (.get app "env") "production")
        port (if (nil? (.-PORT (.-env js/process)))
               3000
               (int (.-PORT (.-env js/process))))
        session-conf (merge session-configuration
                            (when prod?
                              {:cookie {:secure true}}))]
    (when prod? (.set app "trust proxy" 1))
    (.use app (session (clj->js session-conf)))
    (.use app (.json body-parser))
    (.use app (.urlencoded body-parser (clj->js {:extended true})))
    (.use app "/assets" (.static express "assets"))
    (.engine app "handlebars" (.-engine hbs))
    (.set app "view engine" "handlebars")
    (routes app)
    (.listen app port (fn [] (prn "Listening ...")))))

(defn start!
  "Starts the server, as well as updates the `server` atom 
  with the server instance so that we could later stop it."
  []
  (reset! server (start-server)))

(defn stop!
  "Closes the `server` connection as well as sets the `server`
  atom to `nil`."
  []
  (.close @server)
  (reset! server nil))

(defn main
  "Main entrypoint to the app. When `--install` CLI argument
  is provided it will also set-up the SQLite database schema."
  [& _]
  (start!))