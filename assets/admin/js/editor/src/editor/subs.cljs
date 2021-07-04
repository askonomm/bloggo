(ns editor.subs
  (:require [re-frame.core :refer [reg-sub]]))

(reg-sub
 :blocks
 (fn [db _]
   (get db :blocks)))