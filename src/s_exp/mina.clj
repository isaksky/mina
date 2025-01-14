(ns s-exp.mina
  (:require [s-exp.mina.handler]
            [s-exp.mina.options :as options])
  (:import (io.helidon.webserver WebServer WebServerConfig WebServerConfig$Builder)))

(set! *warn-on-reflection* true)

(def default-options {:connection-provider false})

(defn- server-builder
  ^WebServerConfig$Builder
  [options]
  (reduce (fn [builder [k v]]
            (options/set-server-option! builder k v options))
          (WebServerConfig/builder)
          options))

(defn start!
  "Starts a new server.
  
  `options` can contain:

  * `:host` - host of the default socket
  
  * `:port` - port the server listens to, default to 8080

  * `:default-socket` - map-of :write-queue-length :backlog :max-payload-size :receive-buffer-size `:connection-options`(map-of `:socket-receive-buffer-size` `:socket-send-buffer-size` `:socket-reuse-address` `:socket-keep-alive` `:tcp-no-delay` `:read-timeout` `:connect-timeout`)

  * `:tls` - a `io.helidon.nima.common.tls.Tls` instance"
  ([handler options]
   (start! (assoc options :handler handler)))
  ([options]
   (-> (server-builder (merge default-options options))
       .build
       (.start))))

(defn stop!
  "Stops server, noop if already stopped"
  [^WebServer server]
  (.stop server))

;; (def r {:status 200})
;; (def h (fn [req]
;;          {:body (str (counted? (:headers req)))}))
;; (def h (fn [_]
;;          ;; (prn :aasdf ((:headers _) "accept"))
;;          ;; (prn (:headers _))
;;          r))
;; (def s (start!
;;         #'h
;;         {:host "0.0.0.0" :port 8080
;;          :write-queue-length 10240
;;          :connection-options {:socket-send-buffer-size 1024}}))

;; (stop! s)

;; https://api.github.com/repos/mpenet/mina/commits/main?per_page=1


