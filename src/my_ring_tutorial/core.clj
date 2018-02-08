(ns my-ring-tutorial.core
  (:require [ring.adapter.jetty :as jetty]))

(defn hello [request]
  {:status  200
   :headers {"Content-Type" "text/html"}
   :body    "Hello World !"})

(defn info [request]
  {:status  200
   :headers {}
   :body    (pr-str request)})

(defn handler [request]
  (case (:uri request)
    "/"          (hello request)
    "/info"      (info request)
    {:status 404}))

(defn handler-async [request respond raise]
  (respond (handler request)))

(defn -main [port-nr]
  (jetty/run-jetty handler {:port (Integer. port-nr)})
  #_(jetty/run-jetty handler-async {:port (Integer. port-nr) :async? true}))
