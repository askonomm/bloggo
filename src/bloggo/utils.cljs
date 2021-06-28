(ns bloggo.utils
  (:require [clojure.string :as str]))

(defn valid-email?
  "Checks if the provided `email` contains the @
  character or not. I know this is not great 
  e-mail validation, but I was in a hurry."
  [email]
  (str/includes? email "@"))