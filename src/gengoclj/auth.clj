(ns gengoclj.auth
  (:import
   (java.util Calendar)
   (javax.crypto Mac)
   (javax.crypto.spec SecretKeySpec)
   (java.math BigInteger)))

(defn- current-timestamp []
  (let [cal (Calendar/getInstance)
        current-time-millis (long (/ (long (.getTimeInMillis cal)) 1000))]
    (String/valueOf current-time-millis)))

(defn- get-hash-str [data-bytes]
  (apply str
	  (map #(.substring
           (Integer/toString (+ (bit-and % 0xff) 0x100) 16) 1) data-bytes)))

(defn- hmac
  "Calculate HMAC signature for given data."
  [#^String key #^String data]
  (let [hmac-sha1 "HmacSHA1"
        signing-key (SecretKeySpec. (.getBytes key "iso-8859-1") hmac-sha1)
        mac (doto (Mac/getInstance hmac-sha1) (.init signing-key))]
    (get-hash-str (.doFinal mac (.getBytes data "iso-8859-1")))))

(defn- add-auth-params
  "Creates map of required data for gengo authentication"
  [key secret request]
  (let [ts (current-timestamp)]
    (assoc request
      :query (merge
               (request :query)
               {:ts ts
                :api_sig (hmac secret ts)
                :api_key key}))))

(defn- add-data-param [request]
  (assoc-in request [:query :data] (request :body)))

(defn authenticated-request [key secret request]
  (add-auth-params key secret (add-data-param request)))
