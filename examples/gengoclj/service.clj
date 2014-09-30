(ns gengoclj.examples.service
  (:require [gengoclj.gengo :as gengo]))

;; get all service language pairs
(with-gengo ["key" "secret" true]
  (get-service-language-pairs))

;; get specific service language pairs by source language
(with-gengo ["key" "secret" true]
  (get-service-language-pairs "en"))

;; get service languages
(with-gengo ["key" "secret" true]
  (get-service-languages))
