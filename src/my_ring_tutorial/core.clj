(ns my-ring-tutorial.core
  (:require [ring.adapter.jetty :as jetty]))

;; Middleware ---------------------------------------------------------------------
(defn wrap-content-type [handler content-type]
  (fn [request]
    (-> (handler request)
        (assoc-in [:headers "Content-Type"] content-type))))

;; Handlers -----------------------------------------------------------------------
(defn hello [request]
  {:status  200
   :headers {}
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

;; Application --------------------------------------------------------------------
(def app (-> handler
             (wrap-content-type "text/html")))

;; Run server ---------------------------------------------------------------------
(defn -main [port-nr]
  (jetty/run-jetty app {:port (Integer. port-nr)})
  #_(jetty/run-jetty handler-async {:port (Integer. port-nr) :async? true}))
