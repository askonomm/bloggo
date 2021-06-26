(ns bloggo.data
  (:require ["better-sqlite3" :as sqlite]
            ["fs" :as fs]
            ["bcryptjs" :as bcrypt]))

(def db (sqlite "bloggo.db" (clj->js {:verbose (.log js/console)
                                      :fileMustExist true})))

(defn build-schema
  "Builds a schema for all of the tables that we 
  need to successfully run Bloggo."
  []
  (let [posts-sql (.toString (.readFileSync fs "sql/posts.sql"))]
    (.exec db posts-sql))
  (let [users-sql (.toString (.readFileSync fs "sql/users.sql"))]
    (.exec db users-sql))
  (let [config-sql (.toString (.readFileSync fs "sql/config.sql"))]
    (.exec db config-sql)))

(defn any-posts?
  "Checks if there are any posts in the database or not."
  []
  (let [sql (.prepare db "SELECT * FROM posts LIMIT 1")]
    (.get sql)))

(defn any-users?
  "Checks if there are any users in the database or not."
  []
  (let [sql (.prepare db "SELECT * FROM users LIMIT 1")]
    (.get sql)))

(defn add-user!
  "Adds a user by `email` and `password` to the database, but only
  if a user with such email doesn't already exist. Note that the 
  `password` will be salted and hashed with bcrypt."
  [email password]
  (let [sql (.prepare db "INSERT INTO users (`email`, `password`) VALUES (?, ?)")
        password-salt (.genSaltSync bcrypt 10)
        password-hash (.hashSync bcrypt password password-salt)]
    (.run sql email password-hash)))

(defn add-config!
  "Adds a `key`:`value` config pair to the database."
  [key value]
  (let [sql (.prepare db "INSERT INTO config (`key`, `value`) VALUES (?, ?)")]
    (.run sql key value)))