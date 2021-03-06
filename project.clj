(defproject my-ring-tutorial "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [ring/ring-core "1.6.3"]
                 [ring/ring-jetty-adapter "1.6.3"]
                 [ring/ring-devel "1.6.3"]
                 [compojure "1.6.0"]]
  :ring {:handler my-ring-tutorial.core/app}
  :plugins [[lein-ring "0.12.3"]]
  :profiles {:uberjar
             {:aot :all
              :main my-ring-tutorial.core}}
  :main my-ring-tutorial.core)
