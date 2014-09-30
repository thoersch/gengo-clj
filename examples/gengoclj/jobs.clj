(ns gengoclj.examples.jobs
  (:require [gengoclj.gengo :as gengo]))

;; delete a translation job
(with-gengo ["key" "secret" true]
  (delete-translation-job 12345))

;; get a translation job
(with-gengo ["key" "secret" true]
  (get-translation-job 12345))

;; get a translation job's comments
(with-gengo ["key" "secret" true]
  (get-translation-job-comments 12345))

;; get a translation job's feedback
(with-gengo ["key" "secret" true]
  (get-translation-job-feedback 12345))

;; get a translation job's revisions
(with-gengo ["key" "secret" true]
  (get-translation-job-revisions 12345))

;; get a specific translation job's revision
(with-gengo ["key" "secret" true]
  (get-translation-job-revision 12345 6))

;; get multiple translation jobs
(with-gengo ["key" "secret" true]
  (get-translation-jobs (list 1 2 3 4 5)))

;; get all translation jobs
(with-gengo ["key" "secret" true]
  (get-translation-jobs))

;; post a comment on a translation job
(with-gengo ["key" "secret" true]
  (post-translation-job-comment 12345 "This is a comment."))

;; post new translation jobs
(with-gengo ["key" "secret" true]
  (let [job1 (struct job "slug1" "content1" "en" "es" "standard")
        job2 (struct job "slug2" "content2" "en" "es" "standard")]
    (post-translation-jobs (list job1 job2) true)))

;; approve a translation job
;; params - job-id rating (integer 1-5) translator-comments gengo-comments is-public?
(with-gengo ["key" "secret" true]
  (approve-translation-job 12345 4 "Something for the translator" "Something for gengo" true))

;; reject a translation job
;; params - job-id reason (0: Quality 1: Incomplete 2: Other) comments captcha requeue?
(with-gengo ["key" "secret" true]
  (reject-translation-job 12345 0 "Some comment" "captcha text" true))

;; revise a translation job
;; params - job-id comments
(with-gengo ["key" "secret" true]
  (revise-translation-job 12345 "Please fix this and that"))
