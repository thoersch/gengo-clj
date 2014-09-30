(ns gengoclj.examples.order
  (:require [gengoclj.gengo :as gengo]))

;; get group of jobs submitted together by their order id
(with-gengo ["key" "secret" true]
  (get-order-jobs 12345))
