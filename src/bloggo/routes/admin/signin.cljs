(ns bloggo.routes.admin.signin)

(defn get! [_ ^js res]
  (.render res "admin/signin"))