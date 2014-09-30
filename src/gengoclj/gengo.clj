(ns gengoclj.gengo
  (:require
   [clj-http.client :as http]
   [gengoclj.auth :as auth])
  (:use [clojure.data.json :only [json-str read-json]]
        [clojure.string :only [join]]))

(def client-version 0.1)
(def user-agent (format "Gengo Clojure library; Version %f" client-version))
(def gengo-live-base "https://api.gengo.com/v2/")
(def gengo-sandbox-base "http://api.sandbox.gengo.com/v2/")
(def ^{:dynamic true} *gengo-key* nil)
(def ^{:dynamic true} *gengo-secret* nil)
(def ^{:dynamic true} *sandbox?* true)

(defmacro with-gengo [[key secret sandbox?] & body]
  `(binding [*gengo-key* ~key *gengo-secret* ~secret *sandbox?* ~sandbox?]
     ~@body))

(defstruct request :method :path :query :body)

(defstruct job :slug :body_src :lc_src :lc_tgt :tier)

(defn- uri [path]
  (if *sandbox?*
    (str gengo-sandbox-base path)
    (str gengo-live-base path)))

(defn- make-uri [request]
  (uri (request :path)))

(defn- parameter-string [params]
  (join "&"
        (map (fn [[key val]] (str (name key) "=" (str val)))
             (sort-by #(name (key %)) java.lang.String/CASE_INSENSITIVE_ORDER params))))

(defn- make-post-request [request]
  {:body (parameter-string
           (merge
             (:query
               (auth/authenticated-request *gengo-key* *gengo-secret* request))
               (assoc {} :data (json-str (request :body)))))
    :query-params nil
    :headers {"Content-Type" "application/x-www-form-urlencoded"
              "User-Agent" user-agent
              "Accept" "application/json"}})

(defn- make-put-request [request]
  {:body (parameter-string
           (merge
             (:query
               (auth/authenticated-request *gengo-key* *gengo-secret* request))
               (assoc {} :data (json-str (request :body)))))
    :query-params nil
    :headers {"Content-Type" "text/plain"
              "User-Agent" user-agent
              "Accept" "application/json"}})

(defn- do-get! [request]
  (read-json (:body (http/get (make-uri request)
              {:body (request :body)
               :query-params (:query (auth/authenticated-request *gengo-key* *gengo-secret* request))
               :headers {"Content-Type" "application/json"
                         "User-Agent" user-agent
                         "Accept" "application/json"}}))))

(defn- do-post! [request]
  (let [request-body (make-post-request request)]
    (read-json (:body (http/post (make-uri request)
            request-body)))))

(defn- do-put! [request]
  (let [request-body (make-put-request request)]
    (read-json (:body (http/put (make-uri request)
              request-body)))))

(defn- do-delete! [request]
  (read-json (:body (http/delete (make-uri request)
               {:body (request :body)
                :query-params (:query (auth/authenticated-request *gengo-key* *gengo-secret* request))
                :headers {"Content-Type" ""
                          "User-Agent" user-agent
                          "Accept" "application/json"}}))))

(defn get-account-stats
  "Get account statistics"
  []
  (let [request (struct request "GET" "account/stats" nil nil)]
    (do-get! request)))

(defn get-account-balance
  "Get account balance"
  []
  (let [request (struct request "GET" "account/balance" nil nil)]
    (do-get! request)))

(defn get-account-preferred-translators
  "Get preferred translators in array by langs and tier"
  []
  (let [request (struct request "GET" "account/preferred_translators" nil nil)]
    (do-get! request)))

(defn post-translation-jobs
  "Submit multiple jobs for translation"
  [jobs as-group?]
  (let [request (struct request "POST" "translate/jobs" nil {:jobs jobs :as_group (if as-group? "1" "0")})]
    (do-post! request)))

(defn revise-translation-job
  "Request revisions for a job"
  [job-id comments]
  (let [request (struct request "PUT" (format "translate/job/%d" job-id) nil {:action "revise" :comment comments})]
    (do-put! request)))

(defn approve-translation-job
  "Approve a translation"
  [job-id rating translator-comments gengo-comments is-public?]
  (let [request (struct request "PUT" (format "translate/job/%d" job-id) nil
                        (merge {:action "approve" :public (if is-public? "1" "0")}
                          (if (nil? rating) {} {:rating rating})
                          (if (nil? translator-comments) {} {:commentsForTranslator translator-comments})
                          (if (nil? gengo-comments) {} {:commentsForGengo gengo-comments})))]
    (do-put! request)))

(defn reject-translation-job
  "Reject a translation"
  [job-id reason comments captcha requeue?]
  (let [request (struct request "PUT" (format "translate/job/%d" job-id) nil
                  {:action "reject" :reason reason :comment comments :captcha captcha :follow_up (if requeue? "requeue" "cancel")})]
    (do-put! request)))

(defn get-translation-job
  "Get a translation job"
  [job-id]
  (let [request (struct request "GET" (format "translate/job/%d" job-id) nil nil)]
    (do-get! request)))

(defn get-translation-jobs
  "Get all or slected translation jobs"
  [& [job-ids]]
  (let [request (struct request "GET" (if job-ids (str "translate/jobs/" (join "," job-ids)) "translate/jobs/") nil nil)]
    (do-get! request)))

(defn post-translation-job-comment
  "Post a comment for a translation job"
  [job-id comments]
  (let [request (struct request "POST" (format "translate/job/%d/comment" job-id) nil {:body comments})]
    (do-post! request)))

(defn get-translation-job-comments
  "Get comments for a translation job"
  [job-id]
  (let [request (struct request "GET" (format "translate/job/%d/comments" job-id) nil nil)]
    (do-get! request)))

(defn get-translation-job-feedback
  "Get feedback for a translation job"
  [job-id]
  (let [request (struct request "GET" (format "translate/job/%d/feedback" job-id) nil nil)]
    (do-get! request)))

(defn get-translation-job-revisions
  "Get revisions for a translation job"
  [job-id]
  (let [request (struct request "GET" (format "translate/job/%d/revisions" job-id) nil nil)]
    (do-get! request)))

(defn get-translation-job-revision
  "Get a specific revision for a translation job"
  [job-id revision-id]
  (let [request (struct request "GET" (format "translate/job/%d/revisions/%d/" job-id revision-id) nil nil)]
    (do-get! request)))

(defn delete-translation-job
  "Cancel a translation job. It can only be deleted if it has not been started by a translator"
  [job-id]
  (let [request (struct request "DELETE" (format "translate/job/%d" job-id) nil nil)]
    (do-delete! request)))

(defn get-service-languages
  "Get a list of supported languages and their language codes"
  []
  (let [request (struct request "GET" "translate/service/languages" nil nil)]
    (do-get! request)))

(defn get-service-language-pairs
  "Get a list of supported language pairs, tiers, and credit prices"
  [&[source-lang-code]]
  (let [request (struct request "GET" "translate/service/language_pairs" (if source-lang-code {:lc_src source-lang-code} nil) nil)]
    (do-get! request)))

(defn determine-translation-cost
  "Get a quote for translation jobs."
  [jobs]
  (let [request (struct request "POST" "translate/service/quote" nil {:jobs jobs})]
    (do-post! request)))

(defn get-order-jobs
  "Get translation jobs which were previously submitted together by their order id"
  [order-id]
  (let [request (struct request "GET" (format "translate/order/%d" order-id) nil nil)]
    (do-get! request)))
