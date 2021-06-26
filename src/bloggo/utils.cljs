(ns bloggo.utils)

(defn cli-arg? 
  "Attempts to find if `identifier` exists in `args`, and 
  if it does, returns `true`. Otherwise `nil`."
  [identifier args]
  (let [args (into [] args)
        index (.indexOf args identifier)]
    (when (not= index -1)
      true)))