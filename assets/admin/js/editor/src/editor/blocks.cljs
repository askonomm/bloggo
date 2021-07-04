(ns editor.blocks
  (:require [editor.events]
            [editor.blocks.paragraph :as blocks.paragraph]
            [editor.blocks.heading-big :as blocks.heading-big]))

(defn block [index block]
  (cond (= :paragraph (get block :type))
        (blocks.paragraph/block index block)
        (= :heading-big (get block :type))
        (blocks.heading-big/block index block)))