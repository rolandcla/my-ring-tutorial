(ns my-ring-tutorial.core
  (:require [ring.adapter.jetty :as jetty]
            [ring.util.response :refer [response redirect file-response]]
            [ring.middleware.resource :refer [wrap-resource]]
            [ring.middleware.file :refer [wrap-file]]))

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

(defn about [request]
  (response "My ring tutorial"))

(defn rtbf [request]
  (redirect "https://www.rtbf.be/"))

(defn readme [request]
  (file-response "readme.html" {:root "resources"}))

;; -------------------------------------

(defn handler [request]
  (-> request
      ((case (:uri request)
         "/"          hello
         "/info"      info
         "/about"     about
         "/rtbf"      rtbf
         "/readme"    readme
         (fn [_] {:status 404})))))

(defn handler-async [request respond raise]
  (respond (handler request)))

;; Application --------------------------------------------------------------------
(def app (-> handler
             (wrap-content-type "text/html")
             (wrap-resource "public")
             (wrap-file "resources/www/public")))

;; Run server ---------------------------------------------------------------------
(defn -main [port-nr]
  (jetty/run-jetty app {:port (Integer. port-nr)})
  #_(jetty/run-jetty handler-async {:port (Integer. port-nr) :async? true}))
