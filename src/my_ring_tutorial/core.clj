(ns my-ring-tutorial.core
  (:require [ring.adapter.jetty :as jetty]
            [ring.util.response :refer [response redirect file-response set-cookie]]
            [ring.middleware.resource :refer [wrap-resource]]
            [ring.middleware.file :refer [wrap-file]]
            [ring.middleware.not-modified :refer [wrap-not-modified]]
            [ring.middleware.params :refer [wrap-params]]
            [ring.middleware.cookies :refer [wrap-cookies]]))

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
   :body    (->> request
                 (map (fn [[k v]] (format "%20s  %s<br>" k v)))
                 (apply str))})

(defn about [request]
  (response "My ring tutorial"))

(defn rtbf [request]
  (redirect "https://www.rtbf.be/"))

(defn readme [request]
  (file-response "readme.html" {:root "resources"}))

(defn cookie [request]
  (-> (response "Setting a cookie.")
      (set-cookie "session_id" "session_id_hash!" {:max-age 20})))

;; -------------------------------------

(defn handler [request]
  (-> request
      ((case (:uri request)
         "/"          hello
         "/info"      info
         "/about"     about
         "/rtbf"      rtbf
         "/readme"    readme
         "/cookie"    cookie
         (fn [_] {:status 404})))))

(defn handler-async [request respond raise]
  (respond (handler request)))

;; Application --------------------------------------------------------------------
(def app (-> handler
             (wrap-content-type "text/html")
             (wrap-resource "public")
             (wrap-file "resources/www/public")
             (wrap-not-modified)
             (wrap-params)
             (wrap-cookies)
             ))

;; Run server ---------------------------------------------------------------------
(defn -main [port-nr]
  (jetty/run-jetty app {:port (Integer. port-nr)})
  #_(jetty/run-jetty handler-async {:port (Integer. port-nr) :async? true}))
