(ns bloggo.macros
  (:require [cljs.core]))

(defmacro view
  ([view]
   `(.render ~'res ~view))
  ([view data]
   `(.render ~'res ~view (cljs.core/clj->js ~data))))

(defmacro redirect [path]
  `(.redirect ~'res ~path))

(defmacro session [key]
  `(cljs.core/aget ~'req "session" ~key))

(defmacro session! [key value]
  `(cljs.core/aset ~'req "session" ~key ~value))

(defmacro param [key]
  `(cljs.core/aget ~'req "params" ~key))

(defmacro input [key]
  `(cljs.core/aget ~'req "body" ~key))

(defmacro route [path fn]
  `(.get ~'app ~path ~fn))

(defmacro route! [path fn]
  `(.post ~'app ~path ~fn))

(defmacro protected [& body]
  `(if (cljs.core/aget ~'req "session" "uid")
     ~@body
     (.redirect ~'res "/admin/signin")))