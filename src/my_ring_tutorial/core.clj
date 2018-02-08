(ns my-ring-tutorial.core
  (:require [ring.adapter.jetty :as jetty]))

(defn handler [request]
  {:status  200
   :headers {"Content-Type" "text/html"}
   :body    "Hello World !"})

(defn -main [port-nr]
  (jetty/run-jetty handler {:port (Integer. port-nr)}))
