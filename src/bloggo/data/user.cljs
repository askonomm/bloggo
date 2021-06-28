(ns bloggo.data.user
  (:require ["bcryptjs" :as bcrypt]
            [bloggo.data :as data]))

(defn add!
  "Adds a user by `email` and `password` to the database, but only
  if a user with such email doesn't already exist. Note that the 
  `password` will be salted and hashed with bcrypt."
  [email password]
  (let [password-salt (.genSaltSync bcrypt 10)
        password-hash (.hashSync bcrypt password password-salt)]
    (data/run! "INSERT INTO users (`email`, `password`) VALUES (?, ?)" [email password-hash])))

(defn get-by-id
  "Retrieves the user by given `id`."
  [id]
  (data/query "SELECT * FROM users WHERE id = ?" [id]))

(defn get-by-email
  "Retrieves the user by given `email`."
  [email]
  (data/query "SELECT * FROM users WHERE email = ?" [email]))

(defn authenticates?
  "Attempts to authenticate a user by a given `email and `password.
  If the authentication attempt is unsuccessful, will return `nil`.
  Otherwise, will return `true`."
  [email password]
  (let [user (get-by-email email)]
    (when (and user
               (.compareSync bcrypt password (get user :password)))
      true)))

