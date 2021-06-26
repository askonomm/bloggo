(ns bloggo.utils
  (:require [clojure.string :as str]))

(defn valid-email? 
  "Checks if the provided `email` contains the @
  character or not. I know this is not great 
  e-mail validation, but I was in a hurry."
  [email]
  (str/includes? email "@"))

(defn cli-arg?
  "Attempts to find if `identifier` exists in `args`, and 
  if it does, returns `true`. Otherwise `nil`."
  [identifier args]
  (let [args (into [] args)
        index (.indexOf args identifier)]
    (when (not= index -1)
      true)))