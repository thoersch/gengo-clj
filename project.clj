(defproject gengoclj "0.1.0"
  :description "Gengo API for Clojure"
  :url "https://github.com/thoersch/gengo-clj"
  :scm {:name "git"
        :url "https://github.com/thoersch/gengo-clj"}
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.clojure/data.json "0.1.2"]
                 [clj-http "0.3.5"]]
  :signing {:gpg-key "375B1908"}
  :pom-addition [:developers [:developer
                               [:name "Tyler Hoersch"]
                               [:url "http://tylerhoersch.com"]
                               [:email "thoersch@gmail.com"]
                               [:timezone "-5"]]]
  :plugins [[lein-marginalia "0.8.0"]])
