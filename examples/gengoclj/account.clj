(ns gengoclj.examples.account
  (:require [gengoclj.gengo :as gengo]))

;; get account balance
(with-gengo ["key" "secret" true]
  (get-account-balance))

;; get account preferred translators
(with-gengo ["key" "secret" true]
  (get-account-preferred-translators))

;; get account stats
(with-gengo ["key" "secret" true]
  (get-account-stats))
