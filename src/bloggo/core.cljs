(ns bloggo.core
  (:require ["express" :as express]
            ["express-session" :as session]
            [bloggo.data :as data]
            [bloggo.utils :as utils]))

(defonce server (atom nil))

(def session-configuration {:secret "hola"
									                   :resave false
									                   :saveUninitialized true})

(defn routes 
  "Pretty self-explanatory, but for more details I would
  consult the express documentation that you can find 
  from https://expressjs.com/en/guide/routing.html."
  [app]
  (.get app "/" bloggo.routes.blog/get)
  (.get app "/admin" bloggo.routes.admin/get)
  (.get app "/admin/setup" bloggo.routes.admin.setup/get)
  (.post app "/admin/setup" bloggo.routes.admin.setup/post)
  (.get app "/admin/posts" bloggo.routes.admin.posts/get)
  (.get app "/admin/signin" bloggo.routes.admin.signin/get)
  (.get app "/admin/signout" bloggo.routes.admin.signout/get)
  (.get app "/:slug" bloggo.routes.blog-post/get))

(defn start-server 
  "Configures the use of sessions as well as routes and 
  then starts the server on the port specified in ENV or 
  if not found, then it tries the port 3000."
  []
  (let [app (express)]
    (.use app (session (clj->js session-configuration)))
    (routes/routes app)
    (.listen app 3000 (fn [] (prn "Listening ...")))))

(defn start! 
  "Starts the server, as well as updates the `server` atom 
  with the server instance so that we could later stop it."
  []
  (reset! server (start-server)))

(defn stop! 
  "Closes the @server connection as well as sets the `server`
  atom to `nil`."
  []
  (.close @server)
  (reset! server nil))

(defn main 
  "Main entrypoint to the app. When `--install` CLI argument
  is provided it will also set-up the SQLite database schema."
  [& args]
  (when (utils/cli-arg? "--install" args)
    (data/build-schema))
  (start!))